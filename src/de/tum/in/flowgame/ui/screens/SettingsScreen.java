package de.tum.in.flowgame.ui.screens;

import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

import de.tum.in.flowgame.ui.GameMenu;

public class SettingsScreen extends MenuScreen {

	private final Box steeringbox = new Box(BoxLayout.X_AXIS);
	private final JLabel steering = new JLabel("Steering: ");
	private final ButtonGroup steeringgroup = new ButtonGroup();
	private final JRadioButton normal = new JRadioButton(new AbstractAction("normal"){
		@Override
		public void actionPerformed(final ActionEvent e) {
			//TODO
		}
	});
	private final JRadioButton airplane = new JRadioButton(new AbstractAction("airplane"){
		@Override
		public void actionPerformed(final ActionEvent e) {
			//TODO
		}
	});
	private final JCheckBox fps = new JCheckBox(new AbstractAction("FrameCounter") {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO
		}
	});
	
	
	public SettingsScreen(GameMenu menu) {
		super(menu);
		steeringgroup.add(normal);
		steeringgroup.add(airplane);
		steeringbox.add(steering);
		steeringbox.add(normal);
		steeringbox.add(airplane);
	}
	
	
	
	
	

	@Override
	public Container getContents() {
		return centered(title("Settings"),steeringbox, fps);
	}

}
