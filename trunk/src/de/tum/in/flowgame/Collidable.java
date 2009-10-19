package de.tum.in.flowgame;

import javax.media.j3d.Alpha;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Link;
import javax.media.j3d.SharedGroup;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point2d;

import de.tum.in.flowgame.behavior.ShipNavigationBehavior;
import de.tum.in.flowgame.util.TransformGroupBuilder;

public class Collidable extends BranchGroup {
	private final double xPos;
	private final double yPos;
	private final double zPos;

	public Collidable(final SharedGroup group, long speed, float scale, double zPos) {
		this.setCapability(BranchGroup.ALLOW_DETACH);
		this.zPos = zPos;

		setUserData(group.getUserData());

		Link link = new Link(group);
		link.setBoundsAutoCompute(true);

		long duration = (long) (3000 + (Math.random() * 6000));
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

		final TransformGroup tg = new TransformGroupBuilder()
				.translate(0, 0, zPos)
				.add(scaledShape)
				.writable()
				.computeAutoBounds(true)
				.fin();

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

		xPos = position.x;
		yPos = position.y;

		TransformGroup transTG = new TransformGroupBuilder()
				.translate(xPos, yPos, 0)
				.add(scaleTG)
				.fin();
		
		addChild(transTG);
	}

	private boolean withinCircle(Point2d position) {
		double radius = Math.sqrt(Math.pow(position.x, 2)
				+ Math.pow(position.y, 2));
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