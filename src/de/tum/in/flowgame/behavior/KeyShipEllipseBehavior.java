package de.tum.in.flowgame.behavior;

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
import javax.vecmath.Matrix3d;
import javax.vecmath.Vector3d;

import de.tum.in.flowgame.Ellipse;
import de.tum.in.flowgame.Utils;

public class KeyShipEllipseBehavior extends Behavior {

	private static final float STEP = 0.035f;

	private static final Vector3d MOV_LEFT = new Vector3d(STEP, 0, 0);
	private static final Vector3d MOV_RIGHT = new Vector3d(-STEP, 0, 0);

	private final TransformGroup translationGroup;
	private final TransformGroup rotationGroup;
	private final WakeupCondition condition;

	private boolean KEY_LEFT;
	private boolean KEY_RIGHT;

	private final double MAX_ANGLE = 30;

	public KeyShipEllipseBehavior(final TransformGroup translationGroup, TransformGroup rotationGroup) {
		this.translationGroup = translationGroup;
		this.rotationGroup = rotationGroup;

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
//		final long when = e.getWhen();
//		if (when == previousWhen && e.getID() == KeyEvent.KEY_RELEASED) {
//			return;
//		}
//		previousWhen = when;

		final int id = e.getID();
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			KEY_LEFT = (id == KeyEvent.KEY_PRESSED);
			break;
		case KeyEvent.VK_RIGHT:
			KEY_RIGHT = (id == KeyEvent.KEY_PRESSED);
			break;
		}
	}

	private void updatePosition() {
		if (!(KEY_LEFT | KEY_RIGHT)) {
			return; // no flags, no update
		}

		final Transform3D currentTransform = new Transform3D();
		translationGroup.getTransform(currentTransform);
		double[] translationVector = new double[16];
		currentTransform.get(translationVector);
		double x = translationVector[3];

		double yold;
		double ynew;
		double y;
		yold = Ellipse.getYOnPosition(x);

		final Vector3d vector = new Vector3d();
		if (KEY_RIGHT) {
			if (x < 0.9 * Ellipse.ELLIPSE_A) {
				vector.add(MOV_LEFT);
			}
		}
		if (KEY_LEFT) {
			if (x > -0.9 * Ellipse.ELLIPSE_A) {
				vector.add(MOV_RIGHT);
			}
		}
		ynew = Ellipse.getYOnPosition(x);
		y = yold - ynew;

		vector.add(new Vector3d(0.0f, y, 0.0f));

		final Transform3D updatedTransform = new Transform3D();
		updatedTransform.setTranslation(vector);
		currentTransform.mul(updatedTransform);
		translationGroup.setTransform(currentTransform);

		double angle = Math.toRadians(MAX_ANGLE / (Ellipse.ELLIPSE_A / STEP) * (x / STEP));
		Matrix3d m = createZRotationMatrix(angle);

		Transform3D currentRotTransform = new Transform3D();
		currentRotTransform.set(m);
		rotationGroup.setTransform(currentRotTransform);
	}

	private Matrix3d createZRotationMatrix(double angle) {
		return new Matrix3d(Math.cos(angle), -Math.sin(angle), 0, Math.sin(angle), Math.cos(angle), 0, 0, 0, 1);
	}

	private String f(final boolean b) {
		return b ? " 1" : " 0";
	}
}
