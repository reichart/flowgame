package de.tum.in.flowgame.client.ui.screens.story;

import java.awt.Container;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.Utils;
import de.tum.in.flowgame.client.ui.screens.MenuScreen;
import de.tum.in.flowgame.client.ui.screens.UIMessages;

public abstract class StoryScreen extends MenuScreen {

	private final JLabel title;
	private final JTextArea text;

	public StoryScreen() {
		super(Utils.imageResource("/res/spacestation.png", null));

		title = title("default title");

		text = new JTextArea("default text", 5, 100);
		text.setLineWrap(true);
		text.setWrapStyleWord(true);
		text.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
	}

	@Override
	public final Container getContents() {
		return centered(COMMON_BORDER, title, text, next(), goTo("reload " + getClass().getSimpleName(), getClass()));
	}

	protected abstract JButton next();

	protected abstract String getTitleKey();

	protected abstract String getTextKey();

	@Override
	public void update(final GameLogic logic) throws Exception {
		title.setText(UIMessages.getString(getTitleKey()));
		text.setText(UIMessages.getString(getTextKey()));
	}
}
