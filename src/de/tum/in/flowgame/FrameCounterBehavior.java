package de.tum.in.flowgame;

import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupOnElapsedFrames;

public class FrameCounterBehavior extends Behavior {

	public interface FrameCounterListener {
		void updateFramesPerSecond(long fps);
	}
	
	private final Listeners<FrameCounterListener> listeners;
	
	private final WakeupCondition condition;

	private long start = 0;

	private long timeTaken;

	public FrameCounterBehavior(final int interval) {
		this.condition = new WakeupOnElapsedFrames(interval);
		this.listeners = new Listeners<FrameCounterListener>() {
			@Override
			protected void fire(final Object event, final FrameCounterListener listener) {
				listener.updateFramesPerSecond(getFramesPerSecond());
			}
		};
	}

	public Listeners<FrameCounterListener> getListeners() {
		return listeners;
	}
	
	@Override
	public void initialize() {
		wakeupOn(condition);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void processStimulus(final Enumeration/* <WakeupCriterion> */criteria) {
		final long now = System.currentTimeMillis();
		this.timeTaken = now - start;
		this.start = now;
		
		listeners.fire(null);
		
		wakeupOn(condition);
	}

	public long getFramesPerSecond() {
		return timeTaken * 1000 / timeTaken;
	}
}