package com.blueskyconnie.simpleearthquake;

import java.util.List;

import roboguice.inject.ContentView;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.blueskyconnie.simpleearthquake.base.RoboActionBarActivity;
import com.blueskyconnie.simpleearthquake.db.QuakeDataSource;
import com.blueskyconnie.simpleearthquake.db.QuakeSQLiteOpenHelper;
import com.blueskyconnie.simpleearthquake.model.EarthquakeInfo;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.ClusterManager.OnClusterItemClickListener;

@ContentView(R.layout.activity_earthquake_all_map)
public class EarthquakeAllMapActivity extends RoboActionBarActivity /*implements HttpResponseCallback*/ {

	private static final String TAG = "EarthquakeAllMapActivity";
	private static final int RQS_GOOGLE_SERVICE = 1;
	
	private SupportMapFragment fragEarthquake;
	private GoogleMap map;

	// private String restUrl;
//	private ClusterManager<EarthquakeClusterItem> mClusterManager;  
	private ClusterManager<EarthquakeInfo> mClusterManager;  
	private EarthquakeInfo clickedClusterItem;
	
	@InjectView(R.id.adViewAll)
	private AdView adView;
	@InjectResource(R.string.kilometer)
	private String strKM;
	@InjectResource(R.string.mile)
	private String strMile;
	@InjectResource(R.string.summary_tag)
	private String summaryTag;

