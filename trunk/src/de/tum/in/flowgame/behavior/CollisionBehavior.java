package de.tum.in.flowgame.behavior;

import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Node;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupOnElapsedFrames;
import javax.vecmath.Vector3d;

import de.tum.in.flowgame.Collidable;
import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.Ship;

public class CollisionBehavior extends Behavior {
	private final WakeupCondition condition;
	private final GameLogic gameLogic;
	private final BranchGroup branchGroup;
	private final Ship ship;

	public CollisionBehavior(final BranchGroup collidables, final GameLogic gl, final Ship ship) {
		condition = new WakeupOnElapsedFrames(0);
		branchGroup = collidables;
		gameLogic = gl;
		this.ship = ship;
	}

	@Override
	public void initialize() {
		wakeupOn(condition);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void processStimulus(final Enumeration criteria) {
		final Enumeration<Node> children = branchGroup.getAllChildren();
		
		final Vector3d shipCoords = ship.getControls().getCoords();
		final double shipX = shipCoords.getX();
		final double shipY = shipCoords.getY();
//		final double shipZ = shipCoords.getZ();

		while (children.hasMoreElements()) {
			final Node child = children.nextElement();
			if (child instanceof Collidable) {
				final Collidable collidable = (Collidable) child;

				final double zPos = collidable.getZPos();
//				final double xPos = collidable.getXPos();
//				final double yPos = collidable.getYPos();

				final double oldz = collidable.getOldZPos();

				if (zPos > Ship.INITIAL_SHIP_PLACEMENT_Z
						&& oldz <= Ship.INITIAL_SHIP_PLACEMENT_Z) {

					final Bounds collidableBounds = collidable.getBounds();
					final BoundingBox shipBounds = new BoundingBox();
					shipBounds.setLower(shipX - 0.6, shipY - 0.15, oldz);
					shipBounds.setUpper(shipX + 0.6, shipY + 0.15, zPos);
//					ship.setBounds(shipBounds);


					if (collidableBounds.intersect(shipBounds)) {
//						System.out.println("COLLISION! BAAAAM!" + oldz + ", "
//								+ zPos + child.getUserData());
//						System.out.println("shipX: " + shipX + " - shipY: " + shipY
//								+ " - shipZ: " + shipZ);
//						System.out.println("colX: " + xPos + " - colY: " + yPos
//								+ " - colZ: " + zPos);
						gameLogic.collide(child);
					}
					((BranchGroup) child).detach();
				}

				collidable.setOldZPos(zPos);
			}
		}
		wakeupOn(condition);
	}
}
