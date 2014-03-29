package com.blueskyconnie.simpleearthquake.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.blueskyconnie.simpleearthquake.R;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class TabPagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> lstFragment;
	private Context context;

	public TabPagerAdapter(FragmentManager fm, Context context, List<Fragment> lstFragment) {
		super(fm);
		this.context = context;
		this.lstFragment = lstFragment;
	}

	@Override
	public Fragment getItem(int position) {
		return lstFragment.get(position);
	}

	@Override
	public int getCount() {
		return lstFragment.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		switch (position) {
			case 0:
				return context.getString(R.string.past_hour_title);
			case 1:
				return context.getString(R.string.past_day_title);
		}
		return "";
	}
}