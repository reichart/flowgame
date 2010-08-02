package de.tum.in.flowgame.client.engine.behavior;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3d;
import javax.vecmath.Quat4d;
import javax.vecmath.Vector3d;

import de.tum.in.flowgame.GameLogic;

public class ForwardBehavior extends RepeatingBehavior {

	private Vector3d pos = new Vector3d();
	private final Transform3D trans = new Transform3D();

	private Vector3d mov = new Vector3d(0, 0, 0);

	private final TransformGroup translationGroup;

	private double fwdSpeed;

	private TransformGroup viewTG;

	public ForwardBehavior(final TransformGroup translationGroup, final TransformGroup viewTG) {
		this.translationGroup = translationGroup;
		this.viewTG = viewTG;
	}

	@Override
	protected void update() {
		if (!isPaused()) {
			updatePosition();
		}
	}

	/**
	 * Computes a new transform for the next frame based on the current
	 * transform and elapsed time. This new transform is written into the target
	 * transform group. This method should be called once per frame.
	 */
	private void updatePosition() {
		// Get the current transform of the target transform
		// group into a transform3D object.
		translationGroup.getTransform(trans);
		// Extract the position from the transform3D.
		trans.get(pos);

		double deltaTime = getDeltaTime();
		deltaTime *= 0.001;

		mov.z = fwdSpeed;
		/* Integration of velocity to distance */
		Point3d dp = new Point3d();
		dp.scale(deltaTime, mov);

		// add dp into current vp position.
		pos.add(dp);

		// System.out.println("Pos.x: " + pos.x + " - Pos.y: " + pos.y);
		trans.set(new Quat4d(), pos, 1);

		translationGroup.setTransform(trans);

		Transform3D vpTrans = new Transform3D();
		viewTG.getTransform(vpTrans);
		Vector3d vpPos = new Vector3d();
		vpTrans.get(vpPos);

		vpPos.add(dp);

		vpTrans.set(vpPos);

		viewTG.setTransform(vpTrans);
	}

	@Override
	public void gameStarted(GameLogic game) {
		trans.setIdentity();
		translationGroup.setTransform(trans);
		mov = new Vector3d();
		pos = new Vector3d();
		viewTG.setTransform(new Transform3D());
	}

	@Override
	public void gameStopped(GameLogic game) {
		reset();
	}

	public void setFwdSpeed(final double fwdSpeed) {
		this.fwdSpeed = fwdSpeed;
	}

	private void reset() {
		this.pos = new Vector3d();
	}

	public Vector3d getCoords() {
		return pos;
	}
}