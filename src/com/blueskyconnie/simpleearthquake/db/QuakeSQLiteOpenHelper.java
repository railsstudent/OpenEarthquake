package com.blueskyconnie.simpleearthquake.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class QuakeSQLiteOpenHelper extends SQLiteOpenHelper {

	public QuakeSQLiteOpenHelper(Context context) {
		super(context, QuakeDataSource.DATABASE_NAME, null, QuakeDataSource.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// create database
		db.execSQL(QuakeDataSource.SQL_CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion > oldVersion) {
			// drop table
    		db.execSQL("DROP TABLE IF EXISTS " + QuakeDataSource.TABLE_NAME);
			onCreate(db);
		}
	}

}
