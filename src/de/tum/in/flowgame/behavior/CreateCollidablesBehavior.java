package de.tum.in.flowgame.behavior;

import java.io.IOException;
import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.SharedGroup;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnElapsedTime;
import javax.vecmath.Point3d;

import de.tum.in.flowgame.Collidable;
import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.Utils;

public class CreateCollidablesBehavior extends Behavior {

	private long time = 1000;
	private long speed = 120;
	// number of asteroids compared to fuel cans, number between 0 and 1
	private float ratioAsteroids;

	private WakeupCriterion wakeupEvent;

	private final BranchGroup collidableBranchGroup;
	private final SharedGroup asteroid;
	private final SharedGroup fuelcan;

	public CreateCollidablesBehavior(final BranchGroup collidableBranchGroup) throws IOException {
		this.collidableBranchGroup = collidableBranchGroup;
		this.asteroid = loadAsteroid();
		this.fuelcan = loadFuelcan();
		this.wakeupEvent = new WakeupOnElapsedTime(time);
		ratioAsteroids = 0.5f;
	}
	
	private SharedGroup loadFuelcan() throws IOException {
		SharedGroup fuelcan = new SharedGroup();
		fuelcan.addChild(Utils.loadScene("/res/fuelcan2.obj"));
		fuelcan.setUserData(GameLogic.FUELCAN);
		fuelcan.setCollisionBounds(new BoundingBox(new Point3d(-0.35f, -0.5f, -0.125f), new Point3d(0.35f, 0.5f,
				0.125f)));
		return fuelcan;
	}

	private SharedGroup loadAsteroid() throws IOException {
		SharedGroup asteroid = new SharedGroup();
		asteroid.addChild(Utils.loadScene("/res/asteroid.obj"));
		asteroid.setUserData(GameLogic.ASTEROID);
		return asteroid;
	}

	@Override
	public void initialize() {
		wakeupOn(wakeupEvent);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void processStimulus(final Enumeration criteria) {
		final float x = new Float(Math.random() * 3 - 1.5);
		if (ratioAsteroids < Math.random()) {
			collidableBranchGroup.addChild(new Collidable(asteroid, x, speed));
		} else {
			collidableBranchGroup.addChild(new Collidable(fuelcan, x, speed));
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

	public void setRatioAsteroids(long ratioAsteroids) {
		this.ratioAsteroids = ratioAsteroids;
	}

	public long getSpeed() {
		return speed;
	}

	public void setSpeed(final long speed) {
		this.speed = speed;
	}
}
