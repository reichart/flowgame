package de.tum.in.flowgame.engine.behavior;

import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnElapsedFrames;

public abstract class RepeatingBehavior extends Behavior {

	private final WakeupCondition trigger;

	/**
	 * Creates a repeating behavior that wakes up every frame.
	 */
	public RepeatingBehavior() {
		this(0);
	}

	/**
	 * Creates a repeating behavior that wakes up every Nth frame.
	 */
	public RepeatingBehavior(final int frames) {
		this(new WakeupOnElapsedFrames(frames));
	}

	/**
	 * Creates a repeating behavior that wakes up on the specified condition.
	 */
	public RepeatingBehavior(final WakeupCondition trigger) {
		this.trigger = trigger;
	}

	@Override
	public final void initialize() {
		init();
		wakeupOn(trigger);
	}

	@SuppressWarnings("unchecked")
	@Override
	public final void processStimulus(final Enumeration/* <WakeupCriterion> */criteria) {
		update();
		update(criteria);
		wakeupOn(trigger);
	}

	protected void init() {
		// empty
	}

	protected void update() {
		// empty
	}

	protected void update(final Enumeration<WakeupCriterion> criteria) {
		// empty
	}
}
