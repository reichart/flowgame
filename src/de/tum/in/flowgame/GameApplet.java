package de.tum.in.flowgame;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.code.facebookapi.ProfileField;

import de.tum.in.flowgame.client.Client;
import de.tum.in.flowgame.model.Person;
import de.tum.in.flowgame.util.CustomFacebookClient;

public class GameApplet extends Applet {

	private final Game3D game;

	private final CustomFacebookClient facebook;
	
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
		final String server = getParameter("server");
		final String apiKey = getParameter("apiKey");
		final String sessionKey = getParameter("sessionKey");
		final String sessionSecret = getParameter("sessionSecret");
		
		try {
			this.facebook = new CustomFacebookClient(new URL(server + "fbproxy"), apiKey, sessionSecret, sessionKey);
		} catch (final MalformedURLException ex) {
			throw new RuntimeException("Failed to initialize Facebook client", ex);
		}
		
		final long id = facebook.users_getLoggedInUser();
		
		Client client = new Client(server);
		
		// download person information from server
		Person player = client.downloadPerson(id);
		if (player == null) {
			player = new Person(id);
			final String name = facebook.users_getProfileField(id, ProfileField.FIRST_NAME);
			player.setName(name);
			client.uploadQuietly(player);
		}

		this.game = new Game3D(new GameLogic(player, client));
		
		setLayout(new BorderLayout());
		add(BorderLayout.CENTER, game);
	}


}
