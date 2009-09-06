package de.tum.in.flowgame.ui.screens;

import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.Ship;
import de.tum.in.flowgame.ui.GameMenu;

public class SettingsScreen extends MenuScreen {

	private final Box steeringbox = new Box(BoxLayout.X_AXIS);
	private final JLabel steering = new JLabel("Steering: ");
	private final ButtonGroup steeringgroup = new ButtonGroup();

	private final JRadioButton normal = new JRadioButton(new AbstractAction("normal") {
		@Override
		public void actionPerformed(final ActionEvent e) {
			System.out.println("normal");
			menu.getGame().getShip().getControls().setNormalSteering(true);
		}
	});

	private final JRadioButton airplane = new JRadioButton(new AbstractAction("airplane") {
		@Override
		public void actionPerformed(final ActionEvent e) {
			System.out.println("airplane");
			menu.getGame().getShip().getControls().setNormalSteering(false);
		}
	});

	private final JCheckBox fps = new JCheckBox(new AbstractAction("FrameCounter") {
		@Override
		public void actionPerformed(final ActionEvent e) {
			menu.getOverlay().setDrawFPS(fps.isSelected());
		}
	});
	
	private final JButton back = new JButton(new AbstractAction("Back") {
		@Override
		public void actionPerformed(final ActionEvent e) {
			menu.show(MainScreen.class);
		}
	});

	public SettingsScreen(final GameMenu menu) {
		super(menu);
		menu.getGame().getShip().getControls().setNormalSteering(true);
		normal.setSelected(true);
		steeringgroup.add(normal);
		steeringgroup.add(airplane);
		steeringbox.add(steering);
		steeringbox.add(normal);
		steeringbox.add(airplane);
	}

	@Override
	public Container getContents() {
		return centered(title("Settings"), steeringbox, fps, back);
	}

}
