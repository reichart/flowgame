package de.tum.in.flowgame.ui.screens.story;

import java.awt.Container;

import javax.swing.JButton;

import de.tum.in.flowgame.ui.screens.BeforeSessionQuestionnaireScreen;
import de.tum.in.flowgame.ui.screens.MenuScreen;

/**
 * Displayed when going from the main menu to the initial session questionnaire.
 */
public class SessionIntroScreen extends MenuScreen {

	private final JButton next = goTo("Continue", BeforeSessionQuestionnaireScreen.class);

	@Override
	public Container getContents() {
		return centered(title("Session Intro"), next);
	}

}
