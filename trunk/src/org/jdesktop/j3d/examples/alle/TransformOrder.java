package org.jdesktop.j3d.examples.alle;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Group;
import javax.media.j3d.Material;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

public class TransformOrder extends Applet {
	public static final int X = 1;
	public static final int Y = 2;
	public static final int Z = 3;
	public static final int ROTATE_TOP = 4;
	public static final int TRANSLATE_TOP = 5;
	public static final int NO_TRANSFORM = 6;
	private SimpleUniverse universe;
	private Canvas3D canvas;
	private BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000.0);
	private Appearance red = new Appearance();
	private Appearance yellow = new Appearance();
	private Appearance purple = new Appearance();
	Transform3D rotate = new Transform3D();
	Transform3D translate = new Transform3D();

	public void setupView() {
		/**
		 * einige auf den Ansichtszweig bezogene Dinge zur entsprechenden Seite
		 * des Szenengraphen hinzufügen
		 */
		// Mausinteraktion zur ViewingPlatform hinzufügen
		OrbitBehavior orbit = new OrbitBehavior(canvas, OrbitBehavior.REVERSE_ALL | OrbitBehavior.STOP_ZOOM);
		orbit.setSchedulingBounds(bounds);
		ViewingPlatform viewingPlatform = universe.getViewingPlatform();
		// Damit wird die ViewingPlatform etwas zurückgesetzt,
		// so dass man die Objekte in der Szene sehen kann.
		viewingPlatform.setNominalViewingTransform();
		viewingPlatform.setViewPlatformBehavior(orbit);
	}

	// beide Äste des Graphen erstellen, Ändern der Reihenfolge, in der
	// Kindelemente hinzugefügt werden
	// Da der Group-Knoten nur ein Elternelement haben kann, muss man
	// neue Translations- und Rotations-Gruppen-Knoten für jeden Ast erstellen.
	Group rotateOnTop() {
		Group root = new Group();
		TransformGroup objRotate = new TransformGroup(rotate);
		TransformGroup objTranslate = new TransformGroup(translate);
		Cone redCone = new Cone(.3f, 0.7f, Primitive.GENERATE_NORMALS, red);
		root.addChild(objRotate);
		objRotate.addChild(objTranslate);
		objTranslate.addChild(redCone); // roten Kegel hinzufügen
		return root;
	}

	Group translateOnTop() {
		Group root = new Group();
		TransformGroup objRotate = new TransformGroup(rotate);
		TransformGroup objTranslate = new TransformGroup(translate);
		Cone yellowCone = new Cone(.3f, 0.7f, Primitive.GENERATE_NORMALS, yellow);
		root.addChild(objTranslate);
		objTranslate.addChild(objRotate);
		objRotate.addChild(yellowCone); // gelben Kegel hinzufügen
		return root;
	}

	Group noTransform() {
		Cone purpleCone = new Cone(.3f, 0.7f, Primitive.GENERATE_NORMALS, purple);
		return purpleCone;
	}

	/**
	 * Eine Achse mit Hilfe der Zylinder-Primitive darstellen. Zylinder ist an
	 * der y-Achse ausgerichtet, so dass man ihn rotieren muss, wenn man die x-
	 * und z-Achse erstellt.
	 */
	public TransformGroup createAxis(int type) {
		// appearance und lightingProps werden für
		// Beleuchtung verwendet. Jede Achse hat eine andere Farbe.
		Appearance appearance = new Appearance();
		Material lightingProps = new Material();
		Transform3D t = new Transform3D();
		switch (type) {
		case Z:
			t.rotX(Math.toRadians(90.0));
			lightingProps.setAmbientColor(1.0f, 0.0f, 0.0f);
			break;
		case Y:
			// keine Rotation erforderlich, da der Zylinder bereits an der
			// y-Achse ausgerichtet ist
			lightingProps.setAmbientColor(0.0f, 1.0f, 0.0f);
			break;
		case X:
			t.rotZ(Math.toRadians(90.0));
			lightingProps.setAmbientColor(0.0f, 0.0f, 1.0f);
			break;
		default:
			break;
		}
		appearance.setMaterial(lightingProps);
		TransformGroup objTrans = new TransformGroup(t);
		objTrans.addChild(new Cylinder(.03f, 2.5f, Primitive.GENERATE_NORMALS, appearance));
		return objTrans;
	}

	/**
	 * x-, y- und z-Achse sowie drei Kegel erstellen. Plus einige
	 * Beleuchtungseffekte, um die Szene besser darzustellen.
	 */
	public BranchGroup createSceneGraph() {
		// Wurzel des Ast-Graphen erstellen
		BranchGroup objRoot = new BranchGroup();
		// 45°-Rotation um die x-Achse
		rotate.rotX(Math.toRadians(45.0));
		// Translation entlang der y-Achse
		translate.setTranslation(new Vector3f(0.0f, 2.0f, 1.0f)); // SCD
																	// 0.0f));
		// Material-Objekte beziehen sich auf die Beleuchtung,
		// dazu später mehr
		Material redProps = new Material();
		redProps.setAmbientColor(1.0f, 0.0f, 0.0f); // roter Kegel
		red.setMaterial(redProps);
		Material yellowProps = new Material();
		yellowProps.setAmbientColor(1.0f, 1.0f, 0.0f); // gelber Kegel
		yellow.setMaterial(yellowProps);
		Material purpleProps = new Material();
		purpleProps.setAmbientColor(0.8f, 0.0f, 0.8f); // violetter Kegel
		purple.setMaterial(purpleProps);
		// x-, y- und z-Achse und dann drei Kegel-Äste erstellen
		objRoot.addChild(createAxis(X));
		objRoot.addChild(createAxis(Y));
		objRoot.addChild(createAxis(Z));
		objRoot.addChild(noTransform()); // violetter Kegel
		objRoot.addChild(rotateOnTop()); // roter Kegel
		objRoot.addChild(translateOnTop()); // gelber Kegel
		// ein paar Lichtquellen hinzufügen, damit nicht alles
		// im Dunkeln bleibt
		Color3f lightColor = new Color3f(.3f, .3f, .3f);
		AmbientLight ambientLight = new AmbientLight(lightColor);
		ambientLight.setInfluencingBounds(bounds);
		objRoot.addChild(ambientLight);
		DirectionalLight directionalLight = new DirectionalLight();
		directionalLight.setColor(lightColor);
		directionalLight.setInfluencingBounds(bounds);
		objRoot.addChild(directionalLight);
		return objRoot;
	}

	public TransformOrder() {
	}

	public void init() {
		BranchGroup scene = createSceneGraph();
		setLayout(new BorderLayout());
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		canvas = new Canvas3D(config);
		add("Center", canvas);
		// eine einfache Szene erstellen und diese dem virtuellen Universum
		// hinzufügen
		universe = new SimpleUniverse(canvas);
		setupView();
		universe.addBranchGraph(scene);
	}

	public void destroy() {
		universe.removeAllLocales();
	}

	//
	// Der folgende Code ermöglicht, dass TransformOrder als Anwendung
	// und als Applet ausgeführt werden kann
	//
	public static void main(String[] args) {
		new MainFrame(new TransformOrder(), 256, 256);
	}
}
