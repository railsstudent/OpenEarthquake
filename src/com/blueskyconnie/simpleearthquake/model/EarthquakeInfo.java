package com.blueskyconnie.simpleearthquake.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.joda.time.LocalDateTime;

import com.google.common.base.Strings;

public class EarthquakeInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss z", Locale.getDefault()); 
	
	private double lat;
	private double lng;
	private double depth;
	private String url;
	private double magnitude;
	// private String magnitudeType;
	private String place;
	private long time;
	//private double dmin;
	
	public static class Builder {
		
		private double lat;
		private double lng;
		private double depth;
		private String url;
		private double magnitude;
		private String place;
		private long time;
	//	private String magnitudeType;
//		private double dmin;
		
		public Builder lat(final double lat) {
			this.lat = lat;
			return this;
		}

		public Builder lng(final double lng) {
			this.lng = lng;
			return this;
		}

		public Builder depth(final double depth) {
			this.depth = depth;
			return this;
		}
		
		public Builder url(final String url) {
			this.url = Strings.nullToEmpty(url);
			return this;
		}
		
		public Builder magnitude(final double magnitude) {
			this.magnitude = magnitude;
			return this;
		}
		
//		public Builder magnitudeType(final String magnitudeType) {
//			this.magnitudeType = Strings.nullToEmpty(magnitudeType).toLowerCase(Locale.getDefault());
//			return this;
//		}

		public Builder place(final String place) {
			this.place = Strings.nullToEmpty(place);
			return this;
		}
		
		public Builder time(final long time) {
			this.time = time;
			return this;
		}
		
//		public Builder dmin(final double dmin) {
//			this.dmin = dmin;
//			return this;
//		}
		
		public EarthquakeInfo create() {
			return new EarthquakeInfo(this);
		}
	}
	
	public EarthquakeInfo(Builder builder) {
		this.lat = builder.lat;
		this.lng = builder.lng;
		this.depth = builder.depth;
		this.magnitude = builder.magnitude;
		this.place = builder.place;
		this.url = builder.url;
//		this.magnitudeType = builder.magnitudeType;
		this.time = builder.time;
//		this.dmin = builder.dmin;
	}
	
	public double getLatitude() {
		return lat;
	}
	
	public void setLatitude(double latitude) {
		this.lat = latitude;
	}
	public double getLongtitude() {
		return lng;
	}

	public void setLongtitude(double longtitude) {
		this.lng = longtitude;
	}
	
	public double getDepth() {
		return depth;
	}
	
	public void setDepth(double depth) {
		this.depth = depth;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public double getMagnitude() {
		return magnitude;
	}
	
	public void setMagnitude(double magnitude) {
		this.magnitude = magnitude;
	}
	
	public String getPlace() {
		return place;
	}
	
	public void setPlace(String place) {
		this.place = place;
	}
	
	public String getTime() {
		return sdf.format(new LocalDateTime(time).toDate());
	}
	
//	public String getMagnitudeType() {
//		return magnitudeType;
//	}
//
//	public void setMagnitudeType(String magnitudeType) {
//		this.magnitudeType = magnitudeType;
//	}

//	public double getDmin() {
//		return dmin;
//	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(depth);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(lat);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(lng);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(magnitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((place == null) ? 0 : place.hashCode());
		result = prime * result + (int) (time ^ (time >>> 32));
		result = prime * result + ((url == null) ? 0 : url.hashCode());
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
		EarthquakeInfo other = (EarthquakeInfo) obj;
		if (Double.doubleToLongBits(depth) != Double
				.doubleToLongBits(other.depth))
			return false;
		if (Double.doubleToLongBits(lat) != Double.doubleToLongBits(other.lat))
			return false;
		if (Double.doubleToLongBits(lng) != Double.doubleToLongBits(other.lng))
			return false;
		if (Double.doubleToLongBits(magnitude) != Double
				.doubleToLongBits(other.magnitude))
			return false;
		if (place == null) {
			if (other.place != null)
				return false;
		} else if (!place.equals(other.place))
			return false;
		if (time != other.time)
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}
	
	
}
