package de.tum.in.flowgame.ui.screens;

import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;

import de.tum.in.flowgame.ui.GameMenu;

public class MainScreen extends MenuScreen {

	private final JButton play = new JButton(new AbstractAction("Play") {
		@Override
		public void actionPerformed(final ActionEvent e) {
			menu.getLogic().start();
		}
	});

	private final JButton highscores = new JButton(new AbstractAction("Highscores") {
		@Override
		public void actionPerformed(final ActionEvent e) {
			menu.show(HighscoresScreen.class);
		}
	});

	private final JButton settings = new JButton(new AbstractAction("Settings") {
		@Override
		public void actionPerformed(final ActionEvent e) {
			menu.show(SettingsScreen.class);
		}
	});

	public MainScreen(final GameMenu menu) {
		super(menu);
	}

	@Override
	public Container getContents() {
		return centered(title("Flowgame"), play, highscores, settings);
	}

}
