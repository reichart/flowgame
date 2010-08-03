package de.tum.in.flowgame.client;

import java.applet.Applet;
import java.applet.AudioClip;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SoundsHelper {

	private final static Log log = LogFactory.getLog(SoundsHelper.class);
	
	private static Applet applet;

	public static void init(final Applet applet) {
		SoundsHelper.applet = applet;
	}

	public static AudioClip load(final String resource) {
		if (applet == null || !applet.isActive()) {
			log.warn("no applet or inactive, not loading sound: " + resource);
			return null;
		} else {
			log.info("loading sound: " + resource);
			return applet.getAudioClip(SoundsHelper.class.getResource("/res/sound/" + resource + ".wav"));
		}
	}

}
