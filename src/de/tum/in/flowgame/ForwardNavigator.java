package de.tum.in.flowgame;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3d;
import javax.vecmath.Quat4d;
import javax.vecmath.Vector3d;

/**
 * This is the ForwardNavigator class. It computes a new transformation in
 * z-axis direction.
 * 
 */
public class ForwardNavigator {

	private final Vector3d navVec;
	private double fwdSpeed;
	public void setFwdSpeed(double fwdSpeed) {
		this.fwdSpeed = fwdSpeed;
	}

	private long time;

	private final Point3d dp = new Point3d();
	private final Vector3d vpPos = new Vector3d();
	private final Transform3D vpTrans = new Transform3D();
	private final Transform3D nominal = new Transform3D();
	private final TransformGroup targetTG;

	public ForwardNavigator(final TransformGroup targetTG, final double speed) {
		this.targetTG = targetTG;
		targetTG.getTransform(nominal);

		this.fwdSpeed = speed;
		this.navVec = new Vector3d(0.0, 0.0, 0.0);
	}
	
	
	/**
	 * Computes a new transform for the next frame based on the current
	 * transform and elapsed time. This new transform is written into the target
	 * transform group. This method should be called once per frame.
	 */
	public void integrateTransformChanges() {

		// Get the current View Platform transform into a transform3D object.
		targetTG.getTransform(vpTrans);
		// Extract the position, quaterion, and scale from the transform3D.
		vpTrans.get(vpPos);

		double deltaTime = (double) getDeltaTime();
		deltaTime *= 0.001;
		
//		fwdSpeed = fwdSpeed + deltaTime;

		navVec.z = fwdSpeed;

		/* Integration of velocity to distance */
		dp.scale(deltaTime, navVec);

		// add dp into current vp position.
		vpPos.add(dp);

		/* Final update of view platform */
		// Put the transform back into the transform group.
		vpTrans.set(vpPos);
		targetTG.setTransform(vpTrans);
	}

	private long getDeltaTime() {
		final long newTime = System.currentTimeMillis();
		final long deltaTime = newTime - time;
		time = newTime;
		if (deltaTime > 2000)
			return 0;
		else
			return deltaTime;
	}
}
