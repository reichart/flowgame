//TODO: add rotations when movement occurs (rolling left, when moving left, etc)
//TODO: view should follow fighter lazy
//TODO: view should not follow to the tunnelborders

package de.tum.in.flowgame;

import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.DelayQueue;

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
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;

public class KeyShipBehavior extends Behavior {

	private static final float ACCELERATION = 60f;
	private static final float SPEED_MAX = 30f;
	private final Point3d dp = new Point3d();
	private Vector3d dv = new Vector3d();
	private final Vector3d pos = new Vector3d();
	private final Transform3D trans = new Transform3D();
	
	private List<Point3d> delayedList = new LinkedList<Point3d>();
	private List<Double> timeDeltas = new LinkedList<Double>();
	private double pastDelta;
	
	private static final Vector3d mov = new Vector3d(0, 0, 0);
	private static final double MAX_FOLLOWING_DIST = 1;
	private Vector3d a = new Vector3d();

	private Vector3d leftAcc;
	private Vector3d rightAcc;
	private Vector3d upAcc;
	private Vector3d downAcc;

	private Vector3d leftDrag;
	private Vector3d rightDrag;
	private Vector3d upDrag;
	private Vector3d downDrag;

	private double leftVMax;
	private double rightVMax;
	private double upVMax;
	private double downVMax;

	private final TransformGroup translationGroup;
	private final TransformGroup viewTG;
	private Transform3D vpTrans = new Transform3D();
	private Vector3d vpPos = new Vector3d();
	private Point3d vpdp = new Point3d();
	private final TransformGroup rotationGroup;
	private final WakeupCondition condition;

	private boolean KEY_LEFT;
	private boolean KEY_RIGHT;
	private boolean KEY_UP;
	private boolean KEY_DOWN;

	private double MOV_RADIUS = Tunnel.TUNNEL_RADIUS - 0.8;
//	private double MOV_RADIUS = 300;

	private long previousWhen;

	private long time;

