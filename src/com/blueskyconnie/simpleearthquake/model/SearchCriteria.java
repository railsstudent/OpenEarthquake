package com.blueskyconnie.simpleearthquake.model;

import com.blueskyconnie.simpleearthquake.Constants;

public final class SearchCriteria {

	private String place;
	private String strPrefMagValue;
	private String strPrefDepthValue;
	private String strPrefDistValue;
	private String strPrefLimitValue;
	private String infoType;
	
	private double prefMagValue = 0;
	private double prefDepthValue = 0;
	private double prefDistValue = 0;
	private int prefLimitValue = 0;
	
	public double getPrefMagValue() {
		return prefMagValue;
	}

	public double getPrefDepthValue() {
		return prefDepthValue;
	}

	public double getPrefDistValue() {
		return prefDistValue;
	}


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
		if (strPrefMagValue != null && strPrefMagValue.equals(Constants.ALL)) {
			return;
		}
		try { 
			prefMagValue = Double.parseDouble(strPrefMagValue);
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
		}
	}
	
	public String getStrPrefDepthValue() {
		return strPrefDepthValue;
	}
	
	public void setStrPrefDepthValue(String strPrefDepthValue) {
		this.strPrefDepthValue = strPrefDepthValue;
		if (strPrefDepthValue != null && strPrefDepthValue.equals(Constants.ALL)) {
			return;
		}
		try { 
			prefDepthValue = Double.parseDouble(strPrefDepthValue);
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
		}
	}

	public String getStrPrefDistValue() {
		return strPrefDistValue;
	}

	public void setStrPrefDistValue(String strPrefDistValue) {
		this.strPrefDistValue = strPrefDistValue;
		if (strPrefDistValue != null && strPrefDistValue.equals(Constants.ALL)) {
			return;
		}
		try { 
			prefDistValue = Double.parseDouble(strPrefDistValue);
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
		}
	}

	public String getStrPrefLimitValue() {
		return strPrefLimitValue;
	}

	public void setStrPrefLimitValue(String strPrefLimitValue) {
		this.strPrefLimitValue = strPrefLimitValue;
		if (strPrefLimitValue != null && strPrefLimitValue.equals(Constants.ALL)) {
			return;
		}
		try { 
			prefLimitValue = Integer.parseInt(strPrefLimitValue);
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
		}
	}
	
	public int getPrefLimitValue() {
		return prefLimitValue;
	}
}
