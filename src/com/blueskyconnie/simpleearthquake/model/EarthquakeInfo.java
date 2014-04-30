package com.blueskyconnie.simpleearthquake.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.joda.time.LocalDateTime;

import com.google.android.gms.maps.model.LatLng;
import com.google.common.base.Strings;
import com.google.maps.android.clustering.ClusterItem;

public class EarthquakeInfo implements Serializable, ClusterItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss z", Locale.getDefault()); 
	
	public enum INFO_TYPE  { DAILY, HOURLY };
	
	private String id;
	private double lat;
	private double lng;
	private double depth;
	private String url;
	private double magnitude;
	// private String magnitudeType;
	private String place;
	private long time;
	//private double dmin;
	private INFO_TYPE type;
	private String doYouFeelItTag;
	private String summaryTag;
	private long internalSequence;
	private double distance;
	
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
		private String id;
		private INFO_TYPE type;
		private long internalSequence;
		private double distance = 0;
		
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

		public Builder id(final String id) {
			this.id = id;
			return this;
		}
		
		public Builder type(final INFO_TYPE type) {
			this.type = type;
			return this;
		}
		
		public Builder internalSequence(final long internalSequence) {
			this.internalSequence = internalSequence;
			return this;
		}
		
		public Builder distance(final double distance) {
			this.distance = distance;
			return this;
		}
		
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
		this.type = builder.type;
		this.id = builder.id;
		this.internalSequence = builder.internalSequence;
//		this.dmin = builder.dmin;
		this.distance = builder.distance;
	}
	
	public long getInternalSequence() {
		return internalSequence;
	}

	public double getLatitude() {
		return lat;
	}
	
	public double getLongtitude() {
		return lng;
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

	public String getSummaryUrl() {
		return url + summaryTag;
	}

	public String getDoYouFeelItUrl() {
		return url + doYouFeelItTag;
	}

	public void setDoYouFeelItTag(String doYouFeelItTag) {
		this.doYouFeelItTag = doYouFeelItTag;
	}
	
	public double getMagnitude() {
		return magnitude;
	}
	
	public String getPlace() {
		return place;
	}
	
	public String getLocalTime() {
		return sdf.format(new LocalDateTime(time).toDate());
	}
	
	public long getTime() {
		return time;
	}
	
//	public double getDmin() {
//		return dmin;
//	}

	public INFO_TYPE getType() {
		return type;
	}
	
	public String getId() {
		return id;
	}

	@Override
	public LatLng getPosition() {
		return new LatLng(lat, lng);
	}

	public void setSummaryTag(String summaryTag) {
		this.summaryTag = summaryTag;
	}
	
	public double getDistance() {
		return distance;
	}
	
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(depth);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(distance);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((doYouFeelItTag == null) ? 0 : doYouFeelItTag.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ (int) (internalSequence ^ (internalSequence >>> 32));
		temp = Double.doubleToLongBits(lat);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(lng);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(magnitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((place == null) ? 0 : place.hashCode());
		result = prime * result
				+ ((summaryTag == null) ? 0 : summaryTag.hashCode());
		result = prime * result + (int) (time ^ (time >>> 32));
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		if (Double.doubleToLongBits(distance) != Double
				.doubleToLongBits(other.distance))
			return false;
		if (doYouFeelItTag == null) {
			if (other.doYouFeelItTag != null)
				return false;
		} else if (!doYouFeelItTag.equals(other.doYouFeelItTag))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (internalSequence != other.internalSequence)
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
		if (summaryTag == null) {
			if (other.summaryTag != null)
				return false;
		} else if (!summaryTag.equals(other.summaryTag))
			return false;
		if (time != other.time)
			return false;
		if (type != other.type)
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}
}
