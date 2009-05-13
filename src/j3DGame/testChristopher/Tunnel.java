package j3DGame.testChristopher;


import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Container;
import java.net.URL;

import javax.media.j3d.Alpha;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TexCoordGeneration;
import javax.media.j3d.Texture;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4f;

import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.Viewer;
import com.sun.j3d.utils.universe.ViewingPlatform;

public class Tunnel extends Applet{

	public Tunnel() {
		
		BoundingSphere worldBounds = new BoundingSphere(new Point3d(0.0,0.0,0.0), Double.POSITIVE_INFINITY);
		
		setLayout(new BorderLayout());
		Canvas3D canvas3D = new Canvas3D(SimpleUniverse
				.getPreferredConfiguration());
		add("Center", canvas3D);

		// create a ViewingPlatform with 1 TransformGroups above the
		// ViewPlatform
		ViewingPlatform vp = new ViewingPlatform(1);

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
		
		RotationInterpolator ri = new RotationInterpolator(new Alpha(-1, 4000), rc);
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
		
		Background back = new Background(new Color3f(0.2f, 1.0f, 0.8f));
		back.setApplicationBounds(worldBounds);
		scene.addChild(back);
		
		URL url = getClass().getResource("/j3DGame/testChristopher/res/lava.jpg");
		TextureLoader loader = new TextureLoader(url, new Container());
		Texture tunnelTexture = loader.getTexture();
		tunnelTexture.setBoundaryModeS(Texture.WRAP);
		tunnelTexture.setBoundaryModeT(Texture.WRAP);
		tunnelTexture.setBoundaryColor(new Color4f(0.0f, 1.0f, 0.0f, 0.0f));
		// Set up the texture attributes
		// could be REPLACE, BLEND or DECAL instead of MODULATE
		TextureAttributes texAttr = new TextureAttributes();
		texAttr.setTextureMode(TextureAttributes.REPLACE);
	
		TexCoordGeneration coord = new TexCoordGeneration();
		coord.setPlaneS(new Vector4f(0.02f,0,0,0));
		coord.setPlaneT(new Vector4f(0,0.01f,0,0));
		
		Appearance tunnelAppearance = new Appearance();
		tunnelAppearance.setTexture(tunnelTexture);
		tunnelAppearance.setTextureAttributes(texAttr);
		tunnelAppearance.setTexCoordGeneration(coord);
		
		Cylinder cyl = new Cylinder(8.0f, 2000.0f, Cylinder.GENERATE_NORMALS_INWARD, tunnelAppearance);
		Shape3D tunnel = (Shape3D) cyl.getShape(Cylinder.BODY).cloneNode(true);
		Transform3D rotation = new Transform3D();
		rotation.rotX(Math.PI/2);
		TransformGroup tunnelGroup = new TransformGroup(rotation);
		tunnelGroup.addChild(tunnel);
		tc.addChild(tunnelGroup);
		
		
		
//		KeyNavigatorBehavior keyNav = new KeyNavigatorBehavior(vtg);
//		keyNav.setSchedulingBounds(worldBounds);
//		vtg.addChild(keyNav);
		
		ForwardNavigatorBehavior fwdNav = new ForwardNavigatorBehavior(vtg);
		fwdNav.setSchedulingBounds(worldBounds);
		vtg.addChild(fwdNav);
		

		SimpleUniverse su = new SimpleUniverse(vp, viewer);
		su.addBranchGraph(scene);

		viewer.getView().setBackClipDistance(100);
	}	
	
}
