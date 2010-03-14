package de.tum.in.flowgame.facebook;

import java.awt.Image;

public class Friend {

	private final long id;
	private final String name;
	private final Image picture;

	public Friend(final long id, final String name, final Image pictureURL) {
		this.id = id;
		this.name = name;
		this.picture = pictureURL;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public Image getPicture() {
		return picture;
	}

	@Override
	public boolean equals(final Object obj) {
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