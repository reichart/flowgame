package de.tum.in.flowgame;

import java.util.ArrayList;
import java.util.List;

import javax.media.j3d.Node;

import de.tum.in.flowgame.model.Collision.Item;
import de.tum.in.flowgame.model.GameRound;
import de.tum.in.flowgame.model.GameSession;
import de.tum.in.flowgame.model.Person;

public class GameLogic implements GameLogicMBean, Runnable {

//	public enum Item {
//		FUELCAN(Sounds.FUELCAN), ASTEROID(Sounds.ASTEROID);
//
//		private final Sounds snd;
//
//		private Item(final Sounds snd) {
//			this.snd = snd;
//		}
//
//		void play() {
//			snd.play();
//		}
//	}

	public final static int MAX_ASTEROIDS = 10;
	public final static int MAX_FUEL = 10;
	
	private GameSession session;
	
	private final List<GameListener> listeners;

	private volatile int fuel;
	private volatile int asteroids;
	
	private Thread thread;
	private boolean paused;
	
	private GameRoundStorer gameRoundStorer;

	public GameLogic(final Person player) {
		this.listeners = new ArrayList<GameListener>();
		
		session = new GameSession();
		session.setPlayer(player);

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
	
	public void start() {
		System.out.println("GameLogic.start()");
		
		if (isRunning()) {
			throw new IllegalStateException("A game is still running.");
		}
		
		GameRound round = new GameRound();
		session.addRound(round);
		gameRoundStorer.setGameRound(round);
		
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
}