package de.tum.in.flowgame.behavior;

import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnElapsedFrames;

import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.model.Function;

public class SpeedChangeBehavior extends Behavior {

	private final WakeupCriterion newFrame = new WakeupOnElapsedFrames(0);
	private final ForwardNavigatorBehavior forwardNavigator;
	private double speed;
	private GameLogic gameLogic;

	public SpeedChangeBehavior(final ForwardNavigatorBehavior forwardNavigator,
			final GameLogic gameLogic) {
		this.forwardNavigator = forwardNavigator;
		this.gameLogic = gameLogic;
	}

	@Override
	public void initialize() {
		this.wakeupOn(newFrame);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void processStimulus(final Enumeration criteria) {
		if (gameLogic.getCurrentScenarioRound().getDifficutyFunction()
				.getSpeed() == null)
			speed = 0;
		else
			speed = gameLogic.getCurrentScenarioRound().getDifficutyFunction().getSpeed().getValue(gameLogic.getElapsedTime());
		forwardNavigator.setSpeed(speed);
		wakeupOn(newFrame);
	}
}
