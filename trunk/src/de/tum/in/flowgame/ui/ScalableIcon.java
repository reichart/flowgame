package de.tum.in.flowgame.ui;

import java.awt.Dimension;
import java.awt.Graphics;

import com.kitfox.svg.app.beans.SVGIcon;

public class ScalableIcon {

	private final SVGIcon icon;
	private final Dimension size;

	public ScalableIcon(final String svgName) {
		this.size = new Dimension();
		this.icon = new SVGIcon();

		icon.setPreferredSize(size); // avoids NPE
		icon.setSvgResourcePath("/res/" + svgName + ".svg");
		
		icon.setAntiAlias(true);
		icon.setScaleToFit(true);
	}

	public void render(final Graphics g, final int x, final int y, final int w, final int h) {
		size.setSize(w, h);
		icon.setPreferredSize(size);
		icon.paintIcon(null, g, x, y);
	}

}