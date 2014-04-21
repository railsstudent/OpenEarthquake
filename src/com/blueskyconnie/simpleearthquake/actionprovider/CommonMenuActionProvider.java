package com.blueskyconnie.simpleearthquake.actionprovider;

import com.blueskyconnie.simpleearthquake.AboutActivity;
import com.blueskyconnie.simpleearthquake.R;
import com.blueskyconnie.simpleearthquake.model.ActionProviderContext;
import com.blueskyconnie.simpleearthquake.model.IActionProviderContext;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.ActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;

/**
 * 
 * @author connieleung
 *
 */
public  class CommonMenuActionProvider extends ActionProvider implements OnMenuItemClickListener
	, IActionProviderContext  {

	private static final String TAG = "CommonMenuActionProvider";
	protected static final int GROUP_ID = 0;

	protected static final int MI_WEBSITE = Menu.FIRST; 
	protected static final int MI_ABOUT_APP = Menu.FIRST + 10; 
	protected static final int MI_RATE_MY_APP = Menu.FIRST + 20; 

	protected static final int MI_WEBSITE_ORDER = 0; 
	protected static final int MI_ABOUT_APP_ORDER = 10; 
	protected static final int MI_RATE_MY_APP_ORDER = 20; 

	protected Context mContext;
	private boolean isSubMenuCreated;
	
	private String strUsgsSite;
	private String strPlayStore;
	
	public CommonMenuActionProvider(Context context) {
		super(context);
		mContext = context;
		strUsgsSite = context.getString(R.string.usgs_website);
		strPlayStore = context.getString(R.string.play_store_uri);
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
	public boolean onPerformDefaultAction() {
		return super.onPerformDefaultAction();
	}

	@Override
	public void onPrepareSubMenu(SubMenu subMenu) {
		super.onPrepareSubMenu(subMenu);
		
		if (!isSubMenuCreated) {
			isSubMenuCreated = true;
			Log.i(TAG, "OnPrepareSubMenu begins.");

			// add menu item to go to USGS web site
			subMenu.add(GROUP_ID, MI_WEBSITE, MI_WEBSITE_ORDER, R.string.actionProviderWebsite)
				.setOnMenuItemClickListener(this);
			Log.i(TAG, "Add menu item to launch website.");

			// add menu item to rate my app in google play store
			subMenu.add(GROUP_ID, MI_ABOUT_APP, MI_ABOUT_APP_ORDER, R.string.actionProviderAboutApp)
				.setOnMenuItemClickListener(this);
			Log.i(TAG, "Add menu item to open about app activity.");
			
			// add menu item to show about SimpleQuake activity
			subMenu.add(GROUP_ID, MI_RATE_MY_APP, MI_RATE_MY_APP_ORDER, R.string.actionProviderRateApp)
				.setOnMenuItemClickListener(this);
			Log.i(TAG, "Add menu item to launch google play store to rate this application.");
			
			Log.i(TAG, "OnPrepareSubMenu ends.");
		}
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		
		switch (item.getItemId()) {
			case MI_WEBSITE:
				Log.i(TAG, "onMenuItemClick - Visit USGS website clicked.");
				mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(strUsgsSite)));
				return true;
			case MI_ABOUT_APP:
				Log.i(TAG, "onMenuItemClick - About my application clicked.");
				mContext.startActivity(new Intent(mContext, AboutActivity.class));
				return true;
			case MI_RATE_MY_APP:
				Log.i(TAG, "onMenuItemClick - Rate my application clicked.");
				String playStoreFullUrl = String.format(strPlayStore, mContext.getPackageName());
				mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(playStoreFullUrl)));
				return true;
		}
		return false;
	}

	@Override
	public void initializeData(ActionProviderContext context) {
		
	}
}
