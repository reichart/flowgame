package de.tum.in.flowgame.strategy;

import de.tum.in.flowgame.Utils;
import de.tum.in.flowgame.model.Function;

public class FunctionStrategy implements FlowStrategy, FunctionStrategyMBean {
	
	private Function function;
	
	private int currentPosition;
	
	public FunctionStrategy(){
		Utils.export(this);
	}
	
	public int getCurrentPosition() {
		return currentPosition;
	}
	
	public double calculateSpeed(Trend asteroidTrend, Trend fuelTrend, double speedValue) {
		double speed = -function.getValue(getPosition(asteroidTrend, fuelTrend));
		
		//Prevention from driving backwards
		if (speed > -60D) {
			speed = -60D;
		}
		return speed;
	}
	
	private double getPosition(Trend asteroidTrend, Trend fuelTrend) {
		double shortTerm = -asteroidTrend.getShortRatio();
		double midTerm = -asteroidTrend.getMidRatio();
		double longTerm = -asteroidTrend.getLongRatio();
		
		//get faster if player improved
		if (shortTerm > midTerm && shortTerm > longTerm) {
			currentPosition += 1;
		}
		//get slower if player did worse
		else if (shortTerm < midTerm && shortTerm < longTerm) {
			currentPosition -= 1;
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
	
}