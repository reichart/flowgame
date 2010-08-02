package de.tum.in.flowgame.util;

import java.applet.Applet;
import java.util.Arrays;

import netscape.javascript.JSObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JSObjectBrowser implements Browser {

	private final static Log log = LogFactory.getLog(JSObjectBrowser.class);

	private final JSObject js;

	/**
	 * Creates a new instance of this class from an applet.
	 */
	public static Browser from(final Applet applet) {
		JSObject win = null;
		if (applet.isActive()) {
			win = JSObject.getWindow(applet);
		}

		return new JSObjectBrowser(win);
	}

	private JSObjectBrowser(final JSObject js) {
		this.js = js;
	}

	public String getCookie(final String name) {
		return (String) call("get_cookie", name);
	}

	public void setCookie(final String name, final String value) {
		call("set_cookie", name, value);
	}

	public void toggleInviteDialog() {
		call("toggle_invite_content");
	}

	private Object call(final String function, final Object... parameters) {
		final String call = function + "(" + Arrays.toString(parameters) + ")";
		if (js == null) {
			log.warn("no JS object, not calling: " + call);
			return null;
		} else {
			log.info("calling: " + call);
			final Object ret = js.call(function, parameters);
			log.info(call + " returned " + ret);
			return ret;
		}
	}
}
