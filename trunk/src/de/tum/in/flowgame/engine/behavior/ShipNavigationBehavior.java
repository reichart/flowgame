package de.tum.in.flowgame.engine.behavior;

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
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import de.tum.in.flowgame.GameListener;
import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.Utils;
import de.tum.in.flowgame.engine.Ship;
import de.tum.in.flowgame.engine.Tunnel;
import de.tum.in.flowgame.model.Collision.Item;

public class ShipNavigationBehavior extends Behavior implements GameListener {

	private float acceleration = 30f;
	private float maxSpeed = 18f;
	private Vector3d pos = new Vector3d();
	private final Transform3D trans = new Transform3D();

	private Vector3d mov = new Vector3d(0, 0, 0);
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
	private Matrix3f shipRotation = new Matrix3f();
	private final WakeupCondition condition;

	private boolean KEY_LEFT;
	private boolean KEY_RIGHT;
	private boolean KEY_UP;
	private boolean KEY_DOWN;

	private boolean normalSteering;

	private static final char PAUSE_KEY = ' ';
	private boolean pause;
	private long pauseBegin;
	private GameLogic gameLogic;

	public static final double MOV_RADIUS = Tunnel.TUNNEL_RADIUS - 0.8;

	private long lastKeyEventTime;

	private long time;
	private boolean firstPerson = false;

	public ShipNavigationBehavior(final TransformGroup translationGroup, final TransformGroup viewTG) {
		this.translationGroup = translationGroup;
		this.viewTG = viewTG;

		setPhysics(this.maxSpeed, this.acceleration);

		final WakeupCriterion keyPressed = new WakeupOnAWTEvent(
				KeyEvent.KEY_PRESSED);
		final WakeupCriterion keyReleased = new WakeupOnAWTEvent(
				KeyEvent.KEY_RELEASED);
		final WakeupCriterion keyTyped = new WakeupOnAWTEvent(
				KeyEvent.KEY_TYPED);
		final WakeupCriterion currentFrame = new WakeupOnElapsedFrames(0);

		this.condition = new WakeupOr(Utils.asArray(keyPressed, keyReleased,
				keyTyped, currentFrame));

		// Create Timer here.
		time = System.currentTimeMillis();
	}

	private void setPhysics(float maxSpeed, float acceleration) {
		leftAcc = new Vector3d(-acceleration, 0.0, 0.0);
		rightAcc = new Vector3d(acceleration, 0.0, 0.0);
		upAcc = new Vector3d(0.0, acceleration, 0.0);
		downAcc = new Vector3d(0.0, -acceleration, 0.0);

		leftDrag = new Vector3d(acceleration, 0.0, 0.0);
		rightDrag = new Vector3d(-acceleration, 0.0, 0.0);
		upDrag = new Vector3d(0.0, -acceleration, 0.0);
		downDrag = new Vector3d(0.0, acceleration, 0.0);

		leftVMax = -maxSpeed;
		rightVMax = maxSpeed;
		upVMax = maxSpeed;
		downVMax = -maxSpeed;
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
				if (!pause)
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
			if (e.getID() == KeyEvent.KEY_RELEASED
					&& when - lastKeyEventTime < 1) {
				// System.out.println(when-lastKeyEventTime);
				lastKeyEventTime = when;
				return;
			}
			lastKeyEventTime = when;
		}

		final int id = e.getID();

		if (id == KeyEvent.KEY_TYPED) {
			switch (e.getKeyChar()) {
			case PAUSE_KEY:
				if (pause) {
					gameLogic.unpause();
					// System.out.println("resume");
				} else {
					gameLogic.pause();
					// System.out.println("pause");
				}
				break;
			}
		}

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
			if (normalSteering) {
				KEY_DOWN = pressed;
				if (pressed)
					KEY_UP = !pressed;
			} else {
				KEY_UP = pressed;
				if (pressed)
					KEY_DOWN = !pressed;
			}
			break;
		case KeyEvent.VK_UP:
			if (normalSteering) {
				KEY_UP = pressed;
				if (pressed)
					KEY_DOWN = !pressed;
			} else {
				KEY_DOWN = pressed;
				if (pressed)
					KEY_UP = !pressed;
			}
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

