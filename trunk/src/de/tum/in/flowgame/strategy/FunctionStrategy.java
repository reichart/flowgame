package de.tum.in.flowgame.strategy;

import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.model.Function;

public class FunctionStrategy implements FlowStrategy{
	
	private GameLogic logic;
	private Function function;
	
	public FunctionStrategy(GameLogic logic, Function function){
		this.function = function;
		this.logic = logic;
	}
	
	public double calculateSpeed(double trendRating, double speed) {
		speed = -function.getValue(logic.getPosition());
		if (speed > -30D) {
			speed = -30D;
		}
		return speed;
	}
}
