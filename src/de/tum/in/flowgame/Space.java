package de.tum.in.flowgame;

import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.TransformGroup;

import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;

import de.tum.in.flowgame.util.AppearanceBuilder;
import de.tum.in.flowgame.util.Helper;
import de.tum.in.flowgame.util.AppearanceBuilder.TextureMode;
import de.tum.in.flowgame.util.AppearanceBuilder.Transparency;

/**
 * This class creates an animates the background of our universe.
 */
public class Space extends BranchGroup {

	public Space() {
		final TransformGroup stars = sphere(1f, new AppearanceBuilder()
			.texture(Java3DUtils.getTexture("/res/stars.png"), TextureMode.MODULATE, Helper.scale(4, 8, 0))
			.transparency(Transparency.NICEST, 0)
			.fin()
			);

		final TransformGroup nebula = sphere(1f, new AppearanceBuilder()
			.texture(Java3DUtils.getTexture("/res/nebula.png"), TextureMode.MODULATE)
			.transparency(Transparency.NICEST, 0.5f)
			.fin()
			);

		final BranchGroup branch = new BranchGroup();
		branch.addChild(Helper.rotate(Helper.rotating(stars, 10*60, this), 0, 0, 23.5));
		branch.addChild(Helper.rotate(Helper.rotating(nebula, 2*60, this), 45, 0, 0));

		final Background background = new Background(branch);
		background.setApplicationBounds(Game3D.WORLD_BOUNDS);
		addChild(background);
	}

	private TransformGroup sphere(final float radius, final Appearance appearance) {
		final Sphere sphere = new Sphere(radius, Primitive.GENERATE_NORMALS_INWARD | Primitive.GENERATE_TEXTURE_COORDS,
				32, appearance);

		final TransformGroup sphereGroup = new TransformGroup();
		sphereGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		sphereGroup.addChild(sphere);
		return sphereGroup;
	}
}
