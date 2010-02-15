package de.tum.in.flowgame.model;

import java.io.Serializable;

public class Score implements Serializable{
	private long startTime;
	private long score;
	
	public Score (long startTime, long score){
		this.startTime = startTime;
		this.score = score;
	}
	
	public long getStartTime(){
		return startTime;
	}
	
	public long getScore(){
		return score;
	}
}
