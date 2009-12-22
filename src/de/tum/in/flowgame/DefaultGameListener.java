package de.tum.in.flowgame;

import de.tum.in.flowgame.model.Collision.Item;

/**
 * Empty implementations of all {@link GameListener} methods.
 */
public abstract class DefaultGameListener implements GameListener {

	public void added(final GameLogic game) {
		// empty
	}
	
	public void removed(final GameLogic game) {
		// empty
	}
	
	public void collided(final GameLogic logic, final Item item) {
		// empty
	}

	public void gamePaused(final GameLogic game) {
		// empty
	}

	public void gameResumed(final GameLogic game) {
		// empty
	}

	public void gameStarted(final GameLogic game) {
		// TODO empty
	}

	public void gameStopped(final GameLogic game) {
		// TODO empty
	}
	
//	@Override
//	public void sessionFinished(GameLogic game) {
//		// TODO Auto-generated method stub
//		
//	}

}