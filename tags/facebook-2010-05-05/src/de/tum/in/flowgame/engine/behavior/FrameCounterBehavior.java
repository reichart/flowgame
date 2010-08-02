package de.tum.in.flowgame.engine.behavior;

import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupOnElapsedFrames;

import de.tum.in.flowgame.Listeners;

/**
 * Measures frame rate over a number of frames and delivers them to subscribed
 * listeners.
 */
public class FrameCounterBehavior extends Behavior {

	public interface FrameCounterListener {
		void updateFramesPerSecond(long fps);
	}

	private final Listeners<FrameCounterListener> listeners;
	private final WakeupCondition condition;
	private final long frames;

	private long start;
	private long fps;

	/**
	 * @param frames
	 *            number of frames to average frame rate over
	 */
	public FrameCounterBehavior(final int frames) {
		this.frames = frames;
		this.condition = new WakeupOnElapsedFrames(frames);
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
		this.start = System.currentTimeMillis();
		wakeupOn(condition);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void processStimulus(final Enumeration/* <WakeupCriterion> */criteria) {
		final long now = System.currentTimeMillis();
		this.fps = (frames * 1000) / (now - start);
		this.start = now;

		listeners.fire(null);

		wakeupOn(condition);
	}

	public long getFramesPerSecond() {
		return fps;
	}
}