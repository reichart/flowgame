package de.tum.in.flowgame;

import java.awt.Color;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Timer;
import java.util.TimerTask;

import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Group;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Texture;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import de.tum.in.flowgame.behavior.ShipNavigationBehavior;
import de.tum.in.flowgame.behavior.SpeedChangeBehavior;
import de.tum.in.flowgame.model.Collision.Item;

/**
 * This class represents the ship in our game. It loads ({@link #loadShip()}) a
 * 3D model and provides a method ({@link #getControls()}) that returns an
 * object ({@link ShipNavigationBehavior}) that provides access to the steering
 * of the ship.
 */
public class Ship extends TransformGroup implements GameListener {

	/**
	 * The inital position of the ship in x-direction.
	 */
	public static final float INITIAL_SHIP_PLACEMENT_X = 0;
	/**
	 * The initial position of the ship in y-direction.
	 */
	public static final float INITIAL_SHIP_PLACEMENT_Y = -1;
	/**
	 * The initial position of the ship in z-direction.
	 */
	public static final float INITIAL_SHIP_PLACEMENT_Z = -6f;

	private final ShipNavigationBehavior shipNavigationBehavior;

	private final Transform3D staticTransforms;

	private Shape3D shape1;
	private Shape3D shape2;
	private Texture tex2;

	private final Timer flashTimer;
	private final SpeedChangeBehavior speedChange;

	/**
	 * Creates the ship.
	 * 
	 * @param viewTG
	 *            The {@link TransformGroup} of our view.
	 * @throws IOException
	 */
	public Ship(final TransformGroup viewTG) throws IOException {
		this.flashTimer = new Timer("FlashTimer", true);
		this.setBoundsAutoCompute(false);

		final Transform3D t3d = new Transform3D();
		t3d.setTranslation(new Vector3d(INITIAL_SHIP_PLACEMENT_X, INITIAL_SHIP_PLACEMENT_Y, INITIAL_SHIP_PLACEMENT_Z));

		this.setTransform(t3d);

		final TransformGroup moveGroup = new TransformGroup();
		moveGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		this.addChild(moveGroup);

		final TransformGroup rotationGroup = new TransformGroup();
		rotationGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		moveGroup.addChild(rotationGroup);

		final TransformGroup ship = loadShip();

		rotationGroup.addChild(ship);

		shipNavigationBehavior = new ShipNavigationBehavior(moveGroup, viewTG);
		ship.addChild(shipNavigationBehavior);
		shipNavigationBehavior.setSchedulingBounds(Game3D.WORLD_BOUNDS);

		speedChange = new SpeedChangeBehavior(shipNavigationBehavior);
		speedChange.setSchedulingBounds(Game3D.WORLD_BOUNDS);
		addChild(speedChange);

		staticTransforms = new Transform3D();

		Node node = this;
		while (node.getParent() != null) {
			if (node.getParent() instanceof TransformGroup) {
				final TransformGroup tg = (TransformGroup) node.getParent();
				final Transform3D trans = new Transform3D();
				tg.getTransform(trans);
				staticTransforms.mul(trans);
			}
			node = node.getParent();
		}

		// the following call multiplies the staticTransforms with all the
		// transforms of the children of this node
		getTransformsOfChildren(this);

	}

	@SuppressWarnings("unchecked")
	private void getTransformsOfChildren(final TransformGroup tg) {
		final Enumeration<Node> children = tg.getAllChildren();
		while (children.hasMoreElements()) {
			final Node n = children.nextElement();
			if (n instanceof TransformGroup) {
				final Transform3D trans = new Transform3D();
				((TransformGroup) n).getTransform(trans);
				this.staticTransforms.mul(trans);
				getTransformsOfChildren((TransformGroup) n);
			}
		}
	}

	private TransformGroup loadShip() throws IOException {

		final Transform3D rotX = new Transform3D();
		rotX.rotX(Math.toRadians(90));

		final Transform3D rotY = new Transform3D();
		rotY.rotY(Math.toRadians(180));

		final Transform3D scale = new Transform3D();
		scale.setScale(1.5f);

		rotX.mul(rotY);
		rotX.mul(scale);

		final TransformGroup rotateShip = new TransformGroup();
		rotateShip.setTransform(rotX);

		final BranchGroup ship = Java3DUtils.loadScene("/res/SFighter.obj");
		ship.setCapability(Group.ALLOW_CHILDREN_WRITE);

		ship.setCollisionBounds(new BoundingBox(new Point3d(-0.35f, -0.15f, -0.5f), new Point3d(0.35f, 0.06f, 0.5f)));

		shape1 = (Shape3D) ship.getChild(0);
		shape1.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
		shape1.getAppearance().setCapability(Appearance.ALLOW_TEXTURE_WRITE);
		shape1.getAppearance().setCapability(Appearance.ALLOW_MATERIAL_WRITE);
		shape2 = (Shape3D) ship.getChild(1);
		tex2 = shape2.getAppearance().getTexture();
		shape2.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
		shape2.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
		shape2.getAppearance().setCapability(Appearance.ALLOW_TEXTURE_WRITE);
		shape2.getAppearance().setCapability(Appearance.ALLOW_MATERIAL_WRITE);
		final Material cockpit = new Material();
		cockpit.setAmbientColor(new Color3f(Color.CYAN));
		cockpit.setDiffuseColor(new Color3f(Color.BLUE));
		shape1.getAppearance().setMaterial(cockpit);

		rotateShip.addChild(ship);

		return rotateShip;
	}

	public ShipNavigationBehavior getControls() {
		return shipNavigationBehavior;
	}

	@Override
	public void added(final GameLogic game) {
		game.addListener(shipNavigationBehavior);
		speedChange.setGameLogic(game);
	}

	@Override
	public void removed(final GameLogic game) {
		game.removeListener(shipNavigationBehavior);
		speedChange.setGameLogic(null);
	}

	/**
	 * Changes the color of the ship, when a collision with a {@link Collidable}
	 * takes place.
	 */
	@Override
	public void collided(final GameLogic logic, final Item item) {
		// TODO don't create new objects all the time
		final Material mat = new Material();

		if (item == Item.ASTEROID) {
			mat.setAmbientColor(new Color3f(Color.RED));
			mat.setDiffuseColor(new Color3f(Color.MAGENTA));
		} else {
			mat.setAmbientColor(new Color3f(Color.ORANGE));
			mat.setDiffuseColor(new Color3f(Color.RED));
		}
		shape2.getAppearance().setTexture(null);
		shape2.getAppearance().setMaterial(mat);

		flashTimer.schedule(new FlashTimerTask(), 150);
	}

	@Override
	public void gamePaused(final GameLogic game) {
		// empty
	}

	@Override
	public void gameResumed(final GameLogic game) {
		// empty
	}

	@Override
	public void gameStarted(final GameLogic game) {
		// empty
	}

	@Override
	public void gameStopped(final GameLogic game) {
		shipNavigationBehavior.reset();
	}

	/**
	 * This class changes the {@link Texture} of the ship (changed at collison) back to normal.
	 */
	private class FlashTimerTask extends TimerTask {

		@Override
		public void run() {
			shape2.getAppearance().setTexture(tex2);
		}
	}
}
