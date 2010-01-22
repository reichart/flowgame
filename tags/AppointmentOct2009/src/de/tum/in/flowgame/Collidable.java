package de.tum.in.flowgame;

import javax.media.j3d.Alpha;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Link;
import javax.media.j3d.SharedGroup;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point2d;

import de.tum.in.flowgame.behavior.ForwardNavigatorBehavior;
import de.tum.in.flowgame.behavior.ShipNavigationBehavior;
import de.tum.in.flowgame.behavior.SpeedChangeBehavior;
import de.tum.in.flowgame.util.TransformGroupBuilder;

public class Collidable extends BranchGroup {
	private final double xPos;
	private final double yPos;
	private final double zPos;
//	private final ForwardNavigatorBehavior fwdNav;
//	private final SpeedChangeBehavior speedChange;

	public Collidable(final SharedGroup group, long speed, float scale, double zPos,
			GameLogic gameLogic) {
		this.setCapability(BranchGroup.ALLOW_DETACH);
		this.zPos = zPos;

		setUserData(group.getUserData());

		Link link = new Link(group);
		link.setBoundsAutoCompute(true);

		long duration = (long) (3000 + (Math.random() * 6000));
		// System.out.println(duration);
		Alpha rotAlpha = new Alpha(-1, duration);
		rotAlpha.setStartTime(System.currentTimeMillis());

		final TransformGroup shape = new TransformGroupBuilder()
				.add(link)
				.addRotationBehavior(rotAlpha, Game3D.WORLD_BOUNDS)
				.computeAutoBounds(true)
				.fin();

		final TransformGroup scaledShape = new TransformGroupBuilder()
				.scale(scale)
				.add(shape)
				.computeAutoBounds(true)
				.fin();

//		final int parts = Math.max(1, Tunnel.TUNNEL_PARTS - 1);
//		final float endOfTunnel = -parts * Tunnel.TUNNEL_LENGTH;

		final TransformGroup tg = new TransformGroupBuilder()
				.translate(0, 0, zPos)
				.add(scaledShape)
				.writable()
				.computeAutoBounds(true)
				.fin();

//		fwdNav = new ForwardNavigatorBehavior(tg, speed);
//		fwdNav.setSchedulingBounds(Game3D.WORLD_BOUNDS);
//		gameLogic.addListener(fwdNav);
//		tg.addChild(fwdNav);
//		speedChange = new SpeedChangeBehavior(fwdNav, gameLogic);
//		speedChange.setSchedulingBounds(Game3D.WORLD_BOUNDS);
//		this.addChild(speedChange);

		final TransformGroup scaleTG = new TransformGroupBuilder()
				.add(tg)
				.computeAutoBounds(true)
				.fin();

		scaleTG.setBoundsAutoCompute(true);
		
		Point2d position = new Point2d(Math.random()
				* ShipNavigationBehavior.MOV_RADIUS * 2 - ShipNavigationBehavior.MOV_RADIUS,
				Math.random() * ShipNavigationBehavior.MOV_RADIUS * 2
						- ShipNavigationBehavior.MOV_RADIUS);
		while (!withinCircle(position)) {
			position = new Point2d(Math.random() * ShipNavigationBehavior.MOV_RADIUS,
					Math.random() * ShipNavigationBehavior.MOV_RADIUS);
		}

		xPos = position.getX();
		yPos = position.getY();

		// xPos = Ship.INITIAL_SHIP_PLACEMENT_X;
		// yPos = Ship.INITIAL_SHIP_PLACEMENT_Y;

		TransformGroup transTG = new TransformGroupBuilder()
				.translate(xPos, yPos, 0)
				.add(scaleTG)
				.fin();
		
		addChild(transTG);
	}

//	private Point2d polarToCartesian(double radius, double angleInRadians) {
//		double x = Math.cos(angleInRadians) * radius;
//		double y = Math.sin(angleInRadians) * radius;
//		return new Point2d(x, y);
//	}

	private boolean withinCircle(Point2d position) {
		double radius = Math.sqrt(Math.pow(position.getX(), 2)
				+ Math.pow(position.getY(), 2));
		if (radius <= ShipNavigationBehavior.MOV_RADIUS)
			return true;
		else
			return false;
	}

	public double getZPos() {
		return zPos;
	}

	public double getXPos() {
		return xPos;
	}

	public double getYPos() {
		return yPos;
	}

}