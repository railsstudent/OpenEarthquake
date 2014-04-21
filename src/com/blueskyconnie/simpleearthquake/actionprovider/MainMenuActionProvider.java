package com.blueskyconnie.simpleearthquake.actionprovider;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import com.blueskyconnie.simpleearthquake.EarthquakePreferenceActivity;
import com.blueskyconnie.simpleearthquake.R;
import com.blueskyconnie.simpleearthquake.model.ActionProviderContext;

/**
 * Create a main menu action provider with menu items
 * @author connie
 *
 */
public class MainMenuActionProvider extends CommonMenuActionProvider {

	private static final String TAG = "MainMenuActionProvider";

	protected static final int MI_SETTINGS = MI_RATE_MY_APP + 50; 

	protected static final int MI_SETTINGS_ORDER = MI_RATE_MY_APP_ORDER + 50; 
	
	private boolean isSubMenuCreated;

	
	public MainMenuActionProvider(Context context) {
		super(context);
		isSubMenuCreated = false;
	}

	@Override
	public View onCreateActionView() {
		return null;
	}

	@Override
	public boolean hasSubMenu() {
		return true;
	}


	@Override
	public void onPrepareSubMenu(SubMenu subMenu) {
		super.onPrepareSubMenu(subMenu);
		
		if (!isSubMenuCreated) {
			isSubMenuCreated = true;
			Log.i(TAG, "OnPrepareSubMenu begins.");

			// add menu item to go to USGS web site
			subMenu.add(GROUP_ID, MI_SETTINGS, MI_SETTINGS_ORDER, R.string.actionProviderSettings)
				.setOnMenuItemClickListener(this);
			Log.i(TAG, "Add menu item to open preference fragment.");

			Log.i(TAG, "OnPrepareSubMenu ends.");
		}
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		
		switch (item.getItemId()) {
			case MI_SETTINGS:
				Log.i(TAG, "onMenuItemClick - Launch Settings Preference clicked.");
				mContext.startActivity(new Intent(mContext, EarthquakePreferenceActivity.class));
				return true;
		}
		return false;
	}

	@Override
	public void initializeData(ActionProviderContext context) {
		super.initializeData(context);
	}
}
