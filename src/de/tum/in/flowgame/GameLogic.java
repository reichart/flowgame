package de.tum.in.flowgame;

import java.util.ArrayList;
import java.util.List;

import javax.media.j3d.Node;

import de.tum.in.flowgame.client.Client;
import de.tum.in.flowgame.client.DownloadScenarioSession;
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

	private Thread thread;
	private boolean paused;
	
	private boolean baseline = false;

	private GameRoundStorer gameRoundStorer;

	public GameLogic(final Person player) {
		this.listeners = new ArrayList<GameListener>();

		this.player = player;
		
		Utils.export(this);
		gameRoundStorer = new GameRoundStorer();
	}

	public void collide(final Node node) {
		final Item item = (Item) node.getUserData();

		fireCollided(item);

		switch (item) {
		case FUELCAN:
			if (fuel < MAX_FUEL) {
				fuel++;
			}
			Sounds.FUELCAN.play();
			break;
		case ASTEROID:
			if (asteroids < MAX_ASTEROIDS) {
				asteroids++;
			}
			Sounds.ASTEROID.play();
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
		
		Client client = new Client();
		client.updateGameSession(gameSession);
		
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
			DownloadScenarioSession ds = new DownloadScenarioSession();
			gameSession.setScenarioSession(ds.download(player));
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
		}

		ScenarioRound sessionRound = gameSession.getScenarioSession().getNextRound();
		if (sessionRound == null) {
			Client client = new Client();
			client.updateGameSession(gameSession);
			loadNewScenarioSession();
		}
		
		GameRound gameRound = new GameRound();
		
		gameSession.addRound(gameRound);
		gameRoundStorer.setGameRound(gameRound);

		// reset internal state
		fuel = 10;
		asteroids = 0;
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
	
	public ScenarioRound getCurrentScenarioRound() {
		//TODO: iterate those
		return gameSession.getScenarioSession().getRounds().get(0);
	}
	
}