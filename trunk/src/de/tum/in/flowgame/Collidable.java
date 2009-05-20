package de.tum.in.flowgame;

import java.io.IOException;

import javax.media.j3d.Alpha;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.PositionInterpolator;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3d;

public class Collidable {

	public Collidable(BranchGroup collidables) throws IOException {
		BranchGroup collidable = GameApplet.loadScene("/res/fuelcan2.obj");
		Bounds bounds = new BoundingSphere(new Point3d(0,0,0), 10f);
		collidable.setBounds(bounds );
		
		RemoveCollidableBehavior rCB = new RemoveCollidableBehavior(collidables);
		rCB.setSchedulingBounds(GameApplet.WORLD_BOUNDS);
		collidables.addChild(rCB);
		
		TransformGroup rotateCollidable = new TransformGroup();
		rotateCollidable.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		final RotationInterpolator ri = new RotationInterpolator(new Alpha(-1, 10000), rotateCollidable);
		ri.setSchedulingBounds(GameApplet.WORLD_BOUNDS);
		rotateCollidable.addChild(ri);
		rotateCollidable.addChild(collidable);
		
		Transform3D trans;
		trans = new Transform3D();
		trans.rotY(Math.toRadians(90));
		
		TransformGroup moveCollidable = new TransformGroup();
		moveCollidable.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		moveCollidable.setTransform(trans);
		final PositionInterpolator pi = new PositionInterpolator(new Alpha(1, 5000, 0, 10000, 0, 0), moveCollidable , trans, Tunnel.TUNNEL_LENGTH, -50);
		pi.setSchedulingBounds(GameApplet.WORLD_BOUNDS);
		moveCollidable.addChild(pi);
		moveCollidable.addChild(rotateCollidable);
				
		collidables.addChild(moveCollidable);
	}
	
}
