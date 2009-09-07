package de.tum.in.flowgame;

import de.tum.in.flowgame.model.AbstractEntity;
import de.tum.in.flowgame.model.Collision;
import de.tum.in.flowgame.model.GameRound;
import de.tum.in.flowgame.model.Collision.Item;

public class GameRoundStorer extends AbstractEntity implements GameListener {
	
	GameRound gameRound;

	public GameRoundStorer() {
		gameRound = new GameRound();
	}

	public Long getActualPlaytime() {
		return gameRound.getActualPlaytime();
	}

	@Override
	public void gameStarted(final GameLogic game) {
		gameRound.setStartTime(System.currentTimeMillis());
	}

	@Override
	public void gamePaused(final GameLogic game) {
		// TODO implement
	}

	public void gameStopped(final GameLogic game) {
		gameRound.setActualPlaytime((System.currentTimeMillis() - gameRound.getStartTime()));
		
		System.out.println(gameRound.getActualPlaytime());
		System.out.println("stop GameRound");
		
		//TODO: Think about whether we should save GameRound of only GameSession at the end of whole game
	}
	
	@Override
	public void collided(final GameLogic logic, final Item item) {
		//TODO: implement item properly
		gameRound.getCollisions().add(new Collision(null));
	}

	@Override
	public void gameResumed(GameLogic game) {
		// TODO Auto-generated method stub
	}

	public GameRound getGameRound() {
		return gameRound;
	}

	public void setGameRound(GameRound gameRound) {
		this.gameRound = gameRound;
	}
	
}