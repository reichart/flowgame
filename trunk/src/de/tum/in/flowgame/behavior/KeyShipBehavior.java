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
import javax.vecmath.Matrix3f;
import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import de.tum.in.flowgame.Ship;
import de.tum.in.flowgame.Tunnel;
import de.tum.in.flowgame.Utils;

public class KeyShipBehavior extends Behavior {

	private static final float ACCELERATION = 30f;
	private static final float MAX_SPEED = 18f;
	private final Point3d dp = new Point3d();
	private Vector3d dv = new Vector3d();
	private final Vector3d pos = new Vector3d();
	private final Transform3D trans = new Transform3D();

	private final Vector3d mov = new Vector3d(0, 0, 0);
	private static final double MAX_FOLLOWING_DIST = 2;
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
	private Matrix3f shipRotation = new Matrix3f();
	private Vector3d vpPos = new Vector3d();
	private final WakeupCondition condition;

	private boolean KEY_LEFT;
	private boolean KEY_RIGHT;
	private boolean KEY_UP;
	private boolean KEY_DOWN;

	public static final double MOV_RADIUS = Tunnel.TUNNEL_RADIUS - 0.8;
	// private double MOV_RADIUS = 300;

	private long lastKeyEventTime;

	private long time;
	private boolean firstPerson = false;

