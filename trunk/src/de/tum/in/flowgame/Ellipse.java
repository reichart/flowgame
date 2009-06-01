package de.tum.in.flowgame;

public class Ellipse {

	public static final float ELLIPSE_A = 1.7f;
	public static final float ELLIPSE_B = 0.7f;

	private final static Ellipse ellipse = new Ellipse(ELLIPSE_A, ELLIPSE_B);

	private final double aSquared;
	private final double bSquared;

	public static double getYOnPosition(final double x) {
		return ellipse.getY(x);
	}

	public Ellipse(final double a, final double b) {
		this.aSquared = Math.pow(a, 2);
		this.bSquared = Math.pow(b, 2);
	}

	public double getY(final double x) {
		final double xSquared = Math.pow(x, 2);

		if (xSquared > aSquared) {
			return -1;
		} else {
			return Math.sqrt((1 - (xSquared / aSquared)) * bSquared);
		}
	}

}
