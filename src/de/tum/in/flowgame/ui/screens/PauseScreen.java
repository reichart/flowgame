package de.tum.in.flowgame.ui.screens;

import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;

import de.tum.in.flowgame.ui.GameMenu;

/**
 * Displayed when the game is paused.
 */
public class PauseScreen extends MenuScreen {

	private final JButton continueButton = new JButton(new AbstractAction("Continue") {
		public void actionPerformed(final ActionEvent e) {
			menu.getLogic().unpause();
		}
	});

	public PauseScreen(final GameMenu menu) {
		super(menu);
	}

	@Override
	public Container getContents() {
		return centered(title("Pause"), continueButton);
	}

}
