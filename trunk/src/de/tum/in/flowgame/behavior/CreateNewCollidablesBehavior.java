package de.tum.in.flowgame.behavior;

import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupOnElapsedFrames;

import de.tum.in.flowgame.CreateCollidables;
import de.tum.in.flowgame.Ship;

public class CreateNewCollidablesBehavior extends Behavior {

	private final WakeupCondition condition = new WakeupOnElapsedFrames(0);
	private final Ship ship;
	private double offset = -400.0;
	private final CreateCollidables createCollidables;

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
//		System.out.println("Last Collidable: " + createCollidables.getLastCollidable().getZPos() + " - ShipPosition plus Offset: " + (ship.getControls().getCoords().getZ() + offset));
		if (createCollidables.getLastCollidable().getZPos() >= ship.getControls().getCoords().getZ() + offset){
			createCollidables.addCollidable();
		}
		wakeupOn(condition);
	}

}
