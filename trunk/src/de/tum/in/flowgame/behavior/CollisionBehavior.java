package de.tum.in.flowgame.behavior;

import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Node;
import javax.media.j3d.Transform3D;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupOnElapsedFrames;
import javax.vecmath.Vector3d;

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
				Vector3d vec = ship.getVector3dtShipPos();
//				System.out.println("Ship: " + ship.getXPos() + ", " + ship.getYPos());
			}
		}
		final double shipX = ship.getVector3dtShipPos().getX();
		final double shipY = ship.getVector3dtShipPos().getY();
		final double shipZ = ship.getVector3dtShipPos().getZ();
		final Bounds shipBounds = ship.getBounds();
		
		BoundingBox newBounds = new BoundingBox();
		
		while (children.hasMoreElements()) {
			Node child = children.nextElement();
			if (child instanceof Collidable) {
				Collidable collidable = (Collidable) child;
				
				final double zPos = collidable.getZPos();
				final double xPos = collidable.getXPos();
				final double yPos = collidable.getYPos();
				
				final double oldz = collidable.getOldZPos();
				final Bounds collidableBounds = collidable.getBounds();
				
				Transform3D trans = new Transform3D();
				
				newBounds.transform(shipBounds, trans);
				
				
//				double xdiff = xPos-shipX;
//				double ydiff = yPos-shipY;
//				double zdiff = zPos-shipZ;
//				
//				System.out.println("Difference:"+ xdiff + ", "+ydiff+ ", "+zdiff);
//				
//				System.out.println(collidable.getXPos() + ", " + collidable.getYPos() + ", " + zPos);
				collidable.setOldZPos(zPos);
			}
		}
		wakeupOn(condition);
	}

}