	private QuakeSQLiteOpenHelper dbHelper;
	private QuakeDataSource quakeDS;
	private String infoType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_earthquake_all_map);
		
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		if (savedInstanceState == null) {
			fragEarthquake = (SupportMapFragment) getSupportFragmentManager()
								.findFragmentById(R.id.fragEarthquakeAll);
			if (fragEarthquake != null) {
				Intent intent = getIntent();
				if (intent != null) {
					// restUrl = intent.getStringExtra(Constants.EARTHQUAKE_REST_URL);
					setTitle(intent.getStringExtra(Constants.EARTHQUAKE_TITLE));
					//fragEarthquake.getView().setVisibility(View.INVISIBLE);
					Log.i(TAG, "Load all earthquakes information to show in google map.");
					//UsgsEarthquakeClient.get(restUrl, null, new EarthquakeJsonHttpResponseHandler(this, this, "DAILY"));

					dbHelper = new QuakeSQLiteOpenHelper(this);
					quakeDS = new QuakeDataSource(dbHelper.getReadableDatabase());
					infoType = getIntent().getStringExtra(Constants.EARTHQUAKE_TYPE);
					
					List<EarthquakeInfo> earthquakeList = quakeDS.query(QuakeDataSource.TABLE_NAME, "TYPE = ? ", 
							new String[] { infoType }, QuakeDataSource.COLUMN_INT_SEQ);
					
					
					
					Log.i(TAG, "Number of earthquake data retrieved: " + earthquakeList.size());
					
					// make clusters
					int result_code = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
					if (ConnectionResult.SUCCESS == result_code) {
						map = fragEarthquake.getMap();
						// http://stackoverflow.com/questions/21885225/showing-custom-infowindow-for-android-maps-utility-library-for-android
						if (map != null) {
							map.clear();
							map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
							mClusterManager = new ClusterManager<EarthquakeInfo>(this, map);
							map.setInfoWindowAdapter(mClusterManager.getMarkerManager());
							mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(new EarthquakeInfoWindowAdapter());
							mClusterManager.getMarkerCollection().setOnInfoWindowClickListener(infoWindowClickListener);
							map.setOnCameraChangeListener(mClusterManager);
							map.setOnMarkerClickListener(mClusterManager);
							map.setOnInfoWindowClickListener(mClusterManager);
							mClusterManager.setOnClusterItemClickListener(new OnClusterItemClickListener<EarthquakeInfo>() {
								@Override
								public boolean onClusterItemClick(EarthquakeInfo item) {
									clickedClusterItem = item;
									return false;
								}
							});
							convertToClusterItems(earthquakeList);	
						}
					} else {
						GooglePlayServicesUtil.getErrorDialog(result_code, this, RQS_GOOGLE_SERVICE).show();
					}
				}
				
				if (adView != null) {
					AdRequest adRequest = new AdRequest.Builder()
												.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//												.addTestDevice(Constants.TABLET_DEVICE_ID)
												.build();
					
					// Start loading the ad in the background.
					adView.loadAd(adRequest);
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

	private void convertToClusterItems (List<EarthquakeInfo> newResults) {
		if (newResults != null) {
			for (EarthquakeInfo info : newResults) {
				info.setSummaryTag(summaryTag);
			}
			mClusterManager.addItems(newResults);
			if (newResults != null && newResults.size() > 0) {	
				map.moveCamera(CameraUpdateFactory.newLatLng(newResults.get(0).getPosition()));
				map.animateCamera(CameraUpdateFactory.zoomTo(2));
			}		
		}
	}
	

//	@Override
//	public void failedCallback() {
//		finishedLoading();
//	}
//
//	private void finishedLoading() {
//		fragEarthquake.getView().setVisibility(View.VISIBLE);
//		Log.i(TAG, "Finish loading earthquake information in EarthquakeAllMapActivity.");
//	}
	
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

	private class EarthquakeInfoWindowAdapter implements InfoWindowAdapter {

		private TextView tvWinPlace;
		private TextView tvWinMagnitude;
		private TextView tvWinTime;
		private TextView tvWinCoordinate;
		private TextView tvWinDepth;
		
		@Override
		public View getInfoContents(Marker marker) {
			LayoutInflater inflater = EarthquakeAllMapActivity.this.getLayoutInflater();
			View view = inflater.inflate(R.layout.layout_window, null);
			tvWinPlace = (TextView) view.findViewById(R.id.tvWinPlace);
			tvWinMagnitude = (TextView) view.findViewById(R.id.tvWinMagnitude);
			tvWinTime = (TextView) view.findViewById(R.id.tvWinTime);
			tvWinCoordinate = (TextView) view.findViewById(R.id.tvWinCoordinate);
			tvWinDepth = (TextView) view.findViewById(R.id.tvWinDepth);
			
			if (clickedClusterItem != null) {
				if (tvWinPlace != null) {
					tvWinPlace.setText(clickedClusterItem.getPlace());
				}
				if (tvWinMagnitude != null) {
					tvWinMagnitude.setText(String.valueOf(clickedClusterItem.getMagnitude()));
				}
				if (tvWinTime != null) { 
					tvWinTime.setText(clickedClusterItem.getLocalTime());

				}
				if (tvWinCoordinate != null) {
					String coordinate = clickedClusterItem.getPosition().latitude + "," 
								+ clickedClusterItem.getPosition().longitude;
					tvWinCoordinate.setText(coordinate);
				}
				if (tvWinDepth != null) {
					double mile = clickedClusterItem.getDepth() * Constants.KM_2_MILE;
					tvWinDepth.setText(String.format("%s %s (%s %s)", 
							Constants.df.format(clickedClusterItem.getDepth()), strKM, 
							Constants.df.format(mile), strMile));
				}
			}
			return view;
		}

		@Override
		public View getInfoWindow(Marker marker) {
			return null;
		}
	}
	
	private OnInfoWindowClickListener infoWindowClickListener = 
			new OnInfoWindowClickListener() {

		@Override
		public void onInfoWindowClick(Marker marker) {
			Log.i(TAG, "onInfoWindowClick fired. Url = ");
			if (clickedClusterItem != null) {
				Uri uri = Uri.parse(clickedClusterItem.getSummaryUrl());
				startActivity(new Intent(Intent.ACTION_VIEW, uri));
			}
		}
	};
	
	public boolean onCreateOptionsMenu (Menu menu) {
		getMenuInflater().inflate(R.menu.earthquake_all_map, menu);
		return true;
	}
	
}
