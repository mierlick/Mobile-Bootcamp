package com.mie.mbc.maps.entities;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

@SuppressWarnings("serial")
public class CrossReference implements Serializable {
	
	@DatabaseField(columnName = "zipCodeId", canBeNull = false, foreign = true, uniqueCombo = true)
	private ZipCode zipCode;
	
	@DatabaseField(columnName = "locationDataId", canBeNull = false, foreign = true, uniqueCombo = true)
	private Location location;

	
	//Getters and setters
	public ZipCode getZipCode() {
		return zipCode;
	}

	public void setZipCode(ZipCode zipCode) {
		this.zipCode = zipCode;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
	public String toString () {
		return location.getCity() + ", " + location.getState() + " " + zipCode.getZipCode();
	}
	
	public String toFormattedString () {
		return location.toFormattedString() + zipCode.toFormattedString();
	}

}
