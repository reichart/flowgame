package de.tum.in.flowgame.model;

import java.util.Date;

import javax.persistence.Entity;

@Entity
public class ConfigChange extends AbstractEntity implements Comparable<ConfigChange> {

	public enum ConfigKey {
		SOUND, STEERING, PAUSE
	}

	private final long timestamp;
	private final ConfigKey configKey;
	private final String value;

	public ConfigChange(final ConfigKey key, final String value) {
		this.timestamp = System.currentTimeMillis();
		this.configKey = key;
		this.value = value;
	}
	
	@SuppressWarnings("unused") // for JPA
	private ConfigChange() {
		this.configKey = null;
		this.value = null;
		this.timestamp = 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((configKey == null) ? 0 : configKey.hashCode());
		result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ConfigChange other = (ConfigChange) obj;
		if (configKey == null) {
			if (other.configKey != null)
				return false;
		} else if (!configKey.equals(other.configKey))
			return false;
		if (timestamp != other.timestamp)
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	public Date getTimestamp() {
		return new Date(timestamp);
	}

	public ConfigKey getKey() {
		return configKey;
	}

	public String getValue() {
		return value;
	}

	public int compareTo(final ConfigChange o) {
		return Long.valueOf(this.timestamp).compareTo(o.timestamp);
	}
}
