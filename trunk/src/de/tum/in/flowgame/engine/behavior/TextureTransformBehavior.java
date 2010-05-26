package de.tum.in.flowgame.engine.behavior;

import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupOnElapsedTime;
import javax.vecmath.Point3d;
import javax.vecmath.Quat4d;
import javax.vecmath.Vector3d;

import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.GameLogicConsumer;
import de.tum.in.flowgame.engine.Game3D;
import de.tum.in.flowgame.engine.Tunnel;

public class TextureTransformBehavior extends Behavior implements GameLogicConsumer {

	private final Transform3D transform;
	private Vector3d pos = new Vector3d();
	private Vector3d mov = new Vector3d(0, 0, 0);
	
	private long time;
	private double fwdSpeed = 100/Tunnel.TUNNEL_LENGTH;

	private final TextureAttributes attribs;
	private final WakeupCondition condition;

	private GameLogic logic;

	public TextureTransformBehavior(final TextureAttributes attribs) {
		this.attribs = attribs;
		transform = new Transform3D();
		condition = new WakeupOnElapsedTime(10);

		attribs.setCapability(TextureAttributes.ALLOW_TRANSFORM_WRITE);

		setSchedulingBounds(Game3D.WORLD_BOUNDS);
	}

	@Override
	public void initialize() {
		attribs.getTextureTransform(transform);
		wakeupOn(condition);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void processStimulus(final Enumeration/* <WakeupCriterion> */criteria) {
		if (!logic.isPaused()) {	
			attribs.setTextureTransform(updatePosition());
		}
		
		wakeupOn(condition);
	}
	
	public void setGameLogic(final GameLogic logic) {
		this.logic = logic;
		
	}
	
	/**
	 * Computes a new transform for the next frame based on the current
	 * transform and elapsed time. This method should be called once per frame.
	 */
	public Transform3D updatePosition() {

		transform.get(pos);

		double deltaTime = getDeltaTime();
		deltaTime *= 0.001;

		mov.x = 0.1;
		mov.y = -fwdSpeed;

		Point3d dp = new Point3d();
		dp.scale(deltaTime, mov);


		pos.add(dp);

		// System.out.println("Pos.x: " + pos.x + " - Pos.y: " + pos.y);
		transform.set(new Quat4d(), pos, 1);

		return transform;
	}

	private long getDeltaTime() {
		final long newTime = System.currentTimeMillis();
		long deltaTime = newTime - time;
		// System.out.println(deltaTime);
		time = newTime;
		if (deltaTime > 2000)
			return 0;
		else
			return deltaTime;
	}
	
	public void setFwdSpeed(final double fwdSpeed) {
		this.fwdSpeed = -fwdSpeed/Tunnel.TUNNEL_LENGTH;
	}

}
