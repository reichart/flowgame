package de.tum.in.flowgame.util;

import javax.media.j3d.Alpha;
import javax.media.j3d.Behavior;
import javax.media.j3d.Bounds;
import javax.media.j3d.Node;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;

public class TransformGroupBuilder {

	private final TransformGroup tg;
	private final Vector3d rot;

	public TransformGroupBuilder() {
		this.tg = new TransformGroup();
		this.rot = new Vector3d();
	}

	public TransformGroupBuilder writable() {
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		return this;
	}
	
	public TransformGroupBuilder computeAutoBounds(boolean compute) {
		tg.setBoundsAutoCompute(compute);
		return this;
	}

	public TransformGroupBuilder collidable(final Bounds bounds) {
		tg.setCollisionBounds(bounds);
		return this;
	}
	
	public TransformGroupBuilder add(final Node child) {
		tg.addChild(child);
		return this;
	}

	private TransformGroupBuilder addBehavior(final Behavior behavior, final Bounds schedulingBounds) {
		behavior.setSchedulingBounds(schedulingBounds);
		tg.addChild(behavior);
		return this;
	}

	public TransformGroupBuilder addRotationBehavior(final Alpha alpha, final Bounds schedulingBounds) {
		float max = (float)(2*Math.PI);
		if (Math.random()>0.5){
			max = -max;
		}
		return writable().addBehavior(new RotationInterpolator(alpha, tg, new Transform3D(), 0f, max), schedulingBounds);
	}

	public TransformGroupBuilder translate(final double x, final double y, final double z) {
		final Transform3D t3d = new Transform3D();
		t3d.setTranslation(new Vector3d(x, y, z));
		return mul(t3d);
	}

	public TransformGroupBuilder scale(final double scale) {
		final Transform3D t3d = new Transform3D();
		t3d.setScale(scale);
		return mul(t3d);
	}

	public TransformGroupBuilder rotX(final double degree) {
		rot.setX(Math.toRadians(degree));
		return updateRotation();
	}

	public TransformGroupBuilder rotY(final double degree) {
		rot.setY(Math.toRadians(degree));
		return updateRotation();
	}

	public TransformGroupBuilder rotZ(final double degree) {
		rot.setZ(Math.toRadians(degree));
		return updateRotation();
	}

	private TransformGroupBuilder updateRotation() {
		final Transform3D t3d = new Transform3D();
		t3d.setEuler(rot);
		mul(t3d);
		return this;
	}

	private TransformGroupBuilder mul(final Transform3D t3d) {
		final Transform3D current = new Transform3D();
		tg.getTransform(current);
		current.mul(t3d);
		tg.setTransform(t3d);
		return this;
	}

	public TransformGroup fin() {
		return tg;
	}
}
