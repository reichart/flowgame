package de.tum.in.flowgame;

import java.io.IOException;

import javax.media.j3d.Alpha;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.PositionInterpolator;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class Collidable {

	public Collidable(BranchGroup collidables, float x) throws IOException {
		BranchGroup collidable = GameApplet.loadScene("/res/fuelcan2.obj");
		collidable.setCollisionBounds(new BoundingBox(new Point3d(-0.35f, -0.5f, -0.125f), new Point3d(0.35f, 0.5f, 0.125f)));
//		System.out.println("Collidable " + collidable.getCollisionBounds());
//		Box box = new Box(0.7f, 1.0f, 0.25f, new Appearance());

//		RemoveCollidableBehavior rCB = new RemoveCollidableBehavior(collidables);
//		rCB.setSchedulingBounds(GameApplet.WORLD_BOUNDS);
//		collidables.addChild(rCB);
		
		TransformGroup rotateCollidable = new TransformGroup();
		rotateCollidable.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		final RotationInterpolator ri = new RotationInterpolator(new Alpha(-1, 10000), rotateCollidable);
		ri.setSchedulingBounds(GameApplet.WORLD_BOUNDS);
		rotateCollidable.addChild(ri);
		rotateCollidable.addChild(collidable);
//		rotateCollidable.addChild(box);
		
		Transform3D trans = new Transform3D();
		trans.rotY(Math.toRadians(90));
		
		TransformGroup moveCollidable = new TransformGroup();
		moveCollidable.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		moveCollidable.setTransform(trans);
		final PositionInterpolator pi = new PositionInterpolator(new Alpha(-1, 5000, 0, 10000, 0, 0), moveCollidable , trans, Tunnel.TUNNEL_LENGTH, -50);
		pi.setSchedulingBounds(GameApplet.WORLD_BOUNDS);
		moveCollidable.addChild(pi);
		moveCollidable.addChild(rotateCollidable);
//		moveCollidable.addChild(box);
		
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
		
		collidables.addChild(initTransformGroup);
	}
	
}
