package j3DGame.testChristopher;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.media.j3d.Alpha;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Fog;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.LinearFog;
import javax.media.j3d.Material;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Texture;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.Viewer;
import com.sun.j3d.utils.universe.ViewingPlatform;

public class GameApplet extends Applet {

	public static final BoundingSphere WORLD_BOUNDS = new BoundingSphere(new Point3d(), Double.POSITIVE_INFINITY);

	private static final Color3f WHITE = new Color3f(1, 1, 1);
	private static final Color3f BLACK = new Color3f(0, 0, 0);

	@Override
	public void init() {
		System.out.println("GameApplet.init()");
	}
	
	@Override
	public void destroy() {
		System.out.println("GameApplet.destroy()");
	}
	
	public static void main(final String[] args) throws Exception {
		final Frame frame = new Frame();
		frame.setSize(800, 600);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				System.exit(0);
			}
		});
		frame.add(new GameApplet());
		frame.setVisible(true);
	}
	
	public GameApplet() throws Exception {
		System.out.println("GameApplet.GameApplet()");
		
		setLayout(new BorderLayout());
		final Canvas3D canvas3D = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
		add(BorderLayout.CENTER, canvas3D);

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

		scene.addChild(createShip());
		scene.addChild(createBackground());
		scene.addChild(new Tunnel());

		final SimpleUniverse su = createUniverse(canvas3D);
		su.addBranchGraph(scene);

		// com.tornadolabs.j3dtree.Java3dTree tree = new
		// com.tornadolabs.j3dtree.Java3dTree();
		// tree.recursiveApplyCapability(scene);
		// tree.updateNodes(su);
		// tree.setVisible(true);
	}

	private SimpleUniverse createUniverse(final Canvas3D canvas3D) {
		// create a Viewer and attach to its canvas
		// a Canvas3D can only be attached to a single Viewer
		final Viewer viewer = new Viewer(canvas3D);
		final View view = viewer.getView();
		view.setBackClipDistance(150);

		// set capabilities on the TransformGroup so that the
		// KeyNavigatorBehavior can modify the Viewer's position
		final ViewingPlatform vp = new ViewingPlatform();
		final TransformGroup vtg = vp.getViewPlatformTransform();
		vtg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		vtg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

		final KeyNavigatorBehavior keyNav = new KeyNavigatorBehavior(vtg);
		keyNav.setSchedulingBounds(WORLD_BOUNDS);
		vtg.addChild(keyNav);

		final SimpleUniverse su = new SimpleUniverse(vp, viewer);
		return su;
	}

	private Background createBackground() throws IOException {
		final BufferedImage bimage = ImageIO.read(getClass().getResource("/j3DGame/testChristopher/res/stars.jpg"));
		final ImageComponent2D image = new ImageComponent2D(BufferedImage.TYPE_INT_RGB, bimage);

		final Background back = new Background(BLACK);
		back.setImageScaleMode(Background.SCALE_REPEAT);
		back.setApplicationBounds(WORLD_BOUNDS);
		back.setImage(image);

		return back;
	}

	private TransformGroup createShip() throws IOException {
		final TransformGroup tc = new TransformGroup();
		final TransformGroup rc = new TransformGroup();
		rc.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		rc.addChild(loadShip());

		final RotationInterpolator ri = new RotationInterpolator(new Alpha(-1, 10000), rc);
		ri.setSchedulingBounds(WORLD_BOUNDS);
		rc.addChild(ri);
		tc.addChild(rc);

		final Transform3D t3d = new Transform3D();
		t3d.setTranslation(new Vector3d(0, 0, -6f));
		tc.setTransform(t3d);
		return tc;
	}

	private BranchGroup loadShip() throws IOException {
		final Scene scene = loadScene("res/SFighter.obj");
		final BranchGroup sceneGroup = scene.getSceneGroup();

		final Shape3D ship = (Shape3D) sceneGroup.getChild(0);

		final Material material = new Material();
		material.setAmbientColor(.4f, .4f, .4f);
		material.setDiffuseColor(.6f, .6f, .6f);
		material.setSpecularColor(.8f, .8f, .8f);
		material.setLightingEnable(true);
		material.setShininess(32f);

		final Texture tex = getTexture("res/SFighter.bmp");

		final TextureAttributes texAttr = new TextureAttributes();
		texAttr.setTextureMode(TextureAttributes.MODULATE);
		
		final Appearance appear = new Appearance();
		appear.setMaterial(material);
		appear.setTexture(tex);
		appear.setTextureAttributes(texAttr);
		ship.setAppearance(appear);

		return sceneGroup;
	}

	protected static Texture getTexture(final String path) {
		final URL url = GameApplet.class.getResource(path);
		final TextureLoader loader = new TextureLoader(url, TextureLoader.GENERATE_MIPMAP | TextureLoader.BY_REFERENCE,
				null);

		final Texture texture = loader.getTexture();
		texture.setBoundaryModeS(Texture.WRAP);
		texture.setBoundaryModeT(Texture.WRAP);

		texture.setMagFilter(Texture.NICEST); // filtering
		texture.setMinFilter(Texture.NICEST); // tri-linear filtering

		texture.setAnisotropicFilterMode(Texture.ANISOTROPIC_SINGLE_VALUE);
		texture.setAnisotropicFilterDegree(4);

		texture.setBoundaryColor(new Color4f(0.0f, 1.0f, 0.0f, 0.0f));
		return texture;
	}

	public static Scene loadScene(final String resource) throws IOException {
		final ObjectFile loader = new ObjectFile(ObjectFile.RESIZE);
		return loader.load(GameApplet.class.getResource(resource));
	}
}
