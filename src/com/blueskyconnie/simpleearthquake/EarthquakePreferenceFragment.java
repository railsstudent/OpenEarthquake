package com.blueskyconnie.simpleearthquake;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.preference.PreferenceFragment;
import android.util.Log;

public class EarthquakePreferenceFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {

	private static final String TAG = "EarthquakePreferenceFragment";
	private SharedPreferences mPref;
	
	@Override
	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		this.addPreferencesFromResource(R.xml.preferences);
		mPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
	}
	

	@Override
	public void onPause() {
		if (mPref != null) {
			mPref.unregisterOnSharedPreferenceChangeListener(this);
			Log.i(TAG, "Unregister sharedPreferences.");
		}
		super.onPause();

	}


	@Override
	public void onResume() {
		super.onResume();
		if (mPref != null) {
			mPref.registerOnSharedPreferenceChangeListener(this);
			Log.i(TAG, "Register sharedPreferences.");
		}
	}



	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		
		Log.i(TAG, "onSharedPreferenceChanged begins");
		Log.i(TAG, key + "is updated.");
		Activity activity = this.getActivity();
		if (activity != null) {
			EarthquakeApplication application = (EarthquakeApplication) activity.getApplicationContext();
			application.setPreferenceChanged(true);
		}
		Log.i(TAG, "onSharedPreferenceChanged ends");
	}
}
