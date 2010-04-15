package de.tum.in.flowgame.engine.behavior;

import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupOnElapsedTime;
import javax.vecmath.Vector3d;

import de.tum.in.flowgame.engine.Game3D;

public class TextureTransformBehavior extends Behavior {

	private final TextureAttributes attribs;
	private final Transform3D transform;
	private final WakeupCondition condition;

	private final Vector3d vector;

	public TextureTransformBehavior(final TextureAttributes attribs) {
		this.attribs = attribs;
		transform = new Transform3D();
		vector = new Vector3d();
		condition = new WakeupOnElapsedTime(10);

		attribs.setCapability(TextureAttributes.ALLOW_TRANSFORM_WRITE);

		setSchedulingBounds(Game3D.WORLD_BOUNDS);
	}

	@Override
	public void initialize() {
		attribs.getTextureTransform(transform);
		wakeupOn(condition);
	}

	private int deltaX = 0;
	private int deltaY = 0;

	private final static int STEPS = 400;

	@SuppressWarnings("unchecked")
	@Override
	public void processStimulus(final Enumeration/* <WakeupCriterion> */criteria) {

		vector.x = deltaX;
		vector.y = -deltaY;
		vector.scale(1d / STEPS);

		transform.set(vector);
		attribs.setTextureTransform(transform);

		deltaX++;
		deltaY++;

		deltaX %= STEPS;
		deltaY %= STEPS;

		wakeupOn(condition);
	}

}
