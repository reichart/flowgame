package de.tum.in.flowgame.ui.screens;

import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;

import de.tum.in.flowgame.ui.GameMenu;

public class PauseScreen extends MenuScreen {

	private final JButton continueButton = new JButton(new AbstractAction("Continue") {
		@Override
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
