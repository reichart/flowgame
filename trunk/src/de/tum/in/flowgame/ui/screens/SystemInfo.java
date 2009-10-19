package de.tum.in.flowgame.ui.screens;

import java.io.PrintWriter;
import java.util.Map;
import java.util.Map.Entry;

import javax.media.j3d.Canvas3D;
import javax.media.j3d.VirtualUniverse;

public class SystemInfo {

	/*
	 * These system properties can be queried from unsigned code
	 */
	private static final String[] SYSTEM_PROPERTY_KEYS = { "java.class.version", "java.vendor", "java.version",
			"os.name", "os.arch", "os.version" };

	public static void getSystemInfo(final PrintWriter out, final Canvas3D canvas) {
		for (final String key : SYSTEM_PROPERTY_KEYS) {
			out.println(key + ": " + System.getProperty(key));
		}

		out.println();

		final Map<?, ?> universe = VirtualUniverse.getProperties();
		for (final Entry<?, ?> entry : universe.entrySet()) {
			out.println(entry.getKey() + ": " + entry.getValue());
		}
	}
}
