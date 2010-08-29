package de.tum.in.flowgame.client.engine.behavior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.tum.in.flowgame.DefaultGameListener;
import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.Utils;
import de.tum.in.flowgame.client.GameLogicConsumer;
import de.tum.in.flowgame.client.strategy.FlowStrategy;
import de.tum.in.flowgame.client.strategy.FunctionStrategy;
import de.tum.in.flowgame.client.strategy.FunctionStrategy2;
import de.tum.in.flowgame.model.Function;

public class SpeedChangeBehavior extends RepeatingBehavior implements GameLogicConsumer, SpeedChangeBehaviorMBean {

	private final ForwardBehavior forwardNavigator;
	private final TextureTransformBehavior ttb;
	private double speed;
	/**
	 * maximum speed player reaches during current round, used to calculate the baseline
	 */
	private double maxSpeed;
	private GameLogic gameLogic;
	/**
	 * strategy that decides over how the speed is changed
	 */
	private final FlowStrategy strategy;
	/**
	 * list of speeds during the round, used to calculate the baseline
	 */
	private List<Double> speeds;
	
	//debug value for speed at the start of each round
	private double startSpeed;

	public SpeedChangeBehavior(final ForwardBehavior forwardNavigator, final TextureTransformBehavior ttb) {
		this.forwardNavigator = forwardNavigator;
		this.ttb = ttb;
		this.strategy = new FunctionStrategy2();
		Utils.export(this);
		
		speeds = new ArrayList<Double>();
	}

	@Override
	protected void update() {
		if (!isPaused()) {
			if ((this.strategy instanceof FunctionStrategy || this.strategy instanceof FunctionStrategy2)
					&& (strategy.getFunction() == null || strategy.getFunction().getSpeed() == null)) {
				strategy.setFunction(gameLogic.getDifficultyFunction());
			}

			if (gameLogic != null) {
				final Function fun = gameLogic.getDifficultyFunction().getSpeed();
				if (gameLogic.getCurrentScenarioRound().isBaselineRound()) {
					speed = strategy.calculateSpeed(gameLogic.getAsteroidTrend(), gameLogic.getFuelTrend(), speed, getDeltaTime());
					maxSpeed = Math.max(speed, maxSpeed);
					gameLogic.setRating(strategy.getDifficultyRating(gameLogic.getAsteroidTrend(), gameLogic.getFuelTrend()));
				} else {
					long baselineSpeed = 0;
					if (gameLogic.getBaseline() != null) {
						baselineSpeed = gameLogic.getBaseline().getSpeed();
					}
					fun.configure(baselineSpeed, gameLogic.getCurrentScenarioRound().getExpectedPlaytime());
					speed = (fun == null) ? 0 : fun.getValue(gameLogic.getElapsedTime());
					gameLogic.setRating(gameLogic.getDifficultyFunction().getDifficultyRating(gameLogic.getElapsedTime()));
				}
				
				// Prevention from driving backwards
				if (speed < 30D) {
					speed = 30D;
				}
				
				if (startSpeed == 0) {
					startSpeed = speed;
				}

				speeds.add(speed);
				
				forwardNavigator.setFwdSpeed(-speed);
				ttb.setFwdSpeed(-speed);
			}
		}
	}

	public void setGameLogic(final GameLogic gameLogic) {
		this.gameLogic = gameLogic;
		if (gameLogic != null) {
			this.gameLogic.addListener(new DefaultGameListener() {

				@Override
				/**
				 * reset the strategy, speeds listed from previous games, and the maxSpeed the player obtained
				 * 
				 */
				public void gameStarted(GameLogic game) {
					speeds = new ArrayList<Double>();
					if (strategy != null) {
						strategy.reset();
					}
					maxSpeed = Double.MIN_VALUE;
				}

				@Override
				public void gameStopped(GameLogic game) {
					System.err.println("Speed at start of round: " + startSpeed);
					System.err.println("Speed at end of round: " + speed);
					startSpeed = 0;
					if (gameLogic.getCurrentScenarioRound().isBaselineRound()) {
						//						long baseline = (long) (maxSpeed * 0.9);
						Collections.sort(speeds);
						System.err.println("Speeds Size: " + speeds.size() + "\tPosition at 70% " + (int)(speeds.size() * 0.5f));
						System.err.println("maxSpeed " + maxSpeed);
						double baseline = speeds.get((int)(speeds.size() * 0.5f));
						System.err.println("baseline " + baseline);
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