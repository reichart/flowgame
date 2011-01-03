package de.tum.in.flowgame.client.ui.screens;

import javax.swing.JComponent;

import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.client.facebook.FacebookFriendCache;
import de.tum.in.flowgame.client.ui.SocialHighscore;

/**
 * Displays a social highscore comparing the player to his friends.
 */
public class SocialHighscoresScreen extends HighscoresScreen {

	private final SocialHighscore contentPanel;

	public SocialHighscoresScreen() {
		final FacebookFriendCache friendCache = new FacebookFriendCache(menu.getLogic().getFacebookClient());
		contentPanel = new SocialHighscore(friendCache, menu.getLogic());
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