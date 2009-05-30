package de.tum.in.flowgame;

import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.WakeupOnCollisionEntry;
import javax.vecmath.Point3d;

public class RemoveCollidableBehavior extends Behavior {
	BranchGroup group;
	private WakeupOnCollisionEntry wakeup;

	RemoveCollidableBehavior(BranchGroup group) {
		System.out.println("create");
		wakeup = new WakeupOnCollisionEntry(new BoundingSphere(new Point3d(0.0, 0.0f, 0.0f), 1f));
	}

	@Override
	public void initialize() {
		System.out.println("init");
		wakeupOn(wakeup);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void processStimulus(Enumeration criteria) {
		System.out.println("woke up");
		wakeupOn(wakeup);
	}

}
