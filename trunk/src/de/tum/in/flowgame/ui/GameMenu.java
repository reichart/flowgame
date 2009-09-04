package de.tum.in.flowgame.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSlider;

public class GameMenu implements Sprite {

	private final OffscreenJPanel panel;

	/**
	 * For testing just the menu as regular Swing app.
	 */
	public static void main(final String[] args) {
		final JFrame frame = new JFrame();
		frame.add(new GameMenu(frame).panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(200, 200, 500, 400);
		frame.setVisible(true);
	}

	public GameMenu(final Component mouseTrap) {
		final JButton oans = new JButton(new AbstractAction("Oans") {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(panel, "Eins!");
			}
		});

		final JButton zwoa = new JButton(new AbstractAction("Zwoa") {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(panel, "Zwei!");
			}
		});
		
		final JButton gsuffa = new JButton(new AbstractAction("G'Suffa") {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(panel, "Trinken!");
			}
		});
		
		final JCheckBox alkoholfrei = new JCheckBox(new AbstractAction("Alkoholfrei") {
			@Override
			public void actionPerformed(ActionEvent e) {
				final JCheckBox source = (JCheckBox) e.getSource();
				JOptionPane.showMessageDialog(panel, source.isSelected() ? "Kein Alk" : "Mit Alk");
			}
		});
		alkoholfrei.setOpaque(false);

		final JSlider centiliter = new JSlider(0, 100);
		centiliter.setOpaque(false);
		
		final Box buttons = new Box(BoxLayout.Y_AXIS);
		buttons.add(oans);
		buttons.add(Box.createVerticalStrut(20));
		buttons.add(zwoa);
		buttons.add(Box.createVerticalStrut(20));
		buttons.add(gsuffa);
		buttons.add(Box.createVerticalStrut(20));
		buttons.add(centiliter);

		final Box center = new Box(BoxLayout.X_AXIS);
		center.add(Box.createHorizontalGlue());
		center.add(buttons);
		center.add(alkoholfrei);
		center.add(Box.createHorizontalGlue());

		panel = new OffscreenJPanel(mouseTrap);
		panel.setLayout(new BorderLayout());
		panel.setDoubleBuffered(false); // this hides the white background
		panel.setOpaque(false);
		panel.add(center, BorderLayout.CENTER);
	}

	@Override
	public void render(final Graphics2D g, final int x, final int y) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void render(final Graphics2D g, final int x, final int y, final int w, final int h) {
		final BufferedImage img = CachedSprite.createImage(w, h);

		final Graphics2D offscreen = (Graphics2D) img.getGraphics();
		panel.setSize(w, h);
		panel.paintAll(offscreen);
		offscreen.dispose();

		g.drawImage(img, 0, 0, null);
	}
}
