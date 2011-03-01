package de.tum.in.flowgame.server;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.tum.in.flowgame.model.Answer;
import de.tum.in.flowgame.model.GameRound;
import de.tum.in.flowgame.model.GameSession;
import de.tum.in.flowgame.model.Person;

public class SPSSDataExportAction extends DatabaseAction {

	private static final String DELIM_FIELD = " ";
	private static final String DELIM_RECORD = "\r\n";

	private final StringBuilder output;
	private boolean firstField;

	public SPSSDataExportAction() {
		this.output = new StringBuilder();
		this.firstField = true;

		output.append("# user(id,sex,age,personality[1..n]),###,session(id,sessiontype,scoringtype,language,highscore),###,gameround(id, difficulty, asteroids, fuelcans, score, globalRank, socialRank, answeringtime, flow+reqfit[0..1])");
		next();
	}

	@Override
	public String execute() throws Exception {
		for (final GameSession session : list(GameSession.class)) {
			for (final GameRound gameRound : session.getRounds()) {
				output(session.getPlayer());
				field("###");
				output(session);
				field("###");
				output(gameRound, session);
				next();
			}
		}
		return SUCCESS;
	}

	private void output(final Person person) {
		field(person.getId());
		field(normalizeSex(person));
		field(person.getAge());
		output(person.getProfilingAnswers());
	}

	private String normalizeSex(final Person person) {
		final String sex = person.getSex();
		if ("male".equals(sex) || "m&auml;nnlich".equals(sex) || "lad".equals(sex)) {
			return "m";
		} else if ("weiblich".equals(sex) || "female".equals(sex) || "lass".equals(sex)) {
			return "w";
		} else {
			return "-";
		}
	}

	private void output(final GameSession session) {
		field(session.getId());
		field(session.getSessionType()); // ansteigend/kontinuierlich/variabel
		field(session.getType()); // indiv/social
		field(session.getLanguage());
		field(session.getHighscore());
	}

	private void output(final GameRound round, final GameSession session) {
		field(round.getId());
		field(getAbsoluteDifficulty(round, session));
		field(round.getCollisionsWithAsteroids());
		field(round.getCollisionsWithDiamonds());
		field(round.getScore().getScore());
		field(round.getGlobalRank());
		field(round.getSocialRank());
		field(round.getAnsweringTime());
		output(round.getAnswers());
	}

	private double getAbsoluteDifficulty(final GameRound round, final GameSession session) {
		final double offset = round.getScenarioRound().getDifficultyFunction().getSpeed().getInitialValue();
		final long baseline = session.getBaseline().getSpeed();
		return baseline + offset;
	}
	
	private static class OrderingByQuestionNumber implements Comparator<Answer> {
		public int compare(final Answer o1, final Answer o2) {
			final int n1 = o1.getQuestion().getNumber();
			final int n2 = o2.getQuestion().getNumber();
			return Integer.valueOf(n1).compareTo(n2);
		}
	}

	private void output(final List<Answer> answers) {
		Collections.sort(answers, new OrderingByQuestionNumber());
		for (final Answer answer : answers) {
			field(answer.getAnswer());
		}
	}

	private void field(final Object obj) {
		if (!firstField) {
			output.append(DELIM_FIELD);
		}
		output.append(String.valueOf(obj));
		firstField = false;
	}

	private void next() {
		output.append(DELIM_RECORD);
		firstField = true;
	}

	public Object getOutput() {
		return output.toString();
	}

}
