package com.blueskyconnie.simpleearthquake.model;

public final class PreferenceContext {

	private String magValue;
	private String depthValue;
	private int mapType;
	
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
}
