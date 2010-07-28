package de.tum.in.flowgame.ui.screens.story;

import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;

import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.ui.screens.MainScreen;
import de.tum.in.flowgame.ui.screens.MenuScreen;
import de.tum.in.flowgame.ui.screens.UIMessages;

/**
 * Displayed after the scenario has no more rounds.
 */
public class SessionExtroScreen extends MenuScreen {

	private final JButton backToMain = new JButton(new AbstractAction(UIMessages.getString("game.menu")) {
		public void actionPerformed(final ActionEvent e) {
			menu.show(MainScreen.class);
		}
	});

	@Override
	public Container getContents() {
		return centered(title(UIMessages.getString("game.over")), backToMain);
	}

	@Override
	public void update(final GameLogic logic) throws Exception {
		// TODO upload asynchronously
		logic.uploadSession();
	}

}
