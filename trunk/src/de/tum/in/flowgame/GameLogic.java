package de.tum.in.flowgame;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import de.tum.in.flowgame.client.Client;
import de.tum.in.flowgame.model.DifficultyFunction;
import de.tum.in.flowgame.model.GameRound;
import de.tum.in.flowgame.model.GameSession;
import de.tum.in.flowgame.model.Person;
import de.tum.in.flowgame.model.ScenarioRound;
import de.tum.in.flowgame.model.Collision.Item;

public class GameLogic implements GameLogicMBean, Runnable {

	public final static int MAX_ASTEROIDS = 10;
	public final static int MAX_FUEL = 10;

	private GameSession gameSession;
	private Person player;

	private final List<GameListener> listeners;

	private volatile int fuel;
	private volatile int asteroids;

	private volatile int fuelcansCollected;
	private volatile int asteroidsCollected;

	private volatile int fuelcansSeen;
	private volatile int asteroidsSeen;

	private Thread thread;
	private boolean paused;

	private GameRound gameRound = new GameRound();

	private boolean baseline = false;
	// private Function baselineInterval;
	// private Function baselineSpeed;
	// private Function baselineRatio;

	private Trend asteroidTrend = new Trend();
	private Trend fuelTrend = new Trend();

	private long startTime;
	private long startTimeWithoutPause;
	private long pauseStartTime;

	// private int points = 0;

	public GameLogic(final Person player) {
		this.listeners = new CopyOnWriteArrayList<GameListener>();
		this.player = player;
		this.init();

		Utils.export(this);
	}

	public void collide(final Item item) {

		fireCollided(item);

		switch (item) {
		case FUELCAN:
			if (fuel < MAX_FUEL) {
				fuel++;
			}
			fuelcansCollected++;
			Sounds.FUELCAN.play();
			break;
		case ASTEROID:
			if (asteroids < MAX_ASTEROIDS) {
				asteroids++;
			}
			asteroidsCollected++;
			Sounds.ASTEROID.play();
			break;
		}

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

	@Override
	public void run() {
		System.out.println("GameLogic.run() starting");

		fireGameStarted();

		while (asteroids < MAX_ASTEROIDS && fuel > 0) {
			try {
				Thread.sleep(4000);
			} catch (final InterruptedException ex) {
				// ignore
			}

			if (!paused) {
				fuel--;

				if (fuel == 0) {
					break;
				}
			}
		}

		fireGameStopped();

		Client.uploadQuietly(gameSession);

		System.out.println("GameLogic.run() stopped");

		ScenarioRound sessionRound = gameSession.getScenarioSession()
				.getNextRound();
		if (sessionRound == null) {
			Client.uploadQuietly(gameSession);
			gameSession = null;
			fireSessionFinished();
		}
	}

	public int getFuel() {
		return fuel;
	}

	public int getAsteroids() {
		return asteroids;
	}

	public void addListener(final GameListener listener) {
		this.listeners.add(listener);
	}

	public void removeListener(final GameListener listener) {
		this.listeners.remove(listener);
	}

	/**
	 * @return <code>true</code> if a game is running, <code>false</code>
	 *         otherwise
	 */
	public boolean isRunning() {
		if (thread == null) {
			return false;
		} else {
			return thread.isAlive();
		}
	}

	private void loadNewScenarioSession() {
		try {
			gameSession = new GameSession();
			// download Scenario that should be played next
			gameSession.setScenarioSession(Client
					.downloadScenarioSession(player));
			gameSession.setPlayer(player);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void init() {
		if (gameSession == null) {
			loadNewScenarioSession();
		} else {
			ScenarioRound sessionRound = gameSession.getScenarioSession()
					.getNextRound();
			if (sessionRound == null) {
				Client.uploadQuietly(gameSession);
				gameSession = null;
				fireSessionFinished();
			}
		}
	}

	public void start() {
		System.out.println("GameLogic.start()");

		if (isRunning()) {
			throw new IllegalStateException("A game is still running.");
		}

		gameRound = new GameRound();
		gameRound.setScenarioRound(getCurrentScenarioRound());

		gameSession.addRound(gameRound);
		addListener(gameRound);

		// reset internal state
		fuel = MAX_FUEL;
		asteroids = 0;
		fuelcansSeen = 0;
		asteroidsSeen = 0;
		paused = false;
		startTime = System.currentTimeMillis();
		startTimeWithoutPause = startTime;

		// spawn new thread for game updates
		this.thread = new Thread(this, GameLogic.class.getSimpleName());
		thread.setDaemon(true);
		thread.start();
	}

	public void pause() {
		this.paused = true;
		pauseStartTime = System.currentTimeMillis();
		fireGamePaused();
	}

	public void unpause() {
		this.paused = false;
		startTimeWithoutPause += (System.currentTimeMillis() - pauseStartTime);
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

	public void fireSessionFinished() {
		for (final GameListener listener : listeners) {
			listener.sessionFinished(this);
		}
	}

	public ScenarioRound getCurrentScenarioRound() {
		return gameSession.getScenarioSession().getCurrentRound();
	}

	public Person getPlayer() {
		return player;
	}

	public int getAsteroidsSeen() {
		return asteroidsSeen;
	}

	public int getFuelcansSeen() {
		return fuelcansSeen;
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
		return asteroidsSeen == 0 ? 0 : asteroidsCollected
				/ (float) asteroidsSeen;
	}

	// private GameRound dummyRoundForBaseline() {
	// GameRound gameRound = new GameRound();
	// ScenarioRound scenarioRound = new ScenarioRound();
	// DifficultyFunction difficultyFunction = new DifficultyFunction();
	// baselineInterval = new ConstantFunction(100.0);
	// difficultyFunction.setIntervald(baselineInterval);
	// baselineRatio = new ConstantFunction(0.8);
	// difficultyFunction.setRatio(baselineRatio);
	// baselineSpeed = new LinearFunction(80.0, 0.005);
	// difficultyFunction.setSpeed(baselineSpeed);
	// scenarioRound.setDifficutyFunction(difficultyFunction);
	// gameRound.setScenarioRound(scenarioRound);
	// return gameRound;
	// }
	//	
	// private void calculatePoints(Item item){
	// DifficultyFunction diff =
	// gameSession.getScenarioSession().getCurrentRound().getDifficutyFunction();
	// Function intervalFunction = diff.getInterval();
	// Function speedFunction = diff.getSpeed();
	// Function ratioFunction = diff.getRatio();
	// }

	public long getElapsedTime() {
		long actTime = System.currentTimeMillis();
		return actTime - startTimeWithoutPause;
	}
	
	public DifficultyFunction getDifficultyFunction(){
		return gameSession.getScenarioSession().getCurrentRound().getDifficutyFunction();
	}

}