package j3DGame.testChristopher;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.net.URL;

import javax.media.j3d.Alpha;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.LinearFog;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TexCoordGeneration;
import javax.media.j3d.Texture;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.media.j3d.ViewPlatform;
import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.Viewer;
import com.sun.j3d.utils.universe.ViewingPlatform;

public class Tunnel extends Applet {

	private float tunnelPartLength = 500.0f;
	private float tunnelPartRadius = 8.0f;
	private int numberOfTunnelParts = 3;
	private BoundingSphere worldBounds;

	public Tunnel() {

		worldBounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), Double.POSITIVE_INFINITY);

		setLayout(new BorderLayout());
		Canvas3D canvas3D = new Canvas3D(SimpleUniverse
				.getPreferredConfiguration());
		add("Center", canvas3D);

		// create a ViewingPlatform with 1 TransformGroups above the
		// ViewPlatform
		ViewingPlatform vp = new ViewingPlatform(1);
		vp.setBounds(new BoundingSphere(new Point3d(), 1.0));

		// create a Viewer and attach to its canvas
		// a Canvas3D can only be attached to a single Viewer
		Viewer viewer = new Viewer(canvas3D);

		// set capabilities on the TransformGroup so that the
		// KeyNavigatorBehavior
		// can modify the Viewer's position
		TransformGroup vtg = vp.getViewPlatformTransform();
		vtg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		vtg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

		BranchGroup scene = new BranchGroup();

		TransformGroup tc = new TransformGroup();
		TransformGroup rc = new TransformGroup();
		rc.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		rc.addChild(new ColorCube(0.4));

		RotationInterpolator ri = new RotationInterpolator(new Alpha(-1, 4000),
				rc);
		ri.setSchedulingBounds(worldBounds);
		rc.addChild(ri);
		tc.addChild(rc);

		Transform3D trans = new Transform3D();
		trans.set(new Vector3d(0.0f, 0.0f, -25.0f));

		Transform3D t3d = new Transform3D();
		vtg.getTransform(t3d);
		t3d.mul(trans);
		tc.setTransform(t3d);

		scene.addChild(tc);

		Color3f color = new Color3f(0.8f, 0.8f, 0.8f);

		Background back = new Background(color);
		back.setApplicationBounds(worldBounds);
		scene.addChild(back);

		KeyNavigatorBehavior keyNav = new KeyNavigatorBehavior(vtg);
		keyNav.setSchedulingBounds(worldBounds);
		vtg.addChild(keyNav);

		ForwardNavigatorBehavior fwdNav = new ForwardNavigatorBehavior(vtg);
		fwdNav.setSchedulingBounds(worldBounds);
		vtg.addChild(fwdNav);

//	    LinearFog fog = new LinearFog(color, 0, 100);
//	    fog.setCapability(Fog.ALLOW_COLOR_WRITE);
//	    fog.setCapability(ExponentialFog.ALLOW_DENSITY_WRITE);
//	    fog.setInfluencingBounds(worldBounds);
//	    scene.addChild(fog);
		
		SimpleUniverse su = new SimpleUniverse(vp, viewer);
		
		final View view = viewer.getView();
		ViewPlatform test = view.getViewPlatform();
		test.setActivationRadius(1.0f);
		System.out.println(test.getActivationRadius());

		createTunnelElements(scene, numberOfTunnelParts, test);
		

		su.addBranchGraph(scene);
		

		com.tornadolabs.j3dtree.Java3dTree tree = new com.tornadolabs.j3dtree.Java3dTree();
		tree.recursiveApplyCapability(scene);
		tree.updateNodes(su);
		tree.setVisible(true);		

		
		view.setBackClipDistance(150);
	}

	void createTunnelElements(BranchGroup scene, int elements, ViewPlatform test) {
		final Texture texture = getTexture("/j3DGame/testChristopher/res/lava.jpg");
		// Set up the texture attributes
		// could be REPLACE, BLEND or DECAL instead of MODULATE
		TextureAttributes texAttr = new TextureAttributes();
		texAttr.setTextureMode(TextureAttributes.REPLACE);

		TexCoordGeneration coord = new TexCoordGeneration();
		coord.setPlaneS(new Vector4f(0.02f, 0, 0, 0));
		coord.setPlaneT(new Vector4f(0, 0.01f, 0, 0));

		Appearance tunnelAppearance = new Appearance();
		tunnelAppearance.setTexture(texture);
		tunnelAppearance.setTextureAttributes(texAttr);
		tunnelAppearance.setTexCoordGeneration(coord);
		Cylinder cyl = new Cylinder(tunnelPartRadius, tunnelPartLength, 
				Cylinder.GENERATE_NORMALS_INWARD, 50, 1, tunnelAppearance);

		Transform3D rotation = new Transform3D();
		rotation.rotX(Math.PI / 2);

		BoundingBox tunnelPartBounds = new BoundingBox(new Point3d(
				-(double) tunnelPartLength / 2, -(double) tunnelPartLength / 2,
				-(double) tunnelPartLength / 2), new Point3d(
				(double) tunnelPartLength / 2, (double) tunnelPartLength / 2,
				(double) tunnelPartLength / 2));
		// BoundingSphere tunnelPartBounds = new BoundingSphere(new Point3d(0.0,
		// 0.0, 0.0), tunnelPartLength / 2);
		float q = 1.0f / elements;

		for (int i = 0; i < elements; i++) {
			Shape3D tunnelShape = (Shape3D) cyl.getShape(Cylinder.BODY)
					.cloneNode(true);
			tunnelShape.setBoundsAutoCompute(false);
			tunnelShape.setBounds(tunnelPartBounds);

			Appearance a = new Appearance();
			ColoringAttributes c = new ColoringAttributes();
			c.setColor(new Color3f(q * i, q * i, q * i));
			a.setColoringAttributes(c);

			tunnelShape.setAppearance(tunnelAppearance);
//			 tunnelShape.setAppearance(a);

			TransformGroup tunnelRotationGroup = new TransformGroup(rotation);
			TransformGroup tunnelTranslationGroup = new TransformGroup();
			tunnelTranslationGroup
					.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
			tunnelTranslationGroup
					.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
			Transform3D partTrans = new Transform3D();
			partTrans.set(new Vector3f(0.0f, 0.0f, -(tunnelPartLength * i)));
			Transform3D part = new Transform3D();
			tunnelTranslationGroup.getTransform(part);
			part.mul(partTrans);
			tunnelTranslationGroup.setTransform(part);
			TunnelPartMoveBehavior reuse = new TunnelPartMoveBehavior(
					tunnelShape, tunnelTranslationGroup,
					(double) tunnelPartLength, elements);
			reuse.setSchedulingBounds(worldBounds);
			tunnelTranslationGroup.addChild(reuse);
			// tunnelTranslationGroup.addChild(tunnelShape);
			tunnelTranslationGroup.addChild(tunnelRotationGroup);
			tunnelRotationGroup.addChild(tunnelShape);
			scene.addChild(tunnelTranslationGroup);
		}
	}

	private Texture getTexture(final String path) {
		final URL url = getClass().getResource(path);
		final TextureLoader loader = new TextureLoader(url, TextureLoader.GENERATE_MIPMAP | TextureLoader.BY_REFERENCE, null);
		
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
}
