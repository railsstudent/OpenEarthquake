package com.blueskyconnie.openearthquake.base;

import roboguice.inject.InjectView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;


public class RoboActionBarListActivity extends RoboActionBarActivity {

	@InjectView (android.R.id.list)
	private ListView mListView;
	
	protected ListView getListView() {
	    return mListView;
	}

	protected void setListAdapter(ListAdapter adapter) {
	    getListView().setAdapter(adapter);
	}

	protected ListAdapter getListAdapter() {
	    ListAdapter adapter = getListView().getAdapter();
	    if (adapter instanceof HeaderViewListAdapter) {
	        return ((HeaderViewListAdapter)adapter).getWrappedAdapter();
	    } else {
	        return adapter;
	    }
	}
}
