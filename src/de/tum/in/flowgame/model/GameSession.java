package de.tum.in.flowgame.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class GameSession extends AbstractEntity {
	private ScenarioSession scenarioSession;
	@ManyToOne(cascade = CascadeType.PERSIST)
	private Person player;
	private Difficulty baseline;
	@OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	private final List<GameRound> rounds;
	@OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	private List<Answer> answers;

	/**
	 * Amount of time (in milliseconds) used to answer all questions for this
	 * session (excluding round questionnaire).
	 */
	private long answeringTime;
	
	@Column(nullable = false, length = 3)
	private String language;
	
	private GameSession() { // for JPA
		this.rounds = new ArrayList<GameRound>();
	}

	public GameSession(final long playerId, final ScenarioSession scenarioSession) {
		this();
		this.player = new Person(playerId, null);
		this.scenarioSession = scenarioSession;
		
		// Only this constructor gets called on the client (not the no-arg one),
		// so only here we can access the client's locale and get its language
		this.language = Locale.getDefault().getLanguage();
	}

	public GameRound newRound(final ScenarioRound nextRound) {
		final GameRound round = new GameRound(nextRound);
		rounds.add(round);
		return round;
	}
	
	public Person getPlayer() {
		return player;
	}

	public Difficulty getBaseline() {
		return baseline;
	}

	public List<GameRound> getRounds() {
		return rounds;
	}
	
	public long getHighscore (){
		long highscore = 0;
		for (GameRound gr : rounds) {
			if (gr.getScore().getScore() > highscore){
				highscore = gr.getScore().getScore();
			}
		}
		return highscore;
	}

	public ScenarioSession getScenarioSession() {
		return scenarioSession;
	}

	public List<Answer> getAnswers() {
		return answers;
	}
	
	public void setAnswers(final List<Answer> answers, final long answeringTime) {
		this.answers = answers;
		this.answeringTime = answeringTime;
	}

	public long getAnsweringTime() {
		return answeringTime;
	}
	
	public void setBaseline(Difficulty baseline) {
		this.baseline = baseline;		
	}

	public String getLanguage() {
		return language;
	}
}