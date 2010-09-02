package de.tum.in.flowgame.client.ui.screens.story;

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

	@Override
	protected JButton next() {
		return new JButton(new AbstractAction(UIMessages.getString("game.menu")) {
			public void actionPerformed(final ActionEvent e) {
				menu.getLogic().selectNewStory();
				menu.show(MainScreen.class);
			}
		});
	}

	@Override
	protected String getTitleKey() {
		return "game.over";
	}

	@Override
	protected String getTextKey() {
		return "scenario" + menu.getLogic().getStoryScenario() + ".extro";
	}

	@Override
	public void update(final GameLogic logic) throws Exception {
		super.update(logic);
		logic.uploadSession();
	}

}