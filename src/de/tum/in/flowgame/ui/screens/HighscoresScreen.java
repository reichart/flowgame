package de.tum.in.flowgame.ui.screens;

import java.awt.Component;
import java.awt.Container;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import de.tum.in.flowgame.model.Score;
import de.tum.in.flowgame.ui.GameMenu;

public class HighscoresScreen extends MenuScreen {
	
	/*
	 * * Center the text in Table Cells
	 */
	private class CenterRenderer extends DefaultTableCellRenderer {
		public CenterRenderer() {
			setHorizontalAlignment(SwingConstants.CENTER);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			return this;
		}
	}
	
	private final JButton back = goTo("Back", MainScreen.class);
	
	private final JScrollPane highscoresScroll;
	
	private JTable highscores;
	
	private int numRounds = 15;

	public HighscoresScreen(final GameMenu menu) {
		super(menu);
		
		highscores = new JTable(numRounds, 2);
		TableColumnModel tcm = highscores.getColumnModel();
		tcm.getColumn(0).setHeaderValue("Last Runs");
		tcm.getColumn(1).setHeaderValue("Score");
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
		long playerId = this.menu.getGameLogic().getPlayer().getId();
		List<Score> scores = null;
		try {
			scores = this.menu.getGameLogic().getClient().downloadPersonHighscore(playerId, numRounds);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TableModel tm = highscores.getModel();
		int j = 0;
		for (Score score : scores){
			tm.setValueAt(j+1, j, 0);
			tm.setValueAt(score.getScore(), j, 1);
			j++;
//			System.out.println("Id: " + score.getId());
//			System.out.println("Score: " + score.getScore());

		}
	}
}
