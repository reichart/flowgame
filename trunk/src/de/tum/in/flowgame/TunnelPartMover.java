package de.tum.in.flowgame;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * This is the TunnelPartMover class. Old tunnel parts (already flown through)
 * are moved to the front.
 */
public class TunnelPartMover {

	private Point3d dp = new Point3d(0.0,0.0,0.0);
	private Vector3d partPos = new Vector3d();
	private Transform3D partTrans = new Transform3D();
	private TransformGroup targetTG;

	public TunnelPartMover(TransformGroup targetTG, double zDist, int parts) {
		this.targetTG = targetTG;
		dp.z = -zDist*(parts); 
	}

	public void integrateTransformChanges() {

		// Get the current TransformGroup transform into a transform3D object.
		this.targetTG.getTransform(this.partTrans);
		// Extract the position from the transform3D.
		this.partTrans.get(this.partPos);

		partPos.add(dp);

		partTrans.set(partPos);
		targetTG.setTransform(partTrans);
	}

}
