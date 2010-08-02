package de.tum.in.flowgame.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.GraphicsConfiguration;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;

import javax.swing.JPanel;

import de.tum.in.flowgame.Utils;

/**
 * Frankenstein's own JPanel to render regular Swing components to an offscreen
 * image and still have them react to mouse events.
 * 
 * Based on {@link java.awt.LightweightDispatcher}.
 */
public class OffscreenJPanel extends JPanel implements MouseListener, MouseMotionListener {

	private transient Component mouseEventTarget;
	private transient Component targetLastEntered;
	private transient boolean isInContainer;

	/**
	 * @param mouseTrap
	 *            a visible component receiving mouse events for this offscreen
	 *            panel
	 */
	public OffscreenJPanel(final Component mouseTrap) {
		mouseTrap.addMouseListener(this);
		mouseTrap.addMouseMotionListener(this);
		
		// force layout of offscreen Swing/AWT components (Sun bug #4639354)
		addNotify();
	}
	
	@Override
	public GraphicsConfiguration getGraphicsConfiguration() {
		// prevents NPE on JRE 1.5 somewhere in Metal LAF
		return Utils.getDefaultGraphicsConfiguration();
	}

	private boolean isMouseGrab(final MouseEvent e) {
		int modifiers = e.getModifiersEx();

		if (e.getID() == MouseEvent.MOUSE_PRESSED || e.getID() == MouseEvent.MOUSE_RELEASED) {
			switch (e.getButton()) {
			case MouseEvent.BUTTON1:
				modifiers ^= InputEvent.BUTTON1_DOWN_MASK;
				break;
			case MouseEvent.BUTTON2:
				modifiers ^= InputEvent.BUTTON2_DOWN_MASK;
				break;
			case MouseEvent.BUTTON3:
				modifiers ^= InputEvent.BUTTON3_DOWN_MASK;
				break;
			}
		}
		return ((modifiers & (InputEvent.BUTTON1_DOWN_MASK | InputEvent.BUTTON2_DOWN_MASK | InputEvent.BUTTON3_DOWN_MASK)) != 0);
	}

	protected boolean processOffscreenMouseEvent(final MouseEvent e) {
		final int id = e.getID();
		final Component currentTarget = getMouseEventTarget(this, e.getX(), e.getY());

		trackMouseEnterExit(currentTarget, e);

		if (!isMouseGrab(e) && id != MouseEvent.MOUSE_CLICKED) {
			mouseEventTarget = (currentTarget == this) ? null : currentTarget;
		}

		if (mouseEventTarget != null) {
			switch (id) {
			case MouseEvent.MOUSE_ENTERED:
			case MouseEvent.MOUSE_EXITED:
				break;
			case MouseEvent.MOUSE_PRESSED:
				retargetMouseEvent(mouseEventTarget, id, e);
				break;
			case MouseEvent.MOUSE_RELEASED:
				retargetMouseEvent(mouseEventTarget, id, e);
				break;
			case MouseEvent.MOUSE_CLICKED:
				if (currentTarget == mouseEventTarget) {
					retargetMouseEvent(currentTarget, id, e);
				}
				break;
			case MouseEvent.MOUSE_MOVED:
				retargetMouseEvent(mouseEventTarget, id, e);
				break;
			case MouseEvent.MOUSE_DRAGGED:
				if (isMouseGrab(e)) {
					retargetMouseEvent(mouseEventTarget, id, e);
				}
				break;
			case MouseEvent.MOUSE_WHEEL:
				retargetMouseEvent(currentTarget, id, e);
				break;
			}
			e.consume();
		}
		return e.isConsumed();
	}

