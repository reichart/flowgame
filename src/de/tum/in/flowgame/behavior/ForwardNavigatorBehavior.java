package de.tum.in.flowgame.behavior;

import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnElapsedFrames;

import de.tum.in.flowgame.GameListener;
import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.model.Collision.Item;

public class ForwardNavigatorBehavior extends Behavior implements GameListener, SpeedChange {

	private double oldSpeed;
	private double speed;

	private final WakeupCriterion newFrame = new WakeupOnElapsedFrames(0);
	private final ForwardNavigator forwardNavigator;
	private boolean paused;

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

	public void setFwdSpeed(final double speed) {
		if (!paused){
			this.speed = speed;
		} else {
			this.speed = 0;
		}
		this.forwardNavigator.setFwdSpeed(this.speed);
	}

	public ForwardNavigator getForwardNavigator() {
		return forwardNavigator;
	}

	@Override
	public void added(final GameLogic game) {
		// empty
	}
	
	@Override
	public void removed(final GameLogic game) {
		// empty
	}
	
	@Override
	public void collided(GameLogic logic, Item item) {
		// TODO Auto-generated method stub

	}

	@Override
	public void gamePaused(GameLogic game) {
		paused = true;
		oldSpeed = getSpeed();
		setFwdSpeed(0);
	}

	@Override
	public void gameResumed(GameLogic game) {
		paused = false;
		setFwdSpeed(oldSpeed);
	}

	@Override
	public void gameStarted(GameLogic game) {
		// TODO Auto-generated method stub

	}

	@Override
	public void gameStopped(GameLogic game) {
		// TODO Auto-generated method stub

	}

//	@Override
//	public void sessionFinished(GameLogic game) {
//		// TODO Auto-generated method stub
//		
//	}
	
	public double getZCoordinate(){
		return forwardNavigator.getZCoordinate();
	}

}
