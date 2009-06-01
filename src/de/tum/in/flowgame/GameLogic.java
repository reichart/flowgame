package de.tum.in.flowgame;

import javax.media.j3d.Node;

public class GameLogic {

	public static String FUELCAN = "FuelCan";
	public static String ASTEROID = "Asteroid";

	private int fuel;
	private int asteroids;
	
	private NoiseMaker noiseMaker;
	
	public GameLogic() {
		this.noiseMaker = new NoiseMaker();
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
}
