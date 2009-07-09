package de.tum.in.flowgame;

import java.io.IOException;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Fog;
import javax.media.j3d.J3DGraphics2D;
import javax.media.j3d.LinearFog;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
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

	public Game3D() throws IOException {
		super(SimpleUniverse.getPreferredConfiguration());

		this.logic = new GameLogic();
		this.overlay = new GameOverlay(logic);
		this.addComponentListener(overlay);
		
		final SimpleUniverse su = createUniverse();
		final TransformGroup viewTG = su.getViewingPlatform().getViewPlatformTransform();
		
		final BranchGroup scene = createScene(logic);
		scene.addChild(createCollidables(logic, viewTG));
		
		su.addBranchGraph(scene);
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
		scene.addChild(new Tunnel(logic));
		
		return scene;
	}

	public void start() {
		logic.start();
	}
	
	@Override
	public void postRender() {
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
