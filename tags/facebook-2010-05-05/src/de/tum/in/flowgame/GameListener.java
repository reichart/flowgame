package de.tum.in.flowgame;

import de.tum.in.flowgame.model.Collision.Item;

public interface GameListener {

	/**
	 * Notifies the listener that he has been added as listener.
	 * 
	 * @param game
	 *            The {@link GameLogic} he has been added to.
	 */
	void added(GameLogic game);

	/**
	 * Notifies the listener that he has been removed as listener.
	 * 
	 * @param game
	 *            The {@link GameLogic} he has been removed from.
	 */
	void removed(GameLogic game);

	/**
	 * Notifies the listener that the game has started.
	 * 
	 * @param game
	 *            The {@link GameLogic} that started.
	 */
	void gameStarted(GameLogic game);

	/**
	 * Notifies the listener that the game has paused.
	 * 
	 * @param game
	 *            The {@link GameLogic} that paused.
	 */
	void gamePaused(GameLogic game);

	/**
	 * Notifies the listener that the game has resumed.
	 * 
	 * @param game
	 *            The {@link GameLogic} that resumed.
	 */
	void gameResumed(GameLogic game);

	/**
	 * Notifies the listener that the game has stopped.
	 * 
	 * @param game
	 *            The {@link GameLogic} that stopped.
	 */

	void gameStopped(GameLogic game);

	/**
	 * Notifies the listener that a collision happened.
	 * 
	 * @param game
	 *            The {@link GameLogic} that pilots our game.
	 * @param item
	 *            The item that collided with the player.
	 */
	void collided(GameLogic game, Item item);
}
