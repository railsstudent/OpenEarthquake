package com.blueskyconnie.openearthquake;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
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
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.ClusterManager.OnClusterItemClickListener;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class EarthquakeAllMapActivity extends RoboActionBarActivity implements HttpResponseCallback {

	private static final String TAG = "EarthquakeAllMapActivity";
	private static final int RQS_GOOGLE_SERVICE = 1;
	
	@InjectView(R.id.tvHeading)
	private TextView tvHeading;
	
	private SupportMapFragment fragEarthquake;
	private GoogleMap map;

	//private String title;
	private String restUrl;
	private ClusterManager<EarthquakeClusterItem> mClusterManager;  
	private EarthquakeRenderer earthquakeRenderer;
	private EarthquakeClusterItem clickedClusterItem;
	
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
				//	title  = intent.getStringExtra(Constants.EARTHQUAKE_TITLE);
					restUrl = intent.getStringExtra(Constants.EARTHQUAKE_REST_URL);
					tvHeading.setText(intent.getStringExtra(Constants.EARTHQUAKE_TITLE));
					fragEarthquake.getView().setVisibility(View.INVISIBLE);
					UsgsEarthquakeClient.get(restUrl, null, new EarthquakeJsonHttpResponseHandler(this));

					// make clusters
					int result_code = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
					if (ConnectionResult.SUCCESS == result_code) {
						map = fragEarthquake.getMap();
						// http://stackoverflow.com/questions/21885225/showing-custom-infowindow-for-android-maps-utility-library-for-android
						if (map != null) {
							map.clear();
							mClusterManager = new ClusterManager<EarthquakeClusterItem>(this, map);
							map.setInfoWindowAdapter(mClusterManager.getMarkerManager());
							mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(new EarthquakeInfoWindowAdapter());
							earthquakeRenderer = new EarthquakeRenderer();
							mClusterManager.setRenderer(new EarthquakeRenderer());
							map.setOnCameraChangeListener(mClusterManager);
							map.setOnMarkerClickListener(mClusterManager);
							mClusterManager.setOnClusterItemClickListener(new OnClusterItemClickListener<EarthquakeClusterItem>() {
								@Override
								public boolean onClusterItemClick(EarthquakeClusterItem item) {
									clickedClusterItem = item;
									return false;
								}
							});
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
		int id = 0;
		if (newResults != null) {
			List<EarthquakeClusterItem> earthquakeList = new ArrayList<EarthquakeClusterItem>();
			for (EarthquakeInfo info : newResults) {
				EarthquakeClusterItem.Builder builder = new EarthquakeClusterItem.Builder();
				builder.place(info.getPlace())
					.lat(info.getLatitude())
					.lng(info.getLongtitude())
					.magnitude(info.getMagnitude())
					.magnitudeType(info.getMagnitudeType())
					.earthquakeTime(info.getTime())
					.id(id++);
				earthquakeList.add(builder.create());	
			}
			mClusterManager.addItems(earthquakeList);
			if (earthquakeList != null && earthquakeList.size() > 0) {	
				map.moveCamera(CameraUpdateFactory.newLatLng(earthquakeList.get(0).getPosition()));
				map.animateCamera(CameraUpdateFactory.zoomTo(8));
			}		
		}
		finishedLoading();
	}

	@Override
	public void failedCallback() {
		finishedLoading();
	}

	private void finishedLoading() {
		fragEarthquake.getView().setVisibility(View.VISIBLE);
	}
	
	private class EarthquakeRenderer extends DefaultClusterRenderer<EarthquakeClusterItem> {

		public EarthquakeRenderer() {
			 super(EarthquakeAllMapActivity.this, map, mClusterManager);
		}

		@Override
		protected void onBeforeClusterItemRendered(EarthquakeClusterItem item,
				MarkerOptions markerOptions) {
			super.onBeforeClusterItemRendered(item, markerOptions);
			markerOptions.title(String.valueOf(item.getId()));
		}
	}
	
	private class EarthquakeInfoWindowAdapter implements InfoWindowAdapter {

		private TextView tvWinPlace;
		private TextView tvWinMagnitude;
		private TextView tvWinTime;
		
		@Override
		public View getInfoContents(Marker marker) {
			LayoutInflater inflater = EarthquakeAllMapActivity.this.getLayoutInflater();
			View view = inflater.inflate(R.layout.layout_window, null);
			tvWinPlace = (TextView) view.findViewById(R.id.tvWinPlace);
			tvWinMagnitude = (TextView) view.findViewById(R.id.tvWinMagnitude);
			tvWinTime = (TextView) view.findViewById(R.id.tvWinTime);
			if (clickedClusterItem != null) {
				if (tvWinPlace != null) {
					tvWinPlace.setText(clickedClusterItem.getPlace());
				}
				if (tvWinMagnitude != null) {
					tvWinMagnitude.setText(clickedClusterItem.getMagnitude());
				}
				if (tvWinTime != null) { 
					tvWinTime.setText(clickedClusterItem.getEarthquakeTime());
				}
			}
			return view;
		}

		@Override
		public View getInfoWindow(Marker marker) {
			return null;
		}
	}
}
