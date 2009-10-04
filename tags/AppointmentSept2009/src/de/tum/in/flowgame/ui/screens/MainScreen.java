package de.tum.in.flowgame.ui.screens;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;

import com.kitfox.svg.app.beans.SVGIcon;

import de.tum.in.flowgame.ui.GameMenu;
import de.tum.in.flowgame.ui.SVGSprite;

public class MainScreen extends MenuScreen {

	private final JButton play = new JButton(new AbstractAction("Play") {
		@Override
		public void actionPerformed(final ActionEvent e) {
			menu.show(QuestionnaireScreen.class);
		}
	});

	private final JButton highscores = new JButton(new AbstractAction("Highscores") {
		@Override
		public void actionPerformed(final ActionEvent e) {
			menu.show(HighscoresScreen.class);
		}
	});

	private final JButton settings = new JButton(new AbstractAction("Settings") {
		@Override
		public void actionPerformed(final ActionEvent e) {
			menu.show(SettingsScreen.class);
		}
	});

	private final JLabel title;
	
	public MainScreen(final GameMenu menu) {
		super(menu);
		final SVGIcon icon = new SVGSprite("/res/flowspace.svg").getIcon();
		icon.setPreferredSize(new Dimension(480, 96));
		title = new JLabel(icon);
	}

	@Override
	public Container getContents() {
		return centered(title, new JLabel("Welcome " + menu.getLogic().getPlayer().getName() + "!"), play, highscores, settings);
	}

}
