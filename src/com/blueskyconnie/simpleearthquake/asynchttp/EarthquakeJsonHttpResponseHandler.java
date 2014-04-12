package com.blueskyconnie.simpleearthquake.asynchttp;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.blueskyconnie.simpleearthquake.db.QuakeDataSource;
import com.blueskyconnie.simpleearthquake.db.QuakeSQLiteOpenHelper;
import com.blueskyconnie.simpleearthquake.model.EarthquakeInfo;
import com.blueskyconnie.simpleearthquake.model.EarthquakeInfo.INFO_TYPE;
import com.loopj.android.http.JsonHttpResponseHandler;

public class EarthquakeJsonHttpResponseHandler extends JsonHttpResponseHandler {

	private static final String TAG = "EarthquakeJsonHttpResponseHandler";
	private HttpResponseCallback callback;
	
	private QuakeSQLiteOpenHelper dbHelper;
	private String infoType;
	private QuakeDataSource quakeDS;
	
	public interface HttpResponseCallback {
		void successCallback(List<EarthquakeInfo> newResults);
		void failedCallback();
	}
	
	public EarthquakeJsonHttpResponseHandler(Context context, 
			HttpResponseCallback callback, String infoType) {
		this.callback = callback;
		this.infoType = infoType;
		dbHelper = new QuakeSQLiteOpenHelper(context);
		quakeDS = new QuakeDataSource(dbHelper.getWritableDatabase());
	}
	
	@Override
	public void onFailure(Throwable e, JSONObject errorResponse) {
		super.onFailure(e, errorResponse);
		if (callback != null) {
			callback.failedCallback();
		}
		Log.i(TAG, "onFailure - " + e.getMessage());
	}

	@Override
	public void onSuccess(JSONObject response) {
		super.onSuccess(response);
		
		try {
			ArrayList<EarthquakeInfo> lst = new ArrayList<EarthquakeInfo>();
			
			// clear database
			boolean isDeleted = quakeDS.delete(QuakeDataSource.TABLE_NAME, "TYPE = ? ", new String[] { infoType });
			Log.i(TAG, "Is row deleted? " + isDeleted);
			if (response.has("features")) {
				JSONArray features = response.getJSONArray("features");
				if (features != null) {
					long internalSeq = 1;
					for (int i = 0; i < features.length(); i++) {
						EarthquakeInfo.Builder builder = new EarthquakeInfo.Builder();
						JSONObject feature = features.getJSONObject(i);
						if (feature.has("properties")) {
							JSONObject properties = feature.getJSONObject("properties");
							if (properties.has("mag")) {
								builder.magnitude(properties.getDouble("mag"));
							}
							if (properties.has("place")) {
								builder.place(properties.getString("place"));
							}
							if (properties.has("time")) {
								builder.time(properties.getLong("time"));
							}
							if (properties.has("url")) {
								builder.url(properties.getString("url"));
							}
						}
						
						if (feature.has("id")) {
							builder.id(feature.getString("id"));
						}
						
						if (feature.has("geometry")) {
							JSONObject geometry = feature.getJSONObject("geometry");
							if (geometry.has("coordinates")) {
								JSONArray coordinates = geometry.getJSONArray("coordinates");
								if (coordinates.length() >= 3) {
									builder.depth(coordinates.getDouble(2));
								}
								if (coordinates.length() >= 2) {
									builder.lat(coordinates.getDouble(1));
								}
								if (coordinates.length() >= 1) {
									builder.lng(coordinates.getDouble(0));
								}
							}
						}
						
						builder.type(INFO_TYPE.valueOf(infoType))
								.internalSequence(internalSeq);
						EarthquakeInfo info = builder.create();
						quakeDS.insert(QuakeDataSource.TABLE_NAME, info);
						lst.add(info);
						internalSeq++;
					}
				}
			}
			if (callback != null) {
				callback.successCallback(lst);
			}
			Log.i(TAG, "onSuccess ends.");
		} catch (JSONException e) {
			Log.i(TAG, e.getMessage());
			e.printStackTrace();
		} finally {
			
		}
	}
}
