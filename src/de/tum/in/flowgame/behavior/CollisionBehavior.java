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
	private double shipX = Ship.INITIAL_SHIP_PLACEMENT_X;
	private double shipY = Ship.INITIAL_SHIP_PLACEMENT_Y;
	private double shipOldX;
	private double shipOldY;

	public CollisionBehavior(final BranchGroup collidables, final GameLogic gl,
			final Ship ship) {
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

		shipOldX = shipX;
		shipOldY = shipY;
		final Vector3d shipCoords = ship.getControls().getCoords();
		shipX = shipCoords.getX();
		shipY = shipCoords.getY();
		// final double shipZ = shipCoords.getZ();

		while (children.hasMoreElements()) {
			final Node child = children.nextElement();
			if (child instanceof Collidable) {
				final Collidable collidable = (Collidable) child;

				final double zPos = collidable.getZPos();
				// final double xPos = collidable.getXPos();
				// final double yPos = collidable.getYPos();

				final double oldZ = collidable.getOldZPos();

				if (zPos > Ship.INITIAL_SHIP_PLACEMENT_Z
						&& oldZ <= Ship.INITIAL_SHIP_PLACEMENT_Z) {

					final Bounds collidableBounds = collidable.getBounds();

					double factor = (zPos - Ship.INITIAL_SHIP_PLACEMENT_Z)/(zPos-oldZ);
					double xDist = shipX - shipOldX;
					double colX = shipOldX + xDist * factor;
					double yDist = shipY - shipOldY;
					double colY = shipOldY + yDist * factor;
					final BoundingBox shipBounds = new BoundingBox();
					shipBounds.setLower(colX - 0.6, colY - 0.15, oldZ);
					shipBounds.setUpper(colX + 0.6, colY + 0.15, zPos);
					// ship.setBounds(shipBounds);

					if (collidableBounds.intersect(shipBounds)) {
						// System.out.println("COLLISION! BAAAAM!" + oldz + ", "
						// + zPos + child.getUserData());
						// System.out.println("shipX: " + shipX + " - shipY: " +
						// shipY
						// + " - shipZ: " + shipZ);
						// System.out.println("colX: " + xPos + " - colY: " +
						// yPos
						// + " - colZ: " + zPos);
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
