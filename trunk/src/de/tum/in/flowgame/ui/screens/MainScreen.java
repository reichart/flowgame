package de.tum.in.flowgame.ui.screens;

import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;

import com.kitfox.svg.app.beans.SVGIcon;

import de.tum.in.flowgame.ui.GameMenu;
import de.tum.in.flowgame.ui.sprite.SVGSprite;

/**
 * Displays the main menu.
 */
public class MainScreen extends MenuScreen {

	private final JButton play = goTo("Play", BeforeSessionQuestionnaireScreen.class);

	private final JButton individualHighscore = goTo("Individual Highscore", IndividualHighscoresScreen.class);
	
	private final JButton socialHighscore = goTo("Social Highscore", SocialHighscoresScreen.class);

	private final JButton settings = goTo("Settings", SettingsScreen.class);

	private final JButton systemInfo = goTo("System Info", SystemInfoScreen.class);

	private final JLabel title;
	
	public MainScreen(final GameMenu menu) {
		super(menu);
		final SVGIcon icon = new SVGSprite("/res/flowspace.svg").getIcon();
		icon.setPreferredSize(new Dimension(480, 96));
		title = new JLabel(icon);
	}

	@Override
	public Container getContents() {
		return centered(title, play, individualHighscore, socialHighscore, settings, systemInfo);
	}

}
