package de.tum.in.flowgame.client;

import java.io.Serializable;


public class HighscoreRequest implements Serializable{
	private long personId;
	private int numElements;

	public HighscoreRequest (long personId, int numElements){
		this.personId = personId;
		this.numElements = numElements;
	}
	
	public long getPersonId(){
		return personId;
	}
	
	public int getNumElements(){
		return numElements;
	}
}
