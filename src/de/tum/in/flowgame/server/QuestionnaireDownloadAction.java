package de.tum.in.flowgame.server;

import java.util.ArrayList;
import java.util.List;

import de.tum.in.flowgame.model.Questionnaire;

public class QuestionnaireDownloadAction extends GameDataAction<List<String>, List<Questionnaire>> {

	@Override
	@SuppressWarnings("unchecked")
	protected List<Questionnaire> execute(final List<String> questionnaireNames) throws Exception {
		return transform(em.createQuery("SELECT q FROM Questionnaire q " +
			"WHERE q.name IN :names")
			.setParameter("names", questionnaireNames)
			.getResultList());
	}
	
	private List<Questionnaire> transform(final List<Object> questionnaires) {
		final List<Questionnaire> list = new ArrayList<Questionnaire>();
		for (final Object objects : questionnaires) {
			list.add((Questionnaire) objects);
		}
		return list;
	}

}