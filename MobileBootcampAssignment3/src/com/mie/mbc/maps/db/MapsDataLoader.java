package com.mie.mbc.maps.db;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.csvreader.CsvReader;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.mie.mbc.maps.entities.CrossReference;
import com.mie.mbc.maps.entities.Location;
import com.mie.mbc.maps.entities.ZipCode;

public class MapsDataLoader {
	
	private MySQLiteHelper databaseHelper;
	private Context context;
	private int zipCodeFailues = 0,
				locationFailues = 0,
				crossReferenceFailues = 0;
	
	public MapsDataLoader (MySQLiteHelper databaseHelper, Context context) {
		this.databaseHelper = databaseHelper;
		this.context = context;
	}
	
	/**
	 * Create ZipCode in DB
	 * @param zipCode
	 */
	private void createZipCode (ZipCode zipCode) {
    	Dao<ZipCode, Integer> zipCodeDao;
    	
        try {
			zipCodeDao = databaseHelper.getZipCodeDAO();
            zipCodeDao.create(zipCode);
        } catch (SQLException e) {
        	zipCodeFailues++;
        }
    	
    }
	
	/**
	 * Create Location in DB
	 * @param location
	 */
	private void createLocation (Location location) {
    	Dao<Location, Integer> locationDao;
    	
        try {
        	locationDao = databaseHelper.getLocationDAO();
        	locationDao.create(location);
        } catch (SQLException e) {
        	locationFailues++;
        }
    	
    }
	
	
	/**
	 * Create CrossReference in DB
	 * @param reference
	 */
	@SuppressWarnings({ "unchecked" })
	private void createCrossReference (CrossReference reference) {
    	Dao<CrossReference, Integer> crossReferenceDao;
    	
        try {
        	if (reference.getLocation().getLocationDataId() == 0) {
        		Dao<Location, Integer> locationDAO = databaseHelper.getLocationDAO();
        		Location location = reference.getLocation();
        		
        		QueryBuilder<Location, Integer> queryBuilder = locationDAO.queryBuilder();
        		Where<Location, Integer> where = queryBuilder.where();
        		
        		where.and(
        				where.eq("location", location.getLocation()),
        				where.eq("locationText", location.getLocationText()),
        				where.eq("city", location.getCity()),
        				where.eq("state", location.getState()),
        				where.eq("county", location.getCounty()),
        				where.eq("country", location.getCountry()),
        				where.eq("preferred", location.getPreferred()),
        				where.eq("type", location.getType()),
        				where.eq("worldRegion", location.getWorldRegion())
        				);
        		
    			PreparedQuery<Location> preparedQuery = queryBuilder.prepare();
    			
    			List<Location> locations = locationDAO.query(preparedQuery);
    			if (locations.size() > 0) {
    				reference.setLocation(locations.get(0));
    			}
        	}
        	
        	crossReferenceDao = databaseHelper.getCrossReferenceDAO();
        	crossReferenceDao.create(reference);
        } catch (SQLException e) {
        	crossReferenceFailues++;
        }
    	
    }
	
