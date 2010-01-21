package de.tum.in.flowgame.ui.screens;

import java.awt.Component;
import java.awt.Container;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import de.tum.in.flowgame.dao.GameSessionDAOImpl;
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
	
	private class ScoreComparator implements Comparator<Score>{
		  public int compare( Score a, Score b ){
		    if (a.getId() == b.getId()) return 0;
		    else if (a.getId() < b.getId()) return 1;
		    else return -1;
		  }
		}
	
	@Override
	public void update(){
		this.menu.getGameLogic().getPlayer();
		GameSessionDAOImpl gsImpl = new GameSessionDAOImpl();
		List<Score> result = gsImpl.getPersonalScores(this.menu.getGameLogic().getPlayer());
		Collections.sort(result, new ScoreComparator());
		Iterator<Score> i = result.iterator();
		Score s;
		TableModel tm = highscores.getModel();
		int j = 0;
		while (i.hasNext()){
			s = i.next();
			tm.setValueAt(j+1, j, 0);
			tm.setValueAt(s.getScore(), j, 1);
			if (j++ > numRounds-2) break;
//			System.out.println("Id: " + s.getId());
//			System.out.println("Score: " + s.getScore());
		}
	}
}
