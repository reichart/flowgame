package de.tum.in.flowgame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Group;
import javax.media.j3d.Node;
import javax.media.j3d.SharedGroup;
import javax.vecmath.Point3d;

import de.tum.in.flowgame.model.DifficultyFunction;
import de.tum.in.flowgame.model.Collision.Item;

public class CreateCollidables implements GameListener {

	private static final double INITIAL_Z_POS = -100.0;
	
	private final List<Collidable> collidables = new ArrayList<Collidable>();

	private final GameLogic gameLogic;
	
	private final BranchGroup collidableBranchGroup;
	private final SharedGroup asteroid;
	private final SharedGroup fuelcan;

	private long startTime;
	private long pauseBegin;
	private DifficultyFunction difficultyFunction;

	public CreateCollidables(final BranchGroup collidableBranchGroup, final GameLogic gameLogic)
			throws IOException {
		this.gameLogic = gameLogic;
		this.collidableBranchGroup = collidableBranchGroup;
		this.collidableBranchGroup.setCapability(Group.ALLOW_CHILDREN_EXTEND);
		this.asteroid = loadModel(Item.ASTEROID, "/res/asteroid.obj", 0.8);
		this.fuelcan = loadModel(Item.FUELCAN, "/res/fuelcan2.obj", 1.5);
		this.difficultyFunction = gameLogic.getDifficultyFunction();
	}

	private SharedGroup loadModel(final Item item, final String resource, final double bounds) throws IOException {
		final SharedGroup model = new SharedGroup();
		model.setBoundsAutoCompute(false);
		model.addChild(Java3DUtils.loadScene(resource));
		model.setUserData(item);
		model.setBounds(new BoundingSphere(new Point3d(), bounds));
		return model;
	}

	private long getElapsedTime() {
		final long newTime = System.currentTimeMillis();
		final long deltaTime = newTime - startTime;
		return deltaTime;
	}

	private Collidable createCollidable(final double zPos) {
		final double value;
		if (gameLogic.getCurrentScenarioRound().isBaselineRound()) {
			//TODO: value in Abhängigkeit von Leistung des Spielers
			value = 0;
		} else {
			value = getElapsedTime();
		}
		
		final double ratioAsteroids = difficultyFunction.getRatio().getValue(value);

		final Collidable c;
		if (ratioAsteroids  < Math.random()) {
			final double testValue = Math.random();
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
		final double zPos;
		if (!collidables.isEmpty()) {
			final double lastZpos = getLastCollidableZPos();
			zPos = lastZpos + -difficultyFunction.getInterval().getValue(lastZpos);
		} else {
			zPos = INITIAL_Z_POS;
		}
		
		collidables.add(createCollidable(zPos));
	}

	public double getLastCollidableZPos() {
		if (!collidables.isEmpty()) {
			return collidables.get(collidables.size() - 1).getZPos();
		}
		return INITIAL_Z_POS;
	}
	
	@Override
	public void added(final GameLogic game) {
		// empty
	}
	
	@Override
	public void removed(final GameLogic game) {
		// empty
	}
	
	@Override
	public void collided(final GameLogic logic, final Item item) {
		// empty
	}

	@Override
	public void gamePaused(final GameLogic game) {
		pauseBegin = System.currentTimeMillis();
	}
	
	@Override
	public void gameResumed(final GameLogic game) {
		startTime = startTime + (System.currentTimeMillis() - pauseBegin);
	}

	@Override
	public void gameStarted(final GameLogic game) {
		difficultyFunction = gameLogic.getCurrentScenarioRound().getDifficultyFunction();
		startTime = System.currentTimeMillis();
	}

	@Override
	public void gameStopped(final GameLogic game) {
		final int max = collidables.size();
		for (int i = max-1; i >= 0; i--){
			collidables.get(i).detach();
			collidables.remove(i);
		}
	}
}
