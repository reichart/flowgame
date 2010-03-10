package de.tum.in.flowgame.behavior;

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
	private final ShipNavigationBehavior forwardNavigator;
	private double speed;
	private GameLogic gameLogic;
	private FlowStrategy strategy;
	
	public SpeedChangeBehavior(final ShipNavigationBehavior forwardNavigator) {
		this.forwardNavigator = forwardNavigator;
	}

	@Override
	public void initialize() {
		this.wakeupOn(newFrame);
		this.strategy = new AverageTrendStrategy();
		//this.strategy = new FunctionStrategy(gameLogic, gameLogic.getDifficultyFunction().getSpeed());
	}
	private double averageTrend;
	private double speedIncrease = 0.3D;
	@Override
	@SuppressWarnings("unchecked")
	public void processStimulus(final Enumeration criteria) {
		if (gameLogic != null) {
			final Function fun = gameLogic.getDifficultyFunction().getSpeed();
			if (gameLogic.getCurrentScenarioRound().isBaselineRound()) {
				speed = strategy.calculateSpeed(gameLogic.getTrendRating(), speed);
				
			} else {
				speed = (fun == null) ? 0 : -fun.getValue(gameLogic.getElapsedTime());
			}
			gameLogic.setSpeed(speed);
			forwardNavigator.setFwdSpeed(speed);
		}
		wakeupOn(newFrame);
	}
	
	public void setGameLogic(final GameLogic gameLogic) {
		this.gameLogic = gameLogic;
		if(this.strategy instanceof AverageTrendStrategy) ((AverageTrendStrategy)strategy).setGameLogic(gameLogic);
	}
}