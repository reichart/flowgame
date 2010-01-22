package de.tum.in.flowgame.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Person implements Serializable {

	@Id
	@Column(name = "id", nullable = false)
	private Long id;

	String name;
	String sex; // Facebook provides localized values, so no Enum here
	@Temporal(TemporalType.DATE)
	Date dateOfBirth;
	String place;

	/**
	 * Answers given to the initial profiling questionaire for first-time
	 * players.
	 */
	List<Answer> profilingAnswers;

	@SuppressWarnings("unused") // for JPA
	private Person() {
		// empty
	}

	public Person(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Person) {
			if (((Person) obj).getId().equals(this.id)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
}
