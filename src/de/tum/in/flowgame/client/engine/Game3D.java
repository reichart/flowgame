package de.tum.in.flowgame.client.engine;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Fog;
import javax.media.j3d.Geometry;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.GraphicsContext3D;
import javax.media.j3d.Group;
import javax.media.j3d.J3DGraphics2D;
import javax.media.j3d.LinearFog;
import javax.media.j3d.Switch;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TriangleArray;
import javax.media.j3d.View;
import javax.vecmath.Color3f;
import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.Viewer;
import com.sun.j3d.utils.universe.ViewingPlatform;

import de.tum.in.flowgame.DefaultGameListener;
import de.tum.in.flowgame.GameListener;
import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.client.engine.behavior.CollisionBehavior;
import de.tum.in.flowgame.client.engine.behavior.CreateNewCollidablesBehavior;
import de.tum.in.flowgame.client.engine.behavior.ForwardBehavior;
import de.tum.in.flowgame.client.engine.behavior.FrameCounterBehavior;
import de.tum.in.flowgame.client.sound.Sound;
import de.tum.in.flowgame.client.sound.SoundManager;
import de.tum.in.flowgame.client.ui.GameMenu;
import de.tum.in.flowgame.client.ui.GameOverlay;

/**
 * This class defines the 3D layer and some important game components.
 * @see Canvas3D
 * @see GameListener
 * @see Tunnel
 * @see Ship
 * @see CreateCollidables
 * @see CreateNewCollidablesBehavior
 * @see CollisionBehavior
 */
public class Game3D extends Canvas3D {

	/**
	 * {@link BoundingSphere} of our 3D universe.
	 */
	public static final BoundingSphere WORLD_BOUNDS = new BoundingSphere(new Point3d(), Double.POSITIVE_INFINITY);

	private static final Color3f WHITE = new Color3f(1, 1, 1);
	private static final Color3f BLACK = new Color3f(0, 0, 0);

	private final GameOverlay overlay;
	private final BranchGroup collidables;

	private Ship ship;

	private final Switch switsch;

	// part of workaround for Java3D bug #501
	private final transient Geometry glResetGeom;
	private final transient Transform3D glResetTrans;

	private GameListener listener;

	private CreateCollidables cc;
	private CollisionBehavior collisionBehavior;
	private CreateNewCollidablesBehavior cncb;
	private Tunnel tunnel;

	private final TransformGroup viewTG;

