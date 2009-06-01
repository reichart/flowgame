package de.tum.in.flowgame;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameApplet extends Applet {

	public static void main(final String[] args) throws Exception {
		final Frame frame = new Frame();
		frame.setSize(800, 600);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				System.exit(0);
			}
		});
		frame.add(new GameApplet());
		frame.setVisible(true);
	}

	@Override
	public void init() {
		System.out.println("GameApplet.init()");
	}

	@Override
	public void destroy() {
		System.out.println("GameApplet.destroy()");
	}

	public GameApplet() throws Exception {
		System.out.println("GameApplet.GameApplet()");
		setLayout(new BorderLayout());

		add(BorderLayout.CENTER, new Game3D());
	}

}
