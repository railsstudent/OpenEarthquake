package com.blueskyconnie.openearthquake;

import org.json.JSONObject;

import roboguice.fragment.RoboListFragment;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.blueskyconnie.openearthquake.adapter.EarthquakeListAdapter;
import com.blueskyconnie.openearthquake.asynchttp.UsgsEarthquakeClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class EarthquakeFragment extends RoboListFragment {
	
	private static final String TAG = "EarthquakeFragment";
	
	private EarthquakeListAdapter earthquakeAdapter;
	private String restfulUrl;
	private boolean isRestUrlCalled;
	
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
		isRestUrlCalled = false;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_main, container, false);
	}

	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		// by experiment, control is injected here
		btnLoad.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		if (!isRestUrlCalled) {
			// make restful call to retrieve earthquake data
		}
	}

	@Override
	public void onResume() {
		super.onResume();
//		double lat = ((EarthquakeApplication) getActivity().getApplicationContext()).getCurrentLat();
//		double lng = ((EarthquakeApplication) getActivity().getApplicationContext()).getCurrentLng();
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
				UsgsEarthquakeClient.get(restfulUrl, null, new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject response) {
						Log.i(TAG, "onSuccess called - restful url = " + restfulUrl);
					}
	
					@Override
					public void onFinish() {
						super.onFinish();
						lstView.setVisibility(View.VISIBLE);
						btnLoad.setEnabled(true);
						progressbar.setVisibility(View.GONE);
						isRestUrlCalled = true;
						Log.i(TAG, "onFinish called");
					}
				});
			}
		}
	}
}