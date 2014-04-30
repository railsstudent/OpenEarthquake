package com.blueskyconnie.simpleearthquake.helper;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.blueskyconnie.simpleearthquake.Constants;
import com.blueskyconnie.simpleearthquake.EarthquakeApplication;
import com.blueskyconnie.simpleearthquake.db.QuakeDataSource;
import com.blueskyconnie.simpleearthquake.model.EarthquakeInfo;
import com.blueskyconnie.simpleearthquake.model.SearchCriteria;
import com.google.common.base.Strings;

public class SearchDataHelper {

	private static final String TAG = "SearchDataHelper";
	
	private QuakeDataSource quakeDS;
	private Context context;

	public SearchDataHelper(Context context, QuakeDataSource quakeDS) {
		this.context = context;
		this.quakeDS = quakeDS;
	}
	
	public List<EarthquakeInfo> search(SearchCriteria criteria) {
		
		String filter = "";
		String[] selectArgs = null;
		List<EarthquakeInfo> lstEarthquake = null;
		List<String> lstSelectArgs = new ArrayList<String>();
		
		lstSelectArgs.add(criteria.getInfoType());
		if (Strings.isNullOrEmpty(criteria.getPlace())) {
			filter = QuakeDataSource.COLUMN_TYPE + " =  ? ";
		} else {
			filter = QuakeDataSource.COLUMN_TYPE + " =  ? AND " + QuakeDataSource.COLUMN_PLACE + " LIKE ? ";
			lstSelectArgs.add("%" + criteria.getPlace().trim() + "%");
		}
		
		String strPrefMagValue = criteria.getStrPrefMagValue();
		if (!strPrefMagValue.equals("all")) {
			try {
				 filter = filter + " AND " + QuakeDataSource.COLUMN_MAGNITUDE + " >= ? ";
				 lstSelectArgs.add(strPrefMagValue);
				 Log.i(TAG, "prefMagValue = " + strPrefMagValue);
			} catch (NumberFormatException ex) {
				Log.i(TAG, ex.getMessage());
			}
		}
			
		String strPrefDepthValue = criteria.getStrPrefDepthValue();
		if (!strPrefDepthValue.equals("all")) {
			try {
				 filter = filter + " AND " + QuakeDataSource.COLUMN_DEPTH + " <= ? ";
				 lstSelectArgs.add(strPrefDepthValue);
				 Log.i(TAG, "prefDepthValue = " + strPrefDepthValue);
			} catch (NumberFormatException ex) {
				Log.i(TAG, ex.getMessage());
			}
		}
		
		Log.i(TAG, String.format("filter critiera = %s", filter));
		selectArgs = new String[lstSelectArgs.size()];
		lstSelectArgs.toArray(selectArgs);
		lstEarthquake = quakeDS.query(QuakeDataSource.TABLE_NAME, filter, selectArgs, QuakeDataSource.COLUMN_INT_SEQ);
		if (lstEarthquake == null) {
			lstEarthquake = new ArrayList<EarthquakeInfo>();
		}
		// compute distance between earthquake and current location
		EarthquakeApplication application = (EarthquakeApplication) context.getApplicationContext();
		Location currentLocation = application.getCurrentLocation();
		if (currentLocation != null) {
			for (EarthquakeInfo info : lstEarthquake) {
				float[] results = new float[1]; 
				Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(), 
						info.getLatitude(), info.getLongtitude(), results);
				if (results != null && results.length >= 1) {
					results[0] = results[0] / Constants.KM_2_METER;
					info.setDistance(results[0]);
				}
			}
		}
		return lstEarthquake;
	}
}
