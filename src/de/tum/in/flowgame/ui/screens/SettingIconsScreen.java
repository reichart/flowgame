package de.tum.in.flowgame.ui.screens;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.JToggleButton;

import com.kitfox.svg.app.beans.SVGIcon;

import de.tum.in.flowgame.GameApplet;
import de.tum.in.flowgame.Sounds;
import de.tum.in.flowgame.ui.sprite.SVGSprite;

public abstract class SettingIconsScreen extends MenuScreen {

	private static final String STEERING = "steering";
	private static final String SOUND = "sound";
	
	private static final int ICON_SIZE = 32;

	private static final SVGIcon SOUND_ON = new SVGSprite("/res/sound_on.svg").getIcon(ICON_SIZE, ICON_SIZE);
	private static final SVGIcon SOUND_OFF = new SVGSprite("/res/sound_off.svg").getIcon(ICON_SIZE, ICON_SIZE);
	private static final SVGIcon AIRPLANE_ICON = new SVGSprite("/res/airplane.svg").getIcon(ICON_SIZE, ICON_SIZE);
	private static final SVGIcon NORMAL_ICON = new SVGSprite("/res/normal.svg").getIcon(ICON_SIZE, ICON_SIZE);

	private final JToggleButton sound;
	private final JToggleButton steering;

	public SettingIconsScreen() {
		this(null);
	}
	
	protected SettingIconsScreen(final BufferedImage backgroundImage) {
		super(backgroundImage);

		sound = new JToggleButton(new AbstractAction(null, SOUND_ON) {
			public void actionPerformed(final ActionEvent e) {
				final boolean muted = sound.isSelected();
				System.out.println("SettingIconsScreen.SettingIconsScreen().new AbstractAction() {...}.actionPerformed()");
				setSoundMuted(muted);
				setCookie(SOUND, muted);
			}
		});
		sound.setOpaque(false);
		sound.setBorderPainted(false);
		sound.setFocusPainted(false);
		sound.setContentAreaFilled(false);
		sound.setSelectedIcon(SOUND_OFF);

		steering = new JToggleButton(new AbstractAction(null, AIRPLANE_ICON) {
			public void actionPerformed(final ActionEvent e) {
				System.out.println("SettingIconsScreen.SettingIconsScreen().new AbstractAction() {...}.actionPerformed()");
				final boolean normal = steering.isSelected();
				setSteering(normal);
				setCookie(STEERING, normal);
			}
		});
		steering.setOpaque(false);
		steering.setBorderPainted(false);
		steering.setFocusPainted(false);
		steering.setContentAreaFilled(false);
		steering.setSelectedIcon(NORMAL_ICON);
		
		// load user preferences from cookie if available
		setSoundMuted(Boolean.parseBoolean(getCookie(SOUND)));
		setSteering(Boolean.parseBoolean(getCookie(STEERING)));
	}

	private void setSoundMuted(final boolean muted) {
		sound.setSelected(muted);
		Sounds.setMuted(muted);
	}
	
	private void setSteering(final boolean normal) {
		steering.setSelected(normal);
		menu.getGame().getShip().getControls().setNormalSteering(normal);
	}
	
	private void setCookie(final String name, final boolean value) {
		menu.getLogic().getBrowser().setCookie(name, String.valueOf(value));
	}

	private String getCookie(final String name) {
		return menu.getLogic().getBrowser().getCookie(name);
	}

	
	@Override
	public void build() {
		setLayout(null);
		
		Container contents = getContents();
		sound.setBounds(3, 3, ICON_SIZE, ICON_SIZE);
        steering.setBounds(ICON_SIZE + 6, 3, ICON_SIZE, ICON_SIZE);
        contents.setBounds(0, 0, GameApplet.WIDTH, GameApplet.HEIGHT);
        
		add(sound);
		add(steering);
		
		add(contents);
	}

}
