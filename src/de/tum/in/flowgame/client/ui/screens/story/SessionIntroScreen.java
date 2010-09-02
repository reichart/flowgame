package de.tum.in.flowgame.client.ui.screens.story;

import java.awt.Container;

import javax.swing.JButton;

import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.client.ui.screens.BeforeSessionQuestionnaireScreen;
import de.tum.in.flowgame.client.ui.screens.UIMessages;

/**
 * Displayed when going from the main menu to the initial session questionnaire.
 */
public class SessionIntroScreen extends StoryScreen {

	private final JButton next = goTo(UIMessages.CONTINUE, BeforeSessionQuestionnaireScreen.class);
	
	@Override
	public void update(final GameLogic logic) throws Exception {
		super.update(logic);
		title(UIMessages.getString("scenario" + menu.getLogic().getStoryScenario() + ".name"));
		repaint();
	}
	
	@Override
	public Container getContents() {
		return centered(COMMON_BORDER, title("lala"), text, next);
	}

	@Override
	protected String getText() {
		final int storyScenario = menu.getLogic().getStoryScenario();
		return UIMessages.getString("scenario" + storyScenario + ".intro");
	}
	
}