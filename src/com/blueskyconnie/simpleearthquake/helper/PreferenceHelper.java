package com.blueskyconnie.simpleearthquake.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.blueskyconnie.simpleearthquake.Constants;
import com.blueskyconnie.simpleearthquake.model.PreferenceContext;
import com.blueskyconnie.simpleearthquake.model.SearchCriteria;
import com.google.android.gms.maps.GoogleMap;

public final class PreferenceHelper {

	private PreferenceHelper() {
	}
	
	public static PreferenceContext load(Context context) {
		
		PreferenceContext prefContext = new PreferenceContext();
		SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(context);
		if (mPref != null) {
			prefContext.setDepthValue(mPref.getString(Constants.PREF_KEY_DEPTH, Constants.ALL));
			prefContext.setMagValue(mPref.getString(Constants.PREF_KEY_MAGNITUDE, Constants.ALL));
			prefContext.setDistValue(mPref.getString(Constants.PREF_KEY_DIST, Constants.ALL));
			String strMapType = mPref.getString(Constants.PREF_KEY_MAPTYPE, "MAP_TYPE_SATELLITE");
			int mapType;
			try {
				mapType = GoogleMap.class.getDeclaredField(strMapType).getInt(null);
			} catch (IllegalAccessException e) {
				mapType = GoogleMap.MAP_TYPE_SATELLITE;
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				mapType = GoogleMap.MAP_TYPE_SATELLITE;
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				mapType = GoogleMap.MAP_TYPE_SATELLITE;
				e.printStackTrace();
			}
			prefContext.setMapType(mapType);
		} else {
			prefContext.setDepthValue(Constants.ALL);
			prefContext.setMagValue(Constants.ALL);
			prefContext.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		}
		return prefContext;
	}
	
	
	public static void convertPrefContext(Context context, SearchCriteria criteria) {
		
		PreferenceContext prefContext = load(context);
		if (prefContext != null && criteria != null) {
			criteria.setStrPrefDepthValue(prefContext.getDepthValue());
			criteria.setStrPrefMagValue(prefContext.getMagValue());
			criteria.setStrPrefDistValue(prefContext.getDistValue());
		}
	}
}
