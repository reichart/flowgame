package de.tum.in.flowgame.ui;

import java.awt.Graphics2D;

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
