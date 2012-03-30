package com.matt.bootcamp.assignment1.sqlite;

import java.util.Date;

public class RunDateTime {
	private long id;
	private long runDateTime;

	@Override
	public String toString() {
		return new Date(runDateTime).toLocaleString();
	}

	public long getRunDateTime() {
		return runDateTime;
	}

	public void setRunDateTime(long runDateTime) {
		this.runDateTime = runDateTime;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