	public void loadDBFromCSV (String fileName) throws FileNotFoundException, IOException {
		InputStream inputStream;
		InputStreamReader inputStreamReader;
		BufferedReader bufferedReader;
		try {
			inputStream = context.getAssets().open(fileName);
			inputStreamReader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(inputStreamReader);
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
			throw fnfe;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			throw ioe;
		}
		
		try {
			List<CrossReference> references = new ArrayList<CrossReference>();
			List<ZipCode> zipCodes = new ArrayList<ZipCode>();
			List<Location> locations = new ArrayList<Location>();
			CsvReader record = new CsvReader(bufferedReader);
			record.readHeaders();
			
			int zipCodeUpperEnd = 10000;
			
			while (record.readRecord()) {
				
				//Gather objects
				ZipCode zipCode = populateZipCodeObject(record);
				Location location = populateLocationObject(record);
				CrossReference reference = new CrossReference();
				reference.setLocation(location);
				reference.setZipCode(zipCode);
				
				if (zipCode.getZipCodeId() > zipCodeUpperEnd) {
					//Load ZipCodes
					for (ZipCode code : zipCodes) {
						createZipCode(code);
					}
					//Load Locations
					for (Location loc : locations) {
						createLocation(loc);
					}
					//Load Cross Reference
					for (CrossReference ref : references) {
						createCrossReference(ref);
					}
					System.err.println("ZipCode Size: " + zipCodes.size());
					System.err.println("Location Size: " + locations.size());
					System.err.println("CrossReference Size: " + references.size());
					System.err.println("ZipCode Upper End " + zipCodeUpperEnd);
					zipCodes = new ArrayList<ZipCode>();
					locations = new ArrayList<Location>();
					references = new ArrayList<CrossReference>();
					zipCodeUpperEnd += 10000;
				}
				
				//Add To ArrayLists
				if (!zipCodes.contains(zipCode)) {
					zipCodes.add(zipCode);
				}
				if (!locations.contains(location)) {
					locations.add(location);
				}
				references.add(reference);
				
			}
			record.close();
			System.err.println("ZipCode Failues: " + zipCodeFailues);
			System.err.println("Location Failues: " + locationFailues);
			System.err.println("CrossReference Failues: " + crossReferenceFailues);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private ZipCode populateZipCodeObject (CsvReader record) throws IOException {
		ZipCode zipCode = new ZipCode();
		
		try {
			zipCode.setZipCodeId( Integer.parseInt(record.get("Zipcode")) );
			zipCode.setZipCode( record.get("Zipcode") );
			
			String latString =  record.get("lat");
			double lat = ( latString==null || latString.equals("") ) ? 0 : Double.parseDouble(latString);
			zipCode.setLatitude( lat );
			
			String lonString =  record.get("Long");
			double lon = ( lonString==null || lonString.equals("") ) ? 0 : Double.parseDouble(lonString);
			zipCode.setLongitude( lon );
			
			String popString =  record.get("Population");
			int pop = ( popString==null || popString.equals("") ) ? 0 : Integer.parseInt(popString);
			zipCode.setPopulation( pop );
			
			String houseString =  record.get("HousingUnits");
			int house = ( houseString==null || houseString.equals("") ) ? 0 : Integer.parseInt(houseString);
			zipCode.setHousingUnits( house );
			
			String incomeString =  record.get("Income");
			double income = ( incomeString==null || incomeString.equals("") ) ? 0 : Double.parseDouble(incomeString);
			zipCode.setIncome( income );
			
			String landString =  record.get("LandArea");
			double land = ( landString==null || landString.equals("") ) ? 0 : Double.parseDouble(landString);
			zipCode.setLandArea( land );
			
			String waterString =  record.get("WaterArea");
			double water = ( waterString==null || waterString.equals("") ) ? 0 : Double.parseDouble(waterString);
			zipCode.setWaterArea( water );
			
			zipCode.setMilitaryRestrictionCodes( record.get("MilitaryRestrictionCodes") );
		} catch (IOException ioe) {
			ioe.printStackTrace();
			throw ioe;
		}
		
		return zipCode;
	}
	
	private Location populateLocationObject (CsvReader record) throws IOException {
		Location location = new Location();
		
		try {
			location.setCity( record.get("City") );
			location.setState( record.get("State") );
			location.setCounty( record.get("County") );
			location.setType( record.get("Type") );
			location.setPreferred( record.get("Preferred") );
			location.setWorldRegion( record.get("WorldRegion") );
			location.setCountry( record.get("Country") );
			location.setLocationText( record.get("LocationText") );
			location.setLocation( record.get("Location") );
		} catch (IOException ioe) {
			ioe.printStackTrace();
			throw ioe;
		}
		
		return location;
	}

}
