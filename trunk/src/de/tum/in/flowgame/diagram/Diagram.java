package de.tum.in.flowgame.diagram;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import de.tum.in.flowgame.Utils;
import de.tum.in.flowgame.model.Score;

public class Diagram {

	private final SortedSet<Score> scores;
	private BufferedImage i;
	private Graphics2D g;
	private double xDelta;
	private int maxScoreValue;
	private int maxDiagramValue;
	private int increment;
	private double scaleFactor;
	private final static int WIDTH = 500;
	private final static int HEIGHT = 300;
	private final static int UPPER_BUFFER_Y = 10;
	private final static int COORD_BUFFER_X = 50;
	private final static int COORD_BUFFER_Y = 30;
	private final static int RIGHT_MARGIN = 8;
	private final static Font NUMBERS = new Font("Serif", Font.PLAIN, 9);
	private final static Font TEXT = new Font("Serif", Font.PLAIN, 12);

	public Diagram(SortedSet<Score> scores) {
		this.scores = scores;
		i = Utils.createImage(WIDTH, HEIGHT);
		g = (Graphics2D) i.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setFont(NUMBERS);
		calculateMaxScoreValue();
		calculateIncrementAndMaxDiagramValue();
		xDelta = (WIDTH - COORD_BUFFER_X - RIGHT_MARGIN) / (double) (scores.size());
	}

	private void calculateMaxScoreValue() {
		long value = 0;
		for (Score s : scores) {
			if (s.getScore() > value) {
				value = s.getScore();
			}
		}
		// System.err.println("heigt=" + (HEIGHT - COORD_BUFFER_Y) + " value=" +
		// value);
		// System.err.println("scaleFactor: " + (HEIGHT - COORD_BUFFER_Y) /
		// (double) value);
		this.maxScoreValue = (int) value;
	}

	private void calculateIncrementAndMaxDiagramValue() {
		int delta = (maxScoreValue / 8);
		// System.out.println(maxScoreValue);
		int digits = (int) (Math.floor(Math.log10(delta)));
		int powerOfTen = (int) Math.pow(10d, digits);
		this.increment = (powerOfTen == 0) ? 1 : delta / powerOfTen * powerOfTen;
		this.maxDiagramValue = ((maxScoreValue / increment) + 1) * increment;
		this.scaleFactor = (HEIGHT - COORD_BUFFER_Y - UPPER_BUFFER_Y) / (double) maxDiagramValue;
		// System.out.println(maxDiagramValue);
	}

	public Image diagram() {
		// WHITE background
		g.setColor(Color.WHITE);
		g.fillRect(COORD_BUFFER_X, 0, WIDTH, HEIGHT - COORD_BUFFER_Y);

		addYCaptionAndTicks();

		// System.err.println(xDelta);
		Point p1 = new Point(COORD_BUFFER_X, HEIGHT - COORD_BUFFER_Y - 1);
		Point p2 = new Point(COORD_BUFFER_X, HEIGHT - COORD_BUFFER_Y - 1);
		int counter = 1;
		int xCoord = COORD_BUFFER_X;
		addLineTickX(xCoord);
		addXNumber(0, xCoord);
		// System.err.println(scaleFactor);
		for (Score s : scores) {
			double value = (HEIGHT - COORD_BUFFER_Y - 1) - (int) s.getScore() * scaleFactor;
			p1.setLocation(p2);
			xCoord = (int) (counter * xDelta + COORD_BUFFER_X);
			p2.setLocation(new Point(xCoord, (int) value));
			addGraphPart(p1, p2);
			addLineTickX(xCoord);
			addXNumber(counter, xCoord);
			// System.err.println(p1.x + "," + p1.y + " ---> " + p2.x + "," +
			// p2.y);
			counter++;
		}

		addAxisLegends();

		g.dispose();
		return i;
	}

	private void addLineTickX(int xCoord) {
		g.setColor(Color.WHITE);
		g.setStroke(new BasicStroke(1));
		g.drawLine(xCoord, HEIGHT - COORD_BUFFER_Y - 1, xCoord, HEIGHT - COORD_BUFFER_Y + 2);

		g.setColor(Color.LIGHT_GRAY);
		float[] dash = { 2f };
		BasicStroke dashed = new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1f, dash, 0f);
		g.setStroke(dashed);
		g.drawLine(xCoord, 0, xCoord, HEIGHT - COORD_BUFFER_Y - 1);
	}

	private void addYCaptionAndTicks() {
		FontMetrics fm = g.getFontMetrics();
		int value = 0;
		for (int i = 0; i < maxDiagramValue + increment; i += increment) {
			value = (int) ((HEIGHT - COORD_BUFFER_Y - 1) - i * scaleFactor);
			g.setColor(Color.WHITE);
			g.setStroke(new BasicStroke(1));
			g.drawLine(COORD_BUFFER_X - 3, value, COORD_BUFFER_X - 1, value);
			String s = Integer.toString(i);
			int xOffset = fm.stringWidth(s) / 2;
			int yOffset = fm.getAscent() / 2;
			g.drawString(s, 30 - xOffset, value + yOffset);

			g.setColor(Color.LIGHT_GRAY);
			float[] dash = { 2f };
			BasicStroke dashed = new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1f, dash, 0f);
			g.setStroke(dashed);
			g.drawLine(COORD_BUFFER_X, value, WIDTH - 1, value);
		}
	}

	private void addAxisLegends() {
		String x = "Game";
		String y = "Points";
		FontMetrics fm = g.getFontMetrics();
		int xOffset = fm.stringWidth(x) / 2;
		int xCoord = (WIDTH - COORD_BUFFER_X) / 2 + COORD_BUFFER_X - xOffset;
		g.setFont(TEXT);
		g.drawString(x, xCoord, HEIGHT);

		// clockwise 90 degrees
		AffineTransform at = new AffineTransform();
		int yOffset = fm.stringWidth(y) / 2;
		int yCoord = (HEIGHT - COORD_BUFFER_Y) / 2 + yOffset;
		at.setToRotation(-Math.PI / 2.0, 10, yCoord);
		g.setTransform(at);
		g.drawString(y, 10, yCoord);
	}

	private void addXNumber(int x, int xCoord) {
		g.setColor(Color.WHITE);
		g.setStroke(new BasicStroke(1));
		FontMetrics fm = g.getFontMetrics();
		String s = Integer.toString(x);
		int offset = fm.stringWidth(s) / 2;
		g.drawString(s, xCoord - offset, HEIGHT - COORD_BUFFER_Y + 15);
	}

	private void addGraphPart(Point p1, Point p2) {
		g.setColor(Color.BLUE);
		g.setStroke(new BasicStroke(3));
		g.drawLine(p1.x, p1.y, p2.x, p2.y);
	}

	// for testing
	public static void main(String[] args) {
		SortedSet<Score> scores = new TreeSet<Score>();
		Random r = new Random(300);
		long time = System.currentTimeMillis();
		for (int i = 0; i < 10; i++) {
			scores.add(new Score(time + i * 100, (long) r.nextInt(100)));
		}
		JFrame jFrame = new JFrame();
		jFrame.add(new JLabel(new ImageIcon(new Diagram(scores).diagram())));
		jFrame.pack();
		jFrame.setVisible(true);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
