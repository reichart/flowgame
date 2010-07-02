package de.tum.in.flowgame.ui.screens;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JEditorPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CreditsScreen extends MenuScreen {
	private static final Log log = LogFactory.getLog(MenuScreen.class);
	
	private final JButton next = new JButton(new AbstractAction("Continue") {

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
		
		return centered(COMMON_BORDER, title("Credits"), credits, next);
	}
	
}
