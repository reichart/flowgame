package de.tum.in.flowgame.strategy;

import de.tum.in.flowgame.GameLogic;

public class AverageTrendStrategy implements FlowStrategy{
	public AverageTrendStrategy(){
		averageTrend = 0;
		averageSpeed = -60;
	}
	
	private GameLogic logic;
	private double averageTrend;
	private double averageSpeed;
	
	
	public double calculateSpeed(double trendRating, double speed) {
		averageTrend =  (trendRating+100*averageTrend)/101;
		double delta = averageTrend - trendRating;
		if(delta<0) delta = -delta;
		if(delta<0.1) delta = 0.1;
		logic.setAverageTrend(averageTrend);
		if(Math.round(trendRating*1000) < Math.round(averageTrend*1000-averageTrend*25) ){
			speed += delta;
		}
		if(Math.round(trendRating*1000) > Math.round(averageTrend*1000+averageTrend*25) ){
			speed -= delta;
		}
		if(speed > -60D){
			speed = -60D;
		}
		
		averageSpeed = (350*averageSpeed+speed)/351;
		logic.setAverageSpeed(averageSpeed);
		
		return speed;
	}
	
	public void setGameLogic(GameLogic gl){
		this.logic = gl;
	}
}
