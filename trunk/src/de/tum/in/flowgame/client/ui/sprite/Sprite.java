package de.tum.in.flowgame.client.ui.sprite;

import java.awt.Graphics2D;

/**
 * Interface for rendering graphics with defined locations and sizes.
 */
public interface Sprite {

	/**
	 * Render sprite at original size.
	 */
	public void render(final Graphics2D g, final int x, final int y);

	/**
	 * Render sprite at specified size.
	 */
	public void render(final Graphics2D g, final int x, final int y, final int w, final int h);
}
