package de.tum.in.flowgame.behavior;

import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnAWTEvent;
import javax.media.j3d.WakeupOnElapsedFrames;
import javax.media.j3d.WakeupOr;

import de.tum.in.flowgame.Utils;

public class ForwardNavigatorBehavior extends Behavior {

	private double oldSpeed;
	private boolean pause;
	private double speed;
	private final char pauseKey = ' ';

	private final WakeupCondition condition;
	private final ForwardNavigator forwardNavigator;

	public ForwardNavigatorBehavior(final TransformGroup targetTG, final double speed) {
		this.speed = speed;
		final WakeupCriterion newFrame = new WakeupOnElapsedFrames(0);
		final WakeupCriterion keyTyped = new WakeupOnAWTEvent(KeyEvent.KEY_TYPED);
		this.condition = new WakeupOr(Utils.asArray(newFrame, keyTyped));
		this.forwardNavigator = new ForwardNavigator(targetTG, speed);
	}

	@Override
	public void initialize() {
		this.wakeupOn(condition);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void processStimulus(final Enumeration criteria) {
		WakeupCriterion crit;
		while (criteria.hasMoreElements()) {
			crit = (WakeupCriterion) criteria.nextElement();
			if (crit instanceof WakeupOnElapsedFrames){
				forwardNavigator.integrateTransformChanges();
			}
			else if (crit instanceof WakeupOnAWTEvent) {
				final WakeupOnAWTEvent awtEvent = (WakeupOnAWTEvent) crit;
				for (final AWTEvent event : awtEvent.getAWTEvent()) {
					if (event instanceof KeyEvent) {
						processKeyEvent((KeyEvent) event);
					}
				}
			}
		}
		wakeupOn(condition);
	}
	
	private void processKeyEvent(final KeyEvent e) {
		final int id = e.getID();
		if (id == KeyEvent.KEY_TYPED) {
			switch (e.getKeyChar()) {
			case pauseKey:
				if (pause){
					this.setSpeed(oldSpeed);
					pause = false;
				} else {
					oldSpeed = this.getSpeed();
					pause = true;
					this.setSpeed(0);
				}
				break;
			}
		}
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

}
