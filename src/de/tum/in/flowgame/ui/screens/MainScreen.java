package de.tum.in.flowgame.ui.screens;

import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;

import com.kitfox.svg.app.beans.SVGIcon;

import de.tum.in.flowgame.ui.GameMenu;
import de.tum.in.flowgame.ui.SVGSprite;

public class MainScreen extends MenuScreen {

	private final JButton play = goTo("Play", QuestionnaireScreen.class);

	private final JButton highscores = goTo("Highscores", HighscoresScreen.class);
	
	private final JButton highscoresChart = goTo("Highscores Chart", HighscoresChartScreen.class);
	
	private final JButton personalHighscore = goTo("Social Highscore", SocialHighscoresScreen.class);

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
		return centered(title, new JLabel("Welcome " + menu.getLogic().getPlayer().getName() + "!"), play, highscores, highscoresChart, personalHighscore, settings, systemInfo);
	}

}
