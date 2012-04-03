package com.mie.mbc.assignment2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	

	public SQLiteHelper(Context context) {
		super(context, Quake.DATABASE_NAME.getValue(), null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + Quake.DATABASE_TABLE_NAME.getValue() + " (" 
				+ Quake.QUAKE_ID.getValue() + " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ Quake.QUAKE_TITLE.getValue() + " VARCHAR(255)," 
				+ Quake.QUAKE_MAGNITUDE.getValue() + " INTEGER,"
				+ Quake.QUAKE_LATITUDE.getValue() + " NUMERIC,"
				+ Quake.QUAKE_LONGITUDE.getValue() + " NUMERIC," 
				+ Quake.QUAKE_LINK.getValue() + " VARCHAR(255),"
				 + Quake.QUAKE_TIME.getValue() + " INTEGER UNIQUE);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + Quake.DATABASE_TABLE_NAME.getValue());
		onCreate(db);
	}

}
