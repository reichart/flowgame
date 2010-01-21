package de.tum.in.flowgame.ui.screens;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import de.tum.in.flowgame.ui.GameMenu;

public class HighscoresScreen extends MenuScreen {
	
	/*
	 * * Center the text in Table Cells
	 */
	private class CenterRenderer extends DefaultTableCellRenderer {
		public CenterRenderer() {
			setHorizontalAlignment(SwingConstants.CENTER);
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			return this;
		}
	}
	
	private final JButton back = goTo("Back", MainScreen.class);
	
	private final JScrollPane highscoresScroll;
	
	private JTable highscores;

	public HighscoresScreen(final GameMenu menu) {
		super(menu);
		
		final Random random = new Random();
		final int max = 100000;
		final Object[][] rowData = {
				{"Peter", max - random.nextInt(max)},
				{"Paul", max - random.nextInt(max)},
				{"Pierre", max - random.nextInt(max)},
				{"Pascal", max - random.nextInt(max)},
				{"Petra", max - random.nextInt(max)},
				{"Pheobe", max - random.nextInt(max)}
		};
		final Object[] columnNames = {"Player", "Points"};
		highscores = new JTable(rowData, columnNames);
		highscores.setEnabled(false);

		CenterRenderer cr = new CenterRenderer();
		for (int i=0; i < highscores.getColumnCount(); i++) {
			highscores.getColumnModel().getColumn(i).setCellRenderer(cr);
		}
		
		
		highscoresScroll = new JScrollPane(highscores);
	}

	@Override
	public Container getContents() {
		return centered(title("Highscore"), highscoresScroll, back);
	}
	
	@Override
	public void update(){
		System.out.println(highscores.getValueAt(0, 0));
	}
}
