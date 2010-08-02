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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

import com.google.code.facebookapi.ProfileField;

import de.tum.in.flowgame.client.Client;
import de.tum.in.flowgame.engine.Game3D;
import de.tum.in.flowgame.facebook.CustomFacebookClient;
import de.tum.in.flowgame.model.Person;
import de.tum.in.flowgame.ui.screens.MainScreen;
import de.tum.in.flowgame.ui.screens.story.BeforeProfileScreen;
import de.tum.in.flowgame.util.Browser;
import de.tum.in.flowgame.util.JSObjectBrowser;

/**
 * This class starts the Flowgame as {@link Applet} and creates a new
 * {@link CustomFacebookClient} that provides data from facebook.
 */
public class GameApplet extends Applet {

	private final static Log log = LogFactory.getLog(GameApplet.class);
	
	private final Game3D game;
	public static final int WIDTH = 600;
	public static final int HEIGHT = 400;

	private CustomFacebookClient facebook;

	private final static String API_KEY = "df7823f0a4342cd09c7a8ba07a88a449";

	public static void main(final String[] args) throws Exception {
		final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		final Frame frame = new Frame();
		frame.setUndecorated(true);
		frame.setResizable(false);
		frame.setSize(WIDTH, HEIGHT);

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
			log.info("using: applet deployment path");
			server = getCodeBase().toString();
			sessionKey = getParameter("sessionKey");
			sessionSecret = getParameter("sessionSecret");
		} else {
			log.warn("using: local testing path");
			server = "http://localhost:8080/flowgame/";
			sessionSecret = "2957c00dd86887f79b3c4827157ac2ab"; // fb_sig_ss
			sessionKey = "b09011facca373ce59cc53a6-1071363107"; // fb_sig_session_key
		}

		return new CustomFacebookClient(server, API_KEY, sessionSecret, sessionKey, this);
	}

	@Override
	public void init() {
		try {
			SoundsHelper.init(this);
			
			this.facebook = createFacebookClient();

			final Client client = new Client(facebook.getServer());
			final Person player = getPlayer(client);
			final Browser browser = JSObjectBrowser.from(this);
			
			// this initializes all the other classes
			new GameLogic(player, client, facebook, browser).addListener(game.getListener());

			if (player.isNewPlayer()) {
				game.getMenu().show(BeforeProfileScreen.class);
			} else {
				game.getMenu().show(MainScreen.class);
			}

		} catch (final Exception ex) {
			throw new RuntimeException("Failed to connect to " + facebook.getServer(), ex);
		}
	}

	private Person getPlayer(final Client client) {
		Person player;
		try {
			final long loggedInUser = facebook.users_getLoggedInUser();

			// download person information from server
			player = client.downloadPerson(loggedInUser);
			if (player == null) {
				log.info("creating new player");

				final JSONObject userInfo = facebook.users_getInfo(loggedInUser, ProfileField.FIRST_NAME,
						ProfileField.BIRTHDAY_DATE, ProfileField.SEX, ProfileField.HOMETOWN_LOCATION);

				try {
					final String name = userInfo.getString(ProfileField.FIRST_NAME.fieldName());
					final String birthday = userInfo.getString(ProfileField.BIRTHDAY_DATE.fieldName());
					final String sex = userInfo.getString(ProfileField.SEX.fieldName());

					final String country;
					if (userInfo.isNull(ProfileField.HOMETOWN_LOCATION.fieldName())) {
						country = null;
					} else {
						country = userInfo.getJSONObject(ProfileField.HOMETOWN_LOCATION.fieldName()).getString(
								"country");
					}

					player = new Person(loggedInUser, name);
					player.setDateOfBirth(new SimpleDateFormat("MM/dd/yyyy").parse(birthday));
					player.setPlace(country);
					player.setSex(sex);
				} catch (final Exception ex) {
					log.error(userInfo, ex);
					throw ex;
				}

				log.info("created new player: " + player);
			} else {
				log.info("existing player: " + player);
			}
		} catch (final Exception e) {
			log.error("failed to connect to facebook, using dummy user in offline mode");
			player = new Person(-1, "dummy");
		}
		return player;
	}
}
