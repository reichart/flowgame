package de.tum.in.flowgame;

import java.io.IOException;
import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.SharedGroup;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnElapsedTime;

import com.sun.j3d.utils.universe.SimpleUniverse;

public class CreateCollidablesBehavior extends Behavior {

	private long time = 1000;
	
	private WakeupCriterion wakeupEvent;
	
	BranchGroup collidableBranchGroup;
	SharedGroup sharedGroup;
	com.tornadolabs.j3dtree.Java3dTree tree;
	SimpleUniverse su;
	
	private CreateCollidablesBehavior() {
		//empty
	}
	
	public CreateCollidablesBehavior(BranchGroup collidableBranchGroup, SharedGroup sharedGroup) {
		this.collidableBranchGroup = collidableBranchGroup;
		this.sharedGroup = sharedGroup;
		wakeupEvent = new WakeupOnElapsedTime(time);
		this.tree = tree;
		this.su = su;
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
			new Collidable(collidableBranchGroup, sharedGroup, x);
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
