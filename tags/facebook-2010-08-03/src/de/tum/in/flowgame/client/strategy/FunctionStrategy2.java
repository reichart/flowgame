package de.tum.in.flowgame.client.strategy;

import de.tum.in.flowgame.Utils;
import de.tum.in.flowgame.model.DifficultyFunction;

public class FunctionStrategy2 implements FlowStrategy, FunctionStrategy2MBean {

	private DifficultyFunction function;

	private long currentPosition;

	private float longTerm;

	private float midTerm;

	private float shortTerm;

	public FunctionStrategy2() {
		Utils.export(this);
	}

	public long getCurrentPosition() {
		return currentPosition;
	}

	public double calculateSpeed(Trend asteroidTrend, Trend fuelTrend, double speedValue, long deltaTime) {
		double speed = function.getSpeed().getValue(getPosition(asteroidTrend, fuelTrend, deltaTime));

		// Prevention from driving backwards
		if (speed < 30D) {
			speed = 30D;
		}
		return speed;
	}

	private double getPosition(Trend asteroidTrend, Trend fuelTrend, long deltaTime) {
		shortTerm = (1 - asteroidTrend.getShortRatio()) + fuelTrend.getShortRatio();
		midTerm = (1 - asteroidTrend.getMidRatio()) + fuelTrend.getMidRatio();
		longTerm = (1 - asteroidTrend.getLongRatio()) + fuelTrend.getLongRatio();

		if (fuelTrend.getPassedItems() > 10) {
			//get slower when hit by many asteroids or missed many fuel cans
			if (asteroidTrend.getShortRatio() > 0.15 && fuelTrend.getShortRatio() < 0.3) {
				currentPosition -= deltaTime/6.0;
			//get a lot faster when player collected more than 2/3 of the fuel cans
			} else if (fuelTrend.getShortRatio() > 0.6){
				currentPosition += deltaTime/3.0;
			//get slightly faster when player collected more than 1/3 of the fuel cans on mid term
			} else if (fuelTrend.getMidRatio() > 0.3) {
				currentPosition += deltaTime/6.0;
			//get slightly slower when player is neither especially bad nor good
			} else {
				currentPosition -= deltaTime/6.0;
			}
		} else {
			currentPosition += deltaTime/6.0;
		}

		return currentPosition;
	}

	public DifficultyFunction getFunction() {
		return function;
	}

	public void setFunction(DifficultyFunction fun) {
		function = fun;
	}

	public void reset() {
		currentPosition = 0;
	}

	public float getLongTerm() {
		return longTerm;
	}

	public float getMidTerm() {
		return midTerm;
	}

	public float getShortTerm() {
		return shortTerm;
	}
	
	public double getDifficultyRating(Trend asteroidTrend, Trend fuelTrend) {
		return function.getDifficultyRating(getPosition(asteroidTrend, fuelTrend, 0));
	}

}