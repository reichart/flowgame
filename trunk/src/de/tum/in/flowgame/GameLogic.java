package de.tum.in.flowgame;

import javax.media.j3d.Node;

import de.tum.in.flowgame.behavior.CreateCollidablesBehavior;
import de.tum.in.flowgame.behavior.SpeedChangeBehavior;

public class GameLogic {
	
	public static String FUELCAN = "FuelCan";
	public static String ASTEROID = "Asteroid";

	private Tunnel tunnel;
	
	private int fuel;
	private int asteroids;
	
	private NoiseMaker noiseMaker;
	private CreateCollidablesBehavior ccb;
	
	public GameLogic(CreateCollidablesBehavior ccb, Tunnel tunnel) {
		this.noiseMaker = new NoiseMaker();
		this.ccb = ccb;
		this.tunnel = tunnel;
		SpeedChangeBehavior speedChange = new SpeedChangeBehavior(this.tunnel.getFwdNav());
		speedChange.setSchedulingBounds(Game3D.WORLD_BOUNDS);
		tunnel.addChild(speedChange);
	}

	public void add(final Node node) {
		final Object userData = node.getParent().getParent().getUserData();
		if (GameLogic.FUELCAN.equals(userData)) {
			fuel++;
			noiseMaker.playSound(FUELCAN);
		} else if (GameLogic.ASTEROID.equals(userData)) {
			asteroids++;
			noiseMaker.playSound(ASTEROID);
		}
	}

	public int getFuel() {
		return fuel;
	}

	public int getAsteroids() {
		return asteroids;
	}
	
	public double getTunnelSpeed() {
		return this.tunnel.getFwdNav().getSpeed();
	}
	
	public void setTunnelSpeed(double tunnelSpeed){
		this.tunnel.getFwdNav().setSpeed(tunnelSpeed);
	}
}