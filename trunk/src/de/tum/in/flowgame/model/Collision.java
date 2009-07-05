package de.tum.in.flowgame.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import de.tum.in.flowgame.GameLogic.Item;

@Entity
public class Collision extends AbstractEntity{
	@Temporal(TemporalType.TIMESTAMP)
	Date time;
	Item object;
}
