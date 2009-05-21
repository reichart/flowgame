package de.tum.in.flowgame;

import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnAWTEvent;
import javax.media.j3d.WakeupOnElapsedFrames;
import javax.media.j3d.WakeupOr;
import javax.vecmath.Vector3d;

public class KeyShipBehavior extends Behavior {

	private static final float STEP = 0.01f;

	private static final Vector3d MOV_UP = new Vector3d(0, STEP, 0);
	private static final Vector3d MOV_DOWN = new Vector3d(0, -STEP, 0);
	private static final Vector3d MOV_LEFT = new Vector3d(STEP, 0, 0);
	private static final Vector3d MOV_RIGHT = new Vector3d(-STEP, 0, 0);

	private final TransformGroup target;
	private final WakeupCondition condition;

	private boolean KEY_UP;
	private boolean KEY_DOWN;
	private boolean KEY_LEFT;
	private boolean KEY_RIGHT;

	private volatile long previousWhen;

	public KeyShipBehavior(final TransformGroup target) {
		this.target = target;

		final WakeupCriterion keyPressed = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);
		final WakeupCriterion keyReleased = new WakeupOnAWTEvent(KeyEvent.KEY_RELEASED);
		final WakeupCriterion currentFrame = new WakeupOnElapsedFrames(0);

		this.condition = new WakeupOr(Utils.asArray(keyPressed, keyReleased, currentFrame));
	}

	@Override
	public void initialize() {
		wakeupOn(condition);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void processStimulus(final Enumeration criteria) {
		WakeupCriterion crit;
		while (criteria.hasMoreElements()) {
			crit = (WakeupCriterion) criteria.nextElement();
			if (crit instanceof WakeupOnAWTEvent) {
				final WakeupOnAWTEvent awtEvent = (WakeupOnAWTEvent) crit;
				for (final AWTEvent event : awtEvent.getAWTEvent()) {
					if (event instanceof KeyEvent) {
						processKeyEvent((KeyEvent) event);
					}
				}
			} else if (crit instanceof WakeupOnElapsedFrames) {
				updatePosition();
			}
		}
		
		wakeupOn(condition);
	}

	private void processKeyEvent(final KeyEvent e) {
		// Linux does key-repeat by signaling pairs of KEY_PRESSED/KEY_RELEASED
		// (Windows only repeats the KEY_PRESSED). Luckily, Linux uses the same
		// timestamp for key-repeat pairs so we can easily filter them.
		final long when = e.getWhen();
		if (when == previousWhen) {
			return;
		}
		previousWhen = when;

		final int id = e.getID();
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			// pressing a key should set a flag, anything else should unset it
			KEY_UP = (id == KeyEvent.KEY_PRESSED);
			break;
		case KeyEvent.VK_DOWN:
			KEY_DOWN = (id == KeyEvent.KEY_PRESSED);
			break;
		case KeyEvent.VK_LEFT:
			KEY_LEFT = (id == KeyEvent.KEY_PRESSED);
			break;
		case KeyEvent.VK_RIGHT:
			KEY_RIGHT = (id == KeyEvent.KEY_PRESSED);
			break;
		}
	}

	private void updatePosition() {
		if (!(KEY_UP | KEY_DOWN | KEY_LEFT | KEY_RIGHT)) {
			return; // no flags, no update
		}

		final Vector3d vector = new Vector3d();
		if (KEY_UP) {
			vector.add(MOV_UP);
		}
		if (KEY_DOWN) {
			vector.add(MOV_DOWN);
		}
		if (KEY_LEFT) {
			vector.add(MOV_LEFT);
		}
		if (KEY_RIGHT) {
			vector.add(MOV_RIGHT);
		}

		final Transform3D updatedTransform = new Transform3D();
		updatedTransform.setTranslation(vector);

		final Transform3D currentTransform = new Transform3D();
		target.getTransform(currentTransform);
		currentTransform.mul(updatedTransform);
		target.setTransform(currentTransform);
	}
}
