package com.blueskyconnie.simpleearthquake.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.blueskyconnie.simpleearthquake.model.EarthquakeInfo;

public class QuakeDataSource implements DataSource<EarthquakeInfo> {

	 public static final int DATABASE_VERSION = 1;
	 public static final String DATABASE_NAME = "Quake.db";
	 
	 public static final String TABLE_NAME = "Tbl_Quakes";

	 public static final String COLUMN_ID = "ID";
	 public static final String COLUMN_LAT = "LAT";
	 public static final String COLUMN_LNG = "LNG";
	 public static final String COLUMN_DEPTH = "DEPTH";
	 public static final String COLUMN_URL = "URL";
	 public static final String COLUMN_MAGNITUDE = "MAGNITUDE";
	 public static final String COLUMN_PLACE = "PLACE";
	 public static final String COLUMN_TIME = "TIME";
	 public static final String COLUMN_TYPE = "TYPE";
	 public static final String COLUMN_INT_SEQ = "INTERNAL_SEQ";
	 
	 private static final String[] RETRIEVE_COLUMNS = {
		   COLUMN_ID
		 , COLUMN_LAT
		 , COLUMN_LNG
		 , COLUMN_DEPTH
		 , COLUMN_URL
		 , COLUMN_MAGNITUDE
		 , COLUMN_PLACE
		 , COLUMN_TIME
		 , COLUMN_INT_SEQ
	 };
	 
	 // Database creation sql statement
	 public static final String SQL_CREATE_TABLE = "create table " + TABLE_NAME
	             + "(" + COLUMN_ID + " VARCHAR(30) primary key not null "
	             + "," + COLUMN_INT_SEQ + " INTEGER NOT NULL DEFAULT 1"
	             + "," + COLUMN_LAT + " DECIMAL(10, 5) NOT NULL DEFAULT 0"
	             + "," + COLUMN_LNG + " DECIMAL(10, 5) NOT NULL DEFAULT 0"
	             + "," + COLUMN_DEPTH + " DECIMAL(10, 5) NOT NULL DEFAULT 0"
	             + "," + COLUMN_URL + " VARCHAR(500) NOT NULL DEFAULT ''"
	             + "," + COLUMN_MAGNITUDE + " DECIMAL(10, 5)"
	             + "," + COLUMN_PLACE + " VARCHAR(250) NOT NULL DEFAULT ''"
	             + "," + COLUMN_TIME + " INTEGER NOT NULL DEFAULT 0"
	             + "," + COLUMN_TYPE + " VARCHAR(20) NOT NULL DEFAULT '');";
	
	private SQLiteDatabase database;
	public QuakeDataSource(SQLiteDatabase database) {
		this.database = database;
	}

	@Override
	public boolean insert(String table, EarthquakeInfo entity) {
		
		ContentValues  values = new ContentValues();
		values.put(COLUMN_ID, entity.getId());
		values.put(COLUMN_LAT, entity.getLatitude());
		values.put(COLUMN_LNG, entity.getLongtitude());
		values.put(COLUMN_DEPTH, entity.getDepth());
		values.put(COLUMN_URL, entity.getUrl());
		values.put(COLUMN_MAGNITUDE, entity.getMagnitude());
		values.put(COLUMN_PLACE, entity.getPlace());
		values.put(COLUMN_TIME, entity.getTime());
		values.put(COLUMN_TYPE, entity.getType().toString());
		values.put(COLUMN_INT_SEQ, entity.getInternalSequence());
		long result = database.insert(table, null, values);
		return result != -1;
	}

	@Override
	public boolean delete(String table, String whereClause, String[] whereArgs) {
		int count  = database.delete(table, whereClause, whereArgs);
		return count > 0;
	}

	@Override
	public List<EarthquakeInfo> query(String table, String selection, String[] selectionArgs, 
			String orderBy, String limit) {
		
		Cursor cursor =  database.query(table, RETRIEVE_COLUMNS, selection, selectionArgs,
				null, null, orderBy, limit);
		List<EarthquakeInfo> list = new ArrayList<EarthquakeInfo>();
		
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			EarthquakeInfo.Builder builder = new EarthquakeInfo.Builder();
			String place = CursorUtils.getString(COLUMN_PLACE, cursor);
			String url = CursorUtils.getString(COLUMN_URL, cursor);
			double depth = CursorUtils.getDouble(COLUMN_DEPTH, cursor);
			double magnitude = CursorUtils.getDouble(COLUMN_MAGNITUDE, cursor);
			double latitude = CursorUtils.getDouble(COLUMN_LAT, cursor);
			double longtitude = CursorUtils.getDouble(COLUMN_LNG, cursor);
			long time = CursorUtils.getLong(COLUMN_TIME, cursor);
			String id = CursorUtils.getString(COLUMN_ID, cursor);
			int seq = CursorUtils.getInt(COLUMN_INT_SEQ, cursor);
			
			EarthquakeInfo info = builder
									.depth(depth)
									.lat(latitude)
									.lng(longtitude)
									.place(place)
									.magnitude(magnitude)
									.url(url)
									.time(time)
									.id(id)
									.internalSequence(seq)
									.create();
			list.add(info);
			cursor.moveToNext();
		}
		return list;
	}

	@Override
	public List<EarthquakeInfo> query(String table, String selection, String[] selectionArgs, String orderBy) {
		
		Cursor cursor =  database.query(table, RETRIEVE_COLUMNS, selection, selectionArgs, 
				null, null, orderBy);
		List<EarthquakeInfo> list = new ArrayList<EarthquakeInfo>();
		
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			EarthquakeInfo.Builder builder = new EarthquakeInfo.Builder();
			String place = CursorUtils.getString(COLUMN_PLACE, cursor);
			String url = CursorUtils.getString(COLUMN_URL, cursor);
			double depth = CursorUtils.getDouble(COLUMN_DEPTH, cursor);
			double magnitude = CursorUtils.getDouble(COLUMN_MAGNITUDE, cursor);
			double latitude = CursorUtils.getDouble(COLUMN_LAT, cursor);
			double longtitude = CursorUtils.getDouble(COLUMN_LNG, cursor);
			long time = CursorUtils.getLong(COLUMN_TIME, cursor);
			String id = CursorUtils.getString(COLUMN_ID, cursor);
			int seq = CursorUtils.getInt(COLUMN_INT_SEQ, cursor);

			EarthquakeInfo info = builder
									.depth(depth)
									.lat(latitude)
									.lng(longtitude)
									.place(place)
									.magnitude(magnitude)
									.url(url)
									.time(time)
									.id(id)
									.internalSequence(seq)
									.create();
			list.add(info);
			cursor.moveToNext();
		}
		return list;
	}

	@Override
	public boolean update(String table, EarthquakeInfo entity, String whereClause,
			String[] whereArgs) {
		
		ContentValues  values = new ContentValues();
		values.put(COLUMN_LAT, entity.getLatitude());
		values.put(COLUMN_LNG, entity.getLongtitude());
		values.put(COLUMN_DEPTH, entity.getDepth());
		values.put(COLUMN_URL, entity.getUrl());
		values.put(COLUMN_MAGNITUDE, entity.getMagnitude());
		values.put(COLUMN_PLACE, entity.getPlace());
		values.put(COLUMN_TIME, entity.getTime());
		values.put(COLUMN_TYPE, entity.getType().toString());
		values.put(COLUMN_INT_SEQ, entity.getInternalSequence());
		
		int count = database.update(table, values, whereClause, whereArgs);
		return count > 0;
	}
}
