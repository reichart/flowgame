package de.tum.in.flowgame.engine.util;

import javax.media.j3d.Alpha;
import javax.media.j3d.Behavior;
import javax.media.j3d.Group;
import javax.media.j3d.Node;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;

import de.tum.in.flowgame.engine.Game3D;

public class Helper {

	/**
	 * @return the rotated target
	 */
	public static TransformGroup rotate(final Node target, final double x, final double y, final double z) {
		return new TransformGroupBuilder().add(target).rotX(x).rotY(y).rotZ(z).fin();
	}
	
	/**
	 * @return the translated target
	 */
	public static TransformGroup translate(final Node target, final double x, final double y, final double z) {
		return new TransformGroupBuilder().add(target).translate(x, y, z).fin();
	}

	/**
	 * @param target
	 *            what to rotate
	 * @param duration
	 *            how long in <strong>seconds</strong> a whole revolution takes
	 * @param parent
	 *            where to add the {@link RotationInterpolator} to
	 * @return a rotating group containing the specified target
	 */
	public static TransformGroup rotating(final Node target, final long duration, final Group parent) {
		final TransformGroup tg = new TransformGroupBuilder().writable().add(target).fin();
		final Behavior rotation = new RotationInterpolator(new Alpha(-1, duration * 1000), tg);
		rotation.setSchedulingBounds(Game3D.WORLD_BOUNDS);
		parent.addChild(rotation);
		return tg;
	}

	/**
	 * @param x
	 *            the x scale factor
	 * @param y
	 *            the y scale factor
	 * @param z
	 *            the z scale factor
	 * @return a scaling {@link Transform3D}
	 */
	public static Transform3D scale(final double x, final double y, final double z) {
		final Transform3D t = new Transform3D();
		t.setScale(new Vector3d(x, y, z));
		return t;
	}
	
	public static Transform3D scale(final double scale) {
		final Transform3D t = new Transform3D();
		t.setScale(scale);
		return t;
	}
	
	/**
	 * @return a translating {@link Transform3D}
	 */
	public static Transform3D translate(final double x, final double y, final double z) {
		final Transform3D t = new Transform3D();
		t.setTranslation(new Vector3d(x, y, z));
		return t;
	}
}
