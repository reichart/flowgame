package de.tum.in.flowgame.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import de.tum.in.flowgame.DefaultGameListener;
import de.tum.in.flowgame.GameListener;
import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.model.Collision.Item;
import de.tum.in.flowgame.model.ConfigChange.ConfigKey;

@Entity
public class GameRound extends AbstractEntity {
	private ScenarioRound scenarioRound;
	private long actualPlaytime;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Collision> collisions;
	@OneToMany(fetch = FetchType.EAGER)
	private List<Answer> answers;
	
	private long startTime;
	private long endTime;
	
	private long score;
	@OneToMany(fetch = FetchType.EAGER)   @OrderBy("timestamp")   
	private List<ConfigChange> configChanges;
	/**
	 * rank within friends at the time the round was played
	 */
	private int socialRank;
	private double globalRank;

	@Transient
	private transient GameListener listener;
	
	/**
	 * Amount of time (in milliseconds) used to answer all questions for this
	 * round.
	 */
	private long answeringTime;

	private GameRound() { // for JPA
		this.collisions = new ArrayList<Collision>();
		this.answers = new ArrayList<Answer>();
		this.configChanges = new ArrayList<ConfigChange>();
		this.startTime = System.currentTimeMillis();
		this.listener = new DefaultGameListener() {
			@Override
			public void gameStarted(final GameLogic game) {
				setStartTime(System.currentTimeMillis());
			}

			@Override
			public void gameStopped(final GameLogic game) {
				actualPlaytime = game.getElapsedTime();
				game.removeListener(this);
			}

			@Override
			public void collided(GameLogic logic, Item item) {
				collisions.add(new Collision(item));
			}
		};
	}

	public GameRound(final ScenarioRound scenarioRound) {
		this();
		
		if (scenarioRound == null) {
			throw new IllegalArgumentException("scenario round is null");
		}
		this.scenarioRound = scenarioRound;
	}

	public ScenarioRound getScenarioRound() {
		return scenarioRound;
	}

	public List<Collision> getCollisions() {
		return collisions;
	}

	public int getCollisionsWithFuelcans() {
		return getCollisions(Item.FUELCAN);
	}
	
	public int getCollisionsWithAsteroids() {
		return getCollisions(Item.DIAMOND);
	}
	
	/**
	 * @param item
	 *            the item of interest
	 * @return the number of collisions with the item
	 */
	public int getCollisions(final Item item) {
		int sum = 0;
		for (final Collision collision : collisions) {
			if (item.equals(collision.getObject())) {
				sum++;
			}
		}
		return sum;
	}

	public long getActualPlaytime() {
		return actualPlaytime;
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public void addAnswers(final List<Answer> answers, final long answeringTime) {
		this.answers.addAll(answers);
		this.answeringTime += answeringTime;
	}

	public long getAnsweringTime() {
		return answeringTime;
	}
	
	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(final long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}
	
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	
	public Score getScore() {
		return new Score(startTime, score);
	}

	public void increaseScore(final long points) {
		this.score += points;
		if (score < 0) {
			score = 0L;
		}
	}

	public void setScore(Long score) {
		this.score = score;
	}

	public GameListener getListener() {
		return listener;
	}

	public int getSocialRank() {
		return socialRank;
	}

	public void setSocialRank(int socialRank) {
		this.socialRank = socialRank;
	}

	public double getGlobalRank() {
		return globalRank;
	}

	public void setGlobalRank(double globalRank) {
		this.globalRank = globalRank;
	}

	public void configChange (ConfigKey key, String value){
		this.configChanges.add(new ConfigChange(key, value));
	}
}