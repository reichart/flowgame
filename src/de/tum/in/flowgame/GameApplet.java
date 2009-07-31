package de.tum.in.flowgame;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import de.tum.in.flowgame.model.Person;

public class GameApplet extends Applet {

	private final Game3D game;

	public static void main(final String[] args) throws Exception {
		final Frame frame = new Frame();
		frame.setSize(800, 600);
//		frame.setUndecorated(true);
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

		//TODO: make player accessible via httpclient
//		String name = JOptionPane.showInputDialog("Bitte Name eingeben");
//		PersonDAO pdao = new PersonDAOImpl();
//		Person player = null;
//		for (Person person : pdao.findAll()) {
//			if (name.equals(person.getName())) {
//				player = person;
//			}
//		}
//		if (player == null) {
//			//TODO: take facebook id
//			player = new Person(1234L);
//			player.setName(name);
//			pdao.create(player);
//		}
		Person player = new Person(123456L);
		this.game = new Game3D(new GameLogic(player));
		add(BorderLayout.CENTER, game);
	}

}
