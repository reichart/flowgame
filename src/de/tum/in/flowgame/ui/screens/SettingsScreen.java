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

import netscape.javascript.JSObject;

/**
 * Allows changing game settings.
 */
public class SettingsScreen extends MenuScreen {

	private static final String AIRPLANE = "airplane";
	private static final String NORMAL = "normal";
	private static final String STEERING = "steering";
	private final Box steeringbox = new Box(BoxLayout.X_AXIS);
	private final ButtonGroup steeringgroup = new ButtonGroup();

	private final JRadioButton normal = new JRadioButton(new AbstractAction(NORMAL) {
		public void actionPerformed(final ActionEvent e) {
			menu.getGame().getShip().getControls().setNormalSteering(true);
			setSteeringCookie(true);
		}
	});

	private final JRadioButton airplane = new JRadioButton(new AbstractAction(AIRPLANE) {
		public void actionPerformed(final ActionEvent e) {
			menu.getGame().getShip().getControls().setNormalSteering(false);
			setSteeringCookie(false);
		}
	});

	private final JCheckBox fps = new JCheckBox(new AbstractAction("FrameCounter") {
		public void actionPerformed(final ActionEvent e) {
			menu.getOverlay().setDrawFPS(fps.isSelected());
		}
	});

	private final JButton back = goTo("Back", MainScreen.class);

	private void setSteeringCookie(boolean normalSteering) {
		JSObject win = menu.getLogic().getWin();
		if (win != null) {
			Object[] newCookie = new Object[2];
			newCookie[0] = STEERING;
			if (normalSteering) {
				newCookie[1] = NORMAL;
				win.call("set_cookie", newCookie);
			} else {
				newCookie[1] = AIRPLANE;
				win.call("set_cookie", newCookie);
			}
		}
	}
	
	private void setSteering(boolean normalSteering) {
		normal.setSelected(normalSteering);
		menu.getGame().getShip().getControls().setNormalSteering(normalSteering);
		airplane.setSelected(!normalSteering);
	}

	public SettingsScreen() {
		JSObject win = menu.getLogic().getWin();
		if (win != null) {
			Object[] cookieName = new Object[1];
			cookieName[0] = STEERING;
			String steering = (String) win.call("get_cookie", cookieName);
			//cookie not set or set to a corrupt value
			if (steering == null || !(steering.equals(NORMAL) || steering.equals(AIRPLANE))) {
				setSteeringCookie(true);
				setSteering(true);
			} else if (steering.equals(NORMAL)) {
				setSteering(true);
			} else if (steering.equals(AIRPLANE)) {
				setSteering(false);
			}
		}

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
