package de.tum.in.flowgame;

import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnViewPlatformExit;

public class TunnelPartMoveBehavior extends Behavior {

	private final WakeupCriterion wakeEvent;
	private final TunnelPartMover tunnelPartMover;

	public TunnelPartMoveBehavior(final Shape3D element, final TransformGroup targetTG, final double zDist,
			final int parts) {
		this.tunnelPartMover = new TunnelPartMover(targetTG, zDist, parts);
		this.wakeEvent = new WakeupOnViewPlatformExit(element.getBounds());
	}

	@Override
	public void initialize() {
		this.wakeupOn(wakeEvent);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void processStimulus(final Enumeration criteria) {
		this.tunnelPartMover.integrateTransformChanges();
		this.wakeupOn(wakeEvent);
	}

}
