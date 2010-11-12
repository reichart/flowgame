package de.tum.in.flowgame.client.engine;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Group;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.Shape3D;
import javax.media.j3d.SharedGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;

import de.tum.in.flowgame.GameListener;
import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.client.engine.util.Java3DUtils;
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
		this.asteroid = loadModel(Item.ASTEROID, "/res/asteroid.obj", 0.8, null);

		// creates new appearance with yellow colored material
		final Color3f ambient = new Color3f(Color.BLACK);
		final Color3f emissive = new Color3f(Color.BLACK);
		final Color3f diffuse = new Color3f(1.0f, 0.8f, 0.0f);
		final Color3f specular = new Color3f(Color.WHITE);
		final Material mat = new Material(ambient, emissive, diffuse, specular, 10.5f);
		final Appearance a = new Appearance();
		a.setMaterial(mat);

		// The diamond3.obj has a associated material file (diamond3.mtl).
		// If you want to overwrite the material defined in the file, then
		// you have to pass a Material as last parameter (instead of null).
		this.fuelcan = loadModel(Item.FUELCAN, "/res/diamond3.obj", 1.5, null);
	}

	private SharedGroup loadModel(final Item item, final String resource, final double bounds,
			final Appearance appearance) throws IOException {
		final SharedGroup model = new SharedGroup();
		model.setBoundsAutoCompute(false);
		final BranchGroup loaded = Java3DUtils.loadScene(resource);

		if (appearance != null) {
			final Shape3D shape = (Shape3D) loaded.getChild(0);
			shape.setAppearance(appearance);
		}

		model.addChild(loaded);
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
			// TODO: value in Abhängigkeit von Leistung des Spielers
			value = 0;
		} else {
			value = getElapsedTime();
		}

		final double ratioAsteroids = game.getDifficultyFunction().getRatio().getValue(value);

		final Collidable c;
		if (ratioAsteroids < Math.random()) {
			final double testValue = Math.random();
			float scale;
			if (testValue > 0.66) {
				scale = 5f;
			} else if (testValue < 0.66 && testValue >= 0.33) {
				scale = 4f;
			} else {
				scale = 3f;
			}
			c = new Collidable(asteroid, 0, scale, zPos);
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
	 * Use this method to add a new collidable to the tunnel.
	 * 
	 * @param game
	 *            {@link GameLogic}, which references the
	 *            {@link DifficultyFunction} for the calculation of the distance
	 *            between collidables.
	 */
	public synchronized void addCollidable(final GameLogic game) {
		final double zPos;
		if (!collidables.isEmpty()) {
			final double lastZpos = getLastCollidableZPos();
			zPos = lastZpos - game.getDifficultyFunction().getInterval().getValue(-lastZpos);
		} else {
			zPos = INITIAL_Z_POS;
		}
		// System.out.println("add Collidable at zPos: " + zPos);
		collidables.add(createCollidable(zPos, game));
	}

	/**
	 * @return zPosition of the last {@link Collidable} in the tunnel.
	 */
	public double getLastCollidableZPos() {
		if (!collidables.isEmpty()) {
			return collidables.get(collidables.size() - 1).getZPos();
		}
		return INITIAL_Z_POS;
	}

	public void added(final GameLogic game) {
		// empty
	}

	public void removed(final GameLogic game) {
		// empty
	}

	public void collided(final GameLogic logic, final Item item) {
		// empty
	}

	public void gamePaused(final GameLogic game) {
		pauseBegin = System.currentTimeMillis();
	}

	public void gameResumed(final GameLogic game) {
		startTime = startTime + (System.currentTimeMillis() - pauseBegin);
	}

	public void gameStarted(final GameLogic game) {
		startTime = System.currentTimeMillis();
	}

	public synchronized void gameStopped(final GameLogic game) {
		final int max = collidables.size();
		for (int i = max - 1; i >= 0; i--) {
			collidables.get(i).detach();
			collidables.remove(i);
		}
	}
}
