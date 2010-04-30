package de.tum.in.flowgame.ui.screens;

import javax.swing.JComponent;

import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.facebook.FacebookFriendCache;
import de.tum.in.flowgame.ui.GameMenu;
import de.tum.in.flowgame.ui.SocialHighscore;

public class SocialHighscoresScreen extends HighscoresScreen {

	private final SocialHighscore contentPanel;

	public SocialHighscoresScreen(final GameMenu menu) {
		super(menu);
		final FacebookFriendCache friendCash = new FacebookFriendCache(this.menu.getLogic().getFacebookClient());
		contentPanel = new SocialHighscore(this.menu.getLogic().getClient(), friendCash, this.menu.getLogic().getWin());
	}

	@Override
	protected JComponent getHighscoreComponent() {
		return contentPanel;
	}

	@Override
	public void update(final GameLogic logic) throws Exception {
		contentPanel.update();
	}
	
}