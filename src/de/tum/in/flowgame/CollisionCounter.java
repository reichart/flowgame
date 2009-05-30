package de.tum.in.flowgame;

public class CollisionCounter {

	private int positive = 0;
	private int negative = 0;
	private int points = 0;
	
	public CollisionCounter(){}

	public CollisionCounter(int points) {
		this.points = points;
	}

	public CollisionCounter(int positive, int negative) {
		this.positive = positive;
		this.negative = negative;
	}

	public CollisionCounter(int positive, int negative, int points) {
		this.positive = positive;
		this.negative = negative;
		this.points = points;
	}

	public void addPositive() {
		positive += 1;
		points += 1;
	}

	public void addNegative() {
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
