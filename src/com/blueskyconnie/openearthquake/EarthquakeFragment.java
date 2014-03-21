package com.blueskyconnie.openearthquake;

import java.util.List;

import roboguice.fragment.RoboListFragment;
import roboguice.inject.InjectView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.blueskyconnie.openearthquake.adapter.EarthquakeListAdapter;
import com.blueskyconnie.openearthquake.asynchttp.EarthquakeJsonHttpResponseHandler;
import com.blueskyconnie.openearthquake.asynchttp.EarthquakeJsonHttpResponseHandler.HttpResponseCallback;
import com.blueskyconnie.openearthquake.asynchttp.UsgsEarthquakeClient;
import com.blueskyconnie.openearthquake.model.EarthquakeInfo;

public class EarthquakeFragment extends RoboListFragment implements HttpResponseCallback {
	
	private static final String TAG = "EarthquakeFragment";
	
	private EarthquakeListAdapter earthquakeAdapter;
	private String restfulUrl;
	private boolean isDataLoaded;
	
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static final String ARG_URL = "restfulUrl";
	
	@InjectView (R.id.btnLoad)
	private Button btnLoad;
	
	@InjectView (android.R.id.list)
	private ListView lstView;
	
	@InjectView (R.id.progressbar)
	private ProgressBar progressbar;
	
	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static EarthquakeFragment newInstance(String url) {
		EarthquakeFragment fragment = new EarthquakeFragment();
		Bundle args = new Bundle();
		args.putString(ARG_URL, url);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		restfulUrl = "";
		if (getArguments() != null) {
			restfulUrl = getArguments().getString(ARG_URL);
		}
		isDataLoaded = false;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_main, container, false);
	}

	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		earthquakeAdapter = new EarthquakeListAdapter(getActivity(), R.layout.earthquake_row_layout);
		setListAdapter(earthquakeAdapter);
		// by experiment, control is injected here
		btnLoad.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (earthquakeAdapter != null) {
					earthquakeAdapter.loadAdditionalData();
				}
			}
		});
	}
	
	

	@Override
	public void onPause() {
		super.onPause();
		if (progressbar != null) {
			if (progressbar.isShown()) {
				progressbar.setVisibility(View.INVISIBLE);
			}
		}
		
		// cancel requests
		Activity activity = getActivity();
		if (activity != null) {
			UsgsEarthquakeClient.cancelRequests(activity, false);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!isDataLoaded) {
			if (this.getUserVisibleHint()) {
				// call client to get restful data
				if (lstView != null && btnLoad != null && progressbar != null) {
					lstView.setVisibility(View.INVISIBLE);
					btnLoad.setEnabled(false);
					progressbar.setVisibility(View.VISIBLE);
					
					EarthquakeJsonHttpResponseHandler handler = new EarthquakeJsonHttpResponseHandler(this);
					UsgsEarthquakeClient.get(restfulUrl, null, handler);
					Log.i(TAG, "onResume called - resfulUrl = " + restfulUrl);
				}
			}
		}
	}
	
	

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			// call client to get restful data
			if (lstView != null && btnLoad != null && progressbar != null) {
				lstView.setVisibility(View.INVISIBLE);
				btnLoad.setEnabled(false);
				progressbar.setVisibility(View.VISIBLE);
				
				EarthquakeJsonHttpResponseHandler handler = 
						new EarthquakeJsonHttpResponseHandler(this);
				UsgsEarthquakeClient.get(restfulUrl, null, handler);
				Log.i(TAG, "setUserVisibleHint called - resfulUrl = " + restfulUrl);
			}
		}
	}

	@Override
	public void successCallback(List<EarthquakeInfo> newResults) {
		this.isDataLoaded = true;
		this.earthquakeAdapter.addEarthquake(newResults);
		finishLoading();
		Log.i(TAG, "successCallback called.");
	}

	@Override
	public void failedCallback() {
		this.isDataLoaded = false;
		finishLoading();
		Log.i(TAG, "failedCallback called.");
	}
	
	private void finishLoading() {
		if (progressbar != null) {
			progressbar.setVisibility(View.GONE);
		}
		
		if (lstView != null) {
			lstView.setVisibility(View.VISIBLE);
		}
		
		if (btnLoad != null) {
			btnLoad.setEnabled(true);
		}
		Log.i(TAG, "finishLoading called.");
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Object obj = l.getItemAtPosition(position);
		if (obj != null && obj instanceof EarthquakeInfo) {
			EarthquakeInfo info = (EarthquakeInfo) obj;
			Intent mapIntent = new Intent(getActivity(), EarthquakeMapActivity.class);
			mapIntent.putExtra(Constants.EARTHQUAKE_INFO_KEY, info);
			startActivity(mapIntent);
		}
	}
}