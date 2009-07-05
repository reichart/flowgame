package de.tum.in.flowgame;

import java.util.ArrayList;
import java.util.List;

import javax.media.j3d.Node;

import de.tum.in.flowgame.behavior.CreateCollidablesBehavior;
import de.tum.in.flowgame.behavior.SpeedChangeBehavior;
import de.tum.in.flowgame.model.GameRound;

public class GameLogic extends Thread implements GameLogicMBean {

	public enum Item {
		FUELCAN(Sounds.FUELCAN), ASTEROID(Sounds.ASTEROID);

		private final Sounds snd;

		private Item(final Sounds snd) {
			this.snd = snd;
		}

		void play() {
			snd.play();
		}
	};

	public final static int MAX_ASTEROIDS = 10;
	public final static int MAX_FUEL = 10;

	private final List<GameListener> listeners;

	private final Tunnel tunnel;

	private volatile int fuel = 10;
	private int asteroids;

	private final CreateCollidablesBehavior ccb;

	public GameLogic(final CreateCollidablesBehavior ccb, final Tunnel tunnel) {
		this.listeners = new ArrayList<GameListener>();
		this.ccb = ccb;
		this.tunnel = tunnel;

		final SpeedChangeBehavior speedChange = new SpeedChangeBehavior(this.tunnel.getFwdNav());
		speedChange.setSchedulingBounds(Game3D.WORLD_BOUNDS);
		tunnel.addChild(speedChange);

		final GameRound round = new GameRound();
		addListener(round);

		Utils.export(this);
	}

	public void collide(final Node node) {
		final Item item = (Item) node.getUserData();

		fireCollided(item);

		switch (item) {
		case FUELCAN:
			fuel++;
			break;
		case ASTEROID:
			asteroids++;
			break;
		}

		item.play();
	}

	@Override
	public void run() {
		fireGameStarted();

		while (asteroids < MAX_ASTEROIDS && fuel > 0) {
			try {
				Thread.sleep(1000);
			} catch (final InterruptedException ex) {
				// ignore
			}

			fuel--;

			if (fuel == 0) {
				break;
			}
		}

		fireGameStopped();
	}

	public int getFuel() {
		return fuel;
	}

	public int getAsteroids() {
		return asteroids;
	}

	public double getTunnelSpeed() {
		return this.tunnel.getFwdNav().getSpeed();
	}

	public void setTunnelSpeed(final double tunnelSpeed) {
		this.tunnel.getFwdNav().setSpeed(tunnelSpeed);
	}

	public void addListener(final GameListener listener) {
		this.listeners.add(listener);
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
}