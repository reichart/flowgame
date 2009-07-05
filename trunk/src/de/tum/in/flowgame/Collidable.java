package de.tum.in.flowgame;

import javax.media.j3d.Alpha;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Link;
import javax.media.j3d.Node;
import javax.media.j3d.SharedGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;

import de.tum.in.flowgame.behavior.ForwardNavigatorBehavior;
import de.tum.in.flowgame.util.Builders;

public class Collidable extends BranchGroup {
	private double oldZPos;
	private final double xPos;
	private final double yPos;
	final private ForwardNavigatorBehavior fwdNav;
//	final private Transform3D staticTransforms;
	
	public Collidable(final SharedGroup group, final float x, long speed) {
		final TransformGroup shape = Builders.transformGroup()
			.add(new Link(group))
			.addRotationBehavior(new Alpha(-1, 3000), Game3D.WORLD_BOUNDS)
			.fin();

		final TransformGroup tg = Builders.transformGroup()
			.translate(0, 0, -(Tunnel.TUNNEL_PARTS - 1) * Tunnel.TUNNEL_LENGTH)
			.add(shape)
			.writable()
			.fin();

		fwdNav = new ForwardNavigatorBehavior(tg, speed);
		fwdNav.setSchedulingBounds(Game3D.WORLD_BOUNDS);
		tg.addChild(fwdNav);

		final TransformGroup scaleTG = Builders.transformGroup()
//			.scale(0.3)
			.add(tg)
			.fin();
		
		xPos = Math.random() * 10 - 5;
		yPos = Math.random() * 10 - 5;
		
		TransformGroup transTG = Builders.transformGroup()
			.translate(xPos, yPos, 0)
			.add(scaleTG)
			.fin();
		
		addChild(transTG);
		
		
//		this.staticTransforms= new Transform3D();
//		transTG.getTransform(staticTransforms);
//		
//		Node node = this;
//		while(node.getParent() != null){
//			if(node.getParent() instanceof TransformGroup){
//				TransformGroup transgroup = (TransformGroup)node.getParent();
//				Transform3D trans = new Transform3D();
//				transgroup.getTransform(trans);
//				staticTransforms.mul(trans);
//			}
//			node=node.getParent();
//		}
	}
	
	public double getOldZPos() {
		return oldZPos;
	}

	public void setOldZPos(double oldZPos) {
		this.oldZPos = oldZPos;
	}
	
	public double getZPos() {
		Transform3D t3d = new Transform3D();
		fwdNav.getForwardNavigator().getTargetTG().getTransform(t3d);
		
//		//only new line
//		t3d.mul(staticTransforms);
		
		Vector3d vector = new Vector3d();
		t3d.get(vector);
		return vector.getZ();
	}

	public double getXPos() {
//		Transform3D t3d = new Transform3D();
//		fwdNav.getForwardNavigator().getTargetTG().getTransform(t3d);
//		t3d.mul(staticTransforms);
//		Vector3d vector = new Vector3d();
//		t3d.get(vector);
//		
//		double diff = vector.getX()-xPos;
//		
//		return vector.getX();
		
		return xPos;
	}

	public double getYPos() {
//		Transform3D t3d = new Transform3D();
//		fwdNav.getForwardNavigator().getTargetTG().getTransform(t3d);
//		t3d.mul(staticTransforms);
//		Vector3d vector = new Vector3d();
//		t3d.get(vector);
//		
//		double diff = vector.getY()-yPos;
//		return vector.getY();
		
		return yPos;
	}

}