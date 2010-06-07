package de.tum.in.flowgame.facebook;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.code.facebookapi.FacebookException;
import com.google.code.facebookapi.ProfileField;

import de.tum.in.flowgame.GameApplet;
import de.tum.in.flowgame.Utils;

public class FacebookFriendCache {
	
	private static final Log log = LogFactory.getLog(FacebookFriendCache.class);

	/*
	 * Dummy profile pic for users with no PIC_SQUARE, fall back to empty pic.
	 */
	private final static BufferedImage DUMMY_PROFILE_PIC = Utils.image(GameApplet.class
			.getResource("/res/dummy-profile-pic.png"), new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB));

	private final CustomFacebookClient facebook;
	private final List<Friend> friends;

	private final Collection<ProfileField> fields;

	private Friend currentPlayer;

	public FacebookFriendCache(CustomFacebookClient facebook) {
		this.facebook = facebook;
		this.friends = new ArrayList<Friend>();

		this.fields = new ArrayList<ProfileField>();
		fields.add(ProfileField.UID);
		fields.add(ProfileField.NAME);
		fields.add(ProfileField.IS_APP_USER);
		fields.add(ProfileField.PIC_SQUARE);
	}

	public void updateFriends() throws FacebookException, JSONException {
		if (facebook == null) {
			log.warn("no facebook, not updating friends");
			return;
		}
		
		final JSONArray fbfriends = facebook.friends_get();

		if (fbfriends == null || fbfriends.length() == 0) {
			friends.clear();
			return;
		}
		
		Object friendsInfo = facebook.users_getInfo(getFriendsIdsForUpdate(fbfriends), fields);
		if (friendsInfo instanceof JSONArray) {
			JSONArray jay = (JSONArray) friendsInfo;

			for (int i = 0; i < jay.length(); i++) {
				Object temp = jay.get(i);
				if (temp instanceof JSONObject) {
					JSONObject info = (JSONObject) temp;
					if (Boolean.valueOf(info.getString(ProfileField.IS_APP_USER.toString()))) {
						friends.add(getFriendFromJSONObject(info));
					}
				}
			}
		}
	}

	private Friend getFriendFromJSONObject(final JSONObject info) throws JSONException {
		final long uid = info.getLong(ProfileField.UID.toString());
		final String name = info.getString(ProfileField.NAME.toString());

		final String picSquare = info.getString(ProfileField.PIC_SQUARE.toString());
		final Image picture = Utils.imageURL(picSquare, DUMMY_PROFILE_PIC);

		return new Friend(uid, name, picture);
	}

	private List<Long> getFriendsIdsForUpdate(final JSONArray fbfriends) throws JSONException {
		final List<Long> friendsids = new ArrayList<Long>();

		for (int i = 0; i < fbfriends.length(); i++) {
			Long friend = Long.valueOf(fbfriends.getString(i));
			if (!containsFriend(friend)) {
				friendsids.add(friend);
			}

		}
		return friendsids;
	}

	private boolean containsFriend(Long friendID) {
		for (Friend friend : friends) {
			if (friend.getId() == friendID.longValue()) {
				return true;
			}
		}
		return false;
	}

	public List<Long> getFriendsids() {
		List<Long> ids = new ArrayList<Long>();
		for (Friend friend : friends) {
			ids.add(friend.getId());
		}
		return ids;
	}

	public List<Friend> getFriends() {
		return friends;
	}

	public Friend getCurrentPlayer() throws Exception {
		if (facebook == null) {
			log.warn("no facebook, returning dummy user");
			return new Friend(-1, "dummy", DUMMY_PROFILE_PIC);
		}
		
		if (currentPlayer == null) {
			long user = facebook.users_getLoggedInUser();
			List<Long> users = new ArrayList<Long>();
			users.add(user);
			JSONArray jay = (JSONArray) facebook.users_getInfo(users, fields);
			currentPlayer = getFriendFromJSONObject((JSONObject) jay.get(0));
		}
		return currentPlayer;
	}

}