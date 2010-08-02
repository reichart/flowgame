package de.tum.in.flowgame.engine.behavior;

import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnElapsedFrames;

import de.tum.in.flowgame.GameListener;
import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.model.Collision.Item;

public abstract class RepeatingBehavior extends Behavior implements GameListener {

	private final WakeupCondition trigger;

	private long time;

	private boolean pause;

	private long pauseBegin;

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
		// Set start time here to help with time dependent behaviors
		time = System.currentTimeMillis();
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

	public final void gamePaused(final GameLogic game) {
		pause = true;
		pauseBegin = System.currentTimeMillis();
	}

	public final void gameResumed(final GameLogic game) {
		pause = false;
		time = time + (System.currentTimeMillis() - pauseBegin);
	}

	public final void collided(final GameLogic game, final Item item) {
		// empty
	}

	public void added(final GameLogic game) {
		// empty
	}

	public void gameStarted(final GameLogic game) {
		// empty
	}

	public void gameStopped(final GameLogic game) {
		// empty
	}

	public void removed(final GameLogic game) {
		// empty
	}

	/**
	 * calculate elapsed time, if time between two frames is too big ignore it
	 * to avoid jumps
	 * 
	 * @return elapsed time since last frame
	 */
	protected long getDeltaTime() {
		final long newTime = System.currentTimeMillis();
		final long deltaTime = newTime - time;
		time = newTime;
		if (deltaTime > 2000)
			return 0;
		else
			return deltaTime;
	}

	protected boolean isPaused() {
		return pause;
	}
}