	public KeyShipBehavior(final TransformGroup translationGroup,
			TransformGroup rotationGroup, TransformGroup viewTG) {
		
		for (int i = 0; i<3; i++){
			delayedList.add(new Point3d());
			timeDeltas.add(new Double(0));
		}
		
		this.translationGroup = translationGroup;
		this.rotationGroup = rotationGroup;
		this.viewTG = viewTG;


		leftAcc = new Vector3d(-ACCELERATION, 0.0, 0.0);
		rightAcc = new Vector3d(ACCELERATION, 0.0, 0.0);
		upAcc = new Vector3d(0.0, ACCELERATION, 0.0);
		downAcc = new Vector3d(0.0, -ACCELERATION, 0.0);

		leftDrag = new Vector3d(ACCELERATION, 0.0, 0.0);
		rightDrag = new Vector3d(-ACCELERATION, 0.0, 0.0);
		upDrag = new Vector3d(0.0, -ACCELERATION, 0.0);
		downDrag = new Vector3d(0.0, ACCELERATION, 0.0);

		leftVMax = -SPEED_MAX;
		rightVMax = SPEED_MAX;
		upVMax = SPEED_MAX;
		downVMax = -SPEED_MAX;

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

		a.x = a.y = a.z = 0;
		if (KEY_RIGHT) {
			a.add(rightAcc);
			a.sub(rightDrag);
		} else if (KEY_LEFT) {
			a.add(leftAcc);
			a.sub(leftDrag);
		}

		double pre = mov.x + a.x * deltaTime;
		if (pre < 0.0) {
			if (pre + leftDrag.x * deltaTime < 0.0)
				a.add(leftDrag);
			else
				a.x -= pre / deltaTime;
		} else if (pre > 0.0) {
			if (pre + rightDrag.x * deltaTime > 0.0)
				a.add(rightDrag);
			else
				a.x -= pre / deltaTime;
		}

		if (KEY_UP) {
			a.add(upAcc);
			a.sub(upDrag);
		} else if (KEY_DOWN) {
			a.add(downAcc);
			a.sub(downDrag);
		}

		pre = mov.y + a.y * deltaTime;
		if (pre < 0.0) {
			if (pre + downDrag.y * deltaTime < 0.0)
				a.add(downDrag);
			else
				a.y -= pre / deltaTime;
		} else if (pre > 0.0) {
			if (pre + upDrag.y * deltaTime > 0.0)
				a.add(upDrag);
			else
				a.y -= pre / deltaTime;
		}

		/* Integration of acceleration to velocity */
		dv.scale(deltaTime, a);
		mov.add(dv);

		/* Speed limits */
		if (mov.x > rightVMax) {
			mov.x = rightVMax;
		}
		if (mov.x < leftVMax) {
			mov.x = leftVMax;
		}
		if (mov.y > upVMax) {
			mov.y = upVMax;
		}
		if (mov.y < downVMax) {
			mov.y = downVMax;
		}

		double stepx = mov.x * deltaTime;
		double distToRadiusX = allowedDistToCircleRadius(pos.y
				+ Game3D.INITIAL_SHIP_PLACEMENT_Y, MOV_RADIUS);
		double distToRadiusRight = distToRadiusX
				- Game3D.INITIAL_SHIP_PLACEMENT_X;
		double distToRadiusLeft = distToRadiusX
				+ Game3D.INITIAL_SHIP_PLACEMENT_X;
//		System.out.println("DistRight: " + distToRadiusRight);
//		System.out.println("DistLeft: " + distToRadiusLeft);
		if (KEY_RIGHT) {
			if (distToRadiusRight - pos.x - stepx < 0) {
				mov.x = adaptSpeedAtBorder(distToRadiusRight - pos.x, stepx, mov.x);
			}
		} else if (KEY_LEFT) {
			if (distToRadiusLeft + pos.x - stepx < 0) {
				double remainingDist = distToRadiusLeft + pos.x;
				mov.x = -adaptSpeedAtBorder(remainingDist, stepx, mov.x);
			}
		}

		double stepy = mov.y * deltaTime;
		double distToRadiusY = allowedDistToCircleRadius(pos.x
				+ Game3D.INITIAL_SHIP_PLACEMENT_X, MOV_RADIUS);
		double distToRadiusUp = distToRadiusY - Game3D.INITIAL_SHIP_PLACEMENT_Y;
		double distToRadiusDown = distToRadiusY
				+ Game3D.INITIAL_SHIP_PLACEMENT_Y;
//		System.out.println("DistUp: " + distToRadiusUp);
//		System.out.println("DistDown: " + distToRadiusDown);
		if (KEY_UP) {
			if (distToRadiusUp - pos.y - stepy < 0) {
				double remainingDist = distToRadiusUp - pos.y;
				double newspeed = adaptSpeedAtBorder(remainingDist, stepy, mov.y);
				mov.y = newspeed;
			}
		} else if (KEY_DOWN) {
			if (distToRadiusDown + pos.y - stepy < 0) {
				double remainingDist = distToRadiusDown + pos.y;
				mov.y = -adaptSpeedAtBorder(remainingDist, stepy, mov.y);
			}
		}

		/* Integration of velocity to distance */
		dp.scale(deltaTime, mov);

		// add dp into current vp position.
		pos.add(dp);
		
		/* assure Position within Tunnelradius */
		if (pos.x > distToRadiusRight){
			pos.x = distToRadiusRight;
		} else if (pos.x < -distToRadiusLeft){
			pos.x = -distToRadiusLeft;
		}
		if (pos.y > distToRadiusUp){
			pos.y = distToRadiusUp;
		} else if (pos.y < -distToRadiusDown){
			pos.y = -distToRadiusDown;
		}
		
		


		/* Final update of the target transform group */
		// Put the transform back into the transform group.
		trans.set(pos);
		translationGroup.setTransform(trans);
		
		vpPos =new Vector3d(pos);
		vpPos.sub(shipToViewPosition(mov.x, mov.y));

		
//		System.out.println("mov: " + mov);
		
		vpTrans.set(vpPos);
		viewTG.setTransform(vpTrans);

	}
	
	private Point3d shipToViewPosition (double v_x, double v_y){
		Point3d dp = new Point3d();
		double maxDist = MAX_FOLLOWING_DIST;
		if(v_x<0){
			dp.x=-v_x/leftVMax;
		} else if (v_x > 0){
			dp.x=v_x/rightVMax;
		}
		if(v_y<0){
			dp.y=-v_y/downVMax;
		}else if (v_y > 0){
			dp.y=v_y/upVMax;
		}
		if (dp.x!=0|dp.y!=0){
		System.out.println("dp: " + dp);
		}
		return dp;
	}

	private double adaptSpeedAtBorder(double remainingDist, double step,
			double speed) {
		return ((remainingDist / step) * speed);
	}

	private double allowedDistToCircleRadius(double pos, double radius) {
		Double dist = Math.sqrt(Math.pow(radius, 2) - Math.pow(pos, 2));
		if (dist.equals(Double.NaN)){
			return 0;
		} else {
			return dist;
		}
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
