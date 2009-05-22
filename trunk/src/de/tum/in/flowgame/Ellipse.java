package de.tum.in.flowgame;

public class Ellipse {
	public static final float ELLIPSE_A = 1.7f;
	public static final float ELLIPSE_B = 0.7f;

	public final static double getYOnPosition(double x) {
		if (x > ELLIPSE_A) {
			return -1;
		} else {
			return Math.sqrt((1 - (Math.pow(x, 2) / Math.pow(ELLIPSE_A, 2))) * Math.pow(ELLIPSE_B, 2));
		}
	}

}
