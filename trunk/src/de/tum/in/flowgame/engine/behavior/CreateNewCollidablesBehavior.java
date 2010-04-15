package de.tum.in.flowgame.engine.behavior;

import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupOnElapsedFrames;

import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.GameLogicConsumer;
import de.tum.in.flowgame.engine.CreateCollidables;
import de.tum.in.flowgame.engine.Ship;

public class CreateNewCollidablesBehavior extends Behavior implements GameLogicConsumer {

	private final WakeupCondition condition = new WakeupOnElapsedFrames(0);
	private final Ship ship;
	private double offset = -400.0;
	private final CreateCollidables createCollidables;
	
	private GameLogic game;

	public CreateNewCollidablesBehavior(final CreateCollidables createCollidables, final Ship ship) {
		this.createCollidables = createCollidables;
		this.ship = ship;
	}

	public CreateNewCollidablesBehavior(final CreateCollidables createCollidables, final Ship ship, double offset) {
		this.createCollidables = createCollidables;
		this.ship = ship;
		this.offset = offset;
	}

	@Override
	public void initialize() {
		wakeupOn(condition);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void processStimulus(Enumeration criteria) {
		if (createCollidables.getLastCollidableZPos() >= ship.getCoords().z + offset) {
			createCollidables.addCollidable(game);
		}
		wakeupOn(condition);
	}

	public void setGameLogic(final GameLogic game) {
		this.game = game;
	}
}
