package de.tum.in.flowgame.ui.screens;

import java.awt.Container;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import de.tum.in.flowgame.ui.GameMenu;

public class HighscoresChartScreen extends MenuScreen {
	
    private ImageIcon imageIcon = new ImageIcon(new BufferedImage(500, 300, BufferedImage.TYPE_4BYTE_ABGR));
    private JLabel imageLabel = new JLabel(imageIcon);
	
	private final JButton back = goTo("Back", MainScreen.class);
	
	public HighscoresChartScreen(final GameMenu menu) {
		super(menu);
	}

	@Override
	public Container getContents() {
		return centered(title("Highscore"), imageLabel, back);
	}
	
	@Override
	public void update(){
		long playerId = this.menu.getGameLogic().getPlayer().getId();
		BufferedImage im = null;
		try {
			im = this.menu.getGameLogic().getClient().downloadPersonHighscoreChart(playerId);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		imageIcon.setImage(im);
	}
}
