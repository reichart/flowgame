package de.tum.in.flowgame.engine;

import javax.media.j3d.Appearance;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;

import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Primitive;

import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.GameLogicConsumer;
import de.tum.in.flowgame.engine.behavior.TextureTransformBehavior;
import de.tum.in.flowgame.engine.util.AppearanceBuilder;
import de.tum.in.flowgame.engine.util.Helper;
import de.tum.in.flowgame.engine.util.Java3DUtils;
import de.tum.in.flowgame.engine.util.AppearanceBuilder.TextureMode;
import de.tum.in.flowgame.engine.util.AppearanceBuilder.Transparency;

public class Tunnel extends TransformGroup implements GameLogicConsumer {

	public static final float TUNNEL_LENGTH = 1000;
	public static final float TUNNEL_RADIUS = 8;
	public static final int TUNNEL_PARTS = 1;

	private final TextureTransformBehavior ttb;
	
	public Tunnel() {
		setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		final Appearance appearance = new AppearanceBuilder().texture(Java3DUtils.getTexture("/res/tunnel.png"),
				TextureMode.MODULATE).transparency(Transparency.NICEST, 0.5f).fin();

		final Cylinder cyl = new Cylinder(TUNNEL_RADIUS, TUNNEL_LENGTH, Primitive.GENERATE_NORMALS_INWARD
				| Primitive.GENERATE_TEXTURE_COORDS, 16, 1, appearance);
		final Shape3D body = (Shape3D) cyl.getShape(Cylinder.BODY).cloneNode(true);
		body.setAppearance(appearance);

		ttb = new TextureTransformBehavior(appearance.getTextureAttributes());
		
		addChild(Helper.translate(Helper.rotate(body, 90, 0, 0), 0, 0, -TUNNEL_LENGTH / 2));
		addChild(ttb);
	}
	
	public TextureTransformBehavior getTTB(){
		return ttb;
	}

	public void setGameLogic(final GameLogic game) {
		ttb.setGameLogic(game);
	}
}
