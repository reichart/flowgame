package de.tum.in.flowgame.ui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;

public class HealthBar {

	private final Image icon;
	private final String name;
	private final Color valueColor;
	private final Color maxColor;
	private final int max;

	private int value;
	
	public HealthBar(final Image icon, final String name, final Color valueColor, final Color maxColor,
			final int value, final int max) {
		this.icon = icon;
		this.name = name;
		this.valueColor = valueColor;
		this.maxColor = maxColor;
		this.value = value;
		this.max = max;
	}

	public void render(final Graphics g) {
		g.drawImage(icon, 0, 0, null);

		final int s = 16;
		final int HUD_BARS_SCALE = 10;

		g.setColor(maxColor);
		g.fillRect(s * 3 / 2, 0, (int) (HUD_BARS_SCALE * max), s);

		final float percentage = max * value / max;
		g.setColor(valueColor);
		g.fillRect(s * 3 / 2, 0, HUD_BARS_SCALE * (int) percentage, s);

		final FontMetrics fm = g.getFontMetrics();

		g.drawString(name + ": " + value, 2 * s + HUD_BARS_SCALE * max, fm.getHeight() / 5 * 4);
	}

	public void render(final Graphics g, final int x, final int y) {
		g.translate(x, y);
		render(g);
		g.translate(-x, -y);
	}

	public void setValue(final int value) {
		this.value = value;
	}

}
