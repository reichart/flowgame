package de.tum.in.flowgame.ui.screens;

import java.awt.Container;

import javax.swing.JButton;

import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.facebook.FaceBookFriendCash;
import de.tum.in.flowgame.ui.GameMenu;
import de.tum.in.flowgame.ui.SocialHighscore;

public class SocialHighscoresScreen extends MenuScreen {

	private final JButton back = goTo("Back", MainScreen.class);
	private final SocialHighscore contentPanel;

	public SocialHighscoresScreen(final GameMenu menu) {
		super(menu);
		final FaceBookFriendCash friendCash = new FaceBookFriendCash(this.menu.getGameLogic().getFacebookClient());
		contentPanel = new SocialHighscore(this.menu.getGameLogic().getClient(), friendCash);
		add(contentPanel);
	}

	@Override
	public Container getContents() {
		return centered(title("Highscore"), contentPanel, back);
	}

	@Override
	public void update(final GameLogic logic) throws Exception {
		contentPanel.update();
	}
}