	public KeyShipBehavior(final TransformGroup translationGroup,
			TransformGroup viewTG) {

		this.translationGroup = translationGroup;
		this.viewTG = viewTG;

		leftAcc = new Vector3d(-ACCELERATION, 0.0, 0.0);
		rightAcc = new Vector3d(ACCELERATION, 0.0, 0.0);
		upAcc = new Vector3d(0.0, ACCELERATION, 0.0);
		downAcc = new Vector3d(0.0, -ACCELERATION, 0.0);

		leftDrag = new Vector3d(ACCELERATION, 0.0, 0.0);
		rightDrag = new Vector3d(-ACCELERATION, 0.0, 0.0);
		upDrag = new Vector3d(0.0, -ACCELERATION, 0.0);
		downDrag = new Vector3d(0.0, ACCELERATION, 0.0);

		leftVMax = -MAX_SPEED;
		rightVMax = MAX_SPEED;
		upVMax = MAX_SPEED;
		downVMax = -MAX_SPEED;

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

		String os_name = System.getProperty("os.name", "");
		if (os_name.contains("Linux")) {
			final long when = e.getWhen();
			if ((when - lastKeyEventTime < 2)
					&& e.getID() == KeyEvent.KEY_RELEASED) {
				return;
			}
			lastKeyEventTime = when;
			// System.out.println("Filter criteria matched");
		}

		final int id = e.getID();
		final boolean pressed = (id == KeyEvent.KEY_PRESSED);
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			KEY_LEFT = pressed;
			if (pressed)
				KEY_RIGHT = !pressed;
			break;
		case KeyEvent.VK_RIGHT:
			KEY_RIGHT = pressed;
			if (pressed)
				KEY_LEFT = !pressed;
			break;
		case KeyEvent.VK_DOWN:
			KEY_UP = pressed;
			if (pressed)
				KEY_DOWN = !pressed;
			break;
		case KeyEvent.VK_UP:
			KEY_DOWN = pressed;
			if (pressed)
				KEY_UP = !pressed;
			break;
		}
	}

	/**
	 * Computes a new transform for the next frame based on the current
	 * transform and elapsed time. This new transform is written into the target
	 * transform group. This method should be called once per frame.
	 */
	private void updatePosition() {
		// System.out.println("LEFT: "+ KEY_LEFT +" - RIGHT: "+ KEY_RIGHT
		// +" - UP: "+KEY_UP+" - DOWN: "+ KEY_DOWN);

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

		double distToRadiusX = allowedDistToCircleRadius(pos.y
				+ Ship.INITIAL_SHIP_PLACEMENT_Y, MOV_RADIUS);
		double distToRadiusRight = distToRadiusX
				- Ship.INITIAL_SHIP_PLACEMENT_X;
		double distToRadiusLeft = distToRadiusX
				+ Ship.INITIAL_SHIP_PLACEMENT_X;
		// System.out.println("DistRight: " + distToRadiusRight);
		// System.out.println("DistLeft: " + distToRadiusLeft);

		double distToRadiusY = allowedDistToCircleRadius(pos.x
				+ Ship.INITIAL_SHIP_PLACEMENT_X, MOV_RADIUS);
		double distToRadiusUp = distToRadiusY - Ship.INITIAL_SHIP_PLACEMENT_Y;
		double distToRadiusDown = distToRadiusY
				+ Ship.INITIAL_SHIP_PLACEMENT_Y;
		// System.out.println("DistUp: " + distToRadiusUp);
		// System.out.println("DistDown: " + distToRadiusDown);

		/* Integration of velocity to distance */
		dp.scale(deltaTime, mov);

		// add dp into current vp position.
		pos.add(dp);

		/* assure Position within Tunnelradius */
		// adaptMovementToTunnelRadius();
		if (pos.x > distToRadiusRight) {
			pos.x = distToRadiusRight;
		} else if (pos.x < -distToRadiusLeft) {
			pos.x = -distToRadiusLeft;
		}
		if (pos.y > distToRadiusUp) {
			pos.y = distToRadiusUp;
		} else if (pos.y < -distToRadiusDown) {
			pos.y = -distToRadiusDown;
		}

		System.out.println("Pos.x: " + pos.x + " - Pos.y: " + pos.y);

		// calculate the rotation of the ship
		Matrix3f xMov = new Matrix3f();
		Matrix3f yMov = new Matrix3f();
		xMov.setIdentity();
		yMov.setIdentity();
		float factor = (float) ((Math.PI / 8) / MAX_SPEED);
		float rotAngleX = (float) -(mov.x * factor);
		float rotAngleY = (float) (mov.y * factor);
		xMov.rotZ(rotAngleX);
		xMov.rotZ(rotAngleX);
		yMov.rotX(rotAngleY);
		yMov.rotX(rotAngleY);

		// if (mov.x != 0 | mov.y != 0) {
		// System.out.println("mov.x: " + mov.x + " rotX: "+ rotAngleX
		// +" - mov.y: "+ mov.y + " \trotY: " + rotAngleY);
		// }
		shipRotation.mul(xMov, yMov);
		/* Final update of the target transform group */
		// Put the transform back into the transform group.
		trans.set(shipRotation, pos, 1);
		translationGroup.setTransform(trans);

		vpPos = new Vector3d(pos);

		if (!firstPerson) {
			double realY = pos.y + Ship.INITIAL_SHIP_PLACEMENT_Y;
			if (realY > 0) {
				vpPos.y = realY * (MOV_RADIUS-1.5)/MOV_RADIUS + 1;
			} else {
				vpPos.y = realY * (MOV_RADIUS-0.5)/MOV_RADIUS + 1;
			}
			double realX = pos.x + Ship.INITIAL_SHIP_PLACEMENT_X;
			if (realX > 0) {
				vpPos.x = realX * (MOV_RADIUS-1.8)/MOV_RADIUS;
			} else {
				vpPos.x = realX * (MOV_RADIUS-1.8)/MOV_RADIUS;
			}
		} else {
			vpPos.z = Ship.INITIAL_SHIP_PLACEMENT_Z-1;
			vpPos.y = pos.y + Ship.INITIAL_SHIP_PLACEMENT_Y;
			vpPos.x = pos.x + Ship.INITIAL_SHIP_PLACEMENT_X;
		}

		// System.out.println(realY);
//		System.out.println("vpPos.x: " + vpPos.x + " - vpPos.y: " + vpPos.y);

		// vpPos.sub(shipToViewPosition(mov.x, mov.y));

		// System.out.println(vpPos.y + " - " + realY);
		vpTrans.set(vpPos);
		// System.out.println("pos.y: " + pos.y + " - vpPos.y: " + vpPos.y);
		// System.out.println(vpPos.y);
		viewTG.setTransform(vpTrans);
		
		
	}

	private void adaptMovementToTunnelRadius() {
		int circleSection = outOfCircleSection(pos);
		if (circleSection > 0) {
			switch (circleSection) {
			case 1:
				if (mov.x > mov.y) {
					if (pos.x <= MOV_RADIUS - Ship.INITIAL_SHIP_PLACEMENT_X) {
						pos.y = allowedDistToCircleRadius(pos.x
								+ Ship.INITIAL_SHIP_PLACEMENT_X, MOV_RADIUS)
								- Ship.INITIAL_SHIP_PLACEMENT_Y;
					} else {
						pos.x = MOV_RADIUS - Ship.INITIAL_SHIP_PLACEMENT_X;
						pos.y = 0 - Ship.INITIAL_SHIP_PLACEMENT_Y;
					}
				} else if (mov.y > mov.x) {
					if (pos.y <= MOV_RADIUS - Ship.INITIAL_SHIP_PLACEMENT_Y) {
						pos.x = allowedDistToCircleRadius(pos.y
								+ Ship.INITIAL_SHIP_PLACEMENT_Y, MOV_RADIUS)
								- Ship.INITIAL_SHIP_PLACEMENT_X;
					} else {
						pos.y = MOV_RADIUS - Ship.INITIAL_SHIP_PLACEMENT_Y;
						pos.x = 0 - Ship.INITIAL_SHIP_PLACEMENT_X;
					}
				} else {
					Point2d newPos = nearstPointOnCirce(pos, mov, circleSection);
					pos.x = newPos.x;
					pos.y = newPos.y;
				}
				break;
			case 2:
				if (-mov.x > mov.y) {
					if (-pos.x <= MOV_RADIUS - Ship.INITIAL_SHIP_PLACEMENT_X) {
						pos.y = allowedDistToCircleRadius(-pos.x
								+ Ship.INITIAL_SHIP_PLACEMENT_X, MOV_RADIUS)
								- Ship.INITIAL_SHIP_PLACEMENT_Y;
					} else {
						pos.x = -MOV_RADIUS - Ship.INITIAL_SHIP_PLACEMENT_X;
						pos.y = 0 - Ship.INITIAL_SHIP_PLACEMENT_Y;
					}
				} else if (mov.y > -mov.x) {
					if (pos.y <= MOV_RADIUS - Ship.INITIAL_SHIP_PLACEMENT_Y) {
						pos.x = -allowedDistToCircleRadius(pos.y
								+ Ship.INITIAL_SHIP_PLACEMENT_Y, MOV_RADIUS)
								- Ship.INITIAL_SHIP_PLACEMENT_X;
					} else {
						pos.y = MOV_RADIUS - Ship.INITIAL_SHIP_PLACEMENT_Y;
						pos.x = 0 + Ship.INITIAL_SHIP_PLACEMENT_X;
					}
				} else {
					Point2d newPos = nearstPointOnCirce(pos, mov, circleSection);
					pos.x = newPos.x;
					pos.y = newPos.y;
				}
				break;
			case 3:
				if (mov.x < mov.y) {
					if (pos.x >= -MOV_RADIUS - Ship.INITIAL_SHIP_PLACEMENT_X) {
						pos.y = -allowedDistToCircleRadius(pos.x
								+ Ship.INITIAL_SHIP_PLACEMENT_X, MOV_RADIUS)
								- Ship.INITIAL_SHIP_PLACEMENT_Y;
					} else {
						pos.x = -MOV_RADIUS - Ship.INITIAL_SHIP_PLACEMENT_X;
						pos.y = 0 + Ship.INITIAL_SHIP_PLACEMENT_Y;
					}
				} else if (mov.y < mov.x) {
					if (pos.y >= -MOV_RADIUS - Ship.INITIAL_SHIP_PLACEMENT_Y) {
						pos.x = -allowedDistToCircleRadius(pos.y
								+ Ship.INITIAL_SHIP_PLACEMENT_Y, MOV_RADIUS)
								- Ship.INITIAL_SHIP_PLACEMENT_X;
					} else {
						pos.y = -MOV_RADIUS - Ship.INITIAL_SHIP_PLACEMENT_Y;
						pos.x = 0 + Ship.INITIAL_SHIP_PLACEMENT_X;
					}
				} else {
					Point2d newPos = nearstPointOnCirce(pos, mov, circleSection);
					pos.x = newPos.x;
					pos.y = newPos.y;
				}
				break;
			case 4:
				if (mov.x > -mov.y) {
					if (pos.x <= MOV_RADIUS - Ship.INITIAL_SHIP_PLACEMENT_X) {
						pos.y = -allowedDistToCircleRadius(pos.x
								+ Ship.INITIAL_SHIP_PLACEMENT_X, MOV_RADIUS)
								- Ship.INITIAL_SHIP_PLACEMENT_Y;
					} else {
						pos.x = MOV_RADIUS - Ship.INITIAL_SHIP_PLACEMENT_X;
						pos.y = 0 - Ship.INITIAL_SHIP_PLACEMENT_Y;
					}
				} else if (mov.y < mov.x) {
					if (pos.y >= -MOV_RADIUS - Ship.INITIAL_SHIP_PLACEMENT_Y) {
						pos.x = allowedDistToCircleRadius(pos.y
								+ Ship.INITIAL_SHIP_PLACEMENT_Y, MOV_RADIUS)
								- Ship.INITIAL_SHIP_PLACEMENT_X;
					} else {
						pos.y = -MOV_RADIUS - Ship.INITIAL_SHIP_PLACEMENT_Y;
						pos.x = 0 - Ship.INITIAL_SHIP_PLACEMENT_X;
					}
				} else {
					Point2d newPos = nearstPointOnCirce(pos, mov, circleSection);
					pos.x = newPos.x;
					pos.y = newPos.y;
				}

			}
		}
	}

	private Point2d[] circleLineIntersection(double r, double xm, double ym,
			double m, double n) {
		double p = (2 * n * m - 2 * xm - 2 * m * ym - ym) / (1 + m * m) / 2;
		double q = (xm * xm + n * n - 2 * n * ym - r * r) / (1 + m * m);
		double numResults = p * p - q;
		if (numResults < 0) {
			return new Point2d[0];
		}
		double x1 = -p + Math.sqrt(p * p - q);
		double y1 = n + m * x1;
		if (numResults == 0) {
			Point2d[] result = { new Point2d(x1, y1) };
			return result;
		}
		double x2 = -p - Math.sqrt(p * p - q);
		double y2 = n + m * x2;
		Point2d[] result = { new Point2d(x1, y1), new Point2d(x2, y2) };
		return result;
	}

	private Point2d nearstPointOnCirce(Vector3d pos, Vector3d mov, int section) {
		if (mov.x == 0 & mov.y == 0)
			return new Point2d(pos.x, pos.y);
		double m = mov.y / mov.x;
		double n = pos.y - m * pos.x;
		Point2d[] intersections = circleLineIntersection(MOV_RADIUS,
				-Ship.INITIAL_SHIP_PLACEMENT_X,
				-Ship.INITIAL_SHIP_PLACEMENT_Y, m, n);
		if (intersections.length < 2) {
			return intersections[0];
		} else {
			double dist1 = Math.sqrt(Math.pow(pos.x - intersections[0].x, 2)
					+ Math.pow(pos.y - intersections[0].y, 2));
			double dist2 = Math.sqrt(Math.pow(pos.x - intersections[1].x, 2)
					+ Math.pow(pos.y - intersections[1].y, 2));
			if (dist1 < dist2) {
				return intersections[0];
			} else {
				return intersections[1];
			}
		}
	}

	private int outOfCircleSection(Vector3d pos) {
		double radius = Math.sqrt(Math.pow(
				(pos.x + Ship.INITIAL_SHIP_PLACEMENT_X), 2)
				+ Math.pow((pos.y + Ship.INITIAL_SHIP_PLACEMENT_Y), 2));
		if (radius <= MOV_RADIUS) {
			return 0;
		}
		if (pos.x >= 0 & pos.y > 0)
			return 1;
		if (pos.x < 0 & pos.y >= 0)
			return 2;
		if (pos.x <= 0 & pos.y < 0)
			return 3;
		return 4;
	}

	private Point3d shipToViewPosition(double v_x, double v_y) {
		Point3d dp = new Point3d();
		if (v_x < 0) {
			dp.x = -v_x / leftVMax;
		} else if (v_x > 0) {
			dp.x = v_x / rightVMax;
		}
		if (v_y < 0) {
			dp.y = -v_y / downVMax;
		} else if (v_y > 0) {
			dp.y = v_y / upVMax;
		}
		dp.scale(MAX_FOLLOWING_DIST);
		if (dp.x != 0 | dp.y != 0) {
			System.out.println("dp: " + dp);
		}
		return dp;
	}

	private double allowedDistToCircleRadius(double pos, double radius) {
		Double dist = Math.sqrt(Math.pow(radius, 2) - Math.pow(pos, 2));
		if (dist.equals(Double.NaN)) {
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

	public Transform3D getVpTrans() {
		return vpTrans;
	}

}
