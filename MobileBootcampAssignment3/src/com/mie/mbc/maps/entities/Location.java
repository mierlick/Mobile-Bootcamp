package com.mie.mbc.maps.entities;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@SuppressWarnings("serial")
@DatabaseTable(tableName = "location")
public class Location implements Serializable {
	
	@DatabaseField(generatedId = true)
	private int locationDataId;
	
	@DatabaseField(canBeNull = true, uniqueCombo = true)
	private String city;
	
	@DatabaseField(canBeNull = true, uniqueCombo = true)
	private String state;
	
	@DatabaseField(canBeNull = true, uniqueCombo = true)
	private String county;
	
	@DatabaseField(canBeNull = true, uniqueCombo = true)
	private String type;
	
	@DatabaseField(canBeNull = true, uniqueCombo = true)
	private String preferred;
	
	@DatabaseField(canBeNull = true, uniqueCombo = true)
	private String worldRegion;
	
	@DatabaseField(canBeNull = true, uniqueCombo = true)
	private String country;
	
	@DatabaseField(canBeNull = true, uniqueCombo = true)
	private String locationText;
	
	@DatabaseField(canBeNull = true, uniqueCombo = true)
	private String location;

	
	//Getters and Setters
	public int getLocationDataId() {
		return locationDataId;
	}

	public void setLocationDataId(int locationDataId) {
		this.locationDataId = locationDataId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPreferred() {
		return preferred;
	}

	public void setPreferred(String preferred) {
		this.preferred = preferred;
	}

	public String getWorldRegion() {
		return worldRegion;
	}

	public void setWorldRegion(String worldRegion) {
		this.worldRegion = worldRegion;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getLocationText() {
		return locationText;
	}

	public void setLocationText(String locationText) {
		this.locationText = locationText;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((county == null) ? 0 : county.hashCode());
		result = prime * result
				+ ((location == null) ? 0 : location.hashCode());
		result = prime * result + locationDataId;
		result = prime * result
				+ ((locationText == null) ? 0 : locationText.hashCode());
		result = prime * result
				+ ((preferred == null) ? 0 : preferred.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result
				+ ((worldRegion == null) ? 0 : worldRegion.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (county == null) {
			if (other.county != null)
				return false;
		} else if (!county.equals(other.county))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (locationDataId != other.locationDataId)
			return false;
		if (locationText == null) {
			if (other.locationText != null)
				return false;
		} else if (!locationText.equals(other.locationText))
			return false;
		if (preferred == null) {
			if (other.preferred != null)
				return false;
		} else if (!preferred.equals(other.preferred))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (worldRegion == null) {
			if (other.worldRegion != null)
				return false;
		} else if (!worldRegion.equals(other.worldRegion))
			return false;
		return true;
	}
	
	public String toFormattedString () {
		StringBuilder string = new StringBuilder();
		
		string.append("\nCounty: ");
		string.append(getCounty());
		string.append("\nCountry: ");
		string.append(getCountry());
		
		string.append("\nType: ");
		string.append(getType());
		string.append("\nPreffered: ");
		string.append(getPreferred());
		
		string.append("\nWorld Region: ");
		string.append(getWorldRegion());
		string.append("\nLocation: ");
		string.append(getLocation());
		
		return string.toString();
	}

}
