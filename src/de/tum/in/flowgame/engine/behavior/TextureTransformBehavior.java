package de.tum.in.flowgame.engine.behavior;

import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.vecmath.Point3d;
import javax.vecmath.Quat4d;
import javax.vecmath.Vector3d;

import de.tum.in.flowgame.GameListener;
import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.GameLogicConsumer;
import de.tum.in.flowgame.engine.Game3D;
import de.tum.in.flowgame.engine.Tunnel;
import de.tum.in.flowgame.model.Collision.Item;

public class TextureTransformBehavior extends RepeatingBehavior implements GameLogicConsumer, GameListener {

	private final Transform3D transform;
	private Vector3d pos = new Vector3d();
	private Vector3d mov = new Vector3d(0, 0, 0);
	
	private long time;
	private double fwdSpeed = 100/Tunnel.TUNNEL_LENGTH;

	private final TextureAttributes attribs;

	private GameLogic logic;
	
	private boolean pause;
	private long pauseBegin;

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
		if (!pause) {
			attribs.setTextureTransform(updatePosition());
		}
	}
	
	public void setGameLogic(final GameLogic logic) {
		this.logic = logic;
		this.logic.addListener(this);
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

	@Override
	public void added(GameLogic game) {
		// empty
		
	}

	@Override
	public void collided(GameLogic game, Item item) {
		// empty
		
	}

	@Override
	public void gamePaused(GameLogic game) {
		pause = true;
		pauseBegin = System.currentTimeMillis();
	}

	@Override
	public void gameResumed(GameLogic game) {
		pause = false;
		time = time + (System.currentTimeMillis() - pauseBegin);
	}

	@Override
	public void gameStarted(GameLogic game) {
		// empty
		
	}

	@Override
	public void gameStopped(GameLogic game) {
		// empty
		
	}

	@Override
	public void removed(GameLogic game) {
		// empty
		
	}

}
