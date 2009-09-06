package de.tum.in.flowgame.ui.menu;

import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;

import de.tum.in.flowgame.ui.GameMenu;

public class GameOverScreen extends MenuScreen {

	private final JButton backToMain = new JButton(new AbstractAction("Main Menu") {
		@Override
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

}
