package de.tum.in.flowgame;

import javax.media.j3d.Alpha;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Primitive;

import de.tum.in.flowgame.behavior.TunnelPartMoveBehavior;
import de.tum.in.flowgame.util.AppearanceBuilder;
import de.tum.in.flowgame.util.AppearanceBuilder.TextureMode;
import de.tum.in.flowgame.util.AppearanceBuilder.Transparency;

public class Tunnel extends BranchGroup {

	public static final float TUNNEL_LENGTH = 200.0f;
	public static final float TUNNEL_RADIUS = 8.0f;
	public static final int TUNNEL_PARTS = 3;
	
	public Tunnel() {
		setCapability(BranchGroup.ALLOW_DETACH);
		setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

		final Appearance appearance = new AppearanceBuilder()
			.texture(Java3DUtils.getTexture("/res/tunnel.png"), TextureMode.MODULATE)
			.transparency(Transparency.NICEST, 0.5f)
			.fin();

		final Cylinder cyl = new Cylinder(TUNNEL_RADIUS, TUNNEL_LENGTH, Primitive.GENERATE_NORMALS_INWARD
				| Primitive.GENERATE_TEXTURE_COORDS, 50, 1, appearance);

		final Transform3D rotation = new Transform3D();
		rotation.rotX(Math.PI / 2);

		final BoundingBox tunnelPartBounds = new BoundingBox(new Point3d(-(double) TUNNEL_LENGTH / 2,
				-(double) TUNNEL_LENGTH / 2, -(double) TUNNEL_LENGTH / 2), new Point3d((double) TUNNEL_LENGTH / 2,
				(double) TUNNEL_LENGTH / 2, (double) TUNNEL_LENGTH / 2));

		for (int i = 0; i < TUNNEL_PARTS; i++) {
			final Shape3D tunnelShape = (Shape3D) cyl.getShape(Cylinder.BODY).cloneNode(true);
			tunnelShape.setBoundsAutoCompute(false);
			tunnelShape.setBounds(tunnelPartBounds);
			tunnelShape.setCollidable(false);
			tunnelShape.setUserData("Tunnel");

			tunnelShape.setAppearance(appearance);
			
			//create a Rotation group, that includes the tunnel and the rotation group for Animation
			final TransformGroup tunnelRotationGroup = new TransformGroup();
			tunnelRotationGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
			
			final RotationInterpolator rotInt = new RotationInterpolator(new Alpha(-1, 10000), tunnelRotationGroup);
			rotInt.setSchedulingBounds(Game3D.WORLD_BOUNDS);
			
			tunnelRotationGroup.addChild(rotInt);
			tunnelRotationGroup.addChild(tunnelShape);
			
			//lay cylinder down, so that it appears as tunnel
			final TransformGroup layTunnel = new TransformGroup(rotation);
			layTunnel.addChild(tunnelRotationGroup);
			
			final TransformGroup tunnelTranslationGroup = new TransformGroup();
			tunnelTranslationGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
			tunnelTranslationGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
			tunnelTranslationGroup.addChild(layTunnel);
			
			final Vector3f part = new Vector3f(0.0f, 0.0f, -(TUNNEL_LENGTH * i));
			final Transform3D t1 = new Transform3D();
			t1.set(part);
			tunnelTranslationGroup.setTransform(t1);
			final TunnelPartMoveBehavior reUseBevahior = new TunnelPartMoveBehavior(tunnelShape, tunnelTranslationGroup,
					TUNNEL_LENGTH, TUNNEL_PARTS);
			reUseBevahior.setSchedulingBounds(Game3D.WORLD_BOUNDS);
			layTunnel.addChild(reUseBevahior);
			
			addChild(tunnelTranslationGroup);
		}
	}

}
