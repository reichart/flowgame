package de.tum.in.flowgame.behavior;

import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Node;
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
				// System.out.println("Ship: " + ship.getXPos() + ", " +
				// ship.getYPos());
			}
		}
		final double shipX = ship.getVector3dtShipPos().getX();
		final double shipY = ship.getVector3dtShipPos().getY();
		final double shipZ = ship.getVector3dtShipPos().getZ();

		while (children.hasMoreElements()) {
			Node child = children.nextElement();
			if (child instanceof Collidable) {
				Collidable collidable = (Collidable) child;

				final double zPos = collidable.getZPos();
				final double xPos = collidable.getXPos();
				final double yPos = collidable.getYPos();

				final double oldz = collidable.getOldZPos();

				if (zPos > 0 && oldz <= 0) {

					final Bounds collidableBounds = collidable.getBounds();

					BoundingBox newBounds = new BoundingBox();
					newBounds.setLower(shipX, shipY, oldz);
					newBounds.setUpper(shipX, shipY, zPos);

					if (collidableBounds.intersect(newBounds)) {
						System.out.println("COLLISION! BAAAAM!" + oldz + ", "
								+ zPos);

					}
				}

				// double xdiff = xPos-shipX;
				// double ydiff = yPos-shipY;
				// double zdiff = zPos-shipZ;
				//				
				// System.out.println("Difference:"+ xdiff + ", "+ydiff+ ",
				// "+zdiff);
				//				
				// System.out.println(collidable.getXPos() + ", " +
				// collidable.getYPos() + ", " + zPos);
				collidable.setOldZPos(zPos);
			}
		}
		wakeupOn(condition);
	}

}
