package de.tum.in.flowgame;

import java.applet.Applet;

public class SoundsHelper {

	private static Applet applet;

	public static void init(final Applet applet) {
		SoundsHelper.applet = applet;
	}
	
	public static Applet getApplet() {
		return applet;
	}
}
