package com.blueskyconnie.simpleearthquake;

import java.util.HashMap;
import java.util.Map;

import com.blueskyconnie.simpleearthquake.model.EarthquakeInfo;

import android.app.Application;
import android.location.Location;

public class EarthquakeApplication extends Application {
	
	private Location currentLocation;
	private Map<String, Boolean> prefMap = new HashMap<String, Boolean>();

	{
		prefMap.put(EarthquakeInfo.INFO_TYPE.HOURLY.name(), false);
		prefMap.put(EarthquakeInfo.INFO_TYPE.DAILY.name(), false);
	}
	
	public boolean isPreferenceChanged(String key) {
		return prefMap.get(key);
	}

	public void setPreferenceChanged(String key, boolean isPreferenceChanged) {
		prefMap.put(key, isPreferenceChanged);
	}

	public void notifyAllPreferenceChanged() {
		prefMap.put(EarthquakeInfo.INFO_TYPE.HOURLY.name(), true);
		prefMap.put(EarthquakeInfo.INFO_TYPE.DAILY.name(), true);
	}

	public Location getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(Location currentLocation) {
		this.currentLocation = currentLocation;
	}
}
