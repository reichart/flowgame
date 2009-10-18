package de.tum.in.flowgame;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;

import com.google.code.facebookapi.ProfileField;

import de.tum.in.flowgame.client.Client;
import de.tum.in.flowgame.model.Person;
import de.tum.in.flowgame.util.CustomFacebookClient;

public class GameApplet extends Applet {

	private final Game3D game;

	private CustomFacebookClient facebook;
	
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
		
		// this initializes all the other classes
		
		final long id = 1337;
		final Client client = new Client("http://localhost:8080/flowgame/");
		
		// download person information from server
		Person player = client.downloadPerson(id);
		if (player == null) {
			player = new Person(id);
			final String name = "PLAYER";
			player.setName(name);
			client.uploadQuietly(player);
		}
		
		new GameLogic(player, client).addListener(app.game.getListener());
		
		frame.setVisible(true);
		
		app.start();
	}

	@Override
	public void init() {
		final String server = getCodeBase().toString();
		final String apiKey = getParameter("apiKey");
		final String sessionKey = getParameter("sessionKey");
		final String sessionSecret = getParameter("sessionSecret");
		
		try {
			this.facebook = new CustomFacebookClient(new URL(server + "fbproxy"), apiKey, sessionSecret, sessionKey);
		
			final long id = facebook.users_getLoggedInUser();
			
			final Client client = new Client(server);
			
			// download person information from server
			Person player = client.downloadPerson(id);
			if (player == null) {
				player = new Person(id);
				final String name = facebook.users_getProfileField(id, ProfileField.FIRST_NAME);
				player.setName(name);
				client.uploadQuietly(player);
			}
			
			// this initializes all the other classes
			new GameLogic(player, client).addListener(game.getListener());
			
		} catch (final Exception ex) {
			throw new RuntimeException("Failed to connect to " + server, ex);
		}
	}
	
	public GameApplet() throws IOException {
		this.game = new Game3D();
		setLayout(new BorderLayout());
		add(BorderLayout.CENTER, game);
	}
}
