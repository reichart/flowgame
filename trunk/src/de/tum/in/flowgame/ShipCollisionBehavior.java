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
	private WakeupOr wakeUp;
	Node node;
	
	public ShipCollisionBehavior(Node node) {
		this.node = node;
		WakeupOnCollisionEntry w1 = new WakeupOnCollisionEntry(node, WakeupOnCollisionEntry.USE_BOUNDS);
		WakeupOnCollisionExit w2 = new WakeupOnCollisionExit(this.node);
		WakeupOnCollisionMovement w3 = new WakeupOnCollisionMovement(this.node);
		WakeupCriterion[] wakeupCriterion = {w1, w2, w3};
		wakeUp = new WakeupOr(wakeupCriterion);
	}

	@Override
	public void initialize() {
		System.out.println("initCollide");
		wakeupOn(wakeUp);
	}

	@Override
	public void processStimulus(Enumeration criteria) {
		while (criteria.hasMoreElements()) {
			Object element = criteria.nextElement();
			if (element instanceof WakeupOnCollisionEntry) {
				System.out.println("collideEnter");
			}
			if (element instanceof WakeupOnCollisionExit) {
				System.out.println("collideExit");
			}
			if (element instanceof WakeupOnCollisionMovement) {
//				WakeupOnCollisionMovement wocm = (WakeupOnCollisionMovement)element;
//				Node leaf = wocm.getTriggeringPath().getObject();
//				System.out.println(toString(node) + " collides with " + leaf + "(" + (leaf == null ? null : leaf.getUserData()) + ")");
//				while (leaf.getParent() != null) {
//					System.out.print(" < " + toString(leaf));
//					leaf = leaf.getParent();
//				}
//				System.out.println();
			}
		}
		wakeupOn(wakeUp);
	}

	private String toString(final Object n) {
		return n.getClass().getSimpleName() + "@" + Integer.toHexString(n.hashCode());
	}

}
