package com.blueskyconnie.simpleearthquake;

import android.app.Application;
import android.location.Location;

public class EarthquakeApplication extends Application {
	
	private boolean isPreferenceChanged;
	private Location currentLocation;

	public boolean isPreferenceChanged() {
		return isPreferenceChanged;
	}

	public void setPreferenceChanged(boolean isPreferenceChanged) {
		this.isPreferenceChanged = isPreferenceChanged;
	}

	public Location getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(Location currentLocation) {
		this.currentLocation = currentLocation;
	}
}
