package de.tum.in.flowgame.ui.screens;

import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JLabel;

import de.tum.in.flowgame.ui.screens.story.SessionIntroScreen;
import de.tum.in.flowgame.ui.sprite.SVGSprite;

/**
 * Displays the main menu.
 */
public class MainScreen extends MenuScreen {

	private final JButton play = goTo("Play", SessionIntroScreen.class);

	private final JButton individualHighscore = goTo("Individual Highscore", IndividualHighscoresScreen.class);
	
	private final JButton socialHighscore = goTo("Social Highscore", SocialHighscoresScreen.class);

	private final JButton settings = goTo("Settings", SettingsScreen.class);

	private final JButton systemInfo = goTo("System Info", SystemInfoScreen.class);

	private final JLabel title;
	
	public MainScreen() {
		title = new JLabel(new SVGSprite("/res/flowspace.svg").getIcon(480, 96));
	}

	@Override
	public Container getContents() {
		return centered(title, play, individualHighscore, socialHighscore, settings, systemInfo);
	}

}
