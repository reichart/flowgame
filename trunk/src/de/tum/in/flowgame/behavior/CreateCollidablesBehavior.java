package de.tum.in.flowgame.behavior;

import java.io.IOException;
import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.SharedGroup;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnElapsedTime;
import javax.vecmath.Point3d;

import de.tum.in.flowgame.Collidable;
import de.tum.in.flowgame.Utils;
import de.tum.in.flowgame.GameLogic.Item;

public class CreateCollidablesBehavior extends Behavior {

	private long time = 1000;
	private long speed = 300;
	// number of asteroids compared to fuel cans, number between 0 and 1
	private float ratioAsteroids;

	private WakeupCriterion wakeupEvent;

	private final BranchGroup collidableBranchGroup;
	private final SharedGroup asteroid;
	private final SharedGroup fuelcan;

	public CreateCollidablesBehavior(final BranchGroup collidableBranchGroup)
			throws IOException {
		this.collidableBranchGroup = collidableBranchGroup;
		this.asteroid = loadAsteroid();
		this.fuelcan = loadFuelcan();
		this.wakeupEvent = new WakeupOnElapsedTime(time);
		ratioAsteroids = 0.5f;
	}

	private SharedGroup loadFuelcan() throws IOException {
		final SharedGroup fuelcan = new SharedGroup();
		fuelcan.addChild(Utils.loadScene("/res/fuelcan2.obj"));
		fuelcan.setUserData(Item.FUELCAN);
		return fuelcan;
	}

	private SharedGroup loadAsteroid() throws IOException {
		final SharedGroup asteroid = new SharedGroup();
		asteroid.addChild(Utils.loadScene("/res/asteroid.obj"));
		asteroid.setUserData(Item.ASTEROID);
		return asteroid;
	}

	// com.tornadolabs.j3dtree.Java3dTree tree;

	@Override
	public void initialize() {
		// tree = new com.tornadolabs.j3dtree.Java3dTree();
		// tree.setVisible(true);
		wakeupOn(wakeupEvent);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void processStimulus(final Enumeration criteria) {
		final float x = (float) (Math.random() * 3 - 1.5);
		if (ratioAsteroids < Math.random()) {
			Collidable a = new Collidable(asteroid, x, speed);
			a.setCollisionBounds(new BoundingSphere(new Point3d(0f,0f,0f), 1f));
			collidableBranchGroup.addChild(a);
		} else {
			Collidable f = new Collidable(fuelcan, x, speed);
			f.setCollisionBounds(new BoundingBox(new Point3d(-0.35f, -0.5f,
					-0.125f), new Point3d(0.35f, 0.5f, 0.125f)));
			f.setCollisionBounds(new BoundingSphere(new Point3d(0f,0f,0f), 1f));
			collidableBranchGroup.addChild(f);
		}
		// Node node = this;
		// while (node.getParent() != null) {
		// node = node.getParent();
		// }
		// final SimpleUniverse su =
		// (SimpleUniverse)this.getLocale().getVirtualUniverse();
		// tree.recursiveApplyCapability(node);
		// tree.updateNodes(su);
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
