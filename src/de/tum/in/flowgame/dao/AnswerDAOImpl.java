package de.tum.in.flowgame.dao;

import de.tum.in.flowgame.model.Answer;

public class AnswerDAOImpl extends GenericJPADAO<Answer, Integer> implements AnswerDAO {

	public AnswerDAOImpl() {
		super("IDP", Answer.class);
	}

}