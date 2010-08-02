package de.tum.in.flowgame.client.ui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import com.kitfox.svg.app.beans.SVGIcon;

import de.tum.in.flowgame.client.Sounds;
import de.tum.in.flowgame.client.ui.sprite.SVGSprite;
import de.tum.in.flowgame.model.ConfigChange.ConfigKey;

public class SettingsPanel extends JPanel {

	private static final String STEERING = "steering";
	private static final String SOUND = "sound";

	private static final int ICON_SIZE = 32;

	private static final SVGIcon SOUND_ON = new SVGSprite("/res/sound_on.svg").getIcon(ICON_SIZE, ICON_SIZE);
	private static final SVGIcon SOUND_OFF = new SVGSprite("/res/sound_off.svg").getIcon(ICON_SIZE, ICON_SIZE);
	private static final SVGIcon AIRPLANE_ICON = new SVGSprite("/res/airplane.svg").getIcon(ICON_SIZE, ICON_SIZE);
	private static final SVGIcon NORMAL_ICON = new SVGSprite("/res/normal.svg").getIcon(ICON_SIZE, ICON_SIZE);

	private final JToggleButton sound;
	private final JToggleButton steering;

	private final GameMenu menu;

	private static SettingsPanel instance;

	public static SettingsPanel getInstance(final GameMenu menu) {
		if (instance == null) {
			instance = new SettingsPanel(menu);
		}
		return instance;
	}

	private SettingsPanel(final GameMenu menu) {
		this.menu = menu;

		sound = createToggle(SOUND_OFF, new AbstractAction(null, SOUND_ON) {
			public void actionPerformed(final ActionEvent e) {
				final boolean muted = sound.isSelected();
				menu.getLogic().configChange(ConfigKey.SOUND, muted);
				setSoundMuted(muted);
				setCookie(SOUND, muted);
			}
		});

		steering = createToggle(NORMAL_ICON, new AbstractAction(null, AIRPLANE_ICON) {
			public void actionPerformed(final ActionEvent e) {
				final boolean normal = steering.isSelected();
				menu.getLogic().configChange(ConfigKey.STEERING, normal);
				setSteering(normal);
				setCookie(STEERING, normal);
			}
		});

		setOpaque(false);
		setLayout(null);
		sound.setBounds(3, 3, ICON_SIZE, ICON_SIZE);
		steering.setBounds(ICON_SIZE + 6, 3, ICON_SIZE, ICON_SIZE);

		add(sound);
		add(steering);
	}

	public void loadSettings() {
		// load user preferences from cookie if available
		setSoundMuted(Boolean.parseBoolean(getCookie(SOUND)));
		setSteering(Boolean.parseBoolean(getCookie(STEERING)));
	}

	private static JToggleButton createToggle(final Icon selected, final Action action) {
		final JToggleButton toggle = new JToggleButton(action);
		toggle.setOpaque(false);
		toggle.setBorderPainted(false);
		toggle.setFocusPainted(false);
		toggle.setContentAreaFilled(false);
		toggle.setSelectedIcon(selected);
		return toggle;
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

}
