package de.tum.in.flowgame.client.ui.sprite;

import java.awt.Dimension;
import java.awt.Graphics2D;

import com.kitfox.svg.app.beans.SVGIcon;

/**
 * A {@link Sprite} implementation to render SVG images.
 */
public class SVGSprite implements Sprite {

	private final SVGIcon icon;
	private final Dimension size;

	public SVGSprite(final String resource) {
		this.size = new Dimension();
		this.icon = new SVGIcon();

		icon.setPreferredSize(size); // avoids NPE
		icon.setSvgResourcePath(resource);

		icon.setAntiAlias(true);
		icon.setScaleToFit(true);
	}

	public void render(final Graphics2D g, final int x, final int y) {
		icon.setScaleToFit(false);
		icon.paintIcon(null, g, x, y);
	}

	public void render(final Graphics2D g, final int x, final int y, final int w, final int h) {
		size.setSize(w, h);
		icon.setPreferredSize(size);
		icon.paintIcon(null, g, x, y);
	}
	
	public SVGIcon getIcon(final int w, final int h) {
		icon.setPreferredSize(new Dimension(w, h));
		return icon;
	}
}
