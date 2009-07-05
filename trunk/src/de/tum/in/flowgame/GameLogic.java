package de.tum.in.flowgame;

import javax.media.j3d.Node;

import de.tum.in.flowgame.behavior.CreateCollidablesBehavior;
import de.tum.in.flowgame.behavior.SpeedChangeBehavior;

public class GameLogic {
	
	public enum Item{FUELCAN, ASTEROID};

	private Tunnel tunnel;
	
	private int fuel;
	private int asteroids;
	
	private CreateCollidablesBehavior ccb;
	
	public GameLogic(CreateCollidablesBehavior ccb, Tunnel tunnel) {
		this.ccb = ccb;
		this.tunnel = tunnel;
		SpeedChangeBehavior speedChange = new SpeedChangeBehavior(this.tunnel.getFwdNav());
		speedChange.setSchedulingBounds(Game3D.WORLD_BOUNDS);
		tunnel.addChild(speedChange);
	}

	public void add(final Node node) {
		Object userData = node.getUserData();
		System.out.println(userData);
		if (Item.FUELCAN.toString().equals(userData)) {
			fuel++;
			Sounds.FUELCAN.play();
		} else if (Item.ASTEROID.toString().equals(userData)) {
			asteroids++;
			Sounds.ASTEROID.play();
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