package de.tum.in.flowgame;

import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnElapsedTime;

public class SpeedChangeBehavior extends Behavior{
	private long time = 1000;
	private WakeupCriterion wakeupEvent;
	private ForwardNavigatorBehavior forwardNavigator;
	
	public SpeedChangeBehavior (ForwardNavigatorBehavior forwardNavigator){
		wakeupEvent = new WakeupOnElapsedTime(this.time);
		this.forwardNavigator = forwardNavigator;
	}
	
	@Override
	public void initialize() {
		wakeupOn(wakeupEvent);
	}
	@Override
	public void processStimulus(Enumeration criteria) {
		forwardNavigator.setSpeed(forwardNavigator.getSpeed());
		wakeupOn(wakeupEvent);
	}
}
