package com.blueskyconnie.simpleearthquake.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blueskyconnie.simpleearthquake.R;
import com.blueskyconnie.simpleearthquake.model.EarthquakeInfo;

public class EarthquakeListAdapter extends BaseAdapter {

	private static final int BUFFER_SIZE = 8;
	
	private List<EarthquakeInfo> lstInfo;
	private int count;
	private Context context;
	private int resourceId;
	private LoadDataCallback loadCallback;
	
	public EarthquakeListAdapter(Context context, int resourceId, LoadDataCallback loadCallback) {
		this.context = context;
		this.resourceId = resourceId;
		lstInfo = new ArrayList<EarthquakeInfo>();
		count = 0;
		this.loadCallback = loadCallback;
	}
	
	@Override
	public int getCount() {
		return count;
	}

	@Override
	public Object getItem(int position) {
		return lstInfo.get(position);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		EarthquakeHolder holder = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(resourceId, null);
			holder = new EarthquakeHolder();
			holder.tvMagnitude = (TextView) convertView.findViewById(R.id.tvMagnitude);
			holder.tvPlace = (TextView) convertView.findViewById(R.id.tvPlace);
			holder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
			convertView.setTag(holder);
		} else {
			holder = (EarthquakeHolder) convertView.getTag();
		}
		Object objRowData = getItem(position);
		if (objRowData != null) {
			EarthquakeInfo objEarthquake = (EarthquakeInfo) objRowData;
			holder.tvMagnitude.setText("" + objEarthquake.getMagnitude());
			holder.tvPlace.setText(objEarthquake.getPlace());
			holder.tvTime.setText(objEarthquake.getTime());
		}
		return convertView;
	}
	
	public void loadAdditionalData() {
		count += BUFFER_SIZE;
		count = Math.min(count, lstInfo.size());
		this.notifyDataSetChanged();
		if (loadCallback != null) {
			loadCallback.onAfterDataLoad();
			Log.i("EarthquakeListAdapter", "loadCallback fired.");
		}
		Log.i("EarthquakeListAdapter", "count = " + count);
	}
	
	public void addEarthquake(List<EarthquakeInfo> newLstEarthquake) {
		this.lstInfo.clear();
		if (newLstEarthquake != null) {
			this.lstInfo.addAll(newLstEarthquake);
		}
		count = BUFFER_SIZE;
		count = Math.min(count, lstInfo.size());
		this.notifyDataSetChanged();
	}
	
	private static class EarthquakeHolder {
		TextView tvMagnitude;
		TextView tvPlace;
		TextView tvTime;
	}
	
	public interface LoadDataCallback {
		public void onAfterDataLoad();		
	}

}
