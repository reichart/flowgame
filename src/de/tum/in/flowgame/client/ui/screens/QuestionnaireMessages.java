package de.tum.in.flowgame.client.ui.screens;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import de.tum.in.flowgame.model.Question;
import de.tum.in.flowgame.model.Questionnaire;

public class QuestionnaireMessages {
	private static final String BUNDLE_NAME = QuestionnaireMessages.class.getPackage().getName() + ".questionnaires"; //$NON-NLS-1$

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
	
	/**
	 * @return the localized text for a question.
	 */
	public static String getText(final Questionnaire qn, final Question q) {
		return getString(qn.getName() + q.getNumber());
	}
}