package de.tum.in.flowgame.engine.behavior;

import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.GameLogicConsumer;
import de.tum.in.flowgame.engine.CreateCollidables;
import de.tum.in.flowgame.engine.Ship;

public class CreateNewCollidablesBehavior extends RepeatingBehavior implements GameLogicConsumer {

	private final Ship ship;
	private double offset = -400.0;
	private final CreateCollidables createCollidables;
	
	private GameLogic game;

	public CreateNewCollidablesBehavior(final CreateCollidables createCollidables, final Ship ship) {
		this.createCollidables = createCollidables;
		this.ship = ship;
	}

	@Override
	protected void update() {
		if (createCollidables.getLastCollidableZPos() >= ship.getCoords().z + offset) {
			createCollidables.addCollidable(game);
		}
	}

	public void setGameLogic(final GameLogic game) {
		this.game = game;
	}
}
