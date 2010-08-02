package de.tum.in.flowgame.client.util;

public interface Browser {

	/**
	 * Toggles the visibility of the invite dialog.
	 */
	void toggleInviteDialog();

	/**
	 * Sets a cookie in the browser.
	 * 
	 * @param name
	 *            the name of the cookie
	 * @param value
	 *            the value of the cookie
	 */
	void setCookie(String name, String value);

	/**
	 * Gets the value of a cookie.
	 * 
	 * @param name
	 *            the name of the cookie
	 * @return the value of the cookie or <code>null</code> if no cookie with
	 *         the specified name exists
	 */
	String getCookie(String name);

}
