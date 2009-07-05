package de.tum.in.flowgame;

import de.tum.in.flowgame.GameLogic.Item;

public interface GameListener {

	void gameStarted(GameLogic game);

	void gamePaused(GameLogic game);

	void gameStopped(GameLogic game);
	
	void collided(GameLogic logic, Item item);
}
