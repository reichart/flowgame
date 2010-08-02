package de.tum.in.flowgame.client.ui.screens;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class QuestionnaireMessages {
	private static final String BUNDLE_NAME = "de.tum.in.flowgame.ui.screens.questionnaires"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private QuestionnaireMessages() {
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