package de.tum.in.flowgame.client;

import de.tum.in.flowgame.GameLogic;

/**
 * For classes which need an {@link GameLogic} injected.
 */
public interface GameLogicConsumer {

	void setGameLogic(GameLogic game);
}
