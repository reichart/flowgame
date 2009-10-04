package de.tum.in.flowgame;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import de.tum.in.flowgame.client.Client;
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
	
	private boolean baseline = false;

	public GameLogic(final Person player) {
		this.listeners = new CopyOnWriteArrayList<GameListener>();
		this.player = player;
		
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

	public void seen(final Item item) {
		switch (item) {
		case FUELCAN:
			fuelcansSeen++;
			break;
		case ASTEROID:
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
	}

	public int getFuel() {
		return fuel;
	}

	public int getAsteroids() {
		return asteroids;
	}

	public void addListener(final GameListener listener) {
		synchronized (listeners) {
			this.listeners.add(listener);
		}
	}
	
	public void removeListener(final GameListener listener) {
		synchronized (listeners) {
			this.listeners.remove(listener);
		}
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
			gameSession.setScenarioSession(Client.downloadScenarioSession(player));
			gameSession.setPlayer(player);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void start() {
		System.out.println("GameLogic.start()");

		if (isRunning()) {
			throw new IllegalStateException("A game is still running.");
		}
		
		if (gameSession == null) {
			loadNewScenarioSession();	
		} else {
			ScenarioRound sessionRound = gameSession.getScenarioSession().getNextRound();
			if (sessionRound == null) {
				Client.uploadQuietly(gameSession);
				gameSession = null;
				fireSessionFinished();
			}			
		}
		
		GameRound gameRound = new GameRound();
		gameRound.setScenarioRound(getCurrentScenarioRound());
		
		gameSession.addRound(gameRound);
		addListener(gameRound);

		// reset internal state
		fuel = MAX_FUEL;
		asteroids = 0;
		fuelcansSeen = 0;
		asteroidsSeen = 0;
		paused = false;

		// spawn new thread for game updates
		this.thread = new Thread(this, GameLogic.class.getSimpleName());
		thread.setDaemon(true);
		thread.start();
	}

	public void pause() {
		this.paused = true;
		fireGamePaused();
	}

	public void unpause() {
		this.paused = false;
		fireGameResumed();
	}

	private void fireGameStarted() {
		synchronized (listeners) {
			for (final GameListener listener : listeners) {
				listener.gameStarted(this);
			}
		}
	}

	private void fireGamePaused() {
		synchronized (listeners) {
			for (final GameListener listener : listeners) {
				listener.gamePaused(this);
			}
		}
	}

	private void fireGameResumed() {
		synchronized (listeners) {
			for (final GameListener listener : listeners) {
				listener.gameResumed(this);
			}
		}
	}

	private void fireGameStopped() {
		synchronized (listeners) {
			for (final GameListener listener : listeners) {
				listener.gameStopped(this);
			}
		}
	}

	private void fireCollided(Item item) {
		synchronized (listeners) {
			for (final GameListener listener : listeners) {
				listener.collided(this, item);
			}
		}
	}
	
	public void fireSessionFinished() {
		synchronized (listeners) {
			for (final GameListener listener : listeners) {
				listener.sessionFinished(this);
			}
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
	
	public float getFuelRatio() {
		return fuelcansSeen == 0 ? 0 : fuelcansCollected / (float)fuelcansSeen;
	}

	public float getAsteroidRatio() {
		return asteroidsSeen == 0 ? 0 : asteroidsCollected / (float)asteroidsSeen;
	}
}