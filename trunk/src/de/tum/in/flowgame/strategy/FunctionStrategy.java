package de.tum.in.flowgame.strategy;

import de.tum.in.flowgame.model.Function;

public class FunctionStrategy implements FlowStrategy {
	
	private Function function;
	
	private double trendRating;
	private double previousTrend;
	
	private int currentPosition;
	
	public FunctionStrategy(Function function){
		this.function = function;
		currentPosition = 0;
	}
	
	public double calculateSpeed(Trend asteroidTrend, Trend fuelTrend, double speed) {
		speed = -function.getValue(currentPosition);
		if (speed > -30D) {
			speed = -30D;
		}
		return speed;
	}
	
	public double getPosition(Trend asteroidTrend, Trend fuelTrend) {
		previousTrend = trendRating;
		trendRating = getTrendRating(asteroidTrend, fuelTrend);
		if (trendRating < previousTrend) {
			currentPosition -= 1;
		} else {
			currentPosition += 1;
		}
		return currentPosition;
	}
	
	private double getTrendRating(Trend asteroidTrend, Trend fuelTrend) {
		double asteroidTrendRating = asteroidTrend.getShortRatio() + 3 / 4.0 * asteroidTrend.getMidRatio() + 1 / 4.0
				* asteroidTrend.getLongRatio();
		double fuelTrendRating = fuelTrend.getShortRatio() + 3 / 4.0 * fuelTrend.getMidRatio() + 1 / 4.0 * fuelTrend.getLongRatio();
		return 2*fuelTrendRating - asteroidTrendRating;
	}
	
}