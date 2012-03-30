package com.matt.bootcamp.assignment1.sqlite;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class RunDateTimeDataSource {

	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_RUNDATETIME };

	public RunDateTimeDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public RunDateTime createRunDateTime(Date runDateTime) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_RUNDATETIME, runDateTime.getTime());
		long insertId = database.insert(MySQLiteHelper.TABLE_RUNDATETIMES, null,
				values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_RUNDATETIMES,
				allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		RunDateTime newRunDateTime = cursorToRuDateTime(cursor);
		cursor.close();
		return newRunDateTime;
	}

	public void deleteRunDateTime(RunDateTime runDateTime) {
		long id = runDateTime.getId();
		System.out.println("RunDateTime deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_RUNDATETIMES, MySQLiteHelper.COLUMN_ID
				+ " = " + id, null);
	}

	public List<RunDateTime> getAllRunDateTimes() {
		List<RunDateTime> runDateTimes = new ArrayList<RunDateTime>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_RUNDATETIMES,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			RunDateTime runDateTime = cursorToRuDateTime(cursor);
			runDateTimes.add(runDateTime);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return runDateTimes;
	}

	private RunDateTime cursorToRuDateTime(Cursor cursor) {
		RunDateTime runDateTime = new RunDateTime();
		runDateTime.setId(cursor.getLong(0));
		runDateTime.setRunDateTime(cursor.getLong(1));
		return runDateTime;
	}
}
