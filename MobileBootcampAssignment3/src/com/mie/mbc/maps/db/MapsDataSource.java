package com.mie.mbc.maps.db;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.mie.mbc.maps.entities.CrossReference;
import com.mie.mbc.maps.entities.Location;
import com.mie.mbc.maps.entities.ZipCode;

public class MapsDataSource {
	
	private Context context;
	private MySQLiteHelper dbHelper;
	
	public MapsDataSource (Context context) {
		this.context = context;
		dbHelper = new MySQLiteHelper(context);
		try {
			dbHelper.createDatabase();
		} catch (FileNotFoundException fnfe) {
			populateDatabase();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public CrossReference[] search (String search) {
		CrossReference[] array = null;
		List<CrossReference> references = null;
		
		try {
			Dao<ZipCode, Integer> zipCodeDAO = dbHelper.getZipCodeDAO();
			Dao<Location, Integer> locationDAO = dbHelper.getLocationDAO();
			
			try {
				//Try search by zip code
				int zipCodeId = Integer.parseInt(search);
				references = zipCodeSearch(zipCodeId);
				
			} catch (NumberFormatException nfe) {
				//String rather than number use location search
				references = new ArrayList<CrossReference>();
				String tempLocationIdString = "%" + search + "%";
				List<Location> locations = citySearch(tempLocationIdString);
				for (Location location : locations) {
					references.addAll(searchByLocationId(location.getLocationDataId()));
				}
			}
			
			array = new CrossReference[references.size()];
			references.toArray(array);
			for (int i = 0; i < array.length; i++) {
				zipCodeDAO.refresh(array[i].getZipCode());
				locationDAO.refresh(array[i].getLocation());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return array;
	}
	
	/**
	 * Search DB by zipCodeId and return results
	 * This method does a wild card search.
	 * @param zipCode
	 * @return
	 */
	private List<CrossReference> zipCodeSearch (int zipCode) {
		List<CrossReference> references = null;
		
		try {
			Dao<CrossReference, Integer> referenceDAO = dbHelper.getCrossReferenceDAO();
			QueryBuilder<CrossReference, Integer> queryBuilder = referenceDAO.queryBuilder();
			
			String zipCodeString = "\'%" + zipCode + "%\'";
			queryBuilder.where().like("zipCodeId", zipCodeString);
			queryBuilder.orderBy("zipCodeId", true);
			
			PreparedQuery<CrossReference> preparedQuery = queryBuilder.prepare();
			
			references = referenceDAO.query(preparedQuery);
		
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		return references;
	}
	
	/**
	 * Search DB locationId and return results
	 * @param locationId
	 * @return
	 */
	private List<CrossReference> searchByLocationId (int locationId) {
		List<CrossReference> references = null;
		
		try {
			Dao<CrossReference, Integer> referenceDAO = dbHelper.getCrossReferenceDAO();
			QueryBuilder<CrossReference, Integer> queryBuilder = referenceDAO.queryBuilder();
			
			queryBuilder.where().like("locationDataId", locationId);
			
			PreparedQuery<CrossReference> preparedQuery = queryBuilder.prepare();
			
			references = referenceDAO.query(preparedQuery);
		
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		return references;
	}
	
	/**
	 * Search DB by city and return results
	 * This method does a wild card search.
	 * @param city
	 * @return
	 */
	private List<Location> citySearch (String city) {
		Dao<Location, Integer> locationDAO;
		List<Location> locations = null;
		
		try {
			locationDAO = dbHelper.getLocationDAO();
			QueryBuilder<Location, Integer> queryBuilder = locationDAO.queryBuilder();
			String cityWildCard = "%" + city + "%";
			queryBuilder.where().like("city", cityWildCard);
			queryBuilder.orderBy("city", true);
			
			PreparedQuery<Location> preparedQuery = queryBuilder.prepare();
			
			locations = locationDAO.query(preparedQuery);

		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		
		return locations;
	}
	
	/**
	 * Open Database
	 */
	public void open() {
		dbHelper.getWritableDatabase();
	}

	/**
	 * Close Database
	 */
	public void close() {
		dbHelper.close();
	}
	
	/**
	 * Populate Database from CSV File
	 */
	private void populateDatabase() {
		String fileName = "free-zipcode-database.csv";
		MapsDataLoader mapsLoader = new MapsDataLoader(dbHelper, this.context);
		try {
			mapsLoader.loadDBFromCSV(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
