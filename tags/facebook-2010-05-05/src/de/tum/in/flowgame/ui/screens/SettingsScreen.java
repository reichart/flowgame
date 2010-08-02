package de.tum.in.flowgame.ui.screens;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import de.tum.in.flowgame.ui.GameMenu;

public class SettingsScreen extends MenuScreen {

	private final Box steeringbox = new Box(BoxLayout.X_AXIS);
	private final ButtonGroup steeringgroup = new ButtonGroup();

	private final JRadioButton normal = new JRadioButton(new AbstractAction("normal") {
		public void actionPerformed(final ActionEvent e) {
			menu.getGame().getShip().getControls().setNormalSteering(true);
		}
	});

	private final JRadioButton airplane = new JRadioButton(new AbstractAction("airplane") {
		public void actionPerformed(final ActionEvent e) {
			menu.getGame().getShip().getControls().setNormalSteering(false);
		}
	});

	private final JCheckBox fps = new JCheckBox(new AbstractAction("FrameCounter") {
		public void actionPerformed(final ActionEvent e) {
			menu.getOverlay().setDrawFPS(fps.isSelected());
		}
	});
	
	private final JButton back = goTo("Back", MainScreen.class);
	

	public SettingsScreen(final GameMenu menu) {
		super(menu);
		final boolean normalSteering = menu.getGame().getShip().getControls().getNormalSteering();
		normal.setSelected(normalSteering);
		airplane.setSelected(!normalSteering);
		steeringgroup.add(normal);
		steeringgroup.add(airplane);
		final TitledBorder steeringborder = BorderFactory.createTitledBorder("Steering");
		steeringbox.setBorder(steeringborder);
		steeringborder.setTitleColor(Color.WHITE);
		steeringbox.add(normal);
		steeringbox.add(airplane);
	}

	@Override
	public Container getContents() {
		return centered(title("Settings"), steeringbox, fps, back);
	}

}
