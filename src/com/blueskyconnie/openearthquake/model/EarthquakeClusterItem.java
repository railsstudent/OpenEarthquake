package com.blueskyconnie.openearthquake.model;

import java.util.Locale;

import android.annotation.SuppressLint;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class EarthquakeClusterItem implements ClusterItem {
    private final LatLng mPosition;
    private String place;
    private String magnitude;
    
    public EarthquakeClusterItem(String place, double  magnitude, String mag_type, double lat, double lng) {
    	this.place = place;
    	this.magnitude = magnitude + " " + mag_type.toUpperCase(Locale.getDefault());
        mPosition = new LatLng(lat, lng);
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
    
    public String getPlace() {
    	return place;
    }
    
    public String getMagnitude() {
    	return magnitude;
    }
}
