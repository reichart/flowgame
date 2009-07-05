package de.tum.in.flowgame;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameApplet extends Applet {

	private final Game3D game;

	public static void main(final String[] args) throws Exception {
		final Frame frame = new Frame();
		frame.setSize(800, 600);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				System.exit(0);
			}
		});
		final GameApplet app = new GameApplet();
		frame.add(app);
		frame.setVisible(true);
		
		app.start();
	}

	@Override
	public void init() {
		System.out.println("GameApplet.init()");
	}

	@Override
	public void start() {
		game.start();
	}
	
	@Override
	public void destroy() {
		System.out.println("GameApplet.destroy()");
	}

	public GameApplet() throws Exception {
		System.out.println("GameApplet.GameApplet()");
		setLayout(new BorderLayout());

		this.game = new Game3D();
		add(BorderLayout.CENTER, game);
	}

}
