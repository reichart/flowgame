package de.tum.in.flowgame;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

import de.tum.in.flowgame.client.Client;
import de.tum.in.flowgame.client.DownloadPerson;
import de.tum.in.flowgame.model.Person;

public class GameApplet extends Applet {

	private final Game3D game;

	public static void main(final String[] args) throws Exception {
		final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		final Frame frame = new Frame();
		frame.setUndecorated(true);
		frame.setResizable(false);
		frame.setSize(600, 400);
		
		final int centerX = (screen.width - frame.getWidth()) / 2;
		final int centerY = (screen.height - frame.getHeight()) / 2;
		frame.setLocation(centerX , centerY);
		
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
	public void destroy() {
		System.out.println("GameApplet.destroy()");
	}

	public GameApplet() throws Exception {
		System.out.println("GameApplet.GameApplet()");
		setLayout(new BorderLayout());

		//TODO: get id from facebook
		String sid = JOptionPane.showInputDialog("Bitte id eingeben");
		Long id = Long.decode(sid);
		
		//download person information from server
		DownloadPerson dp = new DownloadPerson();
		Person player = dp.download(id);
	
		//player did not exist, create new profile
		if (player == null) {
			//TODO: take information from facebook
			player = new Person(id);
			String name = JOptionPane.showInputDialog("Bitte Name eingeben");
			player.setName(name);
			Client client = new Client();
			client.updatePerson(player);
		}

		this.game = new Game3D(new GameLogic(player));
		add(BorderLayout.CENTER, game);
	}

}
