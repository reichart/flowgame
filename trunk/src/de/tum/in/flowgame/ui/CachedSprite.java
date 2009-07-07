package de.tum.in.flowgame.ui;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

/**
 * Renders the actual sprite into an offscreen image and the just draws this
 * image as long as the requested width/height is the same.
 */
public class CachedSprite implements Sprite {

	private final Sprite sprite;
	private BufferedImage img;
	private int currentW, currentH;

	public CachedSprite(final Sprite sprite) {
		this.sprite = sprite;
	}

	/**
	 * This just passes the call trough to the actual sprite.
	 */
	@Override
	public void render(final Graphics2D g, final int x, final int y) {
		System.out.println("CachedSprite.render()");
		sprite.render(g, x, y);
	}

	/**
	 * Renders the actual sprite to an offscreen image and then draws that
	 * image.
	 */
	@Override
	public void render(final Graphics2D g, final int x, final int y, final int w, final int h) {
		if (currentW != w || currentH != h) {
			// compensate for scaled graphics context
			final int scaledW = (int) (w * g.getTransform().getScaleX());
			final int scaledH = (int) (h * g.getTransform().getScaleY());

			img = createImage(scaledW, scaledH);
			final Graphics2D ig = (Graphics2D) img.getGraphics();
			sprite.render(ig, x, y, img.getWidth(), img.getHeight());
			ig.dispose();

			currentW = w;
			currentH = h;
		}

		g.drawImage(img, x, y, w, h, null);
	}

	private BufferedImage createImage(final int w, final int h) {
		final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		final GraphicsDevice gd = ge.getDefaultScreenDevice();
		final GraphicsConfiguration gc = gd.getDefaultConfiguration();
		return gc.createCompatibleImage(w, h, Transparency.TRANSLUCENT);
	}

}
