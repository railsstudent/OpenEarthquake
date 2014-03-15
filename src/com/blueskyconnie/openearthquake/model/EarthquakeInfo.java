package com.blueskyconnie.openearthquake.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EarthquakeInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss", Locale.getDefault());
	
	private double coordX;
	private double coordY;
	private double altitude;
	private String url;
	private double magnitude;
	private String place;
	private Date date;
	
	static class EarthquakeInfoBuilder {
		
		private double coordX;
		private double coordY;
		private double altitude;
		private String url;
		private double magnitude;
		private String place;
		private long time;
		
		public EarthquakeInfoBuilder coordX(double coordX) {
			this.coordX = coordX;
			return this;
		}

		public EarthquakeInfoBuilder coordY(double coordY) {
			this.coordY = coordY;
			return this;
		}

		public EarthquakeInfoBuilder altitude(double altitude) {
			this.altitude = altitude;
			return this;
		}
		
		public EarthquakeInfoBuilder url(String url) {
			this.url = url;
			return this;
		}
		
		public EarthquakeInfoBuilder magnitude(double magnitude) {
			this.magnitude = magnitude;
			return this;
		}

		public EarthquakeInfoBuilder place(String place) {
			this.place = place;
			return this;
		}
		
		public EarthquakeInfoBuilder time(long time) {
			this.time = time;
			return this;
		}
		
		public EarthquakeInfo create() {
			return new EarthquakeInfo(this);
		}
	}
	
	public EarthquakeInfo(EarthquakeInfoBuilder builder) {
		this.coordX = builder.coordX;
		this.coordY = builder.coordY;
		this.altitude = builder.altitude;
		this.magnitude = builder.magnitude;
		this.place = builder.place;
		this.url = builder.url;
		date = new Date(builder.time);
	}
	
	public double getCoordX() {
		return coordX;
	}
	
	public void setCoordX(double coordX) {
		this.coordX = coordX;
	}
	public double getCoordY() {
		return coordY;
	}

	public void setCoordY(double coordY) {
		this.coordY = coordY;
	}
	
	public double getAltitude() {
		return altitude;
	}
	
	public void setAltitude(double altitude) {
		this.altitude = altitude;
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
		return sdf.format(date);
	}
}
