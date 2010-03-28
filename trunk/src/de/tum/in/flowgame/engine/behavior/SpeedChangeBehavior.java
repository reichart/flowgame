package de.tum.in.flowgame.engine.behavior;

import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnElapsedFrames;

import de.tum.in.flowgame.DefaultGameListener;
import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.GameLogicConsumer;
import de.tum.in.flowgame.Utils;
import de.tum.in.flowgame.model.Function;
import de.tum.in.flowgame.strategy.AverageTrendStrategy;
import de.tum.in.flowgame.strategy.FlowStrategy;
import de.tum.in.flowgame.strategy.FunctionStrategy;

public class SpeedChangeBehavior extends Behavior implements GameLogicConsumer, SpeedChangeBehaviorMBean {

	private final WakeupCriterion newFrame = new WakeupOnElapsedFrames(0);
	private final ShipNavigationBehavior forwardNavigator;
	private double speed;
	private GameLogic gameLogic;
	private FlowStrategy strategy;

	public SpeedChangeBehavior(final ShipNavigationBehavior forwardNavigator) {
		this.forwardNavigator = forwardNavigator;
		Utils.export(this);

	}

	@Override
	public void initialize() {
		this.wakeupOn(newFrame);
		this.strategy = new AverageTrendStrategy();
		// this.strategy = new FunctionStrategy();
	}

	@Override
	@SuppressWarnings("unchecked")
	public void processStimulus(final Enumeration criteria) {
		if (this.strategy instanceof FunctionStrategy && strategy.getFunction() == null) {
			strategy.setFunction(gameLogic.getDifficultyFunction().getSpeed());
		}

		if (gameLogic != null) {
			final Function fun = gameLogic.getDifficultyFunction().getSpeed();
			if (gameLogic.getCurrentScenarioRound().isBaselineRound()) {
				speed = strategy.calculateSpeed(gameLogic.getAsteroidTrend(), gameLogic.getFuelTrend(), speed);
			} else {
				speed = (fun == null) ? 0 : -fun.getValue(gameLogic.getElapsedTime());
			}

			forwardNavigator.setFwdSpeed(speed);
		}
		wakeupOn(newFrame);
	}

	public void setGameLogic(final GameLogic gameLogic) {
		this.gameLogic = gameLogic;
		this.gameLogic.addListener(new DefaultGameListener() {
			@Override
			public void gameStarted(GameLogic game) {
				if (strategy != null) {
					strategy.reset();
				}
			}
		});
	}

	public double getSpeed() {
		return -speed;
	}
}