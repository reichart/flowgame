package de.tum.in.flowgame.engine.behavior;

import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Node;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupOnElapsedFrames;
import javax.vecmath.Vector3d;

import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.GameLogicConsumer;
import de.tum.in.flowgame.engine.Collidable;
import de.tum.in.flowgame.engine.Ship;
import de.tum.in.flowgame.model.Collision.Item;

public class CollisionBehavior extends Behavior implements GameLogicConsumer {
	private final WakeupCondition condition;
	private GameLogic gameLogic;
	private final BranchGroup branchGroup;
	private final Ship ship;
	private double shipX = Ship.INITIAL_SHIP_PLACEMENT_X;
	private double shipY = Ship.INITIAL_SHIP_PLACEMENT_Y;
	private double shipZ = Ship.INITIAL_SHIP_PLACEMENT_Z;
	private double shipOldX;
	private double shipOldY;
	private double shipOldZ;
	private boolean collision;

	public CollisionBehavior(final BranchGroup collidables, final Ship ship) {
		condition = new WakeupOnElapsedFrames(0);
		branchGroup = collidables;
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
		shipOldZ = shipZ;
		final Vector3d shipCoords = ship.getCoords();
		shipX = shipCoords.x;
		shipY = shipCoords.y;
		shipZ = shipCoords.z;
//		System.out.println(shipZ);

		while (children.hasMoreElements()) {
			final Node child = children.nextElement();
			if (child instanceof Collidable) {
				final Collidable collidable = (Collidable) child;

				final double zPos = collidable.getZPos();

				if (shipZ < zPos && shipOldZ >= zPos) {

					final Bounds collidableBounds = collidable.getBounds();

					double factor = (shipOldZ - zPos)/(zPos-shipZ);
					double xDist = shipX - shipOldX;
					double colX = shipOldX + xDist * factor;
					double yDist = shipY - shipOldY;
					double colY = shipOldY + yDist * factor;
					final BoundingBox shipBounds = new BoundingBox();
					shipBounds.setLower(colX - 0.6, colY - 0.15, shipZ);
					shipBounds.setUpper(colX + 0.6, colY + 0.15, shipOldZ);
					// ship.setBounds(shipBounds);

					final Item item = (Item) child.getUserData();
					
					if (collidableBounds.intersect(shipBounds)) {
						// System.out.println("COLLISION! BAAAAM!" + oldz + ", "
						// + zPos + child.getUserData());
						// System.out.println("shipX: " + shipX + " - shipY: " +
						// shipY
						// + " - shipZ: " + shipZ);
						// System.out.println("colX: " + xPos + " - colY: " +
						// yPos
						// + " - colZ: " + zPos);
						gameLogic.collide(item);
						collision = true;
					}
					((BranchGroup) child).detach();
					gameLogic.seen(item, collision);
					collision = false;
				}
			}
		}
		wakeupOn(condition);
	}
	
	public void setGameLogic(final GameLogic logic) {
		this.gameLogic = logic;
	}
}
