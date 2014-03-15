package com.blueskyconnie.openearthquake;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import roboguice.inject.InjectView;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.blueskyconnie.openearthquake.adapter.SectionsPagerAdapter;
import com.blueskyconnie.openearthquake.base.RoboActionBarActivity;
import com.blueskyconnie.openearthquake.helper.AlertDialogHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MainActivity extends RoboActionBarActivity implements
		ActionBar.TabListener,
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener, 
		LocationListener {

	private static final String TAG = "MainActivity";
	
	private static final long ONE_MIN = 1000 * 60;
	private static final long TWO_MIN = ONE_MIN * 2;
	private static final long FIVE_MIN = ONE_MIN * 5;
	private static final long MEASURE_TIME = 1000 * 30;
	private static final long POLLING_FREQ = 1000 * 10;
	private static final long FASTES_UPDATE_FREQ = 1000 * 2;
	private static final float MIN_ACCURACY = 25.0f;
	private static final float MIN_LAST_READ_ACCURACY = 500.0f;
	
	// google request code
	private static final int RQS_GOOGLE_CONNECT = 1;
	
	
	// Define an object that holds accuracy and frequency parameters
	private LocationRequest mLocationRequest;
	
	// Current best location estimate
	private Location mBestReading;
	private LocationClient mLocationClient;
	private Location mCurrentLocation;
	
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	
	@InjectView (R.id.pager)
	private ViewPager mViewPager;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create new Location Client. This class will handle callbacks
		mLocationClient = new LocationClient(this, this, this);
		
		// Create and define the LocationRequest
		mLocationRequest = LocationRequest.create();
		
		// Use high accuracy
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		
		// Update every 10 seconds
		mLocationRequest.setInterval(POLLING_FREQ);
		
		// Receive updates no more often than every 2 seconds
		mLocationRequest.setFastestInterval(FASTES_UPDATE_FREQ);
		
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				this, getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		//mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				switch (position) {
					default:
						actionBar.setSelectedNavigationItem(position);
				}
			}
		});
		
		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onBackPressed() {
		AlertDialogHelper.showConfirmExitDialog(this, imageLoader);
	}

	
	@Override
	public void onLocationChanged(Location location) {

		Log.i(TAG, "Enter onLocationChanged");
		
		// Determine whether new location is better than current best
		// estimate
		if (null == mBestReading
				|| location.getAccuracy() < mBestReading.getAccuracy()) {

			// Update best estimate
			mBestReading = location;

			((EarthquakeApplication) getApplicationContext()).setCurrentLat(mBestReading.getLatitude());
			((EarthquakeApplication) getApplicationContext()).setCurrentLng(mBestReading.getLongitude());
			notifyLocationAvailableListeners();
			
			if (mBestReading.getAccuracy() < MIN_ACCURACY) {
				mLocationClient.removeLocationUpdates(this);
				Log.i(TAG, "onLocationChanged: removeLocationUpdates called.");
			}
		}
		Log.i(TAG, "Exit onLocationChanged");
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		
	}

	@Override
	public void onConnected(Bundle arg0) {
		// Get first reading. Get additional location updates if necessary

		Log.i(TAG, "Enter onConnected");
		
		// Check that Google Play Services are available
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (ConnectionResult.SUCCESS == resultCode) {
			// Get best last location measurement meeting criteria
			mBestReading = bestLastKnownLocation(MIN_LAST_READ_ACCURACY, FIVE_MIN);
			
			if (null != mBestReading) {
				((EarthquakeApplication) getApplicationContext()).setCurrentLat(mBestReading.getLatitude());
				((EarthquakeApplication) getApplicationContext()).setCurrentLng(mBestReading.getLongitude());
				notifyLocationAvailableListeners();
			}
			
			if (null == mBestReading
					|| mBestReading.getAccuracy() > MIN_LAST_READ_ACCURACY
					|| mBestReading.getTime() < System.currentTimeMillis()
							- TWO_MIN) {
	
				mLocationClient.requestLocationUpdates(mLocationRequest, this);
				
				// Schedule a runnable to unregister location listeners
				Executors.newScheduledThreadPool(1).schedule(new Runnable() {
					@Override
					public void run() {
						mLocationClient.removeLocationUpdates(MainActivity.this);
						Log.i(TAG, "onConnected: removeLocationUpdates called.");
					}
				}, MEASURE_TIME, TimeUnit.MILLISECONDS);
			}
		} else {
			GooglePlayServicesUtil.getErrorDialog(resultCode, this, RQS_GOOGLE_CONNECT)
				.show();
		}
		Log.i(TAG, "Exit onConnected");
	}

	@Override
	public void onDisconnected() {
		mLocationClient.removeLocationUpdates(this);
	}

	@Override
	public void onStop() {
		this.mLocationClient.removeLocationUpdates(this);
		this.mLocationClient.disconnect();
		super.onStop();
	}

	@Override
	public void onStart() {
		super.onStart();
		this.mLocationClient.connect();
	}
	
	// Get the last known location from all providers
	// return best reading is as accurate as minAccuracy and
	// was taken no longer then minTime milliseconds ago
	private Location bestLastKnownLocation(float minAccuracy, long minTime) {

			Location bestResult = null;
			float bestAccuracy = Float.MAX_VALUE;
			long bestTime = Long.MIN_VALUE;

			// Get the best most recent location currently available
			mCurrentLocation = mLocationClient.getLastLocation();

			if (mCurrentLocation != null) {

				float accuracy = mCurrentLocation.getAccuracy();
				long time = mCurrentLocation.getTime();

				if (accuracy < bestAccuracy) {

					bestResult = mCurrentLocation;
					bestAccuracy = accuracy;
					bestTime = time;

				}
			}

			// Return best reading or null
			if (bestAccuracy > minAccuracy || bestTime < minTime) {
				return null;
			} else {
				return bestResult;
			}
	}
	
	private void notifyLocationAvailableListeners() {
		List<Fragment> fragments = this.getSupportFragmentManager().getFragments();
		if (fragments != null) {
			for (Fragment fragment : fragments) {
				if (fragment != null) {
					if (fragment instanceof LocationAvailableListener) {
						((LocationAvailableListener) fragment).updateLocation(
								mBestReading.getLatitude(), 
								mBestReading.getLongitude());
					}
				}
			}
		}
	}
	
	public interface LocationAvailableListener {
		public void updateLocation(double lat, double lng);
	}
}
