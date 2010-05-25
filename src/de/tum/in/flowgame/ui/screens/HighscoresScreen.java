package de.tum.in.flowgame.ui.screens;

import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;

import de.tum.in.flowgame.model.ScenarioRound;
import de.tum.in.flowgame.ui.screens.story.RoundExtroScreen;

/**
 * Abstract base class for displaying highscores.
 */
public abstract class HighscoresScreen extends MenuScreen {

	private final JButton next = new JButton(new AbstractAction("Continue") {

		public void actionPerformed(final ActionEvent e) {
			if (MainScreen.class.equals(menu.getPreviousScreen())) {
				menu.show(MainScreen.class); // go back
			} else {
				// don't show a qn after a baseline round
				final ScenarioRound current = menu.getLogic().getCurrentScenarioRound();
				if (current.isBaselineRound()) {
					menu.show(RoundExtroScreen.class);
				} else {
					menu.show(AfterRoundQuestionnaireScreen.class);
				}
			}
		}
	});

	@Override
	public final Container getContents() {
		return centered(title("Highscore"), getHighscoreComponent(), next);
	}

	/**
	 * Subclasses implement this to provide their actual highscore component.
	 */
	protected abstract JComponent getHighscoreComponent();
}
