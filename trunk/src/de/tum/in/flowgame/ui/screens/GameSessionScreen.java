package de.tum.in.flowgame.ui.screens;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JToggleButton;

import netscape.javascript.JSObject;

import com.kitfox.svg.app.beans.SVGIcon;

import de.tum.in.flowgame.Sounds;
import de.tum.in.flowgame.ui.sprite.SVGSprite;

/**
 * Displayed when the game is played.
 */
public class GameSessionScreen extends MenuScreen {

	private static final String STEERING = "steering";
	private static final String SOUND = "sound";
	
	private static final int ICON_SIZE = 32;

	private static final SVGIcon SOUND_ON = new SVGSprite("/res/sound_on.svg").getIcon(ICON_SIZE, ICON_SIZE);
	private static final SVGIcon SOUND_OFF = new SVGSprite("/res/sound_off.svg").getIcon(ICON_SIZE, ICON_SIZE);
	private static final SVGIcon AIRPLANE_ICON = new SVGSprite("/res/airplane.svg").getIcon(ICON_SIZE, ICON_SIZE);
	private static final SVGIcon NORMAL_ICON = new SVGSprite("/res/normal.svg").getIcon(ICON_SIZE, ICON_SIZE);

	private final JToggleButton sound;
	private final JToggleButton steering;

	public GameSessionScreen() {
		// load sound preferences from cookie if available
		setSoundMuted(Boolean.parseBoolean(getCookie(SOUND)));
		
		sound = new JToggleButton(new AbstractAction(null, SOUND_ON) {
			public void actionPerformed(final ActionEvent e) {
				final boolean muted = sound.isSelected();
//				System.err.println("setting sound to " + (muted ? "off" : "on"));
				setSoundMuted(muted);
				setCookie(SOUND, muted);
			}
		});
		sound.setOpaque(false);
		sound.setBorderPainted(false);
		sound.setFocusPainted(false);
		sound.setContentAreaFilled(false);
		sound.setSelectedIcon(SOUND_OFF);

		// load steering preferences from cookie if available
		setSteering(Boolean.parseBoolean(getCookie(STEERING)));
		
		steering = new JToggleButton(new AbstractAction(null, AIRPLANE_ICON) {
			public void actionPerformed(final ActionEvent e) {
				final boolean normal = steering.isSelected();
//				System.err.println("setting steering to " + (normal ? "normal" : "airplane"));
				setSteering(normal);
				setCookie(STEERING, normal);
			}
		});
		steering.setOpaque(false);
		steering.setBorderPainted(false);
		steering.setFocusPainted(false);
		steering.setContentAreaFilled(false);
		steering.setSelectedIcon(NORMAL_ICON);
	}

	private void setSoundMuted(final boolean muted) {
		Sounds.setMuted(muted);
	}
	
	private void setSteering(final boolean normal) {
		menu.getGame().getShip().getControls().setNormalSteering(normal);
	}
	
	private void setCookie(final String name, final boolean value) {
		final String cValue = String.valueOf(value);
		final JSObject win = menu.getLogic().getWin();
		if (win != null) {
			final Object[] newCookie = new Object[2];
			newCookie[0] = name;
			newCookie[1] = cValue;
			win.call("set_cookie", newCookie);
		}
	}

	private String getCookie(final String name) {
		final JSObject win = menu.getLogic().getWin();
		if (win != null) {
			final Object[] cookieName = new Object[1];
			cookieName[0] = name;
			final String value = (String) win.call("get_cookie", cookieName);
			return value;
		}
		return null;
	}

	@Override
	public Container getContents() {
		Container pane = new Container();
		pane.setLayout(null);
		pane.add(sound);
		pane.add(steering);
        sound.setBounds(3, 3, ICON_SIZE, ICON_SIZE);
        steering.setBounds(ICON_SIZE + 6, 3, ICON_SIZE, ICON_SIZE);
		return pane;
	}

}