package de.tum.in.flowgame.engine.behavior;

import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnElapsedFrames;

import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.GameLogicConsumer;
import de.tum.in.flowgame.model.Function;
import de.tum.in.flowgame.strategy.AverageTrendStrategy;
import de.tum.in.flowgame.strategy.FlowStrategy;

public class SpeedChangeBehavior extends Behavior implements GameLogicConsumer {

	private final WakeupCriterion newFrame = new WakeupOnElapsedFrames(0);
	private double speed;
	private GameLogic gameLogic;
	private FlowStrategy strategy;
	
	public SpeedChangeBehavior() {
	}

	@Override
	public void initialize() {
		this.wakeupOn(newFrame);
		this.strategy = new AverageTrendStrategy();
		//this.strategy = new FunctionStrategy(gameLogic, gameLogic.getDifficultyFunction().getSpeed());
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void processStimulus(final Enumeration criteria) {
		if (gameLogic != null) {
			final Function fun = gameLogic.getDifficultyFunction().getSpeed();
			if (gameLogic.getCurrentScenarioRound().isBaselineRound()) {
				speed = strategy.calculateSpeed(gameLogic.getAsteroidTrend(), gameLogic.getFuelTrend(), speed);
			} else {
				speed = (fun == null) ? 0 : -fun.getValue(gameLogic.getElapsedTime());
			}
		}
		wakeupOn(newFrame);
	}
	
	public void setGameLogic(final GameLogic gameLogic) {
		this.gameLogic = gameLogic;
	}
}