package de.tum.in.flowgame.server;

import java.util.ArrayList;
import java.util.List;

import de.tum.in.flowgame.model.Questionnaire;

public class QuestionnaireDownloadAction extends GameDataAction<List<String>, List<Questionnaire>> {

	@Override
	protected List<Questionnaire> execute(final List<String> questionnaireNames) throws Exception {
		final List<Questionnaire> questionnaires = new ArrayList<Questionnaire>();
		for (final String name : questionnaireNames) {
			final Questionnaire qn = (Questionnaire) em.createQuery(
					"SELECT q FROM Questionnaire q WHERE q.name = :name")
					.setParameter("name", name)
					.getSingleResult();
			questionnaires.add(qn);
		}
		return questionnaires;
	}

}
