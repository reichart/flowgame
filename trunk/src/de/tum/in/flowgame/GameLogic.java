package de.tum.in.flowgame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tum.in.flowgame.client.Client;
import de.tum.in.flowgame.client.Sounds;
import de.tum.in.flowgame.client.facebook.CustomFacebookClient;
import de.tum.in.flowgame.client.facebook.JSONUtils;
import de.tum.in.flowgame.client.strategy.Trend;
import de.tum.in.flowgame.client.util.Browser;
import de.tum.in.flowgame.model.Answer;
import de.tum.in.flowgame.model.Difficulty;
import de.tum.in.flowgame.model.DifficultyFunction;
import de.tum.in.flowgame.model.GameRound;
import de.tum.in.flowgame.model.GameSession;
import de.tum.in.flowgame.model.Highscore;
import de.tum.in.flowgame.model.Person;
import de.tum.in.flowgame.model.ScenarioRound;
import de.tum.in.flowgame.model.ScenarioSession;
import de.tum.in.flowgame.model.Collision.Item;
import de.tum.in.flowgame.model.ConfigChange.ConfigKey;

public class GameLogic implements Runnable {

	private final static Log log = LogFactory.getLog(GameLogic.class);

	private static final int POINTS_FOR_FUEL = 10;
	private static final int POINTS_FOR_ASTEROID = 5;

	private boolean paused;

	private final Client client;

	private GameSession gameSession;
	private GameRound gameRound;
	private final Person player;

	private final List<GameListener> listeners;

	private volatile int fuelInRow;
	private volatile int asteroidsInRow;

	private volatile int fuelcansCollected;
	private volatile int asteroidsCollected;

	private volatile int fuelcansSeen;
	private volatile int asteroidsSeen;

	private Thread thread;

	private Trend asteroidTrend;
	private Trend fuelTrend;

	private long startTime;
	private long pauseStartTime;

	private final CustomFacebookClient facebook;

	private long lastPointsAdded;

	private final Browser browser;

	private double rating;

	public GameLogic(final Person player, final Client client, final CustomFacebookClient facebook, final Browser browser) {
		this.browser = browser;
		this.listeners = new CopyOnWriteArrayList<GameListener>();
		this.player = player;
		this.client = client;
		this.facebook = facebook;
	}

	public void collide(final Item item) {
		switch (item) {
		case FUELCAN:
			fuelcansCollected++;
			fuelInRow++;
			asteroidsInRow = 0;

			Sounds.FUELCAN.play();
			break;
		case ASTEROID:
			asteroidsCollected++;
			asteroidsInRow++;
			fuelInRow = 0;
			Sounds.ASTEROID.play();
			break;
		}

		final double rating = getRating();
		final long increase = (long) (rating * 10 * ((fuelInRow * POINTS_FOR_FUEL) - (asteroidsInRow * POINTS_FOR_ASTEROID)));
		gameRound.increaseScore(increase);

		lastPointsAdded = increase;

		fireCollided(item);
	}

	public void seen(final Item item, final boolean collision) {
		switch (item) {
		case FUELCAN:
			fuelTrend.update(collision);
			fuelcansSeen++;
			break;
		case ASTEROID:
			asteroidTrend.update(collision);
			asteroidsSeen++;
			break;
		}
	}

	public void run() {
		log.info("running " + this);

		fireGameStarted();

		while (isRunning()) {
			try {
				Thread.sleep(100);
			} catch (final InterruptedException ex) {
				// ignore
			}
		}

		storeRank();
		fireGameStopped();

		log.info("stopped " + this);
	}

	private boolean isRunning() {
		return getElapsedTime() <= getExpectedPlaytime();
	}

