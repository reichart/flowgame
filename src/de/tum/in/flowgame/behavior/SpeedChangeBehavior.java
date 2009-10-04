package de.tum.in.flowgame.behavior;

import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnElapsedTime;

public class SpeedChangeBehavior extends Behavior{
	
	private final static long TIME = 1000;
	
	private final WakeupCriterion wakeupEvent;
	private final ForwardNavigatorBehavior forwardNavigator;
	
	public SpeedChangeBehavior (final ForwardNavigatorBehavior forwardNavigator){
		this.wakeupEvent = new WakeupOnElapsedTime(TIME);
		this.forwardNavigator = forwardNavigator;
	}
	
	@Override
	public void initialize() {
		wakeupOn(wakeupEvent);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void processStimulus(final Enumeration criteria) {
		forwardNavigator.setSpeed(forwardNavigator.getSpeed());
		wakeupOn(wakeupEvent);
	}
}
