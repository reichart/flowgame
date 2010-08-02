package de.tum.in.flowgame.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
public class Person implements Serializable {

	@Id
	@Column(name = "id", nullable = false)
	private Long id;

	private String name;
	private String sex; // Facebook provides localized values, so no Enum here
	@Temporal(TemporalType.DATE)
	private Date dateOfBirth;
	private String place;

	@Transient
	private transient boolean newPlayer;
	
	/**
	 * Answers given to the initial profiling questionaire for first-time
	 * players.
	 */
	@OneToMany(cascade = CascadeType.PERSIST)
	private List<Answer> profilingAnswers;

	@SuppressWarnings("unused")
	private Person() {
		// for JPA
	}

	public Person(final long id, final String name) {
		this.id = id;
		this.name = name;
		this.newPlayer = true; // constructor call means new person
	}

	public boolean isNewPlayer() {
		return newPlayer;
	}
	
	public String getName() {
		return name;
	}
	
	public String getSex() {
		return sex;
	}

	public void setSex(final String sex) {
		this.sex = sex;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(final Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(final String place) {
		this.place = place;
	}

	public Long getId() {
		return id;
	}

	public List<Answer> getProfilingAnswers() {
		return profilingAnswers;
	}

	public void setProfilingAnswers(final List<Answer> profilingAnswers) {
		this.profilingAnswers = profilingAnswers;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Person) {
			return ((Person) obj).id.equals(this.id);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[id=" + id + ";name=" + name + "]";
	}
}
