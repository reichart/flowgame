package de.tum.in.flowgame;

import javax.media.j3d.Alpha;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Link;
import javax.media.j3d.SharedGroup;
import javax.media.j3d.TransformGroup;

import de.tum.in.flowgame.behavior.ForwardNavigatorBehavior;
import de.tum.in.flowgame.util.Builders;

public class Collidable extends BranchGroup {
	private double oldZPos;
	private final double xPos;
	private final double yPos;
	final private ForwardNavigatorBehavior fwdNav;
	
	public Collidable(final SharedGroup group, final float x, long speed) {
		this.setCapability(BranchGroup.ALLOW_DETACH);
		
		setUserData(group.getUserData());
		
		Link link = new Link(group);
		link.setBoundsAutoCompute(true);
		
		final TransformGroup shape = Builders.transformGroup()
			.add(link)
			.addRotationBehavior(new Alpha(-1, 3000), Game3D.WORLD_BOUNDS)
			.computeAutoBounds(true)
			.fin();

		final TransformGroup scaledShape = Builders.transformGroup()
			.scale(0.5)
			.add(shape)
			.computeAutoBounds(true)
			.fin();
		
		final TransformGroup tg = Builders.transformGroup()
			.translate(0, 0, -(Tunnel.TUNNEL_PARTS - 1) * Tunnel.TUNNEL_LENGTH)
			.add(scaledShape)
			.writable()
			.computeAutoBounds(true)
			.fin();
		
		fwdNav = new ForwardNavigatorBehavior(tg, speed);
		fwdNav.setSchedulingBounds(Game3D.WORLD_BOUNDS);
		tg.addChild(fwdNav);

		final TransformGroup scaleTG = Builders.transformGroup()
			.add(tg)
			.computeAutoBounds(true)
			.fin();
		
		scaleTG.setBoundsAutoCompute(true);
		
		xPos = Math.random() * 10 - 5;
		yPos = Math.random() * 10 - 5;
		
//		xPos = Ship.INITIAL_SHIP_PLACEMENT_X;
//		yPos = Ship.INITIAL_SHIP_PLACEMENT_Y;
		
		TransformGroup transTG = Builders.transformGroup()
			.translate(xPos, yPos, 0)
			.add(scaleTG)
			.fin();
		addChild(transTG);
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