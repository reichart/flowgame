package de.tum.in.flowgame.behavior;

import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Node;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupOnElapsedFrames;

import de.tum.in.flowgame.Collidable;
import de.tum.in.flowgame.Ship;

public class CollisionBehavior extends Behavior {
	private final WakeupCondition condition;
	BranchGroup branchGroup;
	
	public CollisionBehavior(BranchGroup collidables) {
		condition = new WakeupOnElapsedFrames(0);
		branchGroup = collidables;
	}
	
	@Override
	public void initialize() {
		wakeupOn(condition);
	}

	@Override
	public void processStimulus(Enumeration criteria) {
		Enumeration<Node> children = branchGroup.getAllChildren();
		Ship ship = null;
		while (children.hasMoreElements() && ship == null) {
			Node child = children.nextElement();
			if (child instanceof Ship) {
				ship = (Ship) child;
				System.out.println("Ship: " + ship.getXPos() + ", " + ship.getYPos());
			}
		}
		while (children.hasMoreElements()) {
			Node child = children.nextElement();
			if (child instanceof Collidable) {
				Collidable collidable = (Collidable) child;
				
				final double zPos = collidable.getZPos();
//				System.out.println(collidable.getXPos() + ", " + collidable.getYPos() + ", " + zPos);
				collidable.setOldZPos(zPos);
			}
		}
		wakeupOn(condition);
	}

}
