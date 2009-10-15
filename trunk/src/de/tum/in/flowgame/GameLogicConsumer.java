package de.tum.in.flowgame;

/**
 * For classes which need an {@link GameLogic} injected.
 */
public interface GameLogicConsumer {

	void setGameLogic(GameLogic game);
}
