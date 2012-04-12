package com.mie.mbc.maps.entities;

import java.io.Serializable;
import java.text.NumberFormat;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@SuppressWarnings("serial")
@DatabaseTable(tableName = "zipCode")
public class ZipCode implements Serializable {
	
	@DatabaseField(id = true)
	private int zipCodeId;
	
	@DatabaseField(canBeNull = false, unique = true)
	private String zipCode;
	
	@DatabaseField(canBeNull = true)
	private double latitude;
	
	@DatabaseField(canBeNull = true)
	private double longitude;
	
	@DatabaseField(canBeNull = true)
	private int population;
	
	@DatabaseField(canBeNull = true)
	private int housingUnits;
	
	@DatabaseField(canBeNull = true)
	private double income;
	
	@DatabaseField(canBeNull = true)
	private double landArea;
	
	@DatabaseField(canBeNull = true)
	private double waterArea;
	
	@DatabaseField(canBeNull = true)
	private String militaryRestrictionCodes;
	
	
	//Getters and setters
	public int getZipCodeId() {
		return zipCodeId;
	}

	public void setZipCodeId(int zipCodeId) {
		this.zipCodeId = zipCodeId;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public int getPopulation() {
		return population;
	}

	public void setPopulation(int population) {
		this.population = population;
	}

	public int getHousingUnits() {
		return housingUnits;
	}

	public void setHousingUnits(int housingUnits) {
		this.housingUnits = housingUnits;
	}

	public double getIncome() {
		return income;
	}

	public void setIncome(double income) {
		this.income = income;
	}

	public double getLandArea() {
		return landArea;
	}

	public void setLandArea(double landArea) {
		this.landArea = landArea;
	}

	public double getWaterArea() {
		return waterArea;
	}

	public void setWaterArea(double waterArea) {
		this.waterArea = waterArea;
	}

	public String getMilitaryRestrictionCodes() {
		return militaryRestrictionCodes;
	}

	public void setMilitaryRestrictionCodes(String militaryRestrictionCodes) {
		this.militaryRestrictionCodes = militaryRestrictionCodes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((zipCode == null) ? 0 : zipCode.hashCode());
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
		ZipCode other = (ZipCode) obj;
		if (zipCode == null) {
			if (other.zipCode != null)
				return false;
		} else if (!zipCode.equals(other.zipCode))
			return false;
		return true;
	}
	
	public String toFormattedString () {
		StringBuilder string = new StringBuilder();
		NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
		
		string.append("\nLatitude: ");
		string.append(getLatitude());
		string.append("\nLongitude: ");
		string.append(getLongitude());
		
		string.append("\nLand Area: ");
		string.append(getLandArea());
		string.append("\nWater Area: ");
		string.append(getWaterArea());
		
		string.append("\nPopulation: ");
		string.append(getPopulation());
		
		string.append("\nHounsing Units: ");
		string.append(getHousingUnits());
		
		string.append("\nIncome: ");
		string.append( currencyFormatter.format(getIncome()) );
		
		string.append("\nMilitary Restriction Codes: ");
		string.append(getMilitaryRestrictionCodes());

		return string.toString();
	}

}
