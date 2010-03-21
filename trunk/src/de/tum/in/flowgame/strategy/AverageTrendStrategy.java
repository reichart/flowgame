package de.tum.in.flowgame.strategy;


public class AverageTrendStrategy implements FlowStrategy {

	private double averageTrendRating;
	private double averageSpeed;

	public AverageTrendStrategy(){
		averageTrendRating = 0;
		averageSpeed = -60;
	}
	
	public double calculateSpeed(Trend asteroidTrend, Trend fuelTrend, double speed) {
		double trendRating = getTrendRating(asteroidTrend, fuelTrend);
		
		averageTrendRating =  (trendRating+100*averageTrendRating)/101;
		double delta = averageTrendRating - trendRating;
		if(delta<0) delta = -delta;
		if(delta<0.1) delta = 0.1;
		//TODO: allow for displaying this information 
//		logic.setAverageTrend(averageTrend);
		if(Math.round(trendRating*1000) < Math.round(averageTrendRating*1000-averageTrendRating*50) ){
			speed += delta;
		}
		if(Math.round(trendRating*1000) > Math.round(averageTrendRating*1000+averageTrendRating*50) ){
			speed -= delta;
		}
		if(speed > -60D){
			speed = -60D;
		}
		
		averageSpeed = (350*averageSpeed+speed)/351.0;
//		logic.setAverageSpeed(averageSpeed);
		
		return speed;
	}
	
	private double getTrendRating(Trend asteroidTrend, Trend fuelTrend) {
		double asteroidTrendRating = asteroidTrend.getShortRatio() + 3 / 4.0 * asteroidTrend.getMidRatio() + 1 / 4.0
				* asteroidTrend.getLongRatio();
		double fuelTrendRating = fuelTrend.getShortRatio() + 3 / 4.0 * fuelTrend.getMidRatio() + 1 / 4.0 * fuelTrend.getLongRatio();
		return 2*fuelTrendRating - asteroidTrendRating;
	}
	
}