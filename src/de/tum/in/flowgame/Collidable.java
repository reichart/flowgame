package de.tum.in.flowgame;

import javax.media.j3d.Alpha;
import javax.media.j3d.Behavior;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Link;
import javax.media.j3d.SharedGroup;
import javax.media.j3d.TransformGroup;

import de.tum.in.flowgame.util.Builders;
import de.tum.in.flowgame.util.TransformGroupBuilder;

public class Collidable extends BranchGroup {

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

		final Behavior fwdNav = new ForwardNavigatorBehavior(tg, speed);
		fwdNav.setSchedulingBounds(Game3D.WORLD_BOUNDS);
		tg.addChild(fwdNav);

		final TransformGroup scaleTG = Builders.transformGroup()
			.scale(0.3)
			.add(tg)
			.fin();
		
		final TransformGroup transTG = Builders.transformGroup()
			.translate(Math.random() * 10 - 5, Math.random() * 10 - 5, 0)
			.add(scaleTG)
			.fin();
		
		addChild(transTG);
	}

}
