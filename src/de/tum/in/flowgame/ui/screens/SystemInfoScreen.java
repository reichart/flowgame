package de.tum.in.flowgame.ui.screens;

import java.awt.Container;
import java.awt.Dimension;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import de.tum.in.flowgame.engine.SystemInfo;
import de.tum.in.flowgame.ui.GameMenu;

public class SystemInfoScreen extends MenuScreen {

	private final JButton back = goTo("Back", MainScreen.class);

	private final JScrollPane systemInfoScroll;

	public SystemInfoScreen(final GameMenu menu) {
		super(menu);

		final StringWriter stringWriter = new StringWriter();
		SystemInfo.getSystemInfo(new PrintWriter(stringWriter), menu.getGame());

		final JTextArea systemInfo = new JTextArea(stringWriter.toString());
		systemInfo.setEditable(true);
		systemInfo.setWrapStyleWord(true);
		systemInfo.setLineWrap(true);

		this.systemInfoScroll = new JScrollPane(systemInfo,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		systemInfoScroll.setMaximumSize(new Dimension(300, 200));
	}

	@Override
	public Container getContents() {
		return centered(title("System Information"), systemInfoScroll, back);
	}
}
