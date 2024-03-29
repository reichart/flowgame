package de.tum.in.flowgame.client.ui;

import java.awt.LayoutManager;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * An abstract base class for UI components that want to to notify listeners
 * about changes using the {@link ChangeListener} interface.
 */
public abstract class ChangeableComponent extends JPanel {

	protected ChangeableComponent() {
		super();
	}

	protected ChangeableComponent(final LayoutManager layout) {
		super(layout);
	}

	public void addChangeListener(final ChangeListener listener) {
		listenerList.add(ChangeListener.class, listener);
	}

	public void removeChangeListener(final ChangeListener listener) {
		listenerList.remove(ChangeListener.class, listener);
	}

	protected void fireChange(final Object source) {
		final ChangeEvent event = new ChangeEvent(source);
		for (final ChangeListener listener : listenerList.getListeners(ChangeListener.class)) {
			listener.stateChanged(event);
		}
	}
}
