package com.blueskyconnie.openearthquake;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.blueskyconnie.openearthquake.asynchttp.EarthquakeJsonHttpResponseHandler;
import com.blueskyconnie.openearthquake.asynchttp.EarthquakeJsonHttpResponseHandler.HttpResponseCallback;
import com.blueskyconnie.openearthquake.asynchttp.UsgsEarthquakeClient;
import com.blueskyconnie.openearthquake.base.RoboActionBarActivity;
import com.blueskyconnie.openearthquake.model.EarthquakeClusterItem;
import com.blueskyconnie.openearthquake.model.EarthquakeInfo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class EarthquakeAllMapActivity extends RoboActionBarActivity implements HttpResponseCallback {

	private static final String TAG = "EarthquakeAllMapActivity";
	private static final int RQS_GOOGLE_SERVICE = 1;
	
	@InjectView(R.id.tvHeading)
	private TextView tvHeading;
	
	private SupportMapFragment fragEarthquake;
	private String title;
	private String restUrl;
	private ClusterManager<EarthquakeClusterItem> mClusterManager;  
	private GoogleMap map;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_earthquake_all_map);
		
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		if (savedInstanceState == null) {
			fragEarthquake = (SupportMapFragment) getSupportFragmentManager()
								.findFragmentById(R.id.fragEarthquakeAll);
			if (fragEarthquake != null) {
				Intent intent = getIntent();
				if (intent != null) {
					title  = intent.getStringExtra(Constants.EARTHQUAKE_TITLE);
					restUrl = intent.getStringExtra(Constants.EARTHQUAKE_REST_URL);

					tvHeading.setText(title);
					UsgsEarthquakeClient.get(restUrl, null, new EarthquakeJsonHttpResponseHandler(this));
					
					// make clusters
					int result_code = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
					if (ConnectionResult.SUCCESS == result_code) {
						map = fragEarthquake.getMap();
						if (map != null) {
							mClusterManager = new ClusterManager<EarthquakeClusterItem>(this, map);
							mClusterManager.setRenderer(new EarthquakeRenderer());
							map.setOnCameraChangeListener(mClusterManager);
							map.setOnMarkerClickListener(mClusterManager);
						}
					} else {
						GooglePlayServicesUtil.getErrorDialog(result_code, this, RQS_GOOGLE_SERVICE).show();
					}
				}
			}
		}
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
	public void successCallback(List<EarthquakeInfo> newResults) {
		if (newResults != null) {
			List<EarthquakeClusterItem> earthquakeList = new ArrayList<EarthquakeClusterItem>();
			for (EarthquakeInfo info : newResults) {
				earthquakeList.add(new EarthquakeClusterItem(info.getPlace(), 
						info.getMagnitude(), info.getMagnitudeType(),
						info.getLatitude(), info.getLongtitude()));	
			}
			mClusterManager.addItems(earthquakeList);
			if (earthquakeList != null && earthquakeList.size() > 0) {	
				map.moveCamera(CameraUpdateFactory.newLatLng(earthquakeList.get(0).getPosition()));
				map.animateCamera(CameraUpdateFactory.zoomTo(8));
			}		
		}
	}

	@Override
	public void failedCallback() {
	}
	
	
	private class EarthquakeRenderer extends DefaultClusterRenderer<EarthquakeClusterItem> {

		public EarthquakeRenderer() {
			 super(EarthquakeAllMapActivity.this, map, mClusterManager);
		}

		@Override
		protected void onBeforeClusterItemRendered(EarthquakeClusterItem item,
				MarkerOptions markerOptions) {
			super.onBeforeClusterItemRendered(item, markerOptions);
			markerOptions.title(item.getPlace()).snippet(item.getMagnitude());
		}
	}
}
