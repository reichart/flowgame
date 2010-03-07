package strategy;

import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.model.Function;

public class FunctionStrategy implements FlowStrategy {

	private final GameLogic logic;
	private final Function function;

	public FunctionStrategy(final GameLogic logic, final Function function) {
		this.function = function;
		this.logic = logic;
	}

	public double calculateSpeed(final double trendRating, double speed) {
		speed = -function.getValue(logic.getPosition());
		if (speed > -30D) {
			speed = -30D;
		}
		return speed;
	}
}
