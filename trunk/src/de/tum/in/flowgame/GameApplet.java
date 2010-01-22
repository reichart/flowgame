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
import java.text.SimpleDateFormat;

import org.json.JSONObject;

import com.google.code.facebookapi.ProfileField;

import de.tum.in.flowgame.client.Client;
import de.tum.in.flowgame.model.Person;
import de.tum.in.flowgame.ui.screens.MainScreen;
import de.tum.in.flowgame.ui.screens.ProfileScreen;
import de.tum.in.flowgame.util.CustomFacebookClient;

/**
 * This class starts the Flowgame as {@link Applet} and creates a new
 * {@link CustomFacebookClient} that provides data from facebook.
 */
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
		
		app.init();
		
		frame.setVisible(true);
		
		app.start();
	}

	@Override
	public void init() {
		// TODO refactor this for local testability
		final String server = getCodeBase().toString();
		final String apiKey = getParameter("apiKey");
		final String sessionKey = getParameter("sessionKey");
		final String sessionSecret = getParameter("sessionSecret");
		
		try {
			final URL serverUrl = new URL(server + "fbproxy");
			this.facebook = new CustomFacebookClient(serverUrl, apiKey, sessionSecret, sessionKey);
		
			final long loggedInUser = facebook.users_getLoggedInUser();
			
			final Client client = new Client(server);
			
			final boolean newPlayer;
			
			// download person information from server
			Person player = client.downloadPerson(loggedInUser);
			if (player == null) {
				newPlayer = true;
				
				System.err.println("##### creating new player");
				player = new Person(loggedInUser);
				
				final JSONObject userInfo = facebook.users_getInfo(loggedInUser, ProfileField.FIRST_NAME,
						ProfileField.BIRTHDAY_DATE, ProfileField.SEX, ProfileField.HOMETOWN_LOCATION);

				player.setName(userInfo.getString("first_name"));
				player.setSex(userInfo.getString("sex"));
				final SimpleDateFormat fmt = new SimpleDateFormat("MM/dd/yyyy");
				player.setDateOfBirth(fmt.parse(userInfo.getString("birthday_date")));
				player.setPlace(userInfo.getJSONObject("hometown_location").getString("country"));
				
				client.uploadQuietly(player);
				System.err.println("##### stored new player");
			} else {
				newPlayer = false;
				System.err.println("##### existing player");
			}
			
			// this initializes all the other classes
			new GameLogic(player, client).addListener(game.getListener());
			
			if(newPlayer) {
				game.getMenu().show(ProfileScreen.class);
			} else {
				game.getMenu().show(MainScreen.class);
			}
			
		} catch (final Exception ex) {
			throw new RuntimeException("Failed to connect to " + server, ex);
		}
	}
	
	/**
	 * Creates a new GameApplet.
	 * 
	 * @throws IOException
	 */
	public GameApplet() throws IOException {
		this.game = new Game3D();
		setLayout(new BorderLayout());
		add(BorderLayout.CENTER, game);
	}
}