	/**
	 * Generates mouse enter/exit events for all components contained in the
	 * container managed by this {@link OffscreenJPanel} instance.
	 * 
	 * @param currentTarget
	 *            the component currently target by the mouse
	 * @param e
	 *            the current mouse event
	 */
	private void trackMouseEnterExit(final Component currentTarget, final MouseEvent e) {
		final int id = e.getID();

		if (id != MouseEvent.MOUSE_EXITED && id != MouseEvent.MOUSE_DRAGGED && isInContainer == false) {
			isInContainer = true;
		} else if (id == MouseEvent.MOUSE_EXITED) {
			isInContainer = false;
		}

		final Component targetEnter = isInContainer ? currentTarget : null;

		if (targetLastEntered == targetEnter) {
			return;
		}

		if (targetLastEntered != null) {
			retargetMouseEvent(targetLastEntered, MouseEvent.MOUSE_EXITED, e);
		}
		if (id == MouseEvent.MOUSE_EXITED) {
			e.consume();
		}

		if (targetEnter != null) {
			retargetMouseEvent(targetEnter, MouseEvent.MOUSE_ENTERED, e);
		}
		if (id == MouseEvent.MOUSE_ENTERED) {
			e.consume();
		}

		targetLastEntered = targetEnter;
	}

	/**
	 * Retargets a mouse event from a parent container to one of its children.
	 * 
	 * @param target
	 *            the new target component
	 * @param id
	 *            the (possibly changed) mouse event ID
	 * @param e
	 *            the original event
	 */
	private void retargetMouseEvent(final Component target, final int id, final MouseEvent e) {
		if (target == null) {
			return;
		}

		// adapt relative mouse position
		int retargetedX = e.getX();
		int retargetedY = e.getY();
		Component comp;
		for (comp = target; comp != null && comp != this; comp = comp.getParent()) {
			retargetedX -= comp.getX();
			retargetedY -= comp.getY();
		}

		final MouseEvent retargeted;
		if (id == MouseEvent.MOUSE_WHEEL) {
			retargeted = new MouseWheelEvent(target, id, e.getWhen(), e.getModifiersEx() | e.getModifiers(),
					retargetedX, retargetedY, e.getXOnScreen(), e.getYOnScreen(), e.getClickCount(),
					e.isPopupTrigger(), ((MouseWheelEvent) e).getScrollType(), ((MouseWheelEvent) e).getScrollAmount(),
					((MouseWheelEvent) e).getWheelRotation());
		} else {
			retargeted = new MouseEvent(target, id, e.getWhen(), e.getModifiersEx() | e.getModifiers(), retargetedX,
					retargetedY, e.getXOnScreen(), e.getYOnScreen(), e.getClickCount(), e.isPopupTrigger(), e
							.getButton());
		}

		target.dispatchEvent(retargeted);
	}

	/**
	 * Finds the component at position (x,y) which is deepest in the UI
	 * component tree.
	 * 
	 * @param root
	 *            the container to start looking in
	 * @param x
	 *            the x value of the mouse position
	 * @param y
	 *            the y value of the mouse position
	 * @return the component at position (x,y) or <code>null</code> if outside
	 *         root
	 */
	private static Component getMouseEventTarget(final Container root, final int x, final int y) {
		for (final Component comp : root.getComponents()) {
			if (comp != null && comp.isVisible() && comp.contains(x - comp.getX(), y - comp.getY())) {
				if (comp instanceof Container) {
					final Container child = (Container) comp;
					final Component deeper = getMouseEventTarget(child, x - child.getX(), y - child.getY());
					if (deeper != null) {
						return deeper;
					}
				} else {
					return comp;
				}
			}
		}

		if (root.contains(x, y)) {
			return root;
		}

		return null;
	}

	public void mouseClicked(final MouseEvent e) {
		processOffscreenMouseEvent(e);
	}

	public void mouseEntered(final MouseEvent e) {
		processOffscreenMouseEvent(e);
	}

	public void mouseExited(final MouseEvent e) {
		processOffscreenMouseEvent(e);
	}

	public void mousePressed(final MouseEvent e) {
		processOffscreenMouseEvent(e);
	}

	public void mouseReleased(final MouseEvent e) {
		processOffscreenMouseEvent(e);
	}

	public void mouseDragged(final MouseEvent e) {
		processOffscreenMouseEvent(e);
	}

	public void mouseMoved(final MouseEvent e) {
		processOffscreenMouseEvent(e);
	}

}