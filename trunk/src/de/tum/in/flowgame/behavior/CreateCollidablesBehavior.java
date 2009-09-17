package de.tum.in.flowgame.behavior;

import java.io.IOException;
import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Group;
import javax.media.j3d.Node;
import javax.media.j3d.SharedGroup;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnElapsedTime;
import javax.vecmath.Point3d;

import de.tum.in.flowgame.Collidable;
import de.tum.in.flowgame.GameListener;
import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.Java3DUtils;
import de.tum.in.flowgame.model.DifficultyFunction;
import de.tum.in.flowgame.model.Collision.Item;

public class CreateCollidablesBehavior extends Behavior implements GameListener {

	/*
	 * Added factor to speed and time settings so that equidistant creation of
	 * collidables at different speeds is possible. The actual speed now is
	 * computed through factor * baseSpeed. The baseTime value sets the distance
	 * between the collidables. The actual creation time is computed depending
	 * on the speed (to create equidistant collidables).
	 */
	private double factor = 1;
	private final long baseTime = 400;

	private boolean pause;

	private GameLogic gameLogic;
	
	private WakeupCriterion elapsedTime;

	private final BranchGroup collidableBranchGroup;
	private final SharedGroup asteroid;
	private final SharedGroup fuelcan;

	private long startTime;
	private long pauseBegin;
	private DifficultyFunction difficultyFunction;

	// private final boolean showSceneGraph = false;
	// private final com.tornadolabs.j3dtree.Java3dTree tree = new
	// com.tornadolabs.j3dtree.Java3dTree();

	public CreateCollidablesBehavior(final BranchGroup collidableBranchGroup, final GameLogic gameLogic)
			throws IOException {
		this.gameLogic = gameLogic;
		this.collidableBranchGroup = collidableBranchGroup;
		this.collidableBranchGroup.setCapability(Group.ALLOW_CHILDREN_EXTEND);
		this.asteroid = loadAsteroid();
		this.fuelcan = loadFuelcan();
		this.elapsedTime = new WakeupOnElapsedTime(baseTime);
	}

	private SharedGroup loadFuelcan() throws IOException {
		final SharedGroup fuelcan = new SharedGroup();
		fuelcan.setBoundsAutoCompute(false);
		fuelcan.addChild(Java3DUtils.loadScene("/res/fuelcan2.obj"));
		fuelcan.setUserData(Item.FUELCAN);
		// fuelcan.setBounds(new BoundingBox(new Point3d(-0.35f, -0.5f,
		// -0.125f), new Point3d(0.35f, 0.5f, 0.125f)));
		fuelcan.setBounds(new BoundingSphere(new Point3d(), 1.5));
		return fuelcan;
	}

	private SharedGroup loadAsteroid() throws IOException {
		final SharedGroup asteroid = new SharedGroup();
		asteroid.setBoundsAutoCompute(false);
		asteroid.addChild(Java3DUtils.loadScene("/res/asteroid.obj"));
		asteroid.setUserData(Item.ASTEROID);
		asteroid.setBounds(new BoundingSphere(new Point3d(), 0.8));
		return asteroid;
	}

	@Override
	public void initialize() {
		wakeupOn(elapsedTime);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void processStimulus(final Enumeration criteria) {
		if (!pause)	createCollidable();
		wakeupOn(elapsedTime);
	}
	
	private long getElapsedTime() {
		final long newTime = System.currentTimeMillis();
		long deltaTime = newTime - startTime;
		return deltaTime;
	}

	private void createCollidable() {
		Collidable c;
		
		long speed = (long) difficultyFunction.getSpeed().getValue(getElapsedTime());
		double ratioAsteroids = difficultyFunction.getRatio().getValue(getElapsedTime());
		long interval = (long) difficultyFunction.getInterval().getValue(getElapsedTime());
//		System.out.println(interval);
		this.elapsedTime = new WakeupOnElapsedTime(interval);

		if (ratioAsteroids  < Math.random()) {
			double testValue = Math.random();
			float scale;
			if (testValue > 0.66) scale = 3f;
			else if (testValue < 0.66 && testValue >= 0.33) scale = 2f;
			else scale = 1f;
			c = new Collidable(asteroid, speed , scale, gameLogic);
		} else {
			c = new Collidable(fuelcan, speed, 1f, gameLogic);
		}
		c.setCapability(Group.ALLOW_COLLISION_BOUNDS_READ);
		c.setCapability(Group.ALLOW_COLLISION_BOUNDS_WRITE);
		c.setCapability(Node.ALLOW_AUTO_COMPUTE_BOUNDS_READ);
		c.setCapability(Node.ALLOW_AUTO_COMPUTE_BOUNDS_WRITE);
		c.setBoundsAutoCompute(true);
		collidableBranchGroup.addChild(c);

	}

	@Override
	public void collided(GameLogic logic, Item item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gamePaused(GameLogic game) {
		this.pause = true;
		pauseBegin = System.currentTimeMillis();
	}
	
	@Override
	public void gameResumed(GameLogic game) {
		pause = false;
		startTime = startTime + (System.currentTimeMillis() - pauseBegin);
	}

	@Override
	public void gameStarted(GameLogic game) {
		// TODO Auto-generated method stub
		difficultyFunction = gameLogic.getCurrentScenarioRound().getDifficutyFunction();
		startTime = System.currentTimeMillis();
	}

	@Override
	public void gameStopped(GameLogic game) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sessionFinished(GameLogic game) {
		// empty
		
	}
}
