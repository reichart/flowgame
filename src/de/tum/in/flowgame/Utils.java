package de.tum.in.flowgame;

import javax.media.j3d.Node;

public class Utils {

	public static <T> T[] asArray(final T... t) {
		return t;
	}

	public void printRootPath(final Node node) {
		if (node == null) {
			System.out.println();
			return;
		} else {
			final String instance = node.getClass().getSimpleName() + "@" + Integer.toHexString(node.hashCode());
			System.out.print(" > " + instance);
			printRootPath(node.getParent());
		}
	}
}
