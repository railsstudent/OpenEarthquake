package com.blueskyconnie.openearthquake.asynchttp;

import java.lang.ref.WeakReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.blueskyconnie.openearthquake.adapter.EarthquakeListAdapter;
import com.blueskyconnie.openearthquake.model.EarthquakeInfo;
import com.loopj.android.http.JsonHttpResponseHandler;

public class EarthquakeJsonHttpResponseHandler extends JsonHttpResponseHandler {

	private static final String TAG = "EarthquakeJsonHttpResponseHandler";
	
	private WeakReference<Activity> weakActivity;
	private WeakReference<ListView> weakLstView;
	private WeakReference<Button> weakBtnLoad;
	private WeakReference<ProgressBar> weakProgressbar;
	private EarthquakeListAdapter earthquakeAdapter;
	
	public EarthquakeJsonHttpResponseHandler(Activity activity, ListView lstView, 
			Button btnLoad, ProgressBar progressbar, EarthquakeListAdapter adapter) {
		weakActivity = new WeakReference<Activity>(activity);
		weakLstView = new WeakReference<ListView>(lstView);
		weakBtnLoad = new WeakReference<Button>(btnLoad);
		weakProgressbar = new WeakReference<ProgressBar>(progressbar);
		earthquakeAdapter = adapter;
	}
	
	
	@Override
	public void onFailure(Throwable e, JSONObject errorResponse) {
		super.onFailure(e, errorResponse);
		Log.i(TAG, e.getMessage());
		finishLoading();
	}

	@Override
	public void onSuccess(JSONObject response) {
		super.onSuccess(response);
		
		try {
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
						if (properties.has("magnitudeType")) {
							builder.magnitudeType(properties.getString("magnitudeType"));
						}
						if (properties.has("place")) {
							builder.place(properties.getString("place"));
						}
						if (properties.has("time")) {
							builder.time(properties.getLong("time"));
						}
					}
				}
			}
		} catch (JSONException e) {
			Log.i(TAG, e.getMessage());
			e.printStackTrace();
		} finally {
			finishLoading();
		}
	}
	
	private void finishLoading() {
		if (weakProgressbar != null && weakProgressbar.get() != null) {
			weakProgressbar.get() .setVisibility(View.GONE);
		}
		
		if (weakLstView != null && weakLstView.get() != null) {
			weakLstView.get().setVisibility(View.VISIBLE);
		}
		
		if (weakBtnLoad != null && weakBtnLoad.get() != null) {
			weakBtnLoad.get().setEnabled(true);
		}
	}
}
