package de.tum.in.flowgame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Group;
import javax.media.j3d.Node;
import javax.media.j3d.SharedGroup;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnElapsedTime;
import javax.vecmath.Point3d;

import de.tum.in.flowgame.model.DifficultyFunction;
import de.tum.in.flowgame.model.Collision.Item;

public class CreateCollidables implements GameListener {

	private final double initialZPos = -100.0;
	private boolean pause;
	
	private List<Collidable> collidables = new ArrayList<Collidable>();

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

	public CreateCollidables(final BranchGroup collidableBranchGroup, final GameLogic gameLogic)
			throws IOException {
		this.gameLogic = gameLogic;
		this.collidableBranchGroup = collidableBranchGroup;
		this.collidableBranchGroup.setCapability(Group.ALLOW_CHILDREN_EXTEND);
		this.asteroid = loadAsteroid();
		this.fuelcan = loadFuelcan();
		this.difficultyFunction = gameLogic.getDifficultyFunction();
		addCollidable();
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


	private long getElapsedTime() {
		final long newTime = System.currentTimeMillis();
		long deltaTime = newTime - startTime;
		return deltaTime;
	}

	private Collidable createCollidable(double zPos) {
		Collidable c;
		
		long interval = 1000;
		
		double value = 0;
		
		if (gameLogic.getCurrentScenarioRound().isBaselineRound()) {
			//TODO: value in Abhängigkeit von Leistung des Spielers	
		} else {
			value = getElapsedTime();
		}
		
		double ratioAsteroids = difficultyFunction.getRatio().getValue(value);
		interval = (long) difficultyFunction.getInterval().getValue(value);
		
		this.elapsedTime = new WakeupOnElapsedTime(interval);

		if (ratioAsteroids  < Math.random()) {
			double testValue = Math.random();
			float scale;
			if (testValue > 0.66) scale = 5f;
			else if (testValue < 0.66 && testValue >= 0.33) scale = 4f;
			else scale = 3f;
			c = new Collidable(asteroid, 0 , scale, zPos);
		} else {
			c = new Collidable(fuelcan, 0, 2f, zPos);
		}
		c.setCapability(Group.ALLOW_COLLISION_BOUNDS_READ);
		c.setCapability(Group.ALLOW_COLLISION_BOUNDS_WRITE);
		c.setCapability(Node.ALLOW_AUTO_COMPUTE_BOUNDS_READ);
		c.setCapability(Node.ALLOW_AUTO_COMPUTE_BOUNDS_WRITE);
		c.setBoundsAutoCompute(true);
		collidableBranchGroup.addChild(c);

		return c;
	}
	
	public void addCollidable() {
		if (collidables.size() > 0) {
			double interval = -difficultyFunction.getInterval().getValue(getLastCollidableZPos());
			collidables.add(createCollidable(getLastCollidableZPos() + interval));
		} else {
			collidables.add(createCollidable(initialZPos));
		}
	}

	public double getLastCollidableZPos() {
		if (!collidables.isEmpty()) {
			return collidables.get(collidables.size() - 1).getZPos();
		}
		return initialZPos;
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
		difficultyFunction = gameLogic.getCurrentScenarioRound().getDifficultyFunction();
		startTime = System.currentTimeMillis();
	}

	@Override
	public void gameStopped(GameLogic game) {
		int max = collidables.size();
		for (int i = max-1; i >= 0; i--){
			collidables.get(i).detach();
			collidables.remove(i);
		}
		addCollidable();
	}

//	@Override
//	public void sessionFinished(GameLogic game) {
//		// empty
//		
//	}
}
