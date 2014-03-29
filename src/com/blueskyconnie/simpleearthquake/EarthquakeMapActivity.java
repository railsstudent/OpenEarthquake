package com.blueskyconnie.simpleearthquake;

import java.text.DecimalFormat;

import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.blueskyconnie.simpleearthquake.R;
import com.blueskyconnie.simpleearthquake.base.RoboActionBarActivity;
import com.blueskyconnie.simpleearthquake.model.EarthquakeInfo;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class EarthquakeMapActivity extends RoboActionBarActivity {

	private static final String TAG = "EarthquakeMapActivity";
	private static final int RQS_GOOGLE_SERVICE = 1;
	private static final double KM_2_MILE = 0.621371;
	private static final DecimalFormat df = new DecimalFormat("#.##");
	
	@InjectView(R.id.tvLatitude)
	private TextView tvLat;
	@InjectView(R.id.tvLongtitude)
	private TextView tvLng;
	@InjectView(R.id.tvDepth)
	private TextView tvDepth;
	@InjectResource(R.string.kilometer)
	private String strKM;
	@InjectResource(R.string.mile)
	private String strMile;
	
	private SupportMapFragment fragEarthquake;
	private EarthquakeInfo earthquakeInfo;
	
	@InjectResource(R.string.magnitude)
	private String lblMagnitude;
	
	@InjectResource(R.string.place)
	private String lblPlace;
	
	@InjectView(R.id.adView)
	private AdView adView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_earthquake_map);

		ActionBar actionbar = this.getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);

		if (savedInstanceState == null) {
			Intent intent = getIntent();
			if (intent != null) {
				earthquakeInfo  = (EarthquakeInfo) intent.getSerializableExtra(Constants.EARTHQUAKE_INFO_KEY);
				tvLat.setText(String.valueOf(earthquakeInfo.getLatitude()));
				tvLng.setText(String.valueOf(earthquakeInfo.getLongtitude()));
				
				double mile = earthquakeInfo.getDepth() * KM_2_MILE;
				tvDepth.setText(String.format("%s %s (%s %s)", 
						df.format(earthquakeInfo.getDepth()), strKM, df.format(mile), strMile));
			}
			fragEarthquake = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragEarthquake);
			if (fragEarthquake != null) {
				// todo set pin and stuff
				int result_code = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
				if (ConnectionResult.SUCCESS == result_code) {
					GoogleMap map = fragEarthquake.getMap();
					if (map != null) {
						LatLng latLng = new LatLng(earthquakeInfo.getLatitude(), earthquakeInfo.getLongtitude());
						String magnitude = String.format("%s %s", earthquakeInfo.getMagnitude(), earthquakeInfo.getMagnitudeType());
						map.addMarker(new MarkerOptions()
										.position(latLng)
										.title(lblMagnitude + " " + magnitude)
										.snippet(lblPlace + " " + earthquakeInfo.getPlace())
										.icon(BitmapDescriptorFactory.defaultMarker()));
						map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
						map.animateCamera(CameraUpdateFactory.zoomTo(8));
					}
				} else {
					GooglePlayServicesUtil.getErrorDialog(result_code, this, RQS_GOOGLE_SERVICE).show();
				}
			}
			
			if (adView != null) {
				AdRequest adRequest = new AdRequest.Builder()
										.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
										.addTestDevice(Constants.TABLET_DEVICE_ID)
										.build();
				adView.loadAd(adRequest);
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
		 switch (item.getItemId()) {
		    // Respond to the action bar's Up/Home button
		    case android.R.id.home:
		    	Intent upIntent = getSupportParentActivityIntent();
		    	upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		    	supportNavigateUpTo(upIntent);
		    	Log.i(TAG, "android.R.id.home: Up navigation");
		        return true;
		 }
		 return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (adView != null) {
			adView.resume();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (adView != null) {
			adView.pause();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (adView != null) {
			adView.destroy();
		}
	}
}
