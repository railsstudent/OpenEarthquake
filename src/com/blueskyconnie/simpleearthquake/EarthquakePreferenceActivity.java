package com.blueskyconnie.simpleearthquake;

import roboguice.activity.RoboFragmentActivity;

import android.os.Bundle;

public class EarthquakePreferenceActivity extends RoboFragmentActivity  {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(android.R.id.content, new EarthquakePreferenceFragment()).commit();
		}
	}
}
