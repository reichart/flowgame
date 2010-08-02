package de.tum.in.flowgame.strategy;

import de.tum.in.flowgame.Utils;
import de.tum.in.flowgame.model.DifficultyFunction;

public class AverageTrendStrategy implements FlowStrategy, AverageTrendStrategyMBean {

	private double averageTrendRating;
	private double averageSpeed;

	public AverageTrendStrategy(){
		Utils.export(this);
	}
	
	public double calculateSpeed(Trend asteroidTrend, Trend fuelTrend, double speedValue, long deltaTime) {
		double trendRating = getTrendRating(asteroidTrend, fuelTrend);
		
		averageTrendRating =  (trendRating+100*averageTrendRating)/101;
		double delta = averageTrendRating - trendRating;
		if(delta<0) delta = -delta;
		if(delta<0.1) delta = 0.1;
		//TODO: allow for displaying this information 
		if(Math.round(trendRating*1000) < Math.round(averageTrendRating*1000-averageTrendRating*50) ){
			speedValue += delta;
		}
		if(Math.round(trendRating*1000) > Math.round(averageTrendRating*1000+averageTrendRating*50) ){
			speedValue -= delta;
		}
		if(speedValue > -60D){
			speedValue = -60D;
		}
		
		averageSpeed = (350*averageSpeed+speedValue)/351.0;
		
		return speedValue;
	}
	
	private double getTrendRating(Trend asteroidTrend, Trend fuelTrend) {
		double asteroidTrendRating = asteroidTrend.getShortRatio() + 3 / 4.0 * asteroidTrend.getMidRatio() + 1 / 4.0
				* asteroidTrend.getLongRatio();
		double fuelTrendRating = fuelTrend.getShortRatio() + 3 / 4.0 * fuelTrend.getMidRatio() + 1 / 4.0 * fuelTrend.getLongRatio();
		return 2*fuelTrendRating - asteroidTrendRating;
	}

	public DifficultyFunction getFunction() {
		return null;
	}

	public void setFunction(DifficultyFunction fun) {
		// empty
	}

	public double getAverageSpeed() {
		return averageSpeed;
	}

	public double getAverageTrendRating() {
		return averageTrendRating;
	}

	public void reset() {
		averageTrendRating = 0;
		averageSpeed = -60;		
	}

	public double getDifficultyRating(Trend asteroidTrend, Trend fuelTrend) {
		// TODO Unclear
		return 1;
	}
	
}