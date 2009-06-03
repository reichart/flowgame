package de.tum.in.flowgame;

import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupAnd;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnAWTEvent;
import javax.media.j3d.WakeupOnElapsedFrames;
import javax.media.j3d.WakeupOr;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class KeyShipBehavior extends Behavior {

	private static final float xspeed = 20f;
	private static float yspeed = xspeed;
	private final Point3d dp = new Point3d();
	private final Vector3d pos = new Vector3d();
	private final Transform3D trans = new Transform3D();

	private static final Vector3d mov = new Vector3d(0, 0, 0);

	private final TransformGroup translationGroup;
	private final TransformGroup viewTG;
	private Transform3D vpTrans = new Transform3D();
	private Vector3d vpPos = new Vector3d();
	private final TransformGroup rotationGroup;
	private final WakeupCondition condition;

	private boolean KEY_LEFT;
	private boolean KEY_RIGHT;
	private boolean KEY_UP;
	private boolean KEY_DOWN;

	private double MOV_RADIUS = Tunnel.TUNNEL_RADIUS - 1;

	private final double MAX_ANGLE = 30;

	private long previousWhen;

	private long time;

	public KeyShipBehavior(final TransformGroup translationGroup,
			TransformGroup rotationGroup, TransformGroup viewTG) {
		this.translationGroup = translationGroup;
		this.rotationGroup = rotationGroup;
		this.viewTG = viewTG;

		final WakeupCriterion keyPressed = new WakeupOnAWTEvent(
				KeyEvent.KEY_PRESSED);
		final WakeupCriterion keyReleased = new WakeupOnAWTEvent(
				KeyEvent.KEY_RELEASED);
		final WakeupCriterion currentFrame = new WakeupOnElapsedFrames(0);

		this.condition = new WakeupOr(Utils.asArray(keyPressed, keyReleased,
				currentFrame));

		// Create Timer here.
		time = System.currentTimeMillis();
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
		if (when == previousWhen && e.getID() == KeyEvent.KEY_RELEASED) {
			return;
		}
		previousWhen = when;

		final int id = e.getID();
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			KEY_LEFT = (id == KeyEvent.KEY_PRESSED);
			break;
		case KeyEvent.VK_RIGHT:
			KEY_RIGHT = (id == KeyEvent.KEY_PRESSED);
			break;
		case KeyEvent.VK_UP:
			KEY_UP = (id == KeyEvent.KEY_PRESSED);
			break;
		case KeyEvent.VK_DOWN:
			KEY_DOWN = (id == KeyEvent.KEY_PRESSED);
			break;
		}
	}

	/**
	 * Computes a new transform for the next frame based on the current
	 * transform and elapsed time. This new transform is written into the target
	 * transform group. This method should be called once per frame.
	 */
	private void updatePosition() {
		// Get the current transform of the target transform
		// group into a transform3D object.
		translationGroup.getTransform(trans);
		// Extract the position from the transform3D.
		trans.get(pos);

		viewTG.getTransform(vpTrans);
		vpTrans.get(vpPos);

		double deltaTime = (double) getDeltaTime();
		deltaTime *= 0.001;

		double stepx = xspeed * deltaTime;
		double distToRadiusX= allowedDistToCircleRadius(pos.y + Game3D.INITIAL_SHIP_PLACEMENT_Y, MOV_RADIUS);
		double distToRadiusRight = distToRadiusX - Game3D.INITIAL_SHIP_PLACEMENT_X;
		double distToRadiusLeft = distToRadiusX + Game3D.INITIAL_SHIP_PLACEMENT_X;
		System.out.println("DistRight: " + distToRadiusRight);
		System.out.println("DistLeft: " + distToRadiusLeft);
		if (KEY_RIGHT) {
			if (distToRadiusRight - pos.x - stepx > 0) {
				mov.x = xspeed;
			} else {
				double remainingDist = distToRadiusRight - pos.x;
				mov.x = adaptSpeedAtBorder(remainingDist, stepx, xspeed);
			}
		} else if (KEY_LEFT) {
			if (distToRadiusLeft + pos.x - stepx > 0) {
				mov.x = -xspeed;
			} else {
				double remainingDist = distToRadiusLeft + pos.x;
				mov.x = -adaptSpeedAtBorder(remainingDist, stepx, xspeed);
			}
		} else if (!(KEY_LEFT | KEY_RIGHT)) {
			mov.x = 0;
		}

		double stepy = yspeed * deltaTime;
		double distToRadiusY = allowedDistToCircleRadius(pos.x + Game3D.INITIAL_SHIP_PLACEMENT_X, MOV_RADIUS);
		double distToRadiusUp = distToRadiusY - Game3D.INITIAL_SHIP_PLACEMENT_Y;
		double distToRadiusDown = distToRadiusY + Game3D.INITIAL_SHIP_PLACEMENT_Y;
		System.out.println("DistUp: " + distToRadiusUp);
		System.out.println("DistDown: " + distToRadiusDown);
		if (KEY_UP) {
			if (distToRadiusUp - pos.y - stepy > 0) {
				mov.y = yspeed;
			} else {
				double remainingDist = distToRadiusUp - pos.y;
				mov.y = adaptSpeedAtBorder(remainingDist, stepy, yspeed);
			}
		} else if (KEY_DOWN) {
			if (distToRadiusDown + pos.y - stepx > 0) {
				mov.y = -yspeed;
			} else {
				double remainingDist = distToRadiusDown + pos.y;
				mov.y = -adaptSpeedAtBorder(remainingDist, stepy, yspeed);
			}
		} else if (!(KEY_UP | KEY_DOWN)) {
			mov.y = 0;
		}

		/* Integration of velocity to distance */
		dp.scale(deltaTime, mov);

		// add dp into current vp position.
		pos.add(dp);
		System.out.println(pos);
		vpPos.add(dp);

		/* Final update of the target transform group */
		// Put the transform back into the transform group.
		trans.set(pos);
		translationGroup.setTransform(trans);

		vpTrans.set(vpPos);
		viewTG.setTransform(vpTrans);

	}

	private float adaptSpeedAtBorder(double remainingDist, double step,
			float speed) {
		return (float) ((remainingDist / step) * speed);
	}

	private double allowedDistToCircleRadius(double pos, double radius) {
		double dist = Math.sqrt(Math.pow(radius, 2) - Math.pow(pos, 2));
		return dist;
	}

	private long getDeltaTime() {
		final long newTime = System.currentTimeMillis();
		long deltaTime = newTime - time;
		// System.out.println(deltaTime);
		time = newTime;
		if (deltaTime > 2000)
			return 0;
		else
			return deltaTime;
	}

	private String f(final boolean b) {
		return b ? " 1" : " 0";
	}
}
