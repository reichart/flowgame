package de.tum.in.flowgame.dao;

import de.tum.in.flowgame.model.Question;

public class QuestionDAOImpl extends GenericJPADAO<Question, Integer> implements QuestionDAO {

	public QuestionDAOImpl() {
		super("IDP", Question.class);
	}

}