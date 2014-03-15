package com.blueskyconnie.openearthquake;

import roboguice.fragment.RoboListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blueskyconnie.openearthquake.MainActivity.LocationAvailableListener;

/**
 * A placeholder fragment containing a simple view.
 */
public class EarthquakeFragment extends RoboListFragment
	implements LocationAvailableListener {
	
	private static final String TAG = "EarthquakeFragment";
	
	private String restfulUrl;
	
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";

	private TextView textView;
	
	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static EarthquakeFragment newInstance(int sectionNumber) {
		EarthquakeFragment fragment = new EarthquakeFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public EarthquakeFragment() {
		restfulUrl = "";
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		textView = (TextView) rootView.findViewById(R.id.section_label);
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
		}
	}

	@Override
	public void updateLocation(double lat, double lng) {
		if (textView != null) {
			textView.setText(lat + "," + lng + "-" + this.getArguments().get(ARG_SECTION_NUMBER));
			Log.i(TAG, "updateLocation: " + lat + "," + lng + "-" + this.getArguments().get(ARG_SECTION_NUMBER));
		}
	}
}