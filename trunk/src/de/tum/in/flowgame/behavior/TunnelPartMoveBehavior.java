package de.tum.in.flowgame.behavior;

import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnViewPlatformExit;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import de.tum.in.flowgame.GameListener;
import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.model.Collision.Item;


public class TunnelPartMoveBehavior extends Behavior{

	private final WakeupCriterion wakeEvent;
	private TransformGroup targetTG;
	private Point3d dp = new Point3d(0.0,0.0,0.0);
	private Vector3d partPos = new Vector3d();
	private Transform3D partTrans = new Transform3D();

	public TunnelPartMoveBehavior(final Shape3D element,
			final TransformGroup targetTG, final double zDist, final int parts) {
		this.targetTG = targetTG;
		dp.setZ(-zDist*(parts));
		this.wakeEvent = new WakeupOnViewPlatformExit(element.getBounds());
	}

	@Override
	public void initialize() {
		this.wakeupOn(wakeEvent);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void processStimulus(final Enumeration criteria) {
		integrateTransformChanges();
		this.wakeupOn(wakeEvent);
	}
	
	public void integrateTransformChanges() {

		// Get the current TransformGroup transform into a transform3D object.
		this.targetTG.getTransform(this.partTrans);
		// Extract the position from the transform3D.
		this.partTrans.get(this.partPos);
//		System.out.println(partPos);

		partPos.add(dp);
//		System.out.println("move: " + (partPos.getZ()) + " " + dp);

		partTrans.set(partPos);
		targetTG.setTransform(partTrans);
	}
}
