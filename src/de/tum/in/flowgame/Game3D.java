package de.tum.in.flowgame;

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
import javax.media.j3d.GraphicsContext3D;
import javax.media.j3d.J3DGraphics2D;
import javax.media.j3d.LinearFog;
import javax.media.j3d.Switch;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TriangleArray;
import javax.media.j3d.View;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.Viewer;
import com.sun.j3d.utils.universe.ViewingPlatform;

import de.tum.in.flowgame.behavior.CollisionBehavior;
import de.tum.in.flowgame.behavior.CreateCollidablesBehavior;
import de.tum.in.flowgame.ui.GameOverlay;

public class Game3D extends Canvas3D {

	public static final BoundingSphere WORLD_BOUNDS = new BoundingSphere(new Point3d(), Double.POSITIVE_INFINITY);

	private static final Color3f WHITE = new Color3f(1, 1, 1);
	private static final Color3f BLACK = new Color3f(0, 0, 0);

	private final GameLogic logic;
	private final GameOverlay overlay;
	
	private final Switch switsch;

	// part of workaround for Java3D bug #501
	private final Geometry glResetGeom;
	private final Transform3D glResetTrans; 
	
	public Game3D() throws IOException {
		super(SimpleUniverse.getPreferredConfiguration());

		this.logic = new GameLogic();
		this.logic.addListener(new DefaultGameListener() {

			@Override
			public void gameStarted(final GameLogic game) {
				switsch.setWhichChild(Switch.CHILD_ALL);
			}
			
			@Override
			public void gameStopped(final GameLogic game) {
				switsch.setWhichChild(Switch.CHILD_NONE);
			}
		});

		this.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(final MouseEvent e) {
				if (!logic.isRunning()) {
					logic.start();
				}
			}
		});
		
		this.overlay = new GameOverlay(logic);
		this.addComponentListener(overlay);
		
		final SimpleUniverse su = createUniverse();
		final TransformGroup viewTG = su.getViewingPlatform().getViewPlatformTransform();
		
		// allow switching the game contents on/off at runtime
		this.switsch = new Switch();
		switsch.setCapability(Switch.ALLOW_SWITCH_READ);
		switsch.setCapability(Switch.ALLOW_SWITCH_WRITE);
		switsch.addChild(createCollidables(logic, viewTG));
		
		final BranchGroup scene = new BranchGroup();
		scene.addChild(createScene(logic));
		scene.addChild(switsch);
		
		final FrameCounterBehavior fps = new FrameCounterBehavior(100);
		fps.setSchedulingBounds(Game3D.WORLD_BOUNDS);
		fps.getListeners().addListener(overlay);
		scene.addChild(fps);
		
		su.addBranchGraph(scene);
		
		
		/*
		 * This is a workaround for Java3D bug #501
		 * https://java3d.dev.java.net/issues/show_bug.cgi?id=501
		 * from http://forums.java.net/jive/thread.jspa?threadID=28013
		 * 
		 * Without this, texture coordinates used somewhere in the 3D scenegraph
		 * will affect the texture used for rendering J3DGraphics2D in postRender().
		 */
		getGraphicsContext3D().setAppearance(new Appearance());
		
		final TriangleArray tri = new TriangleArray(3, TriangleArray.COORDINATES);
		tri.setCoordinate(0, new float[] { 0, 0, 0, 0, 1, 0, 0, 0, 1 });
		glResetGeom = tri;
		
		glResetTrans = new Transform3D();
		glResetTrans.set(new Vector3d(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE));
	}
	
	private static BranchGroup createCollidables(final GameLogic logic, final TransformGroup viewTG) throws IOException {
		final BranchGroup collidables = new BranchGroup();
		collidables.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		collidables.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		collidables.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);

		final CreateCollidablesBehavior ccb = new CreateCollidablesBehavior(collidables, logic);
		ccb.setSchedulingBounds(WORLD_BOUNDS);
		logic.addListener(ccb);

		final Ship ship = new Ship(logic, viewTG);
		collidables.addChild(ship);
		collidables.addChild(ccb);
		
		final CollisionBehavior collisionBehavior = new CollisionBehavior(collidables, logic, ship);
		collisionBehavior.setSchedulingBounds(WORLD_BOUNDS);
		collidables.addChild(collisionBehavior);
		
		collidables.addChild(new Tunnel(logic));
		
		return collidables;
	}

	private static BranchGroup createScene(final GameLogic logic) {
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

	public void start() {
		logic.start();
	}
	
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
}
