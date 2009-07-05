package de.tum.in.flowgame.behavior;

import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Node;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupOnElapsedFrames;

import de.tum.in.flowgame.Collidable;
import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.Ship;

public class CollisionBehavior extends Behavior {
	private final WakeupCondition condition;
	private final GameLogic gameLogic;
	private final BranchGroup branchGroup;

	public CollisionBehavior(final BranchGroup collidables, final GameLogic gl) {
		condition = new WakeupOnElapsedFrames(0);
		branchGroup = collidables;
		gameLogic = gl;
	}

	@Override
	public void initialize() {
		wakeupOn(condition);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void processStimulus(final Enumeration criteria) {
		final Enumeration<Node> children = branchGroup.getAllChildren();
		final Ship ship = findShip(children);
		final double shipX = ship.getVector3dtShipPos().getX();
		final double shipY = ship.getVector3dtShipPos().getY();
		// final double shipZ = ship.getVector3dtShipPos().getZ();

		while (children.hasMoreElements()) {
			final Node child = children.nextElement();
			if (child instanceof Collidable) {
				final Collidable collidable = (Collidable) child;

				final double zPos = collidable.getZPos();
				// final double xPos = collidable.getXPos();
				// final double yPos = collidable.getYPos();

				final double oldz = collidable.getOldZPos();

				if (zPos > 0 && oldz <= 0) {

					final Bounds collidableBounds = collidable.getCollisionBounds();

					final BoundingBox newBounds = new BoundingBox();
					System.out
							.println("shipX: " + shipX + " - shipY: " + shipY);
					newBounds.setLower(shipX - 1, shipY - 0.5, oldz);
					newBounds.setUpper(shipX + 1, shipY + 0.5, zPos);

					if (collidableBounds.intersect(newBounds)) {
						System.out.println("COLLISION! BAAAAM!" + oldz + ", " + zPos + child.getUserData());
						gameLogic.collide(child);
						((BranchGroup) child).detach();
					}
				}

				collidable.setOldZPos(zPos);
			}
		}
		wakeupOn(condition);
	}

	private Ship findShip(final Enumeration<Node> children) {
		while (children.hasMoreElements()) {
			final Node child = children.nextElement();
			if (child instanceof Ship) {
				return (Ship) child;
				// Vector3d vec = ship.getVector3dtShipPos();
				// System.out.println("Ship: " + ship.getXPos() + ", " +
				// ship.getYPos());
			}
		}
		return null;
	}

}
