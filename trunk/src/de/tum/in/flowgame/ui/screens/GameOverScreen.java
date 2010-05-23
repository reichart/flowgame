package de.tum.in.flowgame.ui.screens;

import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;

import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.ui.GameMenu;

/**
 * Displayed after the scenario has no more rounds.
 */
public class GameOverScreen extends MenuScreen {

	private final JButton backToMain = new JButton(new AbstractAction("Main Menu") {
		public void actionPerformed(final ActionEvent e) {
			menu.show(MainScreen.class);
		}
	});

	public GameOverScreen(final GameMenu menu) {
		super(menu);
	}

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
