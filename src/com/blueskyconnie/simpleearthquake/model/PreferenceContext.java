package com.blueskyconnie.simpleearthquake.model;

public final class PreferenceContext {

	private String magValue;
	private String depthValue;
	private int mapType;
	private String distValue;
	private String limitValue;
	
	
	public int getMapType() {
		return mapType;
	}

	public void setMapType(int mapType) {
		this.mapType = mapType;
	}

	public String getMagValue() {
		return magValue;
	}
	
	public void setMagValue(String magValue) {
		this.magValue = magValue;
	}
	
	public String getDepthValue() {
		return depthValue;
	}
	
	public void setDepthValue(String depthValue) {
		this.depthValue = depthValue;
	}

	public String getDistValue() {
		return distValue;
	}

	public void setDistValue(String distValue) {
		this.distValue = distValue;
	}

	public String getLimitValue() {
		return limitValue;
	}

	public void setLimitValue(String limitValue) {
		this.limitValue = limitValue;
	}
}
