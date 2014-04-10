package com.blueskyconnie.simpleearthquake.db;

import java.util.List;

public interface DataSource<T> {
	
	public boolean insert (String table, T entity);
	public boolean delete (String table, String whereClause, String[] whereArgs);
	public List<T> query(String table, String selection, String[] selectionArgs, String orderBy, String limit);
	public List<T> query(String table, String selection, String[] selectionArgs, String orderBy);
	public boolean update(String table, T entity, String whereClause, String[] whereArgs);
}
