package de.tum.in.flowgame.client.ui.screens.story;

import javax.swing.JButton;

import de.tum.in.flowgame.client.ui.screens.BeforeSessionQuestionnaireScreen;
import de.tum.in.flowgame.client.ui.screens.UIMessages;

/**
 * Displayed when going from the main menu to the initial session questionnaire.
 */
public class SessionIntroScreen extends StoryScreen {

	@Override
	protected JButton next() {
		return goTo(UIMessages.CONTINUE, BeforeSessionQuestionnaireScreen.class);
	}

	@Override
	protected String getTitleKey() {
		return "scenario" + menu.getLogic().getStoryScenario() + ".name";
	}

	@Override
	protected String getTextKey() {
		return "scenario" + menu.getLogic().getStoryScenario() + ".intro";
	}

}