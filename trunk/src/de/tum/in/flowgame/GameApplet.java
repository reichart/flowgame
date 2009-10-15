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

	public GameApplet() throws Exception {
		//TODO: get id from facebook
		final String sid = JOptionPane.showInputDialog("Bitte id eingeben");
		final Long id = Long.decode(sid);
		
		final Client client = new Client("INSERT SERVER HERE");
		
		// download person information from server
		Person player = client.downloadPerson(id);
		if (player == null) {
			player = new Person(id);
			String name = JOptionPane.showInputDialog("Bitte Name eingeben");
			player.setName(name);
			client.uploadQuietly(player);
		}

		this.game = new Game3D(new GameLogic(player, client));
		
		setLayout(new BorderLayout());
		add(BorderLayout.CENTER, game);
	}

}
