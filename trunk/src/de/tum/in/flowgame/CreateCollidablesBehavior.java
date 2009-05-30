package de.tum.in.flowgame;

import java.io.IOException;
import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.SharedGroup;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnElapsedTime;

public class CreateCollidablesBehavior extends Behavior {

	private long time = 1000;

	private final WakeupCriterion wakeupEvent;

	private final BranchGroup collidableBranchGroup;
	private final SharedGroup sharedGroup;

	public CreateCollidablesBehavior(final BranchGroup collidableBranchGroup, final SharedGroup sharedGroup) {
		this.collidableBranchGroup = collidableBranchGroup;
		this.sharedGroup = sharedGroup;
		this.wakeupEvent = new WakeupOnElapsedTime(time);
	}

	@Override
	public void initialize() {
		wakeupOn(wakeupEvent);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void processStimulus(final Enumeration criteria) {
		System.out.println("Create");
		final float x = new Float(Math.random() * 3 - 1.5);
		try {
			new Collidable(collidableBranchGroup, sharedGroup, x);
		} catch (final IOException e) {
			e.printStackTrace();
		}
		wakeupOn(wakeupEvent);
	}

	public long getTime() {
		return time;
	}

	public void setTime(final long time) {
		this.time = time;
	}
}
