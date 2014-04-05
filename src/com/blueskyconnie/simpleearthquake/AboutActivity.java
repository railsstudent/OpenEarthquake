package com.blueskyconnie.simpleearthquake;

import roboguice.inject.ContentView;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.blueskyconnie.simpleearthquake.base.RoboActionBarActivity;

@ContentView(R.layout.activity_about)
public class AboutActivity extends RoboActionBarActivity {

	private static final String TAG = "AboutActivity";

	@InjectResource(R.string.copyright_year)
	private String copyright_year;

	@InjectResource(R.string.copyright)
	private String copyright;

	@InjectResource(R.string.app_name)
	private String appName;

	@InjectView(R.id.tvAppCopyright)
	private TextView tvAppCopyright;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		tvAppCopyright.setText(String.format("%s, %s %s", appName, copyright, copyright_year));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if (item.getItemId() == android.R.id.home) {
			Intent intent = this.getSupportParentActivityIntent();
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			supportNavigateUpTo(intent);
			Log.i(TAG, "Navigate up from AboutActivity to MainActivity");
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
