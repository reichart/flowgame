package de.tum.in.flowgame.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JSlider;

/**
 * Minimal slider that behaves similar to {@link JSlider} but without a default
 * value before.
 */
public class JPsychoSlider extends ChangeableComponent {

	private static final int KNOB_SIZE = 12;
	private static final int KNOB_HALFSIZE = KNOB_SIZE / 2;
	private static final int TRACK_HEIGHT = 16;

	private static final Color COLOR_TRACK = new Color(1f, 1f, 1f, .25f);
	private static final Color COLOR_KNOB = new Color(1f, 1f, 1f);
	private static final Color COLOR_CURSOR = new Color(1f, 1f, 1f, .5f);

	private Integer cursor;
	private Integer value;

	public JPsychoSlider() {
		final MouseBehavior behavior = new MouseBehavior();
		addMouseListener(behavior);
		addMouseMotionListener(behavior);
	}

	@Override
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(200, TRACK_HEIGHT);
	}

	@Override
	public Dimension getMaximumSize() {
		return new Dimension(400, TRACK_HEIGHT);
	}

	@Override
	public void paintComponent(final Graphics g) {
		final Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.translate(0, (getHeight() - TRACK_HEIGHT) / 2);
		
		g.setColor(COLOR_TRACK);
		g.fillRect(0, 0, getWidth(), TRACK_HEIGHT);

		if (value != null) {
			g.setColor(COLOR_KNOB);
			paintKnob(value, g);
		}

		if (cursor != null) {
			g.setColor(COLOR_CURSOR);
			paintKnob(cursor, g);
		}
	}

	private void paintKnob(final int xpos, final Graphics g) {
		g.fillOval(xpos - KNOB_HALFSIZE, (TRACK_HEIGHT - KNOB_SIZE) / 2, KNOB_SIZE, KNOB_SIZE);
	}

	private int clamp(final int xpos) {
		// add half a knob size of space add both ends
		return Math.max(Math.min(xpos, getMaxValue()), KNOB_HALFSIZE);
	}

	private int getMaxValue() {
		return getWidth() - KNOB_HALFSIZE;
	}

	/**
	 * @return relative value between 0 and 1
	 */
	public Float getValue() {
		if (value == null) {
			return null;
		}
		
		final float max = getMaxValue() - KNOB_HALFSIZE;
		final int realValue = clamp(value) - KNOB_HALFSIZE;
		return realValue / max;
	}

	private void setSelectedValue(final Integer value) {
		this.value = value;
		fireChange(this);
		repaint();
	}

	private void setCursorValue(final Integer cursor) {
		this.cursor = cursor;
		repaint();
	}

	/**
	 * Implements behavior for showing a cursor and setting the value by mouse.
	 */
	private class MouseBehavior extends MouseAdapter {
		@Override
		public void mouseClicked(final MouseEvent e) {
			setSelectedValue(clamp(e.getX()));
		}

		@Override
		public void mouseDragged(final MouseEvent e) {
			setSelectedValue(clamp(e.getX()));
			setCursorValue(null);
		}

		@Override
		public void mouseMoved(final MouseEvent e) {
			setCursorValue(clamp(e.getX()));
		}

		@Override
		public void mouseExited(final MouseEvent e) {
			setCursorValue(null);
		}
	}
}
