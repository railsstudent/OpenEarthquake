package com.blueskyconnie.simpleearthquake;

import com.blueskyconnie.simpleearthquake.base.RoboActionBarActivity;

import roboguice.activity.RoboFragmentActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

public class EarthquakePreferenceActivity extends RoboActionBarActivity  {

	private static final String TAG = "EarthquakePreferenceActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(android.R.id.content, new EarthquakePreferenceFragment()).commit();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if (item.getItemId() == android.R.id.home) {
			Intent upIntent = getSupportParentActivityIntent();
	    	upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    	this.supportNavigateUpTo(upIntent);
	    	Log.i(TAG, "android.R.id.home: Up navigation");
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
