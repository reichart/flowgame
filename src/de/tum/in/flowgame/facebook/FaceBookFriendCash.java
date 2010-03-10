package de.tum.in.flowgame.facebook;

import java.awt.Image;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.code.facebookapi.FacebookException;
import com.google.code.facebookapi.ProfileField;

import de.tum.in.flowgame.util.CustomFacebookClient;

public class FaceBookFriendCash {

	public class Friend {
		private long id;
		private String name;
		private Image picture;

		public Friend(long id, String name, Image pictureURL) {
			this.id = id;
			this.name = name;
			this.picture = pictureURL;
		}

		public long getId() {
			return id;
		}

		public Image getPicture() {
			return picture;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Friend) {
				if (id == ((Friend) obj).id) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}

		@Override
		public int hashCode() {
			return Long.valueOf(id).hashCode();
		}
	}

	private final CustomFacebookClient facebook;
	private final List<Friend> friends;

	private final Collection<ProfileField> fields;

	private final Friend currentPlayer;

	public FaceBookFriendCash(CustomFacebookClient facebook) throws FacebookException, NumberFormatException,
			JSONException, MalformedURLException, IOException {
		this.facebook = facebook;
		this.friends = new ArrayList<Friend>();

		this.fields = new ArrayList<ProfileField>();
		fields.add(ProfileField.UID);
		fields.add(ProfileField.NAME);
		fields.add(ProfileField.IS_APP_USER);
		fields.add(ProfileField.PIC_SQUARE);

		currentPlayer = getCurrentPlayer();
	}

	public void updateFriends() throws FacebookException, JSONException, IOException {
		final JSONArray fbfriends = facebook.friends_get();

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

	private Friend getFriendFromJSONObject(JSONObject info) throws JSONException, MalformedURLException, IOException {
		long uid = Long.valueOf(info.getString(ProfileField.UID.toString()));
		String name = info.getString(ProfileField.NAME.toString());
		String pictureURL = info.getString(ProfileField.PIC_SQUARE.toString());
		Image picture = ImageIO.read(new URL(pictureURL));

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

	public Friend getCurrentPlayer() throws FacebookException, JSONException, MalformedURLException, IOException {
		if (currentPlayer == null) {
			long user = facebook.users_getLoggedInUser();
			List<Long> users = new ArrayList<Long>();
			users.add(user);
			JSONArray jay = (JSONArray) facebook.users_getInfo(users, fields);
			return getFriendFromJSONObject((JSONObject) jay.get(0));
		} else {
			return currentPlayer;
		}
	}

}