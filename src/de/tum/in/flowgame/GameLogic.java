package de.tum.in.flowgame;

import javax.media.j3d.Node;

public class GameLogic {
	
	public static String FUELCAN = "FuelCan";
	public static String ASTEROID = "Asteroid";

	private int positive = 0;
	private int negative = 0;
	private int points = 0;
	
	public GameLogic(){}

	public GameLogic(final int points) {
		this.points = points;
	}

	public GameLogic(final int positive, final int negative) {
		this.positive = positive;
		this.negative = negative;
	}

	public GameLogic(final int positive, final int negative, final int points) {
		this.positive = positive;
		this.negative = negative;
		this.points = points;
	}

	public void add(final Node collisionObject) {
		final Object userData = collisionObject.getParent().getUserData();
		if (GameLogic.FUELCAN.equals(userData)) {
			addPositive();
		} else if (GameLogic.ASTEROID.equals(userData)) {
			addNegative();
		} else {
			System.out.println();
		}
	}
	
	private void addPositive() {
		positive += 1;
		points += 1;
	}

	private void addNegative() {
		negative += 1;
		points -= 1;
		if (points < 0) {
			points = 0;
		}
	}

	public int getPoints() {
		return points;
	}
}
