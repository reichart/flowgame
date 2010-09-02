package de.tum.in.flowgame.client.ui.screens.story;

import java.awt.Font;

import javax.swing.JTextArea;

import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.Utils;
import de.tum.in.flowgame.client.ui.screens.MenuScreen;
import de.tum.in.flowgame.client.ui.screens.UIMessages;

public abstract class StoryScreen extends MenuScreen {

	public final JTextArea text = new JTextArea(UIMessages.getString("Default Text"), 5, 100);
	
	public StoryScreen() {
		super(Utils.imageResource("/res/spacestation.png", null));
		text.setLineWrap(true);
		text.setWrapStyleWord(true);
		text.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
	}
	
	protected abstract String getText();
	
	@Override
	public void update(final GameLogic logic) throws Exception {
		super.update(logic);
		text.setText(getText());
	}
	
}
