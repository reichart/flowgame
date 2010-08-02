package de.tum.in.flowgame.client.facebook;

import java.applet.Applet;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageProducer;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.code.facebookapi.FacebookException;
import com.google.code.facebookapi.FacebookJsonRestClient;
import com.google.code.facebookapi.ProfileField;

public class CustomFacebookClient extends FacebookJsonRestClient {

	private final Applet applet;
	private final String server;

	public CustomFacebookClient(final String server, final String apiKey, final String secret, final String sessionKey,
			final Applet applet) throws IOException {
		super(new URL(server + "fbproxy"), apiKey, secret, sessionKey);
		this.server = server;
		this.applet = applet;
	}

	/**
	 * Asynchronously loads an image from an URL.
	 */
	public Image loadImage(final String url) {
		try {
			final URL u = new URL(url);
			if (applet.isActive()) {
				return applet.getImage(u);
			} else {
				// i.e. we're running stand-alone
				final ImageProducer prod = (ImageProducer) u.getContent();
				return Toolkit.getDefaultToolkit().createImage(prod);

			}
		} catch (final Exception ex) {
			throw new RuntimeException("failed to load image from " + url, ex);
		}
	}

	public JSONObject users_getInfo(final long userId, final ProfileField... fields) throws FacebookException,
			JSONException {
		final Set<Long> userIds = Collections.singleton(userId);
		final List<ProfileField> fieldList = Arrays.asList(fields);
		final JSONArray response = (JSONArray) users_getInfo(userIds, fieldList);
		return response.getJSONObject(0);
	}

	public String users_getProfileField(final long userId, final ProfileField field) throws FacebookException,
			JSONException {
		return users_getInfo(userId, field).getString(field.fieldName());
	}
	
	@Override
	public long users_getLoggedInUser() throws FacebookException {
		if (applet.isActive()) {
			// avoid roundtrip to Facebook
			return Long.parseLong(applet.getParameter("userId"));
		} else {
			return super.users_getLoggedInUser();
		}
	}
	
	public String getServer() {
		return server;
	}
}
