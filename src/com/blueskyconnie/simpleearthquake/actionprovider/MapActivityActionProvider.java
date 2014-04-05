package com.blueskyconnie.simpleearthquake.actionprovider;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.MenuItem;
import android.view.SubMenu;

import com.blueskyconnie.simpleearthquake.R;
import com.blueskyconnie.simpleearthquake.model.ActionProviderContext;

public class MapActivityActionProvider extends MainMenuActionProvider {

	private static final String TAG = "MapActivityActionProvider";

	private static final int MI_DID_U_FEEL_IT = MI_WEBSITE + 1;
	private static final int MI_SUMMARY = MI_DID_U_FEEL_IT + 1;

	private static final int MI_DID_U_FEEL_IT_ORDER = MI_WEBSITE_ORDER + 1;
	private static final int MI_SUMMARY_ORDER = MI_DID_U_FEEL_IT_ORDER + 1;
	
	private String didUFeelItUrl;
	private String summaryUrl;
	private boolean isSubMenuCreated;
	
	public MapActivityActionProvider(Context context) {
		super(context);
		isSubMenuCreated = false;
	}

	@Override
	public void initializeData(ActionProviderContext context) {
		super.initializeData(context);
		didUFeelItUrl = context.getDidUFeelItUrl();
		summaryUrl = context.getSummaryUrl();
	}

	@Override
	public void onPrepareSubMenu(SubMenu subMenu) {
		super.onPrepareSubMenu(subMenu);
		if (!isSubMenuCreated) {
			isSubMenuCreated = true;
			subMenu.add(GROUP_ID, MI_DID_U_FEEL_IT, MI_DID_U_FEEL_IT_ORDER, 
				R.string.actionProviderDidUFeelIt)
				.setOnMenuItemClickListener(this);

			subMenu.add(GROUP_ID, MI_SUMMARY, MI_SUMMARY_ORDER, 
					R.string.actionProviderSummary)
					.setOnMenuItemClickListener(this);
		}
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		switch (item.getItemId()) {
			case MI_DID_U_FEEL_IT:
				Log.i(TAG, "url: " + didUFeelItUrl);
				mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(didUFeelItUrl)));
				return true;
			case MI_SUMMARY:
				Log.i(TAG, "url: " + summaryUrl);
				mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(summaryUrl)));
				return true;
		}
		return super.onMenuItemClick(item);
	}
}
