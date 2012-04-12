package com.mie.mbc.maps.db;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.mie.mbc.maps.entities.CrossReference;
import com.mie.mbc.maps.entities.Location;
import com.mie.mbc.maps.entities.ZipCode;

public class MySQLiteHelper extends OrmLiteSqliteOpenHelper {

	private static String DB_PATH = "/data/data/com.mie.mbc.maps/databases/";
	private static final String DATABASE_NAME = "maps.db";
	private static final int DATABASE_VERSION = 1;
    private final Context myContext;
    private ConnectionSource connectionSource;
	
    private Dao<ZipCode, Integer> zipCodeDAO;
	private Dao<Location, Integer> locationDAO;
	private Dao<CrossReference, Integer> crossReferenceDAO;
    
	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		myContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
		this.connectionSource = connectionSource;
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		this.connectionSource = connectionSource;
	}
	
	public void createDatabase () throws FileNotFoundException, IOException {
		if(checkDataBase()){
    		//do nothing - database already exist
    	} else {
    		this.getReadableDatabase();
        	try { 
        		copyDataBase();
        	} catch (FileNotFoundException fnfe) {
        		try {
					TableUtils.createTable(connectionSource, ZipCode.class);
					TableUtils.createTable(connectionSource, Location.class);
					TableUtils.createTable(connectionSource, CrossReference.class);
				} catch (SQLException e) {
					
				}
        		throw fnfe;
        	}
    	}
	}
	
	/**
	 * 
	 * @return Dao<ZipCode, Integer>
	 * @throws SQLException
	 */
	public Dao<ZipCode, Integer> getZipCodeDAO() throws SQLException {
		if (zipCodeDAO == null) {
			zipCodeDAO = getDao(ZipCode.class);
		}
		return zipCodeDAO;
	}
	
	/**
	 * 
	 * @return Dao<Location, Integer>
	 * @throws SQLException
	 */
	public Dao<Location, Integer> getLocationDAO() throws SQLException {
		if (locationDAO == null) {
			locationDAO = getDao(Location.class);
		}
		return locationDAO;
	}
	
	/**
	 * 
	 * @return Dao<CrossReference, Integer>
	 * @throws SQLException
	 */
	public Dao<CrossReference, Integer> getCrossReferenceDAO() throws SQLException {
		if (crossReferenceDAO == null) {
			crossReferenceDAO = getDao(CrossReference.class);
		}
		return crossReferenceDAO;
	}
	
	/**
	 * Checks to see if the Database already exists on the device.
	 * @return
	 */
	private boolean checkDataBase(){
    	SQLiteDatabase checkDB = null;
    	try{
    		String myPath = DB_PATH + DATABASE_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    	}catch(SQLiteException e){
    		//database does't exist yet.
    	}
 
    	if(checkDB != null){
    		checkDB.close();
    	}
    	return checkDB != null ? true : false;
    }
	
	/**
	 * Copies the database from the APK to the device.
	 * @throws IOException
	 */
	private void copyDataBase() throws FileNotFoundException, IOException {
		
		String outFileName = DB_PATH + DATABASE_NAME;
    	InputStream inputStream = null;
    	OutputStream outputStream = null;
    	
		try {
			inputStream = myContext.getAssets().open(DATABASE_NAME);
			outputStream = new FileOutputStream(outFileName);
			byte[] buffer = new byte[1024];
	    	int length;
	    	while ((length = inputStream.read(buffer))>0){
	    		outputStream.write(buffer, 0, length);
	    	}
			
		} finally {
			if (outputStream != null ) {
				outputStream.flush();
				outputStream.close();
			}
			if (inputStream != null ) {
				inputStream.close();
			}
		}
    }

}