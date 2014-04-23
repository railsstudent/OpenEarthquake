package com.blueskyconnie.simpleearthquake;

import java.util.List;

import roboguice.fragment.RoboListFragment;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.blueskyconnie.simpleearthquake.adapter.EarthquakeListAdapter;
import com.blueskyconnie.simpleearthquake.adapter.EarthquakeListAdapter.LoadDataCallback;
import com.blueskyconnie.simpleearthquake.asynchttp.EarthquakeJsonHttpResponseHandler;
import com.blueskyconnie.simpleearthquake.asynchttp.EarthquakeJsonHttpResponseHandler.HttpResponseCallback;
import com.blueskyconnie.simpleearthquake.asynchttp.UsgsEarthquakeClient;
import com.blueskyconnie.simpleearthquake.db.QuakeDataSource;
import com.blueskyconnie.simpleearthquake.db.QuakeSQLiteOpenHelper;
import com.blueskyconnie.simpleearthquake.helper.SearchDataHelper;
import com.blueskyconnie.simpleearthquake.model.EarthquakeInfo;
import com.blueskyconnie.simpleearthquake.model.EarthquakeInfo.INFO_TYPE;
import com.blueskyconnie.simpleearthquake.model.SearchCriteria;
import com.google.common.base.Strings;

public class EarthquakeFragment extends RoboListFragment implements
		HttpResponseCallback, LoadDataCallback,
		SwipeRefreshLayout.OnRefreshListener, 
		SearchView.OnQueryTextListener {

	private static final String TAG = "EarthquakeFragment";

	private EarthquakeListAdapter earthquakeAdapter;
	private String restfulUrl;
	private String title_earthquake_map;
	private boolean isDataLoaded;
	private boolean isLoadingData;

	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	private static final String ARG_URL = "restfulUrl";

	private static final String ARG_TITLE = "eq_title";

	private static final String ARG_TYPE = "eq_info_type";

	@InjectView(R.id.btnLoad)
	private Button btnLoad;

	@InjectView(android.R.id.list)
	private ListView lstView;

	// @InjectView (R.id.progressbar)
	// private ProgressBar progressbar;

	@InjectView(R.id.tvTotal)
	private TextView tvTotal;

	@InjectView(R.id.swiperefreshlayout)
	private SwipeRefreshLayout swipeRefreshLayout;

	@InjectResource(R.string.strTotalFormatter)
	private String strTotalFormatter;

	@InjectResource(R.string.filterPlaceHint)
	private String queryHint;
	
	private int totalRecords;

	private EarthquakeJsonHttpResponseHandler handler;
	private String infoType;
	
	private MenuItem menuShowMap;
	private SearchView mSearchView;
	
	private QuakeSQLiteOpenHelper dbHelper;
	private QuakeDataSource quakeDS;
	
	private SharedPreferences mPref;
	private SearchDataHelper searchHelper;

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static EarthquakeFragment newInstance(String title, String url,
			INFO_TYPE infoType) {
		EarthquakeFragment fragment = new EarthquakeFragment();
		Bundle args = new Bundle();
		args.putString(ARG_URL, url);
		args.putString(ARG_TITLE, title);
		args.putString(ARG_TYPE, infoType.name());
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		setHasOptionsMenu(true);
		restfulUrl = "";
		if (getArguments() != null) {
			Bundle bundle = getArguments();
			restfulUrl = bundle.getString(ARG_URL);
			title_earthquake_map = bundle.getString(ARG_TITLE);
			infoType = bundle.getString(ARG_TYPE);
		}
		isDataLoaded = false;
		isLoadingData = false;
		dbHelper = new QuakeSQLiteOpenHelper(getActivity());
		quakeDS = new QuakeDataSource(dbHelper.getWritableDatabase());
		handler = new EarthquakeJsonHttpResponseHandler(this.getActivity(),
				this, infoType, quakeDS);
		searchHelper = new SearchDataHelper(quakeDS);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_main, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		earthquakeAdapter = new EarthquakeListAdapter(getActivity(),
				R.layout.earthquake_row_layout, this);
		setListAdapter(earthquakeAdapter);
//		setListAdapter(new EndlessQuakeAdapter(earthquakeAdapter, quakeDS, infoType, 8));
		// by experiment, control is injected here
		btnLoad.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (earthquakeAdapter != null) {
					earthquakeAdapter.loadAdditionalData();
				}
			}
		});
		if (swipeRefreshLayout != null) {
			swipeRefreshLayout.setOnRefreshListener(this);
			swipeRefreshLayout.setColorScheme(R.color.color1, R.color.color2,
					R.color.color3, R.color.color4);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		// if (progressbar != null) {
		// if (progressbar.isShown()) {
		// progressbar.setVisibility(View.INVISIBLE);
		// }
		// }
		if (this.swipeRefreshLayout != null) {
			if (swipeRefreshLayout.isRefreshing()) {
				swipeRefreshLayout.setRefreshing(false);
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
		mPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
		loadData();
		Log.i(TAG, "onResume called - resfulUrl = " + restfulUrl);
	}

	private void loadData() {
		if (isLoadingData) {
			Log.i(TAG, "loadData() - isLoadingData = " + isLoadingData + ", exit early.");
			return;
		}
		
		boolean isRefreshing = (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing());
		Log.i(TAG, "loadData - isRefreshing = " + isRefreshing);
		
		Activity activity = this.getActivity();
		EarthquakeApplication application = null;
		if (activity != null) {
			application = (EarthquakeApplication) activity.getApplicationContext();
		}
		boolean isPreferenceChanged = (application != null ? application.isPreferenceChanged() : false);
		Log.i(TAG, "loadData - isPreferenceChanged = " + isPreferenceChanged);
		
//		if (!isDataLoaded || isRefreshing) {
		if (!isDataLoaded || isRefreshing || isPreferenceChanged) {
			if (this.getUserVisibleHint()) {
				if (isPreferenceChanged) {
					application.setPreferenceChanged(false);
				}
				
				// call client to get restful data
				if (lstView != null && btnLoad != null /*&& progressbar != null*/) {
					tvTotal.setVisibility(View.INVISIBLE);
					lstView.setVisibility(View.INVISIBLE);
					btnLoad.setEnabled(false);
					btnLoad.setVisibility(View.INVISIBLE);
					//progressbar.setVisibility(View.VISIBLE);
					// hide menu items 
					setMenuItemVisible(false);
					if (!isRefreshing) {
						swipeRefreshLayout.setEnabled(false);
						swipeRefreshLayout.setRefreshing(true);
					}
					isLoadingData = true;
					UsgsEarthquakeClient.get(restfulUrl, null, handler);
				}
			}
		} 
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			// call client to get restful data
			loadData();
			Log.i(TAG, "setUserVisibleHint called - resfulUrl = " + restfulUrl);
		}
	}

	@Override
	public void successCallback() {
		
		filterData(mSearchView.getQuery().toString());
		finishLoading();
		isDataLoaded = true;
		isLoadingData = false;
		Log.i(TAG, "successCallback called.");
	}

	@Override
	public void failedCallback() {
		totalRecords = 0;
		tvTotal.setText(String.format(strTotalFormatter, totalRecords,
				earthquakeAdapter.getCount()));
		finishLoading();
		isDataLoaded = false;
		isLoadingData = false;
		Log.i(TAG, "failedCallback called.");
	}

	private void finishLoading() {
		// if (progressbar != null) {
		// progressbar.setVisibility(View.GONE);
		// }

		if (swipeRefreshLayout != null) {
			if (swipeRefreshLayout.isRefreshing()) {
				swipeRefreshLayout.setEnabled(true);
				swipeRefreshLayout.setRefreshing(false);
			}
		}

		if (tvTotal != null) {
			tvTotal.setVisibility(View.VISIBLE);
		}

		if (lstView != null) {
			lstView.setVisibility(View.VISIBLE);
		}

		if (btnLoad != null) {
			btnLoad.setEnabled(true);
			btnLoad.setVisibility(View.VISIBLE);
		}

		setMenuItemVisible(true);
		
		Log.i(TAG, "finishLoading called.");
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Object obj = l.getItemAtPosition(position);
		if (obj != null && obj instanceof EarthquakeInfo) {
			EarthquakeInfo info = (EarthquakeInfo) obj;
			Intent mapIntent = new Intent(getActivity(),
					EarthquakeMapActivity.class);
			mapIntent.putExtra(Constants.EARTHQUAKE_INFO_KEY, info);
			startActivity(mapIntent);
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.earthquake_fragment_menu, menu);
		menuShowMap = menu.findItem(R.id.action_map);
		MenuItem menuSearchItem = menu.findItem(R.id.action_search);
		mSearchView = (SearchView) MenuItemCompat.getActionView(menuSearchItem);
		mSearchView.setOnQueryTextListener(this);
		mSearchView.setIconifiedByDefault(false);
		mSearchView.setSubmitButtonEnabled(true);
		mSearchView.setQueryHint(queryHint);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case R.id.action_map:
				Intent mapIntent = new Intent(getActivity(),
						EarthquakeAllMapActivity.class);
				mapIntent
						.putExtra(Constants.EARTHQUAKE_TITLE, title_earthquake_map);
				mapIntent.putExtra(Constants.EARTHQUAKE_TYPE, infoType);
				if (mSearchView != null && mSearchView.getQuery() != null) {
					Log.i(TAG, "mSearchView.getQuery = " + mSearchView.getQuery().toString());
					mapIntent.putExtra(Constants.SEARCH_PLACE, mSearchView.getQuery().toString());
				} else {
					mapIntent.putExtra(Constants.SEARCH_PLACE, "");
				}
				startActivity(mapIntent);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onAfterDataLoad() {
		tvTotal.setText(String.format(strTotalFormatter, totalRecords,
				earthquakeAdapter.getCount()));
	}

	@Override
	public void onRefresh() {
		// call client to get restful data
		swipeRefreshLayout.setEnabled(false);
		swipeRefreshLayout.setRefreshing(true);
		loadData();
		Log.i(TAG, "onRefresh called - resfulUrl = " + restfulUrl);
	}
	
	private void setMenuItemVisible(boolean visible) {
		if (menuShowMap != null) {
			menuShowMap.setVisible(visible);
		}
	}

	private void filterData(String query) {
		List<EarthquakeInfo> lstEarthquake = null;
		String strPrefMagValue = Constants.ALL;
		String strPrefDepthValue = Constants.ALL;
		
		if (mPref != null) {
			strPrefMagValue = mPref.getString(Constants.PREF_KEY_MAGNITUDE, Constants.ALL);
			strPrefDepthValue = mPref.getString(Constants.PREF_KEY_DEPTH, Constants.ALL);
		}
		
		SearchCriteria criteria = new SearchCriteria();
		criteria.setInfoType(infoType);
		criteria.setPlace(query);
		criteria.setStrPrefDepthValue(strPrefDepthValue);
		criteria.setStrPrefMagValue(strPrefMagValue);
		lstEarthquake = searchHelper.search(criteria);
		
		earthquakeAdapter.addEarthquake(lstEarthquake);
		totalRecords = lstEarthquake.size();
		tvTotal.setText(String.format(strTotalFormatter, totalRecords, earthquakeAdapter.getCount()));
	}
	
	@Override
	public boolean onQueryTextChange(String newText) {

		if (Strings.isNullOrEmpty(newText)) {
			if (this.isLoadingData) {
				Log.i(TAG, "onQueryTextChange - isLoadingData = " + isLoadingData + ", skip query.");
				return false;
			}

			// show all result in database
			filterData(newText);
			Log.i(TAG, "onQueryTextChange called - newText = " + newText);
		}
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		// search database
		filterData(query);
		Log.i(TAG, "onQueryTextSubmit called - query = " + query);
		return false;
	}

}