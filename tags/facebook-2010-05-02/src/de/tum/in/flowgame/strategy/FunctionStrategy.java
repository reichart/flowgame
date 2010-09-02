package de.tum.in.flowgame.strategy;

import de.tum.in.flowgame.Utils;
import de.tum.in.flowgame.model.Function;

public class FunctionStrategy implements FlowStrategy, FunctionStrategyMBean {

	private Function function;

	private int currentPosition;

	private float longTerm;

	private float midTerm;

	private float shortTerm;

	public FunctionStrategy() {
		Utils.export(this);
	}

	public int getCurrentPosition() {
		return currentPosition;
	}

	public double calculateSpeed(Trend asteroidTrend, Trend fuelTrend, double speedValue) {
		double speed = function.getValue(getPosition(asteroidTrend, fuelTrend));

		// Prevention from driving backwards
		if (speed < 60D) {
			speed = 60D;
		}
		return speed;
	}

	private double getPosition(Trend asteroidTrend, Trend fuelTrend) {
		shortTerm = (1 - asteroidTrend.getShortRatio()) + fuelTrend.getShortRatio();
		midTerm = (1 - asteroidTrend.getMidRatio()) + fuelTrend.getMidRatio();
		longTerm = (1 - asteroidTrend.getLongRatio()) + fuelTrend.getLongRatio();

		// at the beginning always get faster until we have enough data
		if (longTerm != 0) {
			// get slower if you hit to many asteroids
			if (asteroidTrend.getShortRatio() > 0.3) {
				currentPosition--;
			} else {
				// the higher the shortTerm value the better plays the player
				// get faster for high values slower for low ones
				// shortTerm = 1 basically means the player plays neutral
				if (shortTerm > 1.3) {
					currentPosition += 2;
				} else if (shortTerm >= 1) {
					currentPosition += 1;
				} else if (shortTerm < 0.3) {
					currentPosition -= 2;
				} else if (shortTerm < 1) {
					currentPosition -= 1;
				}
			}
		} else {
			currentPosition++;
		}

		return currentPosition;
	}

	public Function getFunction() {
		return function;
	}

	public void setFunction(Function fun) {
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

}