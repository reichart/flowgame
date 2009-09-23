package de.tum.in.flowgame;

import java.awt.Color;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Timer;
import java.util.TimerTask;

import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.BranchGroup;
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

public class Ship extends TransformGroup implements GameListener {

	public static final float INITIAL_SHIP_PLACEMENT_X = 0;
	public static final float INITIAL_SHIP_PLACEMENT_Y = -1;
	public static final float INITIAL_SHIP_PLACEMENT_Z = -6f;
	private final ShipNavigationBehavior shipNavigationBehavior;
	
	private final Transform3D staticTransforms;

	private Shape3D shape1;
//	private Texture tex1;
	private Shape3D shape2;
	private Texture tex2;

	private Timer flashTimer;
	private SpeedChangeBehavior speedChange;

	public Ship(final GameLogic gameLogic, TransformGroup viewTG)
			throws IOException {
		super();

		this.flashTimer = new Timer("FlashTimer", true);
		this.setBoundsAutoCompute(false);

		final Transform3D t3d = new Transform3D();
		t3d.setTranslation(new Vector3d(INITIAL_SHIP_PLACEMENT_X,
				INITIAL_SHIP_PLACEMENT_Y, INITIAL_SHIP_PLACEMENT_Z));

		this.setTransform(t3d);

		TransformGroup moveGroup = new TransformGroup();
		moveGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		this.addChild(moveGroup);

		TransformGroup rotationGroup = new TransformGroup();
		rotationGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		moveGroup.addChild(rotationGroup);

		TransformGroup ship = loadShip();

		ship.setCollisionBounds(new BoundingBox(new Point3d(-0.35f, -0.15f,
				-0.5f), new Point3d(0.35f, 0.06f, 0.5f)));
		// Box box = new Box(0.7f, 0.19f, 1.0f, new Appearance());
		// box.setCollidable(false);
		rotationGroup.addChild(ship);
//		System.out.println("Ship " + ship.getBounds());

		// KeyShipEllipseBehavior shipNavigationBehavior = new
		// KeyShipEllipseBehavior(moveGroup, rotationGroup);
		// ship.addChild(shipNavigationBehavior);
		// shipNavigationBehavior.setSchedulingBounds(WORLD_BOUNDS);

		// Alternative KeyBehavior
		shipNavigationBehavior = new ShipNavigationBehavior(moveGroup, viewTG, gameLogic);
		gameLogic.addListener(shipNavigationBehavior);
		ship.addChild(shipNavigationBehavior);
		shipNavigationBehavior.setSchedulingBounds(Game3D.WORLD_BOUNDS);
		
		speedChange = new SpeedChangeBehavior(shipNavigationBehavior, gameLogic);
		speedChange.setSchedulingBounds(Game3D.WORLD_BOUNDS);
		addChild(speedChange);

		staticTransforms = new Transform3D();

		Node node = this;
		while (node.getParent() != null) {
			if (node.getParent() instanceof TransformGroup) {
				TransformGroup tg = (TransformGroup) node.getParent();
				Transform3D trans = new Transform3D();
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
	private void getTransformsOfChildren(TransformGroup tg) {
		Enumeration<Node> children = tg.getAllChildren();
		while (children.hasMoreElements()) {
			Node n = children.nextElement();
			if (n instanceof TransformGroup) {
				Transform3D trans = new Transform3D();
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

		rotX.mul(rotY);

		TransformGroup rotateShip = new TransformGroup();
		rotateShip.setTransform(rotX);

		final BranchGroup ship = Java3DUtils.loadScene("/res/SFighter.obj");
		ship.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		shape1 = (Shape3D) ship.getChild(0);
		shape1.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
		shape1.getAppearance().setCapability(Appearance.ALLOW_TEXTURE_WRITE);
		shape1.getAppearance().setCapability(Appearance.ALLOW_MATERIAL_WRITE);
		shape2 = (Shape3D) ship.getChild(1);
		tex2 = shape2.getAppearance().getTexture();
		shape2.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
		shape2.getAppearance().setCapability(Appearance.ALLOW_TEXTURE_WRITE);
		shape2.getAppearance().setCapability(Appearance.ALLOW_MATERIAL_WRITE);
		Material cockpit = new Material();
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
	public void collided(GameLogic logic, Item item) {
		Material mat = new Material();

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
	public void gamePaused(GameLogic game) {
		// TODO Auto-generated method stub

	}

	@Override
	public void gameResumed(GameLogic game) {
		// TODO Auto-generated method stub

	}

	@Override
	public void gameStarted(GameLogic game) {
		// TODO Auto-generated method stub

	}

	@Override
	public void gameStopped(GameLogic game) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void sessionFinished(GameLogic game) {
		// empty		
	}

	private class FlashTimerTask extends TimerTask {

		public FlashTimerTask() {
		}

		@Override
		public void run() {
			shape2.getAppearance().setTexture(tex2);
		}
	}
}
