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

import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.model.Score;
import de.tum.in.flowgame.ui.GameMenu;

public class HighscoresScreen extends MenuScreen {

	/*
	 * * Center the text in Table Cells
	 */
	private static class CenterRenderer extends DefaultTableCellRenderer {
		public CenterRenderer() {
			setHorizontalAlignment(SwingConstants.CENTER);
		}

		@Override
		public Component getTableCellRendererComponent(final JTable table, final Object value,
				final boolean isSelected, final boolean hasFocus, final int row, final int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			return this;
		}
	}

	private final JButton back = goTo("Back", MainScreen.class);

	private final JScrollPane highscoresScroll;

	private final JTable highscores;

	private final int numRounds = 15;

	public HighscoresScreen(final GameMenu menu) {
		super(menu);

		highscores = new JTable(numRounds, 2);
		final TableColumnModel tcm = highscores.getColumnModel();
		tcm.getColumn(0).setHeaderValue("Last Runs");
		tcm.getColumn(1).setHeaderValue("Score");
		highscores.setEnabled(false);

		final CenterRenderer cr = new CenterRenderer();
		for (int i = 0; i < highscores.getColumnCount(); i++) {
			highscores.getColumnModel().getColumn(i).setCellRenderer(cr);
		}

		highscoresScroll = new JScrollPane(highscores);
	}

	@Override
	public Container getContents() {
		return centered(title("Highscore"), highscoresScroll, back);
	}

	@Override
	public void update(final GameLogic logic) throws IOException {
		final long playerId = logic.getPlayer().getId();
		final List<Score> scores = logic.getClient().downloadPersonHighscore(playerId, numRounds);
		final TableModel tm = highscores.getModel();
		int j = 0;
		for (final Score score : scores) {
			tm.setValueAt(j + 1, j, 0);
			tm.setValueAt(score.getScore(), j, 1);
			j++;
		}

	}
}
