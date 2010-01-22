package de.tum.in.flowgame.model;

import java.io.Serializable;

public class Score implements Serializable{
	private int id;
	private long score;
	
	public Score (int id, long score){
		this.id = id;
		this.score = score;
	}
	
	public int getId(){
		return id;
	}
	
	public long getScore(){
		return score;
	}
}
