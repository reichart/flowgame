package de.tum.in.flowgame.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Person extends AbstractEntity {
	enum Sex{MALE, FEMALE}; 
	
	String name;
	Sex sex;
	@Temporal(TemporalType.DATE)
	Date dateOfBirth;
	String place;
	
	public Person() {
		//empty
	}
	
}
