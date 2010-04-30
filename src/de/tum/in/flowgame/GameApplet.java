package de.tum.in.flowgame;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.imageio.ImageIO;

import netscape.javascript.JSObject;

import org.json.JSONObject;

import com.google.code.facebookapi.ProfileField;

import de.tum.in.flowgame.client.Client;
import de.tum.in.flowgame.engine.Game3D;
import de.tum.in.flowgame.facebook.CustomFacebookClient;
import de.tum.in.flowgame.model.Person;
import de.tum.in.flowgame.ui.screens.MainScreen;
import de.tum.in.flowgame.ui.screens.ProfileScreen;

/**
 * This class starts the Flowgame as {@link Applet} and creates a new
 * {@link CustomFacebookClient} that provides data from facebook.
 */
public class GameApplet extends Applet {

	private final Game3D game;

	private CustomFacebookClient facebook;

	private final static String API_KEY = "df7823f0a4342cd09c7a8ba07a88a449";

	public static void main(final String[] args) throws Exception {
		final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		final Frame frame = new Frame();
		frame.setUndecorated(true);
		frame.setResizable(false);
		frame.setSize(600, 400);

		final int centerX = (screen.width - frame.getWidth()) / 2;
		final int centerY = (screen.height - frame.getHeight()) / 2;
		frame.setLocation(centerX, centerY);

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

	public GameApplet() throws IOException {
		ImageIO.setUseCache(false); // stay in JWS sandbox
		
		this.game = new Game3D();
		setLayout(new BorderLayout());
		add(BorderLayout.CENTER, game);
	}
	
	public CustomFacebookClient createFacebookClient() throws IOException {
		final String server;
		final String sessionSecret;
		final String sessionKey;

		if (isActive()) {
			System.err.println("using: applet deployment path");
			server = getCodeBase().toString();
			sessionKey = getParameter("sessionKey");
			sessionSecret = getParameter("sessionSecret");
		} else {
			System.err.println("using: local testing path");
			server = "http://localhost:8080/flowgame/";
			sessionSecret = "2957c00dd86887f79b3c4827157ac2ab"; // fb_sig_ss
			sessionKey = "b09011facca373ce59cc53a6-1071363107"; // fb_sig_session_key
		}

		return new CustomFacebookClient(server, API_KEY, sessionSecret, sessionKey, this);
	}

	@Override
	public void init() {
		try {
			this.facebook = createFacebookClient();

			final long loggedInUser = facebook.users_getLoggedInUser();

			final Client client = new Client(facebook.getServer());

			final boolean newPlayer;

			// download person information from server
			Person player = client.downloadPerson(loggedInUser);
			if (player == null) {
				newPlayer = true;

				System.err.println("##### creating new player");

				final JSONObject userInfo = facebook.users_getInfo(loggedInUser, ProfileField.FIRST_NAME,
						ProfileField.BIRTHDAY_DATE, ProfileField.SEX, ProfileField.HOMETOWN_LOCATION);

				try {
					final String name = userInfo.getString(ProfileField.FIRST_NAME.fieldName());
					final String birthday = userInfo.getString(ProfileField.BIRTHDAY_DATE.fieldName());
					final String sex = userInfo.getString(ProfileField.SEX.fieldName());

					final String country;
					if (userInfo.isNull("hometown_location")) {
						country = null;
					} else {
						country = userInfo.getJSONObject(ProfileField.HOMETOWN_LOCATION.fieldName()).getString(
								"country");
					}

					player = new Person(loggedInUser, name);
					player.setDateOfBirth(new SimpleDateFormat("MM/dd/yyyy").parse(birthday));
					player.setPlace(country);
					player.setSex(sex);
				} catch (Exception e) {
					System.err.println(userInfo);
					throw e;
				}

				System.err.println("##### created new player: " + player);
			} else {
				newPlayer = false;
				System.err.println("##### existing player: " + player);
			}

			JSObject win = null;
			if (isActive()) {
				win = JSObject.getWindow(this);
			}
			
			// this initializes all the other classes
			new GameLogic(player, client, facebook, win).addListener(game.getListener());

			if (newPlayer) {
				game.getMenu().show(ProfileScreen.class);
			} else {
				game.getMenu().show(MainScreen.class);
			}

		} catch (final Exception ex) {
			throw new RuntimeException("Failed to connect to " + facebook.getServer(), ex);
		}
	}
}
