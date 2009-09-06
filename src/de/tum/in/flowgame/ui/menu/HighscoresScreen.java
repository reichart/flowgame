package de.tum.in.flowgame.ui.menu;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import de.tum.in.flowgame.ui.GameMenu;

public class HighscoresScreen extends MenuScreen {

	private final JButton back = new JButton(new AbstractAction("Back") {
		@Override
		public void actionPerformed(final ActionEvent e) {
			menu.show(MainScreen.class);
		}
	});
	
	private final JScrollPane highscoresScroll;

	public HighscoresScreen(final GameMenu menu) {
		super(menu);
		
		final Random random = new Random();
		final int max = 1000;
		final Object[][] rowData = {
				{"Peter", max - random.nextInt(max)},
				{"Paul", max - random.nextInt(max)},
				{"Pierre", max - random.nextInt(max)},
				{"Pascal", max - random.nextInt(max)},
				{"Petra", max - random.nextInt(max)},
				{"Pheobe", max - random.nextInt(max)}
		};
		final Object[] columnNames = {"Player", "Points"};
		JTable highscores = new JTable(rowData, columnNames);
		
		highscoresScroll = new JScrollPane(highscores);
		highscores.setOpaque(false);
	}

	@Override
	public Container getContents() {
		return centered(title("Highscore"), highscoresScroll, back);
	}
}