		double deltaTime = getDeltaTime();
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
		Vector3d dv = new Vector3d();
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
		double distToRadiusLeft = distToRadiusX + Ship.INITIAL_SHIP_PLACEMENT_X;
		// System.out.println("DistRight: " + distToRadiusRight);
		// System.out.println("DistLeft: " + distToRadiusLeft);

		double distToRadiusY = allowedDistToCircleRadius(pos.x
				+ Ship.INITIAL_SHIP_PLACEMENT_X, MOV_RADIUS);
		double distToRadiusUp = distToRadiusY - Ship.INITIAL_SHIP_PLACEMENT_Y;
		double distToRadiusDown = distToRadiusY + Ship.INITIAL_SHIP_PLACEMENT_Y;
		// System.out.println("DistUp: " + distToRadiusUp);
		// System.out.println("DistDown: " + distToRadiusDown);

		/* Integration of velocity to distance */
		Point3d dp = new Point3d();
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

		// System.out.println("Pos.x: " + pos.x + " - Pos.y: " + pos.y);

		// calculate the rotation of the ship
		Matrix3f xMov = new Matrix3f();
		Matrix3f yMov = new Matrix3f();
		xMov.setIdentity();
		yMov.setIdentity();
		float factor = (float) ((Math.PI / 8) / maxSpeed);
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

		Transform3D vpTrans = new Transform3D();
		viewTG.getTransform(vpTrans);
		Vector3d vpPos = new Vector3d();
		vpTrans.get(vpPos);
		
		if (!firstPerson) {
			double realY = pos.y + Ship.INITIAL_SHIP_PLACEMENT_Y;
			if (realY > 0) {
				vpPos.y = realY * (MOV_RADIUS - 1.5) / MOV_RADIUS + 1;
			} else {
				vpPos.y = realY * (MOV_RADIUS - 0.5) / MOV_RADIUS + 1;
			}
			double realX = pos.x + Ship.INITIAL_SHIP_PLACEMENT_X;
			if (realX > 0) {
				vpPos.x = realX * (MOV_RADIUS - 1.8) / MOV_RADIUS;
			} else {
				vpPos.x = realX * (MOV_RADIUS - 1.8) / MOV_RADIUS;
			}
		} else {
			vpPos.z = Ship.INITIAL_SHIP_PLACEMENT_Z - 1;
			vpPos.y = pos.y + Ship.INITIAL_SHIP_PLACEMENT_Y;
			vpPos.x = pos.x + Ship.INITIAL_SHIP_PLACEMENT_X;
		}

		vpTrans.set(vpPos);
		
		viewTG.setTransform(vpTrans);

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

	public Vector3d getCoords() {
		Vector3d vec = new Vector3d();
		trans.get(vec);
		vec.x = vec.x + Ship.INITIAL_SHIP_PLACEMENT_X;
		vec.y = vec.y + Ship.INITIAL_SHIP_PLACEMENT_Y;
		vec.z = vec.z + Ship.INITIAL_SHIP_PLACEMENT_Z;
		return vec;
	}

	public void setNormalSteering(final boolean normal) {
		this.normalSteering = normal;
	}

	public boolean getNormalSteering() {
		return this.normalSteering;
	}

	public void added(final GameLogic game) {
		this.gameLogic = game;
	}
	
	public void removed(final GameLogic game) {
		this.gameLogic = null;
	}
	
	public void collided(GameLogic logic, Item item) {
		// empty
	}

	public void gamePaused(GameLogic game) {
		pause = true;
		pauseBegin = System.currentTimeMillis();
	}

	public void gameResumed(GameLogic game) {
		pause = false;
		time = time + (System.currentTimeMillis() - pauseBegin);
	}

	public void gameStarted(GameLogic game) {
		trans.setIdentity();
		translationGroup.setTransform(trans);
		a = new Vector3d();
		mov = new Vector3d();
		pos = new Vector3d();
	}

	public void gameStopped(GameLogic game) {
		// empty
	}

}