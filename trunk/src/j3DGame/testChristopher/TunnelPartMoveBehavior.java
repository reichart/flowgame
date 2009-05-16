package j3DGame.testChristopher;

import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.Bounds;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.ViewPlatform;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnCollisionEntry;
import javax.media.j3d.WakeupOnCollisionExit;
import javax.media.j3d.WakeupOnElapsedFrames;
import javax.media.j3d.WakeupOnSensorExit;
import javax.media.j3d.WakeupOnViewPlatformExit;
import javax.media.j3d.WakeupOr;

import com.sun.j3d.utils.behaviors.keyboard.KeyNavigator;

public class TunnelPartMoveBehavior extends Behavior {

	private WakeupCriterion wakeEvent;
	private WakeupCriterion we2;
	private WakeupCriterion[] warray = new WakeupCriterion[2];
	private WakeupCondition w;
	private TunnelPartMover tunnelPartMover;
	private Shape3D element;
	private TransformGroup tg;

	public TunnelPartMoveBehavior(Shape3D element, TransformGroup targetTG, double zDist,
			int parts) {
		this.tunnelPartMover = new TunnelPartMover(targetTG, zDist, parts);
		this.wakeEvent = new WakeupOnViewPlatformExit(element.getBounds());
		this.we2 = new WakeupOnElapsedFrames(0);
		this.warray[0] = this.wakeEvent;
		this.warray[1] = this.we2;
		this.w = new WakeupOr(this.warray);
		this.element = element;
		this.tg = targetTG;
	}

	@Override
	public void initialize() {
		// Transform3D blub = new Transform3D();
		// test.getLocalToVworld(blub);
		// System.out.println(blub);
		this.wakeupOn(wakeEvent);
	}

	@Override
	public void processStimulus(Enumeration criteria) {
		this.tunnelPartMover.integrateTransformChanges();
		this.wakeupOn(wakeEvent);
	}

}
