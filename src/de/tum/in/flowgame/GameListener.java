package de.tum.in.flowgame;

import de.tum.in.flowgame.model.Collision.Item;

public interface GameListener {

	/**
	 * Notifies the listener that he has been added as listener.
	 * 
	 * @param game
	 *            the game he has been added to
	 */
	void added(GameLogic game);

	/**
	 * Notifies the listener that he has been removed as listener.
	 * 
	 * @param game
	 *            the game he has been removed from
	 */
	void removed(GameLogic game);
	
	void gameStarted(GameLogic game);

	void gamePaused(GameLogic game);
	
	void gameResumed(GameLogic game);

	void gameStopped(GameLogic game);
	
//	void sessionFinished(GameLogic game);
	
	void collided(GameLogic logic, Item item);
}
