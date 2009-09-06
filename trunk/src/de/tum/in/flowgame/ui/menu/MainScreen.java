package de.tum.in.flowgame.ui.menu;

import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;

import de.tum.in.flowgame.ui.GameMenu;

public class MainScreen extends MenuScreen {

	final JButton play = new JButton(new AbstractAction("Play") {
		@Override
		public void actionPerformed(final ActionEvent e) {
			System.out.println("MainMenu.play.new AbstractAction() {...}.actionPerformed()");
			menu.getLogic().start();
		}
	});

	final JButton highscores = new JButton(new AbstractAction("Highscores") {
		@Override
		public void actionPerformed(final ActionEvent e) {
			System.out.println("MainMenu.highscores.new AbstractAction() {...}.actionPerformed()");
			menu.show(HighscoresScreen.class);
		}
	});

	final JButton settings = new JButton("Settings");

	public MainScreen(final GameMenu menu) {
		super(menu);
	}

	@Override
	public Container getContents() {
		return centered(title("Flowgame"), play, highscores, settings);
	}

}
