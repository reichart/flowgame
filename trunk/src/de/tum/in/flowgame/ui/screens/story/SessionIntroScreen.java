package de.tum.in.flowgame.ui.screens.story;

import java.awt.Container;

import javax.swing.JButton;

import de.tum.in.flowgame.ui.screens.BeforeSessionQuestionnaireScreen;
import de.tum.in.flowgame.ui.screens.MenuScreen;
import de.tum.in.flowgame.ui.screens.UIMessages;

/**
 * Displayed when going from the main menu to the initial session questionnaire.
 */
public class SessionIntroScreen extends MenuScreen {

	private final JButton next = goTo(UIMessages.CONTINUE, BeforeSessionQuestionnaireScreen.class);

	@Override
	public Container getContents() {
		return centered(title(UIMessages.getString("session.intro")), next);
	}

}
