package de.tum.in.flowgame.ui.screens;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import com.lowagie.text.Image;

import de.tum.in.flowgame.dao.GameSessionDAOImpl;
import de.tum.in.flowgame.model.Score;
import de.tum.in.flowgame.ui.GameMenu;

public class HighscoresChartScreen extends MenuScreen {
	
    private ImageIcon imageIcon = new ImageIcon(new BufferedImage(500, 300, Image.ORIGINAL_PNG));
    private JLabel imageLabel = new JLabel(imageIcon);
	
	private final JButton back = goTo("Back", MainScreen.class);
	
	public HighscoresChartScreen(final GameMenu menu) {
		super(menu);
	}

	@Override
	public Container getContents() {
		return centered(title("Highscore"), imageLabel, back);
	}
	
	@Override
	public void update(){
		long playerId = this.menu.getGameLogic().getPlayer().getId();
		BufferedImage im = null;
		try {
			im = this.menu.getGameLogic().getClient().downloadPersonHighscoreChart(playerId);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		imageIcon.setImage(im);
	}
}
