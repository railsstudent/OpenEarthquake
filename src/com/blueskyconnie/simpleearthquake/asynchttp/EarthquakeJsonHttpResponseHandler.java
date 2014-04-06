package com.blueskyconnie.simpleearthquake.asynchttp;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.blueskyconnie.simpleearthquake.model.EarthquakeInfo;
import com.loopj.android.http.JsonHttpResponseHandler;

public class EarthquakeJsonHttpResponseHandler extends JsonHttpResponseHandler {

	private static final String TAG = "EarthquakeJsonHttpResponseHandler";
	private HttpResponseCallback callback;
	
	public interface HttpResponseCallback {
		void successCallback(List<EarthquakeInfo> newResults);
		void failedCallback();
	}
	
	public EarthquakeJsonHttpResponseHandler(HttpResponseCallback callback) {
		this.callback = callback;
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
			if (response.has("features")) {
				JSONArray features = response.getJSONArray("features");
				if (features != null) {
					for (int i = 0; i < features.length(); i++) {
						EarthquakeInfo.Builder builder = new EarthquakeInfo.Builder();
						JSONObject feature = features.getJSONObject(i);
						if (feature.has("properties")) {
							JSONObject properties = feature.getJSONObject("properties");
							if (properties.has("mag")) {
								builder.magnitude(properties.getDouble("mag"));
							}
//							if (properties.has("magnitudeType")) {
//								builder.magnitudeType(properties.getString("magnitudeType"));
//							}
							if (properties.has("place")) {
								builder.place(properties.getString("place"));
							}
							if (properties.has("time")) {
								builder.time(properties.getLong("time"));
							}
							if (properties.has("url")) {
								builder.url(properties.getString("url"));
							}
//							if (properties.has("dmin")) {
//								if (properties.isNull("dmin")) {
//									builder.dmin(0);
//								} else {
//									builder.dmin(properties.getDouble("dmin"));
//								}
//							}
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
						lst.add(builder.create());
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
