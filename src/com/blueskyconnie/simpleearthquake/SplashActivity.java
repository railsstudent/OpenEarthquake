package com.blueskyconnie.simpleearthquake;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.blueskyconnie.simpleearthquake.helper.AlertDialogHelper;
import com.blueskyconnie.simpleearthquake.helper.ConnectionDetector;

@ContentView(R.layout.activity_splash)
public class SplashActivity extends RoboActivity {

	private static final String TAG = "SplashActivity";
	private static final int DELAY_SECONDS = (int) 2.5 * 1000;

	private ConnectionDetector detector;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		detector = new ConnectionDetector(this);
		if (!detector.isConnectingToInternet()) {
			AlertDialogHelper.showNoInternetDialog(this);
			Log.i(TAG, "No Internet Connection. Quit now.");
		} else {
			final Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				public void run() {
					startNextActivity();	
					Log.i(TAG, "Launch next activity");
				}
			}, DELAY_SECONDS);
			
		}
	}

	protected void startNextActivity() {
		if (detector.isConnectingToInternet()) {
			startActivity(new Intent(this, MainActivity.class));
			finish();
		}
	}
}
