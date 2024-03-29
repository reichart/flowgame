package de.tum.in.flowgame.client.ui.sprite;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import de.tum.in.flowgame.Utils;

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
	public void render(final Graphics2D g, final int x, final int y) {
		sprite.render(g, x, y);
	}

	/**
	 * Renders the actual sprite to an offscreen image and then draws that
	 * image.
	 */
	public void render(final Graphics2D g, final int x, final int y, final int w, final int h) {
		if (currentW != w || currentH != h) {
			// compensate for scaled graphics context
			final int scaledW = (int) (w * g.getTransform().getScaleX());
			final int scaledH = (int) (h * g.getTransform().getScaleY());

			img = Utils.createImage(scaledW, scaledH);
			final Graphics2D ig = (Graphics2D) img.getGraphics();
			sprite.render(ig, x, y, img.getWidth(), img.getHeight());
			ig.dispose();

			currentW = w;
			currentH = h;
		}

		g.drawImage(img, x, y, w, h, null);
	}

}
