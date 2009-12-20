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

/**
 * A Class, which provides methods to add {@link Collidable}s to the tunnel.
 */
public class CreateCollidables implements GameListener {

	/**
	 * Initial zPosition of the collidables.
	 */
	private static final double INITIAL_Z_POS = -100.0;
	
	private final List<Collidable> collidables = new ArrayList<Collidable>();

	private final BranchGroup collidableBranchGroup;
	private final SharedGroup asteroid;
	private final SharedGroup fuelcan;

	private long startTime;
	private long pauseBegin;

	/**
	 * Creates a new CreateCollidables class, which provides methods to add
	 * collidables in dependency to a {@link DifficultyFunction} to the tunnel.
	 * 
	 * @param collidableBranchGroup
	 *            The BranchGroup, where the collidables are placed.
	 * @throws IOException
	 */
	public CreateCollidables(final BranchGroup collidableBranchGroup) throws IOException {
		this.collidableBranchGroup = collidableBranchGroup;
		this.collidableBranchGroup.setCapability(Group.ALLOW_CHILDREN_EXTEND);
		this.asteroid = loadModel(Item.ASTEROID, "/res/asteroid.obj", 0.8);
		this.fuelcan = loadModel(Item.FUELCAN, "/res/fuelcan2.obj", 1.5);
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

	private Collidable createCollidable(final double zPos, final GameLogic game) {
		final double value;
		if (game.getCurrentScenarioRound().isBaselineRound()) {
			//TODO: value in Abhängigkeit von Leistung des Spielers
			value = 0;
		} else {
			value = getElapsedTime();
		}
		
		final double ratioAsteroids = game.getDifficultyFunction().getRatio().getValue(value);
		
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
	
	/**
	 * 
	 * Use this method to add a new collidable to the tunnel.
	 * 
	 * @param game
	 *            {@link GameLogic}, which references the {@link DifficultyFunction} for the
	 *            calculation of the distance between collidables.
	 */
	public synchronized void addCollidable(final GameLogic game) {
		final double zPos;
		if (!collidables.isEmpty()) {
			final double lastZpos = getLastCollidableZPos();
			zPos = lastZpos - game.getDifficultyFunction().getInterval().getValue(-lastZpos);
		} else {
			zPos = INITIAL_Z_POS;
		}
//		System.out.println("add Collidable at zPos: " + zPos);
		collidables.add(createCollidable(zPos, game));
	}

	/**
	 * 
	 * @return zPosition of the last {@link Collidable} in the tunnel.
	 */
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
		startTime = System.currentTimeMillis();
	}

	@Override
	public synchronized void gameStopped(final GameLogic game) {
		final int max = collidables.size();
		for (int i = max-1; i >= 0; i--){
			collidables.get(i).detach();
			collidables.remove(i);
		}
	}
}
