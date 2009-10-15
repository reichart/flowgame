package de.tum.in.flowgame.behavior;

import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnElapsedFrames;

import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.GameLogicConsumer;
import de.tum.in.flowgame.model.Function;

public class SpeedChangeBehavior extends Behavior implements GameLogicConsumer {

	private final WakeupCriterion newFrame = new WakeupOnElapsedFrames(0);
	private final ShipNavigationBehavior forwardNavigator;
	private double speed;
	private GameLogic gameLogic;

	public SpeedChangeBehavior(final ShipNavigationBehavior forwardNavigator) {
		this.forwardNavigator = forwardNavigator;
	}

	@Override
	public void initialize() {
		this.wakeupOn(newFrame);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void processStimulus(final Enumeration criteria) {
		if (gameLogic != null) {
			final Function fun = gameLogic.getDifficultyFunction().getSpeed();
			if (gameLogic.getCurrentScenarioRound().isBaselineRound()) {
				speed = -fun.getValue(gameLogic.getPosition());
				if (speed > -30D) {
					speed = -30D;
				}
			} else {
				speed = (fun == null) ? 0 : -fun.getValue(gameLogic.getElapsedTime());
			}
			
			forwardNavigator.setFwdSpeed(speed);
		}
		wakeupOn(newFrame);
	}
	
	@Override
	public void setGameLogic(final GameLogic gameLogic) {
		this.gameLogic = gameLogic;
	}
}