package de.tum.in.flowgame;

import java.io.IOException;

import javax.media.j3d.Alpha;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Link;
import javax.media.j3d.PositionInterpolator;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.SharedGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;

public class Collidable {

	private double speed = 120;
	
	public Collidable(BranchGroup collidables, SharedGroup group, float x) throws IOException {
//		BranchGroup collidable = GameApplet.loadScene("/res/asteroid.obj");
//		System.out.println("Collidable " + collidable.getCollisionBounds());
//		Box box = new Box(0.7f, 1.0f, 0.25f, new Appearance());

//		RemoveCollidableBehavior rCB = new RemoveCollidableBehavior(collidables);
//		rCB.setSchedulingBounds(GameApplet.WORLD_BOUNDS);
//		collidables.addChild(rCB);
		
		Link link = new Link(group);
		
		TransformGroup rotateCollidable = new TransformGroup();
		rotateCollidable.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		final RotationInterpolator ri = new RotationInterpolator(new Alpha(-1, 10000), rotateCollidable);
		ri.setSchedulingBounds(GameApplet.WORLD_BOUNDS);
		rotateCollidable.addChild(ri);
		rotateCollidable.addChild(link);
		
		Transform3D trans = new Transform3D();
		trans.rotY(Math.toRadians(90));
		
		TransformGroup moveCollidable = new TransformGroup();
		moveCollidable.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		moveCollidable.setTransform(trans);
		moveCollidable.addChild(rotateCollidable);
		
		//set deep in tunnel and add movement towards players ship
		final Transform3D t3d = new Transform3D();
		t3d.setTranslation(new Vector3d(0, 0, -(Tunnel.TUNNEL_PARTS-1)*Tunnel.TUNNEL_LENGTH));
		moveCollidable.setTransform(t3d);
		ForwardNavigatorBehavior fwdNav = new ForwardNavigatorBehavior(moveCollidable, speed);
		fwdNav.setSchedulingBounds(GameApplet.WORLD_BOUNDS);
		moveCollidable.addChild(fwdNav);
		
		Transform3D initialTransform = new Transform3D();
		initialTransform.setTranslation(new Vector3d(x, -Ellipse.getYOnPosition(x)-0.1, 0.0f));
		
		Transform3D initialScaleTransform = new Transform3D();
		initialScaleTransform.setScale(0.3f);
		
		TransformGroup initScale = new TransformGroup();
		initScale.setTransform(initialScaleTransform);
		initScale.addChild(moveCollidable);
		
		TransformGroup initTransformGroup = new TransformGroup();
		initTransformGroup.setTransform(initialTransform);
		initTransformGroup.addChild(initScale);
		
		BranchGroup bg = new BranchGroup();
		bg.addChild(initTransformGroup);		
//		bg.addChild(pi);
		
		collidables.addChild(bg);
	}
	
}
