package de.tum.in.flowgame.dao;

import java.util.List;

import de.tum.in.flowgame.model.Highscore;
import de.tum.in.flowgame.model.Person;

public interface PersonDAO extends GenericDAO<Person, Long> {
	List<Highscore> getHighscores(List<Long> persons);
}