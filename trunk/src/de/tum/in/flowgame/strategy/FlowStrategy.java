package de.tum.in.flowgame.strategy;


public interface FlowStrategy {
	public double calculateSpeed(Trend asteroidTrend, Trend fuelTrend, double speed);
}