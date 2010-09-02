package de.tum.in.flowgame.client.ui.screens.story;

import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;

import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.client.ui.screens.MainScreen;
import de.tum.in.flowgame.client.ui.screens.UIMessages;

/**
 * Displayed after the scenario has no more rounds.
 */
public class SessionExtroScreen extends StoryScreen {

	private final JButton backToMain = new JButton(new AbstractAction(UIMessages.getString("game.menu")) {
		public void actionPerformed(final ActionEvent e) {
			menu.getLogic().selectNewStory();
			menu.show(MainScreen.class);
		}
	});	

	@Override
	public Container getContents() {
		return centered(COMMON_BORDER, title(UIMessages.getString("game.over")), text, backToMain);
	}

	@Override
	public void update(final GameLogic logic) throws Exception {
		super.update(logic);
		logic.uploadSession();
	}

	@Override
	protected String getText() {
		final int storyScenario = menu.getLogic().getStoryScenario();
		return UIMessages.getString("scenario" + storyScenario + ".extro");
	}

}