package de.tum.in.flowgame.facebook;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;

public class JSONUtils {

	public static Set<Long> toLongs(final JSONArray array) throws JSONException {
		final Set<Long> result = new HashSet<Long>();
		for (int i = 0; i < array.length(); i++) {
			result.add(array.getLong(i));
		}
		return result;
	}
}