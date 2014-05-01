package com.blueskyconnie.simpleearthquake;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.v4.preference.PreferenceFragment;
import android.util.Log;

@SuppressLint("DefaultLocale")
public class EarthquakePreferenceFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {

	private static final String TAG = "EarthquakePreferenceFragment";
	private SharedPreferences mPref;
	
	@Override
	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		this.addPreferencesFromResource(R.xml.preferences);
		mPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
		
		Log.i(TAG, "Update current location in summary begins");
		EarthquakeApplication application = (EarthquakeApplication) getActivity().getApplication();
		Location location = application.getCurrentLocation();
		
		PreferenceScreen prefScreen = this.getPreferenceScreen();
		for (int i = 0; i < prefScreen.getPreferenceCount(); i++) {
			if (prefScreen.getPreference(i).getKey().equals("pref_key_map_settings")) {
				PreferenceCategory  prefCategory = (PreferenceCategory) prefScreen.getPreference(i);
				for (int j = 0; j < prefCategory.getPreferenceCount(); j++) {
					if (prefCategory.getPreference(j).getKey().equals(Constants.PREF_KEY_CURRENTLOC)) {
						String summary = String.format(Locale.getDefault(), 
								"%1$s %2$8.4f, %3$s %4$8.4f", getString(R.string.latitude)
							, location.getLatitude()
							,getString(R.string.longtitude)
							,location.getLongitude());
						prefCategory.getPreference(j).setSummary(summary);
					}
				}
			}
		}
		
		Log.i(TAG, "Update current location in summary ends");

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
			application.notifyAllPreferenceChanged();
		}
		Log.i(TAG, "onSharedPreferenceChanged ends");
	}
}
