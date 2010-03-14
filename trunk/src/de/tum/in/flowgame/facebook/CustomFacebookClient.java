package de.tum.in.flowgame.facebook;

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

	public CustomFacebookClient(final URL serverUrl, final String apiKey, final String secret, final String sessionKey) {
		super(serverUrl, apiKey, secret, sessionKey);
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
}
