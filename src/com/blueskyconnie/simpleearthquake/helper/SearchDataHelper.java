package com.blueskyconnie.simpleearthquake.helper;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.blueskyconnie.simpleearthquake.db.QuakeDataSource;
import com.blueskyconnie.simpleearthquake.model.EarthquakeInfo;
import com.blueskyconnie.simpleearthquake.model.SearchCriteria;
import com.google.common.base.Strings;

public class SearchDataHelper {

	private static final String TAG = "SearchDataHelper";
	
	private QuakeDataSource quakeDS;

	public SearchDataHelper(QuakeDataSource quakeDS) {
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
		return lstEarthquake;
	}
}
