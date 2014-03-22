package com.blueskyconnie.openearthquake;

import android.os.Bundle;
import android.view.MenuItem;

import com.blueskyconnie.openearthquake.base.RoboActionBarActivity;

public class EarthquakeAllMapActivity extends RoboActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_earthquake_all_map);
		
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		if (savedInstanceState == null) {
//			getSupportFragmentManager().beginTransaction()
//					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
