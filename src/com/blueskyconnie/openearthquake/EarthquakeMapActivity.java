package com.blueskyconnie.openearthquake;

import java.text.DecimalFormat;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.blueskyconnie.openearthquake.base.RoboActionBarActivity;
import com.blueskyconnie.openearthquake.model.EarthquakeInfo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class EarthquakeMapActivity extends RoboActionBarActivity {

	private static final int RQS_GOOGLE_SERVICE = 1;
	private static final double KM_2_MILE = 0.621371;
	private static final DecimalFormat df = new DecimalFormat("#.##");
	
	@InjectView(R.id.tvLatitude)
	private TextView tvLat;
	@InjectView(R.id.tvLongtitude)
	private TextView tvLng;
	@InjectView(R.id.tvDepth)
	private TextView tvDepth;
	
	private SupportMapFragment fragEarthquake;
	
	private EarthquakeInfo earthquakeInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_earthquake_map);

		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		if (savedInstanceState == null) {
			Intent intent = getIntent();
			if (intent != null) {
				earthquakeInfo  = (EarthquakeInfo) intent.getSerializableExtra(Constants.EARTHQUAKE_INFO_KEY);
				tvLat.setText(String.valueOf(earthquakeInfo.getLatitude()));
				tvLng.setText(String.valueOf(earthquakeInfo.getLongtitude()));
				
				double mile = earthquakeInfo.getDepth()  * KM_2_MILE;
				tvDepth.setText(String.format("%s %s (%s %s)", 
						df.format(earthquakeInfo.getDepth()), getString(R.string.kilometer),
						df.format(mile), getString(R.string.mile)));
			}
			fragEarthquake = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragEarthquake);
			if (fragEarthquake != null) {
				// todo set pin and stuff
				int result_code = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
				if (ConnectionResult.SUCCESS == result_code) {
					GoogleMap map = fragEarthquake.getMap();
					if (map != null) {
						LatLng latLng = new LatLng(earthquakeInfo.getLatitude(), earthquakeInfo.getLongtitude());
						map.addMarker(new MarkerOptions().position(latLng)
										.icon(BitmapDescriptorFactory
												.defaultMarker()));
						map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
						map.animateCamera(CameraUpdateFactory.zoomTo(8));
					}
				} else {
					GooglePlayServicesUtil.getErrorDialog(result_code, this, RQS_GOOGLE_SERVICE).show();
				}
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.earthquake_map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		} else if (id == android.R.id.home) {
			// close this activity
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