	/**
	 * Creates a new Game3D.
	 * 
	 * @throws IOException
	 */
	public Game3D() throws IOException {
		super(SimpleUniverse.getPreferredConfiguration());

		this.listener = new DefaultGameListener() {

			@Override
			public void added(final GameLogic game) {
				game.addListener(cc);
				game.addListener(ship);
				game.addListener(overlay);
				collisionBehavior.setGameLogic(game);
				cncb.setGameLogic(game);
				tunnel.setGameLogic(game);
			}

			@Override
			public void removed(final GameLogic game) {
				game.removeListener(cc);
				game.removeListener(ship);
				game.removeListener(overlay);
				collisionBehavior.setGameLogic(null);
				cncb.setGameLogic(null);
				tunnel.setGameLogic(null);
			}

			@Override
			public void gamePaused(final GameLogic game) {
				SoundManager.getInstance().pause(Sound.GAMEPLAY);
			}

			@Override
			public void gameResumed(final GameLogic game) {
				SoundManager.getInstance().unpause(Sound.GAMEPLAY);
			}

			@Override
			public void gameStarted(final GameLogic game) {
				System.out.println("Game3D.gameStarted()");
				switsch.setWhichChild(Switch.CHILD_ALL);
				SoundManager.getInstance().stop(Sound.MENU, false);
				SoundManager.getInstance().once(Sound.GAMEPLAY);
			}

			@Override
			public void gameStopped(final GameLogic game) {
				System.out.println("Game3D.gameStopped()");
				switsch.setWhichChild(Switch.CHILD_NONE);
				SoundManager.getInstance().stop(Sound.GAMEPLAY, true);
				SoundManager.getInstance().loop(Sound.MENU);
			}
		};

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(final MouseEvent e) {
				requestFocus();
			}
		});

		final SimpleUniverse su = createUniverse();
		viewTG = su.getViewingPlatform().getViewPlatformTransform();

		final BranchGroup scene = new BranchGroup();
		scene.addChild(createScene());

		// allow switching the game contents on/off at runtime
		this.switsch = new Switch();
		switsch.setCapability(Switch.ALLOW_SWITCH_READ);
		switsch.setCapability(Switch.ALLOW_SWITCH_WRITE);

		this.collidables = createCollidables(viewTG);
		switsch.addChild(this.collidables);

		scene.addChild(switsch);

		this.overlay = new GameOverlay(this);
		this.addComponentListener(overlay);

		final FrameCounterBehavior fps = new FrameCounterBehavior(100);
		fps.setSchedulingBounds(Game3D.WORLD_BOUNDS);
		fps.getListeners().addListener(overlay);
		scene.addChild(fps);

		scene.compile();
		
		su.addBranchGraph(scene);
		
		/*
		 * This is a workaround for Java3D bug #501
		 * https://java3d.dev.java.net/issues/show_bug.cgi?id=501 from
		 * http://forums.java.net/jive/thread.jspa?threadID=28013
		 * 
		 * Without this, texture coordinates used somewhere in the 3D scenegraph
		 * will affect the texture used for rendering J3DGraphics2D in
		 * postRender().
		 */
		getGraphicsContext3D().setAppearance(new Appearance());

		final TriangleArray tri = new TriangleArray(3, GeometryArray.COORDINATES);
		tri.setCoordinate(0, new float[] { 0, 0, 0, 0, 1, 0, 0, 0, 1 });
		glResetGeom = tri;

		glResetTrans = new Transform3D();
		glResetTrans.set(new Vector3d(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE));
	}

	private BranchGroup createCollidables(final TransformGroup viewTG) throws IOException {
		final BranchGroup collidables = new BranchGroup();
		collidables.setCapability(Group.ALLOW_CHILDREN_EXTEND);
		collidables.setCapability(Group.ALLOW_CHILDREN_READ);
		collidables.setCapability(Group.ALLOW_CHILDREN_WRITE);
		
		TransformGroup forwardGroup = new TransformGroup();
		forwardGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		forwardGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		ForwardBehavior forwardBehavior = new ForwardBehavior(forwardGroup, viewTG);
		forwardBehavior.setSchedulingBounds(Game3D.WORLD_BOUNDS);

		tunnel = new Tunnel();
		forwardGroup.addChild(tunnel);
		
		ship = new Ship(viewTG, forwardBehavior, tunnel);
		ship.addChild(forwardBehavior);
		forwardGroup.addChild(ship);
		collidables.addChild(forwardGroup);
		
		this.cc = new CreateCollidables(collidables);
		this.cncb = new CreateNewCollidablesBehavior(cc, ship);
		cncb.setSchedulingBounds(Game3D.WORLD_BOUNDS);

		collidables.addChild(cncb);

		this.collisionBehavior = new CollisionBehavior(collidables, ship);
		collisionBehavior.setSchedulingBounds(WORLD_BOUNDS);
		collidables.addChild(collisionBehavior);
		return collidables;
	}

	private static BranchGroup createScene() {
		final BranchGroup scene = new BranchGroup();

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

		scene.addChild(new Space());

		return scene;
	}

	/**
	 * Renders the {@link GameOverlay}(score, messages, etc...) for our 3D plane.
	 */
	@Override
	public void postRender() {
		// part of workaround for Java3D bug #501
		final GraphicsContext3D gc = getGraphicsContext3D();
		gc.setModelTransform(glResetTrans);
		gc.draw(glResetGeom);

		final J3DGraphics2D g2 = getGraphics2D();
		overlay.render(g2);
		g2.flush(true);
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

	/**
	 * 
	 * @return The {@link Ship} that we are flying in our game.
	 */
	public Ship getShip() {
		return this.ship;
	}
	
	public Point2d getShip2DCoords() {
		Point3d shipPos1 = new Point3d(getShip().getCoords());
		shipPos1.z = shipPos1.z + Ship.INITIAL_SHIP_PLACEMENT_Z;
		Transform3D viewTrans = new Transform3D();
		viewTG.getTransform(viewTrans);
		viewTrans.invert();

		Transform3D t = new Transform3D();
		this.getVworldToImagePlate(t);
		viewTrans.add(t);
		
		t.transform(shipPos1);

		Point2d pointOnScreen = new Point2d();
		this.getPixelLocationFromImagePlate(shipPos1, pointOnScreen);
		
		return pointOnScreen;
	}
	
	public GameListener getListener() {
		return listener;
	}
	
	public GameMenu getMenu() {
		return overlay.getMenu();
	}
}
