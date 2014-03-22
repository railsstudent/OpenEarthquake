package com.blueskyconnie.openearthquake;

import roboguice.activity.RoboSplashActivity;
import android.content.Intent;
import android.os.Bundle;

import com.blueskyconnie.openearthquake.helper.AlertDialogHelper;
import com.blueskyconnie.openearthquake.helper.ConnectionDetector;

public class SplashActivity extends RoboSplashActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_splash);
	}

	@Override
	protected void startNextActivity() {
		ConnectionDetector detector = new ConnectionDetector(this);
		if (detector.isConnectingToInternet()) {
			startActivity(new Intent(this, MainActivity.class));
		} else {
			 AlertDialogHelper.showNoInternetDialog(this);
		}
		
	}
}
