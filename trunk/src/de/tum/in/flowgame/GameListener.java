package de.tum.in.flowgame;

import de.tum.in.flowgame.model.Collision.Item;

public interface GameListener {

	void gameStarted(GameLogic game);

	void gamePaused(GameLogic game);
	
	void gameResumed(GameLogic game);

	void gameStopped(GameLogic game);
	
//	void sessionFinished(GameLogic game);
	
	void collided(GameLogic logic, Item item);
}
