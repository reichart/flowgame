package de.tum.in.flowgame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Fog;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.J3DGraphics2D;
import javax.media.j3d.LinearFog;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.Viewer;
import com.sun.j3d.utils.universe.ViewingPlatform;

import de.tum.in.flowgame.behavior.CreateCollidablesBehavior;
import de.tum.in.flowgame.behavior.KeyShipBehavior;
import de.tum.in.flowgame.behavior.ShipCollisionBehavior;
import de.tum.in.flowgame.ui.HealthBar;

public class Game3D extends Canvas3D {

	public static final BoundingSphere WORLD_BOUNDS = new BoundingSphere(new Point3d(), Double.POSITIVE_INFINITY);

	private static final Color3f WHITE = new Color3f(1, 1, 1);
	private static final Color3f BLACK = new Color3f(0, 0, 0);

	public static final float INITIAL_SHIP_PLACEMENT_X = 0;
	public static final float INITIAL_SHIP_PLACEMENT_Y = -1;
	public static final float INITIAL_SHIP_PLACEMENT_Z = -6f;

	private HealthBar fuel, damage;

	private final BranchGroup collidables;
	private final GameLogic logic;

	private Font bigFont;

	public Game3D() throws IOException {
		super(SimpleUniverse.getPreferredConfiguration());

		this.fuel = new HealthBar(ImageIO.read(getClass().getResource("/res/fuel.png")), "Fuel", Color.YELLOW,
				Color.YELLOW.darker(), 0, 10);

		this.damage = new HealthBar(ImageIO.read(getClass().getResource("/res/asteroid_icon.png")), "Damage",
				Color.RED, Color.RED.darker(), 0, 10);

		collidables = new BranchGroup();
		collidables.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		collidables.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		collidables.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);

		CreateCollidablesBehavior ccb = new CreateCollidablesBehavior(collidables);
		ccb.setSchedulingBounds(WORLD_BOUNDS);

		Tunnel tunnel = new Tunnel();
		this.logic = new GameLogic(ccb, tunnel);
		final SimpleUniverse su = createUniverse();
		collidables.addChild(createShip(logic, su.getViewingPlatform().getViewPlatformTransform()));
		collidables.addChild(ccb);

		final BranchGroup scene = new BranchGroup();

		// Fog fog = new ExponentialFog(color, 0.2f);
		final Fog fog = new LinearFog(BLACK, 0, Tunnel.TUNNEL_LENGTH);
		fog.setInfluencingBounds(WORLD_BOUNDS);
		scene.addChild(fog);

		final AmbientLight ambient = new AmbientLight(true, WHITE);
		ambient.setInfluencingBounds(WORLD_BOUNDS);
		scene.addChild(ambient);

		final Vector3f dir = new Vector3f(0.0f, 0.0f, -1.0f);
		final DirectionalLight dirLight = new DirectionalLight(WHITE, dir);
		dirLight.setInfluencingBounds(WORLD_BOUNDS);
		scene.addChild(dirLight);

		scene.addChild(createBackground());
		scene.addChild(tunnel);
		scene.addChild(collidables);

		su.addBranchGraph(scene);
	}

	@Override
	public void postRender() {
		final J3DGraphics2D g2 = getGraphics2D();
		renderHUD(g2);
		g2.flush(true);
	}

	private void renderHUD(final Graphics2D g) {
		if (bigFont == null) {
			bigFont = getFont().deriveFont(Font.BOLD, 16f);
		}

		// too slow
		//g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		//g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.setFont(bigFont);

		fuel.setValue(logic.getFuel());
		damage.setValue(logic.getAsteroids());

		damage.render(g, 20, 20);
		fuel.render(g, 20, 40);
	}

	private SimpleUniverse createUniverse() {
		final Viewer viewer = new Viewer(this);
		final View view = viewer.getView();
		view.setBackClipDistance(150);

		final ViewingPlatform vp = new ViewingPlatform();
		final TransformGroup vtg = vp.getViewPlatformTransform();
		vtg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		vtg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

		return new SimpleUniverse(vp, viewer);
	}

	private Background createBackground() throws IOException {
		final BufferedImage bimage = ImageIO.read(getClass().getResource("/res/stars.jpg"));
		final ImageComponent2D image = new ImageComponent2D(BufferedImage.TYPE_INT_RGB, bimage);

		final Background back = new Background(BLACK);
		back.setImageScaleMode(Background.SCALE_REPEAT);
		back.setApplicationBounds(WORLD_BOUNDS);
		back.setImage(image);

		return back;
	}

	private static TransformGroup createShip(final GameLogic logic, TransformGroup viewTG) throws IOException {
		final TransformGroup initialTranslation = new TransformGroup();

		final Transform3D t3d = new Transform3D();
		t3d.setTranslation(new Vector3d(INITIAL_SHIP_PLACEMENT_X, INITIAL_SHIP_PLACEMENT_Y, INITIAL_SHIP_PLACEMENT_Z));

		initialTranslation.setTransform(t3d);

		TransformGroup moveGroup = new TransformGroup();
		moveGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		initialTranslation.addChild(moveGroup);

		TransformGroup rotationGroup = new TransformGroup();
		rotationGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		moveGroup.addChild(rotationGroup);

		TransformGroup ship = loadShip();

		ship.setCollisionBounds(new BoundingBox(new Point3d(-0.35f, -0.15f, -0.5f), new Point3d(0.35f, 0.06f, 0.5f)));
		// Box box = new Box(0.7f, 0.19f, 1.0f, new Appearance());
		// box.setCollidable(false);
		rotationGroup.addChild(ship);
		System.out.println("Ship " + ship.getBounds());

//		KeyShipEllipseBehavior keyShipBehavior = new KeyShipEllipseBehavior(moveGroup, rotationGroup);
//		ship.addChild(keyShipBehavior);
//		keyShipBehavior.setSchedulingBounds(WORLD_BOUNDS);
		
		// Alternative KeyBehavior
		KeyShipBehavior keyShipBehavior = new KeyShipBehavior(moveGroup, viewTG);
		ship.addChild(keyShipBehavior);
		keyShipBehavior.setSchedulingBounds(WORLD_BOUNDS);

		ShipCollisionBehavior collisionBehavior = new ShipCollisionBehavior(ship, logic);
		ship.addChild(collisionBehavior);
		collisionBehavior.setSchedulingBounds(WORLD_BOUNDS);

		return initialTranslation;
	}

	private static TransformGroup loadShip() throws IOException {

		final Transform3D rotX = new Transform3D();
		rotX.rotX(Math.toRadians(90));

		final Transform3D rotY = new Transform3D();
		rotY.rotY(Math.toRadians(180));

		rotX.mul(rotY);

		TransformGroup rotateShip = new TransformGroup();
		rotateShip.setTransform(rotX);

		final BranchGroup ship = Utils.loadScene("/res/SFighter.obj");

		rotateShip.addChild(ship);

		return rotateShip;
	}

}
