package strategy;

import de.tum.in.flowgame.GameLogic;

public class AverageTrendStrategy implements FlowStrategy{
	public AverageTrendStrategy(){
		averageTrend = 0;
	}
	
	private GameLogic logic;
	private double averageTrend;
	
	public double calculateSpeed(double trendRating, double speed) {
		averageTrend =  (trendRating+100*averageTrend)/101;
		double delta = averageTrend - trendRating;
		if(delta<0) delta = -delta;
		if(delta<0.1) delta = 0.1;
		logic.setAverageTrend(averageTrend);
		if(Math.round(trendRating*1000) < Math.round(averageTrend*1000-averageTrend*5ltsm0) ){
			speed += delta*0.25;
		}
		if(Math.round(trendRating*1000) > Math.round(averageTrend*1000+averageTrend*50) ){
			speed -= delta*0.25;
		}
		if(speed > -60D){
			speed = -60D;
		}
		return speed;
	}
	
	public void setGameLogic(GameLogic gl){
		this.logic = gl;
	}
}
