package de.tum.in.flowgame.ui.screens;

import java.awt.Container;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.ui.GameMenu;

public class IndividualHighscoresScreen extends MenuScreen {

	private final ImageIcon imageIcon;

	private final JButton back = goTo("Back", MainScreen.class);

	public IndividualHighscoresScreen(final GameMenu menu) {
		super(menu);
		this.imageIcon = new ImageIcon(new BufferedImage(500, 300, BufferedImage.TYPE_BYTE_BINARY));
	}

	@Override
	public Container getContents() {
		return centered(title("Highscore"), new JLabel(imageIcon), back);
	}

	@Override
	public void update(final GameLogic logic) throws IOException {
		final long playerId = logic.getPlayerId();
		final BufferedImage im = logic.getClient().downloadPersonHighscoreChart(playerId);
		imageIcon.setImage(im);
	}
}
