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

	private final JButton play = goTo(UIMessages.getString("game.play"), SessionIntroScreen.class);

	private final JButton individualHighscore = goTo(UIMessages.getString("highscore.individual"), IndividualHighscoresScreen.class);
	
	private final JButton socialHighscore = goTo(UIMessages.getString("highscore.social"), SocialHighscoresScreen.class);

	private final JButton systemInfo = goTo(UIMessages.getString("info"), SystemInfoScreen.class);
	
	private final JButton credits = goTo(UIMessages.getString("credits"), CreditsScreen.class);

	private final JLabel title;
	
	public MainScreen() {
		title = new JLabel(new SVGSprite("/res/flowspace.svg").getIcon(480, 96));
	}

	@Override
	public Container getContents() {
		return centered(title, play, individualHighscore, socialHighscore, systemInfo, credits);
	}

}
