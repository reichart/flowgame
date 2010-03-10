package de.tum.in.flowgame.ui.screens;

import java.awt.Container;
import java.io.IOException;

import javax.swing.JButton;

import org.json.JSONException;

import com.google.code.facebookapi.FacebookException;

import de.tum.in.flowgame.facebook.FaceBookFriendCash;
import de.tum.in.flowgame.ui.GameMenu;
import de.tum.in.flowgame.ui.SocialHighscore;

public class SocialHighscoresScreen extends MenuScreen {
	
	private final JButton back = goTo("Back", MainScreen.class);
	private SocialHighscore contentPanel;
	
	public SocialHighscoresScreen(final GameMenu menu) throws FacebookException, JSONException, IOException {
		super(menu);
		FaceBookFriendCash friendCash = new FaceBookFriendCash(this.menu.getGameLogic().getFacebookClient());
		contentPanel = new SocialHighscore(this.menu.getGameLogic().getClient(), friendCash);
		add(contentPanel);
	}

	@Override
	public Container getContents() {
		return centered(title("Highscore"), contentPanel, back);
	}
	
	@Override
	public void update() {
		try {
			contentPanel.update();
		} catch (FacebookException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