	private void storeRank() {
		try {
			final CustomFacebookClient fb = getFacebookClient();
			final Set<Long> persons = JSONUtils.toLongs(fb.friends_get());
			persons.add(getPlayerId());

			final Client client = getClient();
			final List<Highscore> globalScores = client.getHighscores(new ArrayList<Long>(persons));

			// determine rank of player within his friends
			int counter = 1;
			for (final Iterator<Highscore> iterator = globalScores.iterator(); iterator.hasNext();) {
				final Highscore highscore = iterator.next();
				if (highscore.getPersonid() == getPlayerId()) {
					gameRound.setSocialRank(counter);
					gameRound.setGlobalRank(highscore.getPercentage());
					break;
				}
				counter++;
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public void addListener(final GameListener listener) {
		this.listeners.add(listener);
		listener.added(this);
	}

	public void removeListener(final GameListener listener) {
		this.listeners.remove(listener);
		listener.removed(this);
	}

	public void newSession() throws IOException {
		final ScenarioSession scenarioSession = client.downloadScenarioSession(getPlayerId());
		this.gameSession = new GameSession(getPlayerId(), scenarioSession);
	}

	public void uploadSession() {
		log.info("uploading game session to server");
		client.uploadQuietly(gameSession);
		gameSession = null; // if this breaks stuff, that stuff is broken
	}

	public void start(final ScenarioRound round) {
		if (thread != null && thread.isAlive()) {
			throw new IllegalStateException("A game is still running.");
		}
		if (round == null) {
			throw new IllegalArgumentException("scenario round is null");
		}

		log.info("starting new game round");

		this.fuelTrend = new Trend();
		this.asteroidTrend = new Trend();
		this.fuelcansCollected = 0;
		this.fuelcansSeen = 0;
		this.asteroidsCollected = 0;
		this.asteroidsSeen = 0;

		gameRound = gameSession.newRound(round);
		addListener(gameRound.getListener());

		// reset internal state
		fuelcansSeen = 0;
		asteroidsSeen = 0;
		
		fuelInRow = 0;
		asteroidsInRow = 0;

		startTime = System.currentTimeMillis();

		// spawn new thread for game updates
		this.thread = new Thread(this, GameLogic.class.getSimpleName());
		thread.setDaemon(true);
		thread.start();
	}

	public void pause() {
		if (isRunning()) {
			pauseStartTime = System.currentTimeMillis();
			paused = true;
			configChange(ConfigKey.PAUSE, paused);
			fireGamePaused();
		} else {
			log.warn("not running");
		}
	}

	public void unpause() {
		if (isRunning()) {
			startTime += (System.currentTimeMillis() - pauseStartTime);
			paused = false;
			configChange(ConfigKey.PAUSE, paused);
			fireGameResumed();
		} else {
			log.warn("not running");
		}
	}

	private void fireGameStarted() {
		for (final GameListener listener : listeners) {
			listener.gameStarted(this);
		}
	}

	private void fireGamePaused() {
		for (final GameListener listener : listeners) {
			listener.gamePaused(this);
		}
	}

	private void fireGameResumed() {
		for (final GameListener listener : listeners) {
			listener.gameResumed(this);
		}
	}

	private void fireGameStopped() {
		for (final GameListener listener : listeners) {
			listener.gameStopped(this);
		}
	}

	private void fireCollided(final Item item) {
		for (final GameListener listener : listeners) {
			listener.collided(this, item);
		}
	}

	public long getPlayerId() {
		return getPlayer().getId();
	}

	public Person getPlayer() {
		return player;
	}

	public float getSlidingFuelRatio() {
		return fuelTrend.getMidRatio();
	}

	public float getSlidingAsteroidRatio() {
		return asteroidTrend.getMidRatio();
	}

	public float getTotalFuelRatio() {
		return fuelcansSeen == 0 ? 0 : fuelcansCollected / (float) fuelcansSeen;
	}

	public float getTotalAsteroidRatio() {
		return asteroidsSeen == 0 ? 0 : asteroidsCollected / (float) asteroidsSeen;
	}

	public long getElapsedTime() {
		if (paused) {
			return pauseStartTime - startTime;
		} else {
			return System.currentTimeMillis() - startTime;
		}
	}

	public long getRemainingTime() {
		return getExpectedPlaytime() - getElapsedTime();
	}

	private long getExpectedPlaytime() {
		return getCurrentScenarioRound().getExpectedPlaytime() / 10;
	}

	public long getScore() {
		return gameRound.getScore().getScore();
	}

	public long getPointsAdded() {
		return lastPointsAdded;
	}

	public double getRating() {
		return rating;
	}

	public DifficultyFunction getDifficultyFunction() {
		return getCurrentScenarioRound().getDifficultyFunction();
	}

	public ScenarioSession getCurrentScenarioSession() {
		return gameSession.getScenarioSession();
	}
	
	public GameSession getCurrentGameSession(){
		return gameSession;
	}

	public void setBaseline(double baseline) {
		//FIXME: baseline probably should be double or speed should be long
		gameSession.setBaseline(new Difficulty(0, (long) baseline, 0));
	}

	public Difficulty getBaseline() {
		return gameSession.getBaseline();
	}

	public ScenarioRound getCurrentScenarioRound() {
		return gameRound.getScenarioRound();
	}

	public void configChange(final ConfigKey key, final Object value){
		if (this.gameRound != null) {
			this.gameRound.configChange(key, String.valueOf(value));
		} else {
			log.info("ConfigChange not saved in Database as there is no GameRound active.");
		}
	}
	
	public Client getClient() {
		return client;
	}

	public CustomFacebookClient getFacebookClient() {
		return facebook;
	}

	public Trend getAsteroidTrend() {
		return asteroidTrend;
	}

	public Trend getFuelTrend() {
		return fuelTrend;
	}

	public void saveRoundAnswers(final List<Answer> answers) {
		gameRound.setAnswers(answers);
		gameRound = null; // if this breaks stuff, the stuff is broken
	}

	public void saveSessionAnswers(final List<Answer> answers) {
		gameSession.setAnswers(answers);
	}

	public Browser getBrowser() {
		return browser;
	}

	public void setRating(final double difficultyRating) {
		rating = difficultyRating;
	}

	public boolean isPaused() {
		return paused;
	}
	
	@Override
	public String toString() {
		return getClass().getName() + "[running=" + isRunning() + ";paused=" + paused
			+ ";startTime=" + startTime + ";pauseStartTime=" + pauseStartTime + "]";
	}
}