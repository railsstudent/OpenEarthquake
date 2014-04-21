package com.blueskyconnie.simpleearthquake.model;

public final class ActionProviderContext {
	
	private String didUFeelItUrl;
	private String summaryUrl;
	
	public ActionProviderContext() {
		didUFeelItUrl = "";
		summaryUrl = "";
	}
	
	public void setDidUFeelItUrl(String url) {
		this.didUFeelItUrl = url;
	}
	
	public String getDidUFeelItUrl() {
		return didUFeelItUrl;
	}

	public void setSummaryUrl(String summaryUrl) {
		this.summaryUrl = summaryUrl;
	}
	
	public String getSummaryUrl() {
		return summaryUrl;
	}
}
