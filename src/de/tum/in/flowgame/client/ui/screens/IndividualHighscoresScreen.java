package de.tum.in.flowgame.client.ui.screens;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Set;
import java.util.SortedSet;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.client.Diagram;
import de.tum.in.flowgame.model.Score;

/**
 * Displays an chart of all individual highscores showing the player his actual
 * and previous scores.
 */
public class IndividualHighscoresScreen extends HighscoresScreen {

	private final ImageIcon imageIcon;

	public IndividualHighscoresScreen() {
		this.imageIcon = new ImageIcon(new BufferedImage(500, 250, BufferedImage.TYPE_BYTE_BINARY));
	}

	@Override
	protected JComponent getHighscoreComponent() {
		return new JLabel(imageIcon);
	}

	@Override
	public void update(final GameLogic logic) throws IOException {
		final long playerId = logic.getPlayerId();
		final SortedSet<Score> scores = logic.getClient().downloadPersonHighscoreSet(playerId);
		if (logic.getCurrentGameSession() != null) {
			Set<Score> currentScores = logic.getCurrentGameSession().getRoundScores();
			for (Score score : currentScores) {
				scores.add(score);	
			}
		}
		imageIcon.setImage(new Diagram(scores).diagram());
	}
}