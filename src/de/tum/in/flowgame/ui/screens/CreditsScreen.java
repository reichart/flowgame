package de.tum.in.flowgame.ui.screens;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CreditsScreen extends MenuScreen {
	private static final Log log = LogFactory.getLog(MenuScreen.class);
	
	private final JButton next = new JButton(new AbstractAction(UIMessages.CONTINUE) {

		public void actionPerformed(final ActionEvent e) {
			menu.show(MainScreen.class); // go back
		}
	});
	
	@Override
	public Container getContents() {
		JEditorPane credits;
		try {
			credits = new JEditorPane(getClass().getResource("/res/credits.html"));
		} catch (final IOException e) {
			log.error("Credits file not found");
			credits = new JEditorPane();
		}

		final JScrollPane scrollpane = new JScrollPane(credits, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		return centered(COMMON_BORDER, title(UIMessages.getString("credits")), scrollpane, next);
	}
	
}
