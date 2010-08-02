package de.tum.in.flowgame.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import de.tum.in.flowgame.DefaultGameListener;
import de.tum.in.flowgame.GameListener;
import de.tum.in.flowgame.GameLogic;
import de.tum.in.flowgame.model.Collision.Item;

@Entity
public class GameRound extends AbstractEntity {
	private ScenarioRound scenarioRound;
	private long actualPlaytime;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Collision> collisions;
	@OneToMany(fetch = FetchType.EAGER)
	private List<Answer> answers;
	private long startTime;
	private long score;
	/**
	 * rank within friends at the time the round was played
	 */
	private int socialRank;
	private double globalRank;

	@Transient
	private transient GameListener listener;

	@SuppressWarnings("unused")
	private GameRound() { // for JPA
		this(null);
	}

	public GameRound(final ScenarioRound scenarioRound) {
		this.collisions = new ArrayList<Collision>();
		this.answers = new ArrayList<Answer>();
		this.startTime = System.currentTimeMillis();
		this.scenarioRound = scenarioRound;
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
		return getCollisions(Item.ASTEROID);
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

	public void setAnswers(final List<Answer> answers) {
		this.answers = answers;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(final long startTime) {
		this.startTime = startTime;
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

}