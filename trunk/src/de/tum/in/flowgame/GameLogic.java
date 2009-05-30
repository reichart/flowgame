package de.tum.in.flowgame;

import javax.media.j3d.Node;

public class GameLogic {
	
	public static String FUELCAN = "FuelCan";
	public static String ASTEROID = "Asteroid";

	private int positive = 0;
	private int negative = 0;
	private int points = 0;
	
	public GameLogic(){}

	public GameLogic(int points) {
		this.points = points;
	}

	public GameLogic(int positive, int negative) {
		this.positive = positive;
		this.negative = negative;
	}

	public GameLogic(int positive, int negative, int points) {
		this.positive = positive;
		this.negative = negative;
		this.points = points;
	}

	public void add(Node collisionObject){
		if (collisionObject.getParent().getUserData().equals(GameLogic.FUELCAN)){
			addPositive();
		}
		else if (collisionObject.getUserData().equals(GameLogic.ASTEROID)){
			addNegative();
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
