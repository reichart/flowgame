package de.tum.in.flowgame.ui.sprite;

import java.awt.Color;
import java.awt.Graphics2D;

public class HealthBar implements Sprite {

	private final Sprite icon;
	private final Color valueColor;
	private final Color maxColor;
	private final int max;

	private int value;

	public HealthBar(final Sprite icon, final Color valueColor, final Color maxColor, final int value, final int max) {
		this.icon = icon;
		this.valueColor = valueColor;
		this.maxColor = maxColor;
		this.value = value;
		this.max = max;
	}

	private void render(final Graphics2D g) {
		final int HEIGHT = 20;

		// spacing 2:1:9:1:7 for icon: :bar: :text

		icon.render(g, 0, 0, 20, HEIGHT);

		final int scaledValue = (int) ((90f * value) / max);

		g.setColor(maxColor);
		g.fillRect(30 + scaledValue, 0, 90 - scaledValue, HEIGHT);

		g.setColor(valueColor);
		g.fillRect(30, 0, scaledValue, HEIGHT);
	}

	public void render(final Graphics2D g, final int x, final int y) {
		g.translate(x, y);
		render(g);
		g.translate(-x, -y);
	}

	/**
	 * @param h
	 *            if less than zero, only w will be used
	 */
	public void render(final Graphics2D g, final int x, final int y, final int w, final int h) {
		final float scaleX = w / 200f;
		final float scaleY = (h < 0) ? scaleX : h / 20f;

		g.scale(scaleX, scaleY);
		render(g, x, y);
		g.scale(1 / scaleX, 1 / scaleY);
	}

	public void setValue(final int value) {
		this.value = value;
	}

}
