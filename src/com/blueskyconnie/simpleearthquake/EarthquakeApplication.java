package com.blueskyconnie.simpleearthquake;

import android.app.Application;

public class EarthquakeApplication extends Application {
	
	private boolean isPreferenceChanged;

	public boolean isPreferenceChanged() {
		return isPreferenceChanged;
	}

	public void setPreferenceChanged(boolean isPreferenceChanged) {
		this.isPreferenceChanged = isPreferenceChanged;
	}
}
