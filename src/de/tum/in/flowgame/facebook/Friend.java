package de.tum.in.flowgame.facebook;

import java.awt.Image;

public class Friend {

	private final long id;
	private final String name;
	private final Image picture;

	public Friend(final long id, final String name, final Image picture) {
		this.id = id;
		this.name = name;
		this.picture = picture;
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
			return id == ((Friend) obj).id;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return (int) id;
	}
}