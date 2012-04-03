package com.mie.mbc.assignment2;

public enum Quake {
	
	DATABASE_NAME("quakes"),
	DATABASE_TABLE_NAME("quaketable"),
	QUAKE_ID("_id"),
	QUAKE_TITLE("quake_title"),
	QUAKE_LATITUDE("quake_latitud"),
	QUAKE_LONGITUDE("quake_longitude"),
	QUAKE_TIME ("quake_time"),
	QUAKE_MAGNITUDE("quake_magnitude"),
	QUAKE_LINK("quake_link"),
	
	RSS_QUAKE_TITLE("/title"),
	RSS_QUAKE_LATITUDE("/geo:lat"),
	RSS_QUAKE_LONGITUDE("/geo:long"),
	RSS_QUAKE_TIME ("/description"),
	RSS_QUAKE_MAGNITUDE("/dc:subject"),
	RSS_QUAKE_LINK("/link");
	
	String value;


	private Quake(String name) {
		value = name;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString () {
		return value;
	}
	
}
