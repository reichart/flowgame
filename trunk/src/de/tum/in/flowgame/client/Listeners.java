package de.tum.in.flowgame.client;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides generic listener and event firing handling for arbitrary events and
 * listener classes. Subclasses only have to override the
 * {@link #fire(Object, Object)} method to deliver an event to a single
 * listener.
 * 
 * @param <L>
 *            the class of listener
 */
public abstract class Listeners<L> {

	private final List<L> listeners;

	public Listeners() {
		this.listeners = new ArrayList<L>();
	}

	public void addListener(final L listener) {
		this.listeners.add(listener);
	}

	public void removeListener(final L listener) {
		this.listeners.remove(listener);
	}

	public void fire(final Object event) {
		for (final L listener : listeners) {
			fire(event, listener);
		}
	}

	protected abstract void fire(final Object event, final L listener);
}
