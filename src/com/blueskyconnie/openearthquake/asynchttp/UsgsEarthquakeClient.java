package com.blueskyconnie.openearthquake.asynchttp;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class UsgsEarthquakeClient {

	private static final String BASE_URL = "http://earthquake.usgs.gov/earthquakes/feed/v0.1/summary/";

	private static final AsyncHttpClient client = new AsyncHttpClient();
	
	public static void get (String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}
	
	public static void post (String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		client.post(getAbsoluteUrl(url), params, responseHandler);
	}
	
	private static final String getAbsoluteUrl(String relativeUrl) {
		return BASE_URL + relativeUrl.trim();
	}
	
	public static void cancelRequests(android.content.Context context, boolean mayInterruptIfRunning) {
		client.cancelRequests(context, mayInterruptIfRunning);
	}
}
