package de.tum.in.flowgame.ui.screens;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
			menu.getGame().getShip().getControls().setNormalSteering(true);
		}
	});

	private final JRadioButton airplane = new JRadioButton(new AbstractAction("airplane") {
		@Override
		public void actionPerformed(final ActionEvent e) {
			menu.getGame().getShip().getControls().setNormalSteering(false);
		}
	});

	private final JCheckBox fps = new JCheckBox(new AbstractAction("FrameCounter") {
		@Override
		public void actionPerformed(final ActionEvent e) {
			menu.getOverlay().setDrawFPS(fps.isSelected());
		}
	});
	
	private final JSlider accSlider = new JSlider(500, 10000);	
	private final JSlider maxSpeedSlider = new JSlider(500, 10000);
	
	private final JButton back = new JButton(new AbstractAction("Back") {
		@Override
		public void actionPerformed(final ActionEvent e) {
			menu.show(MainScreen.class);
		}
	});
	

	public SettingsScreen(final GameMenu menu) {
		super(menu);
		accSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
//				System.out.println("Acceleration: " + (float)accSlider.getValue()/100);
				menu.getGame().getShip().getControls().setAcceleration((float)accSlider.getValue()/100);
			}
		});
		maxSpeedSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
//				System.out.println("Max Speed: " + (float)maxSpeedSlider.getValue()/100);
				menu.getGame().getShip().getControls().setMaxSpeed((float)maxSpeedSlider.getValue()/100);
			}
		});
		accSlider.setValue((int) menu.getGame().getShip().getControls().getAcceleration()*100);
		accSlider.setBorder(BorderFactory.createTitledBorder("Acceleration"));
		maxSpeedSlider.setValue((int) menu.getGame().getShip().getControls().getMax_speed()*100);
		maxSpeedSlider.setBorder(BorderFactory.createTitledBorder("Max Speed"));
		normal.setSelected(menu.getGame().getShip().getControls().getNormalSteering());
		airplane.setSelected(!menu.getGame().getShip().getControls().getNormalSteering());
		steeringgroup.add(normal);
		steeringgroup.add(airplane);
		steeringbox.add(steering);
		steeringbox.add(normal);
		steeringbox.add(airplane);
	}

	@Override
	public Container getContents() {
		return centered(title("Settings"), steeringbox, fps, accSlider, maxSpeedSlider, back);
	}

}
