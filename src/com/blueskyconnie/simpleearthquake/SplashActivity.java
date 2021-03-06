package com.blueskyconnie.simpleearthquake;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectResource;
import android.annotation.TargetApi;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.provider.Settings.SettingNotFoundException;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.blueskyconnie.simpleearthquake.helper.AlertDialogHelper;
import com.blueskyconnie.simpleearthquake.helper.ConnectionDetector;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

@ContentView(R.layout.activity_splash)
public class SplashActivity extends RoboActivity implements
	GooglePlayServicesClient.ConnectionCallbacks,
	GooglePlayServicesClient.OnConnectionFailedListener, 
	LocationListener {

	private static final String TAG = "SplashActivity";
//	private static final int DELAY_SECONDS = (int) 2.5 * 1000;
	
	private static final long ONE_MIN = 1000 * 60;
//	private static final long TWO_MIN = ONE_MIN * 2;
	private static final long FIVE_MIN = ONE_MIN * 5;
//	private static final long MEASURE_TIME = 1000 * 30;
	private static final long POLLING_FREQ = 1000 * 10;
	private static final long FASTES_UPDATE_FREQ = 1000 * 2;
	private static final float MIN_ACCURACY = 25.0f;
	private static final float MIN_LAST_READ_ACCURACY = 500.0f;

	private ConnectionDetector detector;
	private LocationClient mLocationClient;
	private Location mCurrentLocation;
	
	// Define an object that holds accuracy and frequency parameters
	private LocationRequest mLocationRequest;
	
	// Current best location estimate
	private Location mBestReading;
	
	private boolean isNextActivityStarted;

	@InjectResource(R.string.latitude)
	private String strLatitude;

	@InjectResource(R.string.longtitude)
	private String strLongtitude;
	
	@TargetApi(Build.VERSION_CODES.KITKAT)
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isNextActivityStarted = false;
		detector = new ConnectionDetector(this);
		if (!detector.isConnectingToInternet()) {
			AlertDialogHelper.showNoInternetDialog(this);
			Log.i(TAG, "No Internet Connection. Quit now.");
		} else {

			if (!servicesAvailable()) {
				Log.i(TAG, "No Google Play Service installed. Quit now.");
				AlertDialogHelper.showNoGooglePlayService(this);
			}
			
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
		        String providers = Secure.getString(getContentResolver(),
		                Secure.LOCATION_PROVIDERS_ALLOWED);
		        if (TextUtils.isEmpty(providers)) {
		        	AlertDialogHelper.accessMyLocationFailed(this);
		        }
		    } else {
		        final int locationMode;
		        try {
		            locationMode = Secure.getInt(getContentResolver(), Secure.LOCATION_MODE);
		            if (locationMode != Secure.LOCATION_MODE_BATTERY_SAVING && 
			        		locationMode != Secure.LOCATION_MODE_HIGH_ACCURACY && 
			        		locationMode != Secure.LOCATION_MODE_SENSORS_ONLY) {
			        	AlertDialogHelper.accessMyLocationFailed(this);
			        }
		        } catch (SettingNotFoundException e) {
		            Log.i(TAG, e.getMessage());
		        	AlertDialogHelper.accessMyLocationFailed(this);
		        }
		    }
			
			mLocationClient = new LocationClient(this, this, this);
			mLocationRequest = LocationRequest.create();
			// Use high accuracy
			mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

			// Update every 10 seconds
			mLocationRequest.setInterval(POLLING_FREQ);

			// Receive updates no more often than every 2 seconds
			mLocationRequest.setFastestInterval(FASTES_UPDATE_FREQ);
			
//			final Handler handler = new Handler();
//			handler.postDelayed(new Runnable() {
//				public void run() {
//					startNextActivity();	
//					Log.i(TAG, "Launch next activity");
//				}
//			}, DELAY_SECONDS);
			
		}
	}

	protected void startNextActivity() {
		if (detector.isConnectingToInternet()) {
			startActivity(new Intent(this, MainActivity.class));
			finish();
		}
	}
	
	@Override
	public void onLocationChanged(Location location) {
		// Determine whether new location is better than current best
		// estimate
		if (null == mBestReading
				|| location.getAccuracy() <= mBestReading.getAccuracy()) {
	
			// Update best estimate
			mBestReading = location;
				
			if (mBestReading.getAccuracy() < MIN_ACCURACY) {
				if (mLocationClient != null) {
					mLocationClient.removeLocationUpdates(this);
				}
				if (!isNextActivityStarted) {
					isNextActivityStarted = true;
					// Update display
					updateDisplay(location, "onLocationChanged");
					startNextActivity();	
					Log.i(TAG, "Launch next activity in onLocationChanged");
				}
			}
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		Log.i(TAG, "onConnectionFailed called.");
	}

	@Override
	public void onConnected(Bundle bundle) {
		// find current location
		// Get first reading. Get additional location updates if necessary
		if (servicesAvailable()) {
			// Get best last location measurement meeting criteria
			mBestReading = bestLastKnownLocation(MIN_LAST_READ_ACCURACY, FIVE_MIN);
	
			if (null == mBestReading) {
//					|| mBestReading.getAccuracy() > MIN_LAST_READ_ACCURACY
//					|| mBestReading.getTime() < System.currentTimeMillis()
//							- TWO_MIN) {
	
				mLocationClient.requestLocationUpdates(mLocationRequest, this);
				
				// Schedule a runnable to unregister location listeners
//				Executors.newScheduledThreadPool(1).schedule(new Runnable() {
//					@Override
//					public void run() {
//						mLocationClient.removeLocationUpdates(SplashActivity.this);
//			    //    	AlertDialogHelper.accessMyLocationFailed(SplashActivity.this);
//						Toast.makeText(SplashActivity.this, "waits 30 seconds", Toast.LENGTH_SHORT).show();
//					}
//				}, MEASURE_TIME, TimeUnit.MILLISECONDS);
			} else {
				if (!isNextActivityStarted) {
					isNextActivityStarted = true;
					// Display last reading information
					updateDisplay(mBestReading, "onConnected");
					startNextActivity();	
					Log.i(TAG, "Launch next activity in onConnected");
				}
			}
		}
	}

	@Override
	public void onDisconnected() {
		Log.i(TAG, "onDisconnected called.");
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
	
	private boolean servicesAvailable() {

		// Check that Google Play Services are available
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

		// If Google Play services is available
		return (ConnectionResult.SUCCESS == resultCode);
	}
	
	// Update display
	private void updateDisplay(Location location, String methodName) {
		Log.i(TAG, String.format("%3$s: latitude:%1$f, longtitude: %2$f", 
				location.getLatitude(), location.getLongitude(), methodName));

		Toast.makeText(this, String.format("%1$s: %2$f, %3$s %4$f", 
				strLatitude, location.getLatitude(), strLongtitude, location.getLongitude()), 
			Toast.LENGTH_LONG).show();
		
		((EarthquakeApplication) getApplication()).setCurrentLocation(location);
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (mLocationClient !=null) {
			mLocationClient.connect();
		}
	}

	@Override
	protected void onStop() {
		if (mLocationClient != null) {
			mLocationClient.removeLocationUpdates(this);
			mLocationClient.disconnect();
		}
		super.onStop();
	}
	
	

}
