package de.tum.in.flowgame.strategy;

import de.tum.in.flowgame.model.Function;


public interface FlowStrategy {
	public double calculateSpeed(Trend asteroidTrend, Trend fuelTrend, double speedValue);
	public void setFunction(Function fun);
	public Function getFunction();
	public void reset();
}