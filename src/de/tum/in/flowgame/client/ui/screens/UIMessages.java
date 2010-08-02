package de.tum.in.flowgame.client.ui.screens;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class UIMessages {
	
	private static final String BUNDLE_NAME = "de.tum.in.flowgame.ui.screens.screens"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	public static final String CONTINUE = getString("continue");
	
	private UIMessages() {
		// allow no instances
	}

	public static String getString(final String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (final MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}