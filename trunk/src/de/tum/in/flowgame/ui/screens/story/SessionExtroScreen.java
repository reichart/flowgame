package de.tum.in.flowgame.ui.screens.story;

import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;

import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.ui.screens.MainScreen;
import de.tum.in.flowgame.ui.screens.SettingIconsScreen;

/**
 * Displayed after the scenario has no more rounds.
 */
public class SessionExtroScreen extends SettingIconsScreen {

	private final JButton backToMain = new JButton(new AbstractAction("Main Menu") {
		public void actionPerformed(final ActionEvent e) {
			menu.show(MainScreen.class);
		}
	});

	@Override
	public Container getContents() {
		return centered(title("Game Over"), backToMain);
	}

	@Override
	public void update(final GameLogic logic) throws Exception {
		// TODO upload asynchronously
		logic.uploadSession();
	}

}
