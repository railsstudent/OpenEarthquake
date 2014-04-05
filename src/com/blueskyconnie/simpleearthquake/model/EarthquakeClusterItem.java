package com.blueskyconnie.simpleearthquake.model;

import android.annotation.SuppressLint;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

@SuppressLint("DefaultLocale")
public class EarthquakeClusterItem implements ClusterItem {
    private LatLng mPosition;
    private String place;
    private double magnitude;
    private String earthquakeTime;
    
    public EarthquakeClusterItem(Builder builder) {
    	this.place = builder.place;
    	this.magnitude = builder.magnitude;
    	this.mPosition = new LatLng(builder.lat, builder.lng);
    	this.earthquakeTime = builder.earthquakeTime;
    }
    
    public String getEarthquakeTime() {
    	return this.earthquakeTime;
    }
    
    @Override
    public LatLng getPosition() {
        return mPosition;
    }
    
    public String getPlace() {
    	return place;
    }
    
    public double getMagnitude() {
    	return magnitude;
    }
    
    public static class Builder {
    	
    	private String place;
//    	private String magnitudeType;
    	private double magnitude;
    	private double lat;
    	private double lng;
    	private String earthquakeTime;
    	
    	public Builder place(String place) {
    		this.place = place;
    		return this;
    	}

    	public Builder magnitude(double magnitude) {
    		this.magnitude = magnitude;
    		return this;
    	}
    	
//    	public Builder magnitudeType(String magnitudeType) {
//    		this.magnitudeType = magnitudeType;
//    		return this;
//    	}

    	public Builder lat(double lat) {
    		this.lat = lat;
    		return this;
    	}
    	
    	public Builder lng(double lng) {
    		this.lng = lng;
    		return this;
    	}

    	public Builder earthquakeTime(String earthquakeTime) {
    		this.earthquakeTime = earthquakeTime;
    		return this;
    	}
    	
    	public EarthquakeClusterItem create() {
    		return new EarthquakeClusterItem(this);
    	}
    	
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((earthquakeTime == null) ? 0 : earthquakeTime.hashCode());
		result = prime * result
				+ ((mPosition == null) ? 0 : mPosition.hashCode());
		long temp;
		temp = Double.doubleToLongBits(magnitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((place == null) ? 0 : place.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EarthquakeClusterItem other = (EarthquakeClusterItem) obj;
		if (earthquakeTime == null) {
			if (other.earthquakeTime != null)
				return false;
		} else if (!earthquakeTime.equals(other.earthquakeTime))
			return false;
		if (mPosition == null) {
			if (other.mPosition != null)
				return false;
		} else if (!mPosition.equals(other.mPosition))
			return false;
		if (Double.doubleToLongBits(magnitude) != Double
				.doubleToLongBits(other.magnitude))
			return false;
		if (place == null) {
			if (other.place != null)
				return false;
		} else if (!place.equals(other.place))
			return false;
		return true;
	}
}
