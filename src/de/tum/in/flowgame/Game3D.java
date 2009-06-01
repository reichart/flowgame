package de.tum.in.flowgame;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;

import javax.media.j3d.Canvas3D;
import javax.media.j3d.J3DGraphics2D;

import com.sun.j3d.utils.universe.SimpleUniverse;

public class Game3D extends Canvas3D {

	private final GameLogic logic;

	private Font bigFont;

	public Game3D(final GameLogic logic) {
		super(SimpleUniverse.getPreferredConfiguration());
		this.logic = logic;

	}

	@Override
	public void postRender() {
		final J3DGraphics2D g2 = getGraphics2D();
		renderHUD(g2);
		g2.flush(false);
	}

	private void renderHUD(final Graphics2D g2) {
		if (bigFont == null) {
			bigFont = getFont().deriveFont(Font.BOLD, 32f);
		}

		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setColor(Color.WHITE);
		g2.setFont(bigFont);

		final FontMetrics fm = g2.getFontMetrics();

		g2.drawString("Asteroids: " + logic.getAsteroids(), 20, fm.getHeight());
		g2.drawString("Fuel: " + logic.getFuel(), 20, 2 * fm.getHeight());
	}

}
