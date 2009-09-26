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
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.tum.in.flowgame.ui.GameMenu;

public class SettingsScreen extends MenuScreen {

	private final Box steeringbox = new Box(BoxLayout.X_AXIS);
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
	
	private final JSlider accSlider = new JSlider(500, 30000);	
	private final JSlider maxSpeedSlider = new JSlider(500, 10000);
	
	private final JButton back = goTo("Back", MainScreen.class);
	

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
		final TitledBorder accBorder = BorderFactory.createTitledBorder("Acceleration");
		accSlider.setBorder(accBorder);
		accBorder.setTitleColor(Color.WHITE);
		maxSpeedSlider.setValue((int) menu.getGame().getShip().getControls().getMax_speed()*100);
		final TitledBorder speedBorder = BorderFactory.createTitledBorder("Max Speed");
		maxSpeedSlider.setBorder(speedBorder);
		speedBorder.setTitleColor(Color.WHITE);
		normal.setSelected(menu.getGame().getShip().getControls().getNormalSteering());
		airplane.setSelected(!menu.getGame().getShip().getControls().getNormalSteering());
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
		return centered(title("Settings"), steeringbox, fps, accSlider, maxSpeedSlider, back);
	}

}
