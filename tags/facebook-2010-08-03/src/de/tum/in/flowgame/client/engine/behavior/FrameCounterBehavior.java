package de.tum.in.flowgame.client.engine.behavior;

import de.tum.in.flowgame.client.Listeners;

/**
 * Measures frame rate over a number of frames and delivers them to subscribed
 * listeners.
 */
public class FrameCounterBehavior extends RepeatingBehavior {

	public interface FrameCounterListener {
		void updateFramesPerSecond(long fps);
	}

	private final Listeners<FrameCounterListener> listeners;
	private final long frames;

	private long start;
	private long fps;

	/**
	 * @param frames
	 *            number of frames to average frame rate over
	 */
	public FrameCounterBehavior(final int frames) {
		super(frames);
		this.frames = frames;
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
	protected void init() {
		this.start = System.currentTimeMillis();
	}

	@Override
	protected void update() {
		final long now = System.currentTimeMillis();
		this.fps = (frames * 1000) / (now - start);
		this.start = now;

		listeners.fire(null);
	}

	public long getFramesPerSecond() {
		return fps;
	}
}