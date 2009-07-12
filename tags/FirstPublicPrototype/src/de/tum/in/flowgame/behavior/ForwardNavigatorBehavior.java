package de.tum.in.flowgame.behavior;

import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnElapsedFrames;

import de.tum.in.flowgame.GameListener;
import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.GameLogic.Item;

public class ForwardNavigatorBehavior extends Behavior implements GameListener {

	private double oldSpeed;
	private double speed;

	private final WakeupCriterion newFrame = new WakeupOnElapsedFrames(0);
	private final ForwardNavigator forwardNavigator;

	public ForwardNavigatorBehavior(final TransformGroup targetTG,
			final double speed) {
		this.speed = speed;
		this.forwardNavigator = new ForwardNavigator(targetTG, speed);
	}

	@Override
	public void initialize() {
		this.wakeupOn(newFrame);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void processStimulus(final Enumeration criteria) {
		forwardNavigator.integrateTransformChanges();
		wakeupOn(newFrame);
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

	@Override
	public void collided(GameLogic logic, Item item) {
		// TODO Auto-generated method stub

	}

	@Override
	public void gamePaused(GameLogic game) {
		oldSpeed = getSpeed();
		setSpeed(0);
	}

	@Override
	public void gameResumed(GameLogic game) {
		setSpeed(oldSpeed);
	}

	@Override
	public void gameStarted(GameLogic game) {
		// TODO Auto-generated method stub

	}

	@Override
	public void gameStopped(GameLogic game) {
		// TODO Auto-generated method stub

	}

}
