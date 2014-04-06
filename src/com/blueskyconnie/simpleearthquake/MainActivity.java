package com.blueskyconnie.simpleearthquake;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.blueskyconnie.simpleearthquake.adapter.TabPagerAdapter;
import com.blueskyconnie.simpleearthquake.base.RoboActionBarActivity;

public class MainActivity extends RoboActionBarActivity implements
		ActionBar.TabListener /*,
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener, 
		LocationListener*/ {

	private static final String TAG = "MainActivity";
	
	@InjectResource(R.string.shareSubject)
	private String strExtraSubject;
	
	@InjectResource(R.string.shareText)
	private String strExtraText;
	
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	TabPagerAdapter mTabsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	
	@InjectView (R.id.pager)
	private ViewPager mViewPager;
//	private ImageLoader imageLoader = ImageLoader.getInstance();
	
	private ShareActionProvider mShareActionProvider;
	
	private String appUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		List<Fragment> lstFragment = new ArrayList<Fragment>();
		lstFragment.add(EarthquakeFragment.newInstance(getString(R.string.title_eq_past_hour), 
					"all_hour.geojson"));
		lstFragment.add(EarthquakeFragment.newInstance(getString(R.string.title_eq_past_day), 
					"all_day.geojson"));
		mTabsPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), this, lstFragment);

		// Set up the ViewPager with the sections adapter.
		mViewPager.setAdapter(mTabsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});
		
		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mTabsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mTabsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
		
		appUrl = "http://play.google.com/store/apps/details?id=" + getPackageName();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		MenuItem shareItem = menu.findItem(R.id.action_share);
		mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
		if (mShareActionProvider != null) {
			mShareActionProvider.setShareIntent(createShareIntent());
			Log.i(TAG, "ShareItent set in ShareActionProvider.");
		}
		
		return super.onCreateOptionsMenu(menu);
	}
	


	private Intent createShareIntent() {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, strExtraSubject);
		intent.putExtra(Intent.EXTRA_TEXT, String.format(strExtraText, appUrl));
		return intent;
	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

//	@Override
//	public void onBackPressed() {
//		AlertDialogHelper.showConfirmExitDialog(this);
//	}
	
}
