package de.tum.in.flowgame.strategy;

import de.tum.in.flowgame.model.DifficultyFunction;


public interface FlowStrategy {
	public double calculateSpeed(Trend asteroidTrend, Trend fuelTrend, double speedValue);
	public void setFunction(DifficultyFunction fun);
	public DifficultyFunction getFunction();
	public void reset();
	public double getDifficultyRating(Trend asteroidTrend, Trend fuelTrend);
}