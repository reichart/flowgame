package de.tum.in.flowgame.client.engine.util;

import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.media.j3d.Texture;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransparencyAttributes;

public class AppearanceBuilder {

	private final Appearance app;

	public AppearanceBuilder() {
		this.app = new Appearance();
	}

	public AppearanceBuilder material(final Material material) {
		this.app.setMaterial(material);
		return this;
	}
	
	public static enum TextureMode {
		MODULATE(TextureAttributes.MODULATE), DECAL(TextureAttributes.DECAL), BLEND(TextureAttributes.BLEND), REPLACE(
				TextureAttributes.REPLACE), COMBINE(TextureAttributes.COMBINE);

		private final int key;

		private TextureMode(final int key) {
			this.key = key;
		}
	}

	public AppearanceBuilder texture(final Texture texture, final TextureMode mode, final Transform3D transform) {

		final TextureAttributes attr = new TextureAttributes();
		if (mode != null) {
			attr.setTextureMode(mode.key);
			app.setTextureAttributes(attr);
		}

		if (transform != null) {
			attr.setTextureTransform(transform);
			app.setTextureAttributes(attr);
		}

		app.setTexture(texture);

		return this;
	}

	public AppearanceBuilder texture(final Texture texture, final TextureMode mode) {
		return texture(texture, mode, null);
	}

	public AppearanceBuilder texture(final Texture texture, final Transform3D transform) {
		return texture(texture, null, transform);
	}

	public AppearanceBuilder texture(final Texture texture) {
		return texture(texture, null, null);
	}

	public static enum Transparency {
		NONE(TransparencyAttributes.NONE), FASTEST(TransparencyAttributes.FASTEST), NICEST(
				TransparencyAttributes.NICEST), SCREEN_DOOR(TransparencyAttributes.SCREEN_DOOR), BLENDED(
				TransparencyAttributes.BLENDED);

		private final int key;

		private Transparency(final int key) {
			this.key = key;
		}
	}

	public AppearanceBuilder transparency(final Transparency mode, final float value) {
		final TransparencyAttributes att = new TransparencyAttributes(mode.key, value);
		app.setTransparencyAttributes(att);
		return this;
	}

	public Appearance fin() {
		return app;
	}
}
