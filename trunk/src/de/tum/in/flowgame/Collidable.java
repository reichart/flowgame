package de.tum.in.flowgame;

import javax.media.j3d.Alpha;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Link;
import javax.media.j3d.SharedGroup;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point2d;

import de.tum.in.flowgame.behavior.ForwardNavigatorBehavior;
import de.tum.in.flowgame.behavior.KeyShipBehavior;
import de.tum.in.flowgame.util.Builders;

public class Collidable extends BranchGroup {
	private double oldZPos;
	private final double xPos;
	private final double yPos;
	final private ForwardNavigatorBehavior fwdNav;

	public Collidable(final SharedGroup group, long speed,
			float scale, GameLogic gameLogic) {
		this.setCapability(BranchGroup.ALLOW_DETACH);

		setUserData(group.getUserData());

		Link link = new Link(group);
		link.setBoundsAutoCompute(true);

		long duration = (long) (3000 + (Math.random() * 6000));
//		System.out.println(duration);
		Alpha rotAlpha = new Alpha(-1, duration);
		rotAlpha.setStartTime(System.currentTimeMillis());

		final TransformGroup shape = Builders.transformGroup()
				.add(link)
				.addRotationBehavior(rotAlpha, Game3D.WORLD_BOUNDS)
				.computeAutoBounds(true)
				.fin();

		final TransformGroup scaledShape = Builders.transformGroup()
				.scale(scale)
				.add(shape)
				.computeAutoBounds(true)
				.fin();

		final int parts = Math.max(1, Tunnel.TUNNEL_PARTS - 1);
		final float endOfTunnel = -parts * Tunnel.TUNNEL_LENGTH;
		
		final TransformGroup tg = Builders.transformGroup()
				.translate(0, 0, endOfTunnel)
				.add(scaledShape)
				.writable()
				.computeAutoBounds(true)
				.fin();

		fwdNav = new ForwardNavigatorBehavior(tg, speed);
		fwdNav.setSchedulingBounds(Game3D.WORLD_BOUNDS);
		gameLogic.addListener(fwdNav);
		tg.addChild(fwdNav);

		final TransformGroup scaleTG = Builders.transformGroup()
				.add(tg)
				.computeAutoBounds(true)
				.fin();

		scaleTG.setBoundsAutoCompute(true);

		Point2d position = polarToCartesian(Math.random()*KeyShipBehavior.MOV_RADIUS, Math.random() * 2 * Math.PI);
		xPos = position.getX();
		yPos = position.getY();

//		 xPos = Ship.INITIAL_SHIP_PLACEMENT_X;
//		 yPos = Ship.INITIAL_SHIP_PLACEMENT_Y;

		TransformGroup transTG = Builders.transformGroup()
			.translate(xPos,yPos, 0).add(scaleTG)
			.fin();
		addChild(transTG);
	}
	
	private Point2d polarToCartesian (double radius, double angleInRadians){
		double x = Math.cos( angleInRadians ) * radius;
		double y = Math.sin( angleInRadians ) * radius;
		return new Point2d(x,y);
	}

	public double getOldZPos() {
		return oldZPos;
	}

	public void setOldZPos(double oldZPos) {
		this.oldZPos = oldZPos;
	}

	public double getZPos() {
		return fwdNav.getForwardNavigator().getPos().getZ();
	}

	public double getXPos() {
		return xPos;
	}

	public double getYPos() {
		return yPos;
	}

}