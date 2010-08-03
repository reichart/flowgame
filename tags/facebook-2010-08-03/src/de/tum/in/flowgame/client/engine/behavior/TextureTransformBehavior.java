package de.tum.in.flowgame.client.engine.behavior;

import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.vecmath.Point3d;
import javax.vecmath.Quat4d;
import javax.vecmath.Vector3d;

import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.client.GameLogicConsumer;
import de.tum.in.flowgame.client.engine.Game3D;
import de.tum.in.flowgame.client.engine.Tunnel;

public class TextureTransformBehavior extends RepeatingBehavior implements GameLogicConsumer {

	private final Transform3D transform;
	private Vector3d pos = new Vector3d();
	private Vector3d mov = new Vector3d(0, 0, 0);
	
	private double fwdSpeed = 100/Tunnel.TUNNEL_LENGTH;

	private final TextureAttributes attribs;
	
	public TextureTransformBehavior(final TextureAttributes attribs) {
		this.attribs = attribs;
		transform = new Transform3D();

		attribs.setCapability(TextureAttributes.ALLOW_TRANSFORM_WRITE);

		setSchedulingBounds(Game3D.WORLD_BOUNDS);
	}

	@Override
	protected void init() {
		attribs.getTextureTransform(transform);
	}
	
	@Override
	protected void update() {
		if (!isPaused()) {
			attribs.setTextureTransform(updatePosition());
		}
	}
	
	public void setGameLogic(final GameLogic logic) {
		if (logic != null) {
			logic.addListener(this);
		}
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

	public void setFwdSpeed(final double fwdSpeed) {
		this.fwdSpeed = -fwdSpeed/Tunnel.TUNNEL_LENGTH;
	}

}
