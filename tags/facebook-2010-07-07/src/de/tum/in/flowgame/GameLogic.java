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
import de.tum.in.flowgame.facebook.CustomFacebookClient;
import de.tum.in.flowgame.facebook.JSONUtils;
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
import de.tum.in.flowgame.strategy.Trend;
import de.tum.in.flowgame.util.Browser;

public class GameLogic implements Runnable {

	private final static Log log = LogFactory.getLog(GameLogic.class);

	private static final int pointsForFuel = 10;
	private static final int pointsForAsteroid = 5;

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
	private long startTimeWithoutPause;
	private long pauseStartTime;

	private final CustomFacebookClient facebook;

	private long lastPointsAdded;

	private final Browser browser;

	private double rating;

	public GameLogic(final Person player, final Client client, final CustomFacebookClient facebook, Browser browser) {
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

		double rating = getRating();
		long increase = (long) (rating * 10 * ((fuelInRow * pointsForFuel) - (asteroidsInRow * pointsForAsteroid)));
		gameRound.increaseScore(increase);

		lastPointsAdded = increase;

		fireCollided(item);
	}

	public void seen(final Item item, boolean collision) {
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
		log.info("running");

		fireGameStarted();

		boolean timeOver = false;
		Long maxPlaytime = getCurrentScenarioRound().getExpectedPlaytime();

		while (!timeOver) {
			try {
				Thread.sleep(1000);
			} catch (final InterruptedException ex) {
				// ignore
			}

			if (maxPlaytime != null) {
				timeOver = getElapsedTime() > maxPlaytime;
			}
		}

		storeRank();
		fireGameStopped();

		log.info("stopped");
	}

	private void storeRank() {
		try {
			final CustomFacebookClient fb = getFacebookClient();
			final Set<Long> persons = JSONUtils.toLongs(fb.friends_get());
			persons.add(getPlayerId());

			Client client = getClient();
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
		} catch (Exception e) {
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
		startTimeWithoutPause = startTime;

		// spawn new thread for game updates
		this.thread = new Thread(this, GameLogic.class.getSimpleName());
		thread.setDaemon(true);
		thread.start();
	}

	public void pause() {
		pauseStartTime = System.currentTimeMillis();
		paused = true;
		fireGamePaused();
	}

	public void unpause() {
		startTimeWithoutPause += (System.currentTimeMillis() - pauseStartTime);
		paused = false;
		fireGameResumed();
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

	private void fireCollided(Item item) {
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
			return pauseStartTime - startTimeWithoutPause;
		} else {
			return System.currentTimeMillis() - startTimeWithoutPause;
		}
	}

	public long getRemainingTime() {
		return getCurrentScenarioRound().getExpectedPlaytime() - getElapsedTime();
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

	public void setBaseline(long baseline) {
		gameSession.setBaseline(new Difficulty(0, baseline, 0));
	}

	public Difficulty getBaseline() {
		return gameSession.getBaseline();
	}

	public ScenarioRound getCurrentScenarioRound() {
		return gameRound.getScenarioRound();
	}

	public void configChange(ConfigKey key, String value){
		if (this.gameRound != null) {
			this.gameRound.configChange(key, value);
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

	public void setRating(double difficultyRating) {
		rating = difficultyRating;
	}

	public boolean isPaused() {
		return paused;
	}
}