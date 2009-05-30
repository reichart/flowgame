package de.tum.in.flowgame;

import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnElapsedFrames;

public class ForwardNavigatorBehavior extends Behavior{

	private WakeupCriterion wakeEvent = new WakeupOnElapsedFrames(0);
	private ForwardNavigator forwardNavigator;
	
    public ForwardNavigatorBehavior(TransformGroup targetTG) {
    	this.forwardNavigator = new ForwardNavigator(targetTG);
        }
    
    public ForwardNavigatorBehavior(TransformGroup targetTG, double speed){
    	this.forwardNavigator = new ForwardNavigator(targetTG, speed);
    }
	
	@Override
	public void initialize() {
		this.wakeupOn(wakeEvent);
	}

	@Override
	public void processStimulus(Enumeration criteria) {
		forwardNavigator.integrateTransformChanges();
		wakeupOn(wakeEvent);
	}
	
}
