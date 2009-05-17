package de.tum.in.flowgame;

import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnElapsedFrames;

import com.sun.j3d.utils.behaviors.keyboard.KeyNavigator;

public class ForwardNavigatorBehavior extends Behavior{

	private WakeupCriterion wakeEvent = new WakeupOnElapsedFrames(0);
	private ForwardNavigator forwardNavigator;
	
    public ForwardNavigatorBehavior(TransformGroup targetTG) {
    	this.forwardNavigator = new ForwardNavigator(targetTG);
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
