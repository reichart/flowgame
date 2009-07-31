package de.tum.in.flowgame;

import de.tum.in.flowgame.model.Collision.Item;

/**
 * Empty implementations of all {@link GameListener} methods.
 */
public abstract class DefaultGameListener implements GameListener {

	@Override
	public void collided(final GameLogic logic, final Item item) {
		// empty
	}

	@Override
	public void gamePaused(final GameLogic game) {
		// empty
	}

	@Override
	public void gameResumed(final GameLogic game) {
		// empty
	}

	@Override
	public void gameStarted(final GameLogic game) {
		// TODO empty
	}

	@Override
	public void gameStopped(final GameLogic game) {
		// TODO empty
	}

}
