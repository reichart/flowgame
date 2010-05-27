package de.tum.in.flowgame.engine.behavior;

import de.tum.in.flowgame.DefaultGameListener;
import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.GameLogicConsumer;
import de.tum.in.flowgame.Utils;
import de.tum.in.flowgame.model.Function;
import de.tum.in.flowgame.strategy.FlowStrategy;
import de.tum.in.flowgame.strategy.FunctionStrategy;
import de.tum.in.flowgame.strategy.FunctionStrategy2;

public class SpeedChangeBehavior extends RepeatingBehavior implements GameLogicConsumer, SpeedChangeBehaviorMBean {

	private final ForwardBehavior forwardNavigator;
	private final TextureTransformBehavior ttb;
	private double speed;
	private double maxSpeed;
	private GameLogic gameLogic;
	private final FlowStrategy strategy;

	private boolean pause;

	public SpeedChangeBehavior(final ForwardBehavior forwardNavigator, final TextureTransformBehavior ttb) {
		this.forwardNavigator = forwardNavigator;
		this.ttb = ttb;
		this.strategy = new FunctionStrategy2();
		pause = false;
		Utils.export(this);
	}

	@Override
	protected void update() {
		if (!pause) {
			if ((this.strategy instanceof FunctionStrategy || this.strategy instanceof FunctionStrategy2)
					&& (strategy.getFunction() == null || strategy.getFunction().getSpeed() == null)) {
				strategy.setFunction(gameLogic.getDifficultyFunction());
			}

			if (gameLogic != null) {
				final Function fun = gameLogic.getDifficultyFunction().getSpeed();
				if (gameLogic.getCurrentScenarioRound().isBaselineRound()) {
					speed = strategy.calculateSpeed(gameLogic.getAsteroidTrend(), gameLogic.getFuelTrend(), speed);
					maxSpeed = Math.max(speed, maxSpeed);
					gameLogic.setRating(strategy.getDifficultyRating(gameLogic.getAsteroidTrend(), gameLogic.getFuelTrend()));
				} else {
					long speedAddFromDifficulty = 0;
					if (gameLogic.getBaseline() != null) {
						speedAddFromDifficulty = gameLogic.getBaseline().getSpeed();
					}
					speed = (fun == null) ? 0 : fun.getValue(gameLogic.getElapsedTime()) + speedAddFromDifficulty;
					gameLogic.setRating(gameLogic.getDifficultyFunction().getDifficultyRating(gameLogic.getElapsedTime()));
				}

				forwardNavigator.setFwdSpeed(-speed);
				ttb.setFwdSpeed(-speed);
				//forwardNavigator.setFwdSpeed(0);
			}
		}
	}

	public void setGameLogic(final GameLogic gameLogic) {
		this.gameLogic = gameLogic;
		if (gameLogic != null) {
			this.gameLogic.addListener(new DefaultGameListener() {
				@Override
				public void gameStarted(GameLogic game) {
					if (strategy != null) {
						strategy.reset();
					}
					pause = false;
					maxSpeed = Double.MIN_VALUE;
				}

				@Override
				public void gamePaused(GameLogic game) {
					pause = true;
				}

				@Override
				public void gameResumed(GameLogic game) {
					pause = false;
				}

				@Override
				public void gameStopped(GameLogic game) {
					// game.removeListener(this);
					if (gameLogic.getCurrentScenarioRound().isBaselineRound()) {
						long baseline = (long) (maxSpeed * 0.9);
						gameLogic.setBaseline(baseline);
					}
				}
			});
		}
	}

	public double getSpeed() {
		return speed;
	}
}