package de.tum.in.flowgame.ui.screens;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

import de.tum.in.flowgame.GameLogic;

/**
 * Displays an individual highscore comparing the player to his previous scores.
 */
public class IndividualHighscoresScreen extends HighscoresScreen {

	private final ImageIcon imageIcon;

	public IndividualHighscoresScreen() {
		this.imageIcon = new ImageIcon(new BufferedImage(500, 300, BufferedImage.TYPE_BYTE_BINARY));
	}

	@Override
	protected JComponent getHighscoreComponent() {
		return new JLabel(imageIcon);
	}

	@Override
	public void update(final GameLogic logic) throws IOException {
		final long playerId = logic.getPlayerId();
		final BufferedImage im = logic.getClient().downloadPersonHighscoreChart(playerId);
		imageIcon.setImage(im);
	}
}