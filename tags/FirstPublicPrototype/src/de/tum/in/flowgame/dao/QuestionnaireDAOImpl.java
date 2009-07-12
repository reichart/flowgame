package de.tum.in.flowgame.dao;

import de.tum.in.flowgame.model.Questionnaire;

public class QuestionnaireDAOImpl extends GenericJPADAO<Questionnaire, Integer> implements QuestionnaireDAO {

	public QuestionnaireDAOImpl() {
		super("IDP", Questionnaire.class);
	}

}