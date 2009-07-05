package de.tum.in.flowgame.behavior;

import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnElapsedFrames;
import javax.vecmath.Vector3d;

public class ForwardNavigatorBehavior extends Behavior {

	private double speed;

	private final WakeupCriterion wakeEvent;
	private final ForwardNavigator forwardNavigator;

	public ForwardNavigatorBehavior(final TransformGroup targetTG, final double speed) {
		this.speed = speed;
		this.wakeEvent = new WakeupOnElapsedFrames(0);
		this.forwardNavigator = new ForwardNavigator(targetTG, speed);
	}

	@Override
	public void initialize() {
		this.wakeupOn(wakeEvent);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void processStimulus(final Enumeration criteria) {
		forwardNavigator.integrateTransformChanges();
		wakeupOn(wakeEvent);
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(final double speed) {
		this.speed = speed;
		this.forwardNavigator.setFwdSpeed(speed);
	}

	public ForwardNavigator getForwardNavigator() {
		return forwardNavigator;
	}

}
