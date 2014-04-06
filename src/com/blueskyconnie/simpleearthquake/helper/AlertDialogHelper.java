package com.blueskyconnie.simpleearthquake.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.blueskyconnie.simpleearthquake.R;

public final class AlertDialogHelper {

	public static void showNoInternetDialog(final Activity currentActivity) {
		AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
		builder.setTitle(currentActivity.getString(R.string.info_title));
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setMessage(currentActivity.getString(R.string.no_internet_error));
		builder.setNeutralButton(currentActivity.getString(R.string.confirm_exit), 
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (currentActivity != null) {
						currentActivity.finish();
					}
				}
			})
			.create()
			.show();
	}
	
//	public static void showConfirmExitDialog(final Activity activity) {
//		
//		// prompt confirmation dialog before exit
//		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				switch (which) {
//					case DialogInterface.BUTTON_NEGATIVE:   // confirm to exit
//						// close dialog and do nothing
//						if (activity != null) {
//							dialog.dismiss();
//							activity.finish();
//						} else {
//							Crouton.makeText(activity, R.string.exit_error, Style.ALERT).show();	
//						}
//						break;
//					case DialogInterface.BUTTON_POSITIVE:   // cancel
//						dialog.dismiss();
//						break;
//				}
//			}
//		};
//		
//		if (activity != null) {
//			new AlertDialog.Builder(activity)
//				.setTitle(R.string.app_name)
//				.setIcon(R.drawable.ic_launcher)
//				.setMessage(R.string.title_confirm_exit)
//				.setPositiveButton(R.string.cancel_exit, listener)
//				.setNegativeButton(R.string.confirm_exit, listener)
//				.create()
//				.show();
//		}
//	}
}