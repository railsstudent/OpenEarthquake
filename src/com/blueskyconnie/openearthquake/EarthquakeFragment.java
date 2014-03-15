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
import android.widget.TextView;

import com.blueskyconnie.openearthquake.MainActivity.LocationAvailableListener;
import com.blueskyconnie.openearthquake.asynchttp.UsgsEarthquakeClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class EarthquakeFragment extends RoboListFragment
	implements LocationAvailableListener {
	
	private static final String TAG = "EarthquakeFragment";
	
	private String restfulUrl;
	private boolean isDataLoaded;
	
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static final String ARG_URL = "restfulUrl";

	@InjectView (R.id.section_label)
	private TextView textView;
	
	@InjectView (R.id.btnLoad)
	private Button btnLoad;
	
	@InjectView (android.R.id.list)
	private ListView lstView;
	
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
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		
		btnLoad.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// increase size of baseAdapter
			}
		});
		
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		double lat = ((EarthquakeApplication) getActivity().getApplicationContext()).getCurrentLat();
		double lng = ((EarthquakeApplication) getActivity().getApplicationContext()).getCurrentLng();
		updateLocation(lat, lng);
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			// call client to get restful data
			lstView.setVisibility(View.INVISIBLE);
			btnLoad.setEnabled(false);
			UsgsEarthquakeClient.get(restfulUrl, null, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject response) {
				}

				@Override
				public void onFinish() {
					super.onFinish();
					lstView.setVisibility(View.VISIBLE);
					btnLoad.setEnabled(true);
				}
			});
		}
	}

	@Override
	public void updateLocation(double lat, double lng) {
		if (textView != null) {
			textView.setText(lat + "," + lng + "-");
			Log.i(TAG, "updateLocation: " + lat + "," + lng);
		}
	}
}