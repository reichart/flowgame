package de.tum.in.flowgame;

import javax.media.j3d.Alpha;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Texture;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Primitive;

public class Tunnel extends TransformGroup {

	public static final float TUNNEL_LENGTH = 200.0f;
	public static final float TUNNEL_RADIUS = 8.0f;
	public static final int TUNNEL_PARTS = 3;

	public Tunnel() {
		setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

		final Appearance appearance = createAppearance();

		final Cylinder cyl = new Cylinder(TUNNEL_RADIUS, TUNNEL_LENGTH, Primitive.GENERATE_NORMALS_INWARD
				| Primitive.GENERATE_TEXTURE_COORDS, 50, 1, appearance);

		final Transform3D rotation = new Transform3D();
		rotation.rotX(Math.PI / 2);

		final BoundingBox tunnelPartBounds = new BoundingBox(new Point3d(-(double) TUNNEL_LENGTH / 2,
				-(double) TUNNEL_LENGTH / 2, -(double) TUNNEL_LENGTH / 2), new Point3d((double) TUNNEL_LENGTH / 2,
				(double) TUNNEL_LENGTH / 2, (double) TUNNEL_LENGTH / 2));
		// BoundingSphere tunnelPartBounds = new BoundingSphere(new Point3d(0.0,
		// 0.0, 0.0), tunnelPartLength / 2);

		for (int i = 0; i < TUNNEL_PARTS; i++) {
			final Shape3D tunnelShape = (Shape3D) cyl.getShape(Cylinder.BODY).cloneNode(true);
			tunnelShape.setBoundsAutoCompute(false);
			tunnelShape.setBounds(tunnelPartBounds);

			tunnelShape.setAppearance(appearance);
			
			//create a Rotation group, that includes the tunnel and the rotation group for Animation
			final TransformGroup tunnelRotationGroup = new TransformGroup();
			tunnelRotationGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
			
			final RotationInterpolator rotInt = new RotationInterpolator(new Alpha(-1, 10000), tunnelRotationGroup);
			rotInt.setSchedulingBounds(GameApplet.WORLD_BOUNDS);
			
			tunnelRotationGroup.addChild(rotInt);
			tunnelRotationGroup.addChild(tunnelShape);
			
			//lay cylinder down, so that it appears as tunnel
			final TransformGroup layTunnel = new TransformGroup(rotation);
			layTunnel.addChild(tunnelRotationGroup);
			
			final TransformGroup tunnelTranslationGroup = new TransformGroup();
			tunnelTranslationGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
			tunnelTranslationGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
			tunnelTranslationGroup.addChild(layTunnel);
			
			final Transform3D partTrans = new Transform3D();
			partTrans.set(new Vector3f(0.0f, 0.0f, -(TUNNEL_LENGTH * i)));
			final Transform3D part = new Transform3D();
			tunnelTranslationGroup.getTransform(part);
			part.mul(partTrans);
			tunnelTranslationGroup.setTransform(part);
			final TunnelPartMoveBehavior reUseBevahior = new TunnelPartMoveBehavior(tunnelShape, tunnelTranslationGroup,
					TUNNEL_LENGTH, TUNNEL_PARTS);
			reUseBevahior.setSchedulingBounds(GameApplet.WORLD_BOUNDS);
			layTunnel.addChild(reUseBevahior);
			
			addChild(tunnelTranslationGroup);
		}
		final ForwardNavigatorBehavior fwdNav = new ForwardNavigatorBehavior(this);
		fwdNav.setSchedulingBounds(GameApplet.WORLD_BOUNDS);
		
		addChild(fwdNav);
	}

	private Appearance createAppearance() {
		final Texture texture = GameApplet.getTexture("/res/water01.jpg");

		final TextureAttributes texAttr = new TextureAttributes();
		texAttr.setTextureMode(TextureAttributes.MODULATE);

		final TransparencyAttributes transp = new TransparencyAttributes();
		transp.setTransparency(.5f);
		transp.setTransparencyMode(TransparencyAttributes.NICEST);

		final Appearance tunnelAppearance = new Appearance();
		tunnelAppearance.setTexture(texture);
		tunnelAppearance.setTextureAttributes(texAttr);
		tunnelAppearance.setTransparencyAttributes(transp);
		return tunnelAppearance;
	}

}
