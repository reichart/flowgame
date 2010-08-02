package de.tum.in.flowgame.ui.screens;

import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;

import de.tum.in.flowgame.ui.GameMenu;

public abstract class HighscoresScreen extends MenuScreen {

	private final JButton next = new JButton(new AbstractAction("Continue") {

		public void actionPerformed(final ActionEvent e) {
			if (MainScreen.class.equals(menu.getPreviousScreen())) {
				menu.show(MainScreen.class); // go back
			} else {
				menu.show(AfterRoundQuestionnaireScreen.class); // go on
			}
		}
	});

	public HighscoresScreen(final GameMenu menu) {
		super(menu);
	}

	@Override
	public final Container getContents() {
		return centered(title("Highscore"), getHighscoreComponent(), next);
	}

	protected abstract JComponent getHighscoreComponent();
}
