package de.tum.in.flowgame;

import java.io.IOException;
import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnElapsedTime;

public class CreateCollidablesBehavior extends Behavior {

	private long time = 3000;
	
	private WakeupCriterion wakeupEvent;
	
	BranchGroup collidableBranchGroup;
	
	private CreateCollidablesBehavior() {
		//empty
	}
	
	public CreateCollidablesBehavior(BranchGroup collidableBranchGroup) {
		this.collidableBranchGroup = collidableBranchGroup;
		wakeupEvent = new WakeupOnElapsedTime(time);
	}

	@Override
	public void initialize() {
		wakeupOn(wakeupEvent);
	}

	@Override
	public void processStimulus(Enumeration criteria) {
		System.out.println("Create");
		float x = new Float(Math.random() * 3 - 1.5);
		try {
			new Collidable(collidableBranchGroup, 0.8f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		wakeupOn(wakeupEvent);
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

}
