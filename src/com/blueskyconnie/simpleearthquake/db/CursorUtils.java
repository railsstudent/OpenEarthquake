package com.blueskyconnie.simpleearthquake.db;

import android.database.Cursor;

public final class CursorUtils {

    public static String getString( String columnName, Cursor cursor ) {
            return cursor.getString(cursor.getColumnIndexOrThrow(columnName));
    }

    public static int getInt( String columnName, Cursor cursor ) {
        return cursor.getInt(cursor.getColumnIndexOrThrow(columnName));
    }

    public static long getLong( String columnName, Cursor cursor ) {
        return cursor.getLong(cursor.getColumnIndexOrThrow(columnName));
    }

    public static double getDouble( String columnName, Cursor cursor ) {
        return cursor.getDouble(cursor.getColumnIndexOrThrow(columnName));
    }
}
