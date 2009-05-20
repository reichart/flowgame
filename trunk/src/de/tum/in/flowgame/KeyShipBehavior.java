package de.tum.in.flowgame;

import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnAWTEvent;
import javax.media.j3d.WakeupOnElapsedFrames;
import javax.media.j3d.WakeupOr;
import javax.vecmath.Vector3d;

public class KeyShipBehavior extends Behavior {
	private WakeupOr wakeup;

	boolean KEY_UP;
	boolean KEY_DOWN;
	boolean KEY_LEFT;
	boolean KEY_RIGHT;
	
	TransformGroup tg;
	
	KeyShipBehavior(TransformGroup tg) {
		this.tg = tg;
		
		WakeupCriterion w1 = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);
		WakeupCriterion w2 = new WakeupOnAWTEvent(KeyEvent.KEY_RELEASED);
		WakeupOnElapsedFrames w3 = new WakeupOnElapsedFrames(0);
		WakeupCriterion[] warray = { w1, w2, w3 };

		KEY_DOWN = false;
		KEY_UP = false;
		KEY_LEFT = false;
		KEY_RIGHT = false;

		wakeup = new WakeupOr(warray);
	}

	@Override
	public void initialize() {
		System.out.println("init KeyShipBejavior");
		wakeupOn(wakeup);
	}

	@Override
	public void processStimulus(Enumeration criteria) {
		WakeupCriterion genericEvt;

		while (criteria.hasMoreElements()) {
			genericEvt = (WakeupCriterion) criteria.nextElement();
			if (genericEvt instanceof WakeupOnAWTEvent) {
				WakeupOnAWTEvent awtEvent = (WakeupOnAWTEvent) genericEvt;
				processAWTEvent(awtEvent.getAWTEvent());
			}
			if (genericEvt instanceof WakeupOnElapsedFrames) {
				updatePosition();
			}
		}
		wakeupOn(wakeup);
	}

	private void updatePosition() {
		Vector3d transformVector = new Vector3d();
		if (KEY_RIGHT) {
			transformVector.add(new Vector3d(-0.01f, 0.0f, 0.0f));
		}
		if (KEY_LEFT) {
			transformVector.add(new Vector3d(0.01f, 0.0f, 0.0f));
		}
		if (KEY_UP) {
			transformVector.add(new Vector3d(0.0f, 0.01f, 0.0f));
		}
		if (KEY_DOWN) {
			transformVector.add(new Vector3d(0.0f, -0.01f, 0.0f));
		}
		
		Transform3D currentTransform = new Transform3D();
		tg.getTransform(currentTransform);
		Transform3D updatedTransform = new Transform3D();
		updatedTransform.setTranslation(transformVector);
		currentTransform.mul(updatedTransform);
		tg.setTransform(currentTransform);
	}

	private void processAWTEvent(AWTEvent[] events) {
		for (int loop = 0; loop < events.length; loop++) {
			if (events[loop] instanceof KeyEvent) {
				KeyEvent eventKey = (KeyEvent) events[loop];
				if (eventKey.getID() == KeyEvent.KEY_PRESSED) {
					switch (eventKey.getKeyCode()) {
					case KeyEvent.VK_UP:
						KEY_UP = true;
						break;
					case KeyEvent.VK_DOWN:
						KEY_DOWN = true;
						break;
					case KeyEvent.VK_LEFT:
						KEY_LEFT = true;
						break;
					case KeyEvent.VK_RIGHT:
						KEY_RIGHT = true;
						break;
					default:
						System.out.println("andere Taste");
						break;
					}
				}
				if (eventKey.getID() == KeyEvent.KEY_RELEASED) {
					switch (eventKey.getKeyCode()) {
					case KeyEvent.VK_UP:
						KEY_UP = false;
						break;
					case KeyEvent.VK_DOWN:
						KEY_DOWN = false;
						break;
					case KeyEvent.VK_LEFT:
						KEY_LEFT = false;
						break;
					case KeyEvent.VK_RIGHT:
						KEY_RIGHT = false;
						break;
					default:
						System.out.println("andere Taste");
						break;
					}
				}
			}
		}
	}

}
