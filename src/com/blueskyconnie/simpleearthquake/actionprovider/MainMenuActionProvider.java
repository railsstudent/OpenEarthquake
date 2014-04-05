package com.blueskyconnie.simpleearthquake.actionprovider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.ActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.SubMenu;
import android.view.View;

import com.blueskyconnie.simpleearthquake.AboutActivity;
import com.blueskyconnie.simpleearthquake.R;

/**
 * Create a main menu action provider with menu items
 * @author connie
 *
 */
public class MainMenuActionProvider extends ActionProvider implements OnMenuItemClickListener {

	private static final String TAG = "MainMenuActionProvider";
	private static final int GROUP_ID = 0;

	private static final int MENU_ITEM_WEBSITE = Menu.FIRST; 
	private static final int MENU_ITEM_ABOUT_APP = Menu.FIRST + 1; 
	private static final int MENU_ITEM_RATE_MY_APP = Menu.FIRST + 2; 
	private static final int MENU_ITEM_EXIT_APP = Menu.FIRST + 3;
	
	private Context mContext;
	private boolean isSubMenuCreated;
	
	private String strUsgsSite;
	private String strPlayStore;
	
	public MainMenuActionProvider(Context context) {
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
		// TODO Auto-generated method stub
		return super.onPerformDefaultAction();
	}

	@Override
	public void onPrepareSubMenu(SubMenu subMenu) {
		super.onPrepareSubMenu(subMenu);
		
		if (!isSubMenuCreated) {
			isSubMenuCreated = true;
			int order = 0;
			Log.i(TAG, "OnPrepareSubMenu begins.");

			// add menu item to go to USGS web site
			subMenu.add(GROUP_ID, MENU_ITEM_WEBSITE, order, R.string.actionProviderWebsite)
				.setOnMenuItemClickListener(this);
			order++;
			Log.i(TAG, "Add menu item to launch website.");

			// add menu item to rate my app in google play store
			subMenu.add(GROUP_ID, MENU_ITEM_ABOUT_APP, order, R.string.actionProviderAboutApp)
				.setOnMenuItemClickListener(this);
			order++;
			
			// add menu item to show about SimpleQuake activity
			subMenu.add(GROUP_ID, MENU_ITEM_RATE_MY_APP, order, R.string.actionProviderRateApp)
				.setOnMenuItemClickListener(this);
			order++;
			
			// add menu item to exit this application
			subMenu.add(GROUP_ID, MENU_ITEM_EXIT_APP, order, R.string.actionProviderExit)
				.setOnMenuItemClickListener(this);
			Log.i(TAG, "OnPrepareSubMenu ends.");
		}
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		
		switch (item.getItemId()) {
			case MENU_ITEM_WEBSITE:
				Log.i(TAG, "onMenuItemClick - Visit USGS website clicked.");
				mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(strUsgsSite)));
				return true;
			case MENU_ITEM_ABOUT_APP:
				Log.i(TAG, "onMenuItemClick - About my application clicked.");
				mContext.startActivity(new Intent(mContext, AboutActivity.class));
				return true;
			case MENU_ITEM_RATE_MY_APP:
				Log.i(TAG, "onMenuItemClick - Rate my application clicked.");
				String playStoreFullUrl = String.format(strPlayStore, mContext.getPackageName());
				mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(playStoreFullUrl)));
				return true;
			case MENU_ITEM_EXIT_APP:
				Log.i(TAG, "onMenuItemClick - Exit my application clicked.");
				((Activity) mContext).finish();
				return true;
		}
		return false;
	}
}
