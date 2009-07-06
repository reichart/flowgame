package de.tum.in.flowgame.behavior;

import java.io.IOException;
import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Group;
import javax.media.j3d.Node;
import javax.media.j3d.SharedGroup;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnElapsedTime;
import javax.vecmath.Point3d;

import com.sun.j3d.utils.universe.SimpleUniverse;

import de.tum.in.flowgame.Collidable;
import de.tum.in.flowgame.Game3D;
import de.tum.in.flowgame.Utils;
import de.tum.in.flowgame.GameLogic.Item;

public class CreateCollidablesBehavior extends Behavior {

	private long time = 1000;
	private long speed = 20;
	// number of asteroids compared to fuel cans, number between 0 and 1
	private float ratioAsteroids;

	private WakeupCriterion wakeupEvent;

	private final BranchGroup collidableBranchGroup;
	private final SharedGroup asteroid;
	private final SharedGroup fuelcan;

	private final boolean showSceneGraph = false;
	private final com.tornadolabs.j3dtree.Java3dTree tree = new com.tornadolabs.j3dtree.Java3dTree();

	public CreateCollidablesBehavior(final BranchGroup collidableBranchGroup)
			throws IOException {
		this.collidableBranchGroup = collidableBranchGroup;
		this.collidableBranchGroup
				.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		this.asteroid = loadAsteroid();
		this.fuelcan = loadFuelcan();
		this.wakeupEvent = new WakeupOnElapsedTime(time);
		ratioAsteroids = 0.5f;
	}

	private SharedGroup loadFuelcan() throws IOException {
		final SharedGroup fuelcan = new SharedGroup();
		fuelcan.addChild(Utils.loadScene("/res/fuelcan2.obj"));
		fuelcan.setUserData(Item.FUELCAN);
		fuelcan.setCollisionBounds(new BoundingBox(new Point3d(-0.35f, -0.5f,
				-0.125f), new Point3d(0.35f, 0.5f, 0.125f)));
		return fuelcan;
	}

	private SharedGroup loadAsteroid() throws IOException {
		final SharedGroup asteroid = new SharedGroup();
		asteroid.addChild(Utils.loadScene("/res/asteroid.obj"));
		asteroid.setUserData(Item.ASTEROID);
		asteroid.setCollisionBounds(new BoundingSphere());
		return asteroid;
	}

	@Override
	public void initialize() {
		if (showSceneGraph)
			tree.setVisible(true);
		wakeupOn(wakeupEvent);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void processStimulus(final Enumeration criteria) {
		final float x = (float) (Math.random() * 3 - 1.5);
		if (ratioAsteroids < Math.random()) {
			Collidable a = new Collidable(asteroid, x, speed);
			a.setCapability(Group.ALLOW_COLLISION_BOUNDS_READ);
			a.setCapability(Group.ALLOW_COLLISION_BOUNDS_WRITE);
			a.setCapability(Group.ALLOW_AUTO_COMPUTE_BOUNDS_READ);
			a.setCapability(Group.ALLOW_AUTO_COMPUTE_BOUNDS_WRITE);
			a.setBoundsAutoCompute(true);
//			BranchGroup t = new BranchGroup();
//			BoundsBehavior b = new BoundsBehavior(a);
//			b.setSchedulingBounds(Game3D.WORLD_BOUNDS);
//			b.addBehaviorToParentGroup(t);
//			collidableBranchGroup.addChild(t);
			collidableBranchGroup.addChild(a);
		} else {
			Collidable f = new Collidable(fuelcan, x, speed);
			f.setCapability(Group.ALLOW_COLLISION_BOUNDS_READ);
			f.setCapability(Group.ALLOW_COLLISION_BOUNDS_WRITE);
			f.setCapability(Group.ALLOW_AUTO_COMPUTE_BOUNDS_READ);
			f.setCapability(Group.ALLOW_AUTO_COMPUTE_BOUNDS_WRITE);
			f.setBoundsAutoCompute(true);
//			BranchGroup t = new BranchGroup();
//			BoundsBehavior b = new BoundsBehavior(f);
//			b.setSchedulingBounds(Game3D.WORLD_BOUNDS);
//			b.addBehaviorToParentGroup(t);
//			collidableBranchGroup.addChild(t);
			collidableBranchGroup.addChild(f);
		}
		if (showSceneGraph) {
			Node node = this;
			while (node.getParent() != null) {
				node = node.getParent();
			}
			final SimpleUniverse su = (SimpleUniverse) this.getLocale()
					.getVirtualUniverse();
			tree.recursiveApplyCapability(node);
			tree.updateNodes(su);
		}
			wakeupOn(wakeupEvent);
	}

	public long getTime() {
		return time;
	}

	public void setTime(final long time) {
		this.time = time;
		wakeupEvent = new WakeupOnElapsedTime(this.time);
	}

	public float getRatioAsteroids() {
		return ratioAsteroids;
	}

	public void setRatioAsteroids(final long ratioAsteroids) {
		this.ratioAsteroids = ratioAsteroids;
	}

	public long getSpeed() {
		return speed;
	}

	public void setSpeed(final long speed) {
		this.speed = speed;
	}
}
