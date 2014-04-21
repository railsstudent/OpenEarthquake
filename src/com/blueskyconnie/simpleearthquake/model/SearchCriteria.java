package com.blueskyconnie.simpleearthquake.model;

public final class SearchCriteria {

	private String place;
	private String strPrefMagValue;
	private String strPrefDepthValue;
	private String infoType;
	
	public String getInfoType() {
		return infoType;
	}

	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}

	public String getPlace() {
		return place;
	}
	
	public void setPlace(String place) {
		this.place = place;
	}
	
	public String getStrPrefMagValue() {
		return strPrefMagValue;
	}
	
	public void setStrPrefMagValue(String strPrefMagValue) {
		this.strPrefMagValue = strPrefMagValue;
	}
	
	public String getStrPrefDepthValue() {
		return strPrefDepthValue;
	}
	
	public void setStrPrefDepthValue(String strPrefDepthValue) {
		this.strPrefDepthValue = strPrefDepthValue;
	}
	
}
