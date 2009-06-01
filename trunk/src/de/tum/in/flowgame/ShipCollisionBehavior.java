package de.tum.in.flowgame;

import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.Node;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnCollisionEntry;
import javax.media.j3d.WakeupOnCollisionExit;
import javax.media.j3d.WakeupOnCollisionMovement;
import javax.media.j3d.WakeupOr;

public class ShipCollisionBehavior extends Behavior {

	private final WakeupOr wakeUp;
	private final GameLogic logic;

	public ShipCollisionBehavior(final Node node, final GameLogic logic) {
		final WakeupOnCollisionEntry w1 = new WakeupOnCollisionEntry(node, WakeupOnCollisionEntry.USE_BOUNDS);
		final WakeupOnCollisionExit w2 = new WakeupOnCollisionExit(node);
		final WakeupOnCollisionMovement w3 = new WakeupOnCollisionMovement(node);
		final WakeupCriterion[] wakeupCriterion = { w1, w2, w3 };

		this.wakeUp = new WakeupOr(wakeupCriterion);
		this.logic = logic;
	}

	@Override
	public void initialize() {
		wakeupOn(wakeUp);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void processStimulus(final Enumeration criteria) {
		while (criteria.hasMoreElements()) {
			final Object element = criteria.nextElement();
			if (element instanceof WakeupOnCollisionEntry) {
				final WakeupOnCollisionEntry collision = (WakeupOnCollisionEntry) element;
				logic.add(collision.getTriggeringPath().getObject());
			}
		}
		wakeupOn(wakeUp);
	}
}
