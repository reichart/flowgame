package de.tum.in.flowgame.ui.screens;

import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;

/**
 * Displayed when the game is paused.
 */
public class PauseScreen extends SettingIconsScreen {

	private final JButton continueButton = new JButton(new AbstractAction("Continue") {
		public void actionPerformed(final ActionEvent e) {
			menu.getLogic().unpause();
		}
	});

	@Override
	public Container getContents() {
		return centered(title("Pause"), continueButton);
	}

}
