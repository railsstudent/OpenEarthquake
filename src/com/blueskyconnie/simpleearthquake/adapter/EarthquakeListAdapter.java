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

import com.blueskyconnie.simpleearthquake.Constants;
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
		if (position < lstInfo.size()) { 
			return lstInfo.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		if (position < lstInfo.size()) {
			return getItem(position).hashCode();
		}
		return - 1;
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
			holder.tvDepth = (TextView) convertView.findViewById(R.id.tvDepth);
			holder.tvDist = (TextView) convertView.findViewById(R.id.tvDist);
			convertView.setTag(holder);
		} else {
			holder = (EarthquakeHolder) convertView.getTag();
		}
		Object objRowData = getItem(position);
		if (objRowData != null) {
			EarthquakeInfo objEarthquake = (EarthquakeInfo) objRowData;
			holder.tvMagnitude.setText(Constants.df.format(objEarthquake.getMagnitude()));
			holder.tvPlace.setText(objEarthquake.getPlace());
			holder.tvTime.setText(objEarthquake.getLocalTime());
			String adapterDepth = context.getString(R.string.adapterDepth);
			adapterDepth = String.format(adapterDepth, Constants.df.format(objEarthquake.getDepth()), 
					context.getString(R.string.kilometer));
			holder.tvDepth.setText(adapterDepth);

			String adapterDist = context.getString(R.string.adapterDist);
			adapterDist = String.format(adapterDist, Constants.df.format(objEarthquake.getDistance()), 
					context.getString(R.string.kilometer));
			holder.tvDist.setText(adapterDist);
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
	
	public void setEarthquake(List<EarthquakeInfo> newLstEarthquake) {
		this.lstInfo.clear();
		if (newLstEarthquake != null) {
			lstInfo.addAll(newLstEarthquake);
		}
		count = BUFFER_SIZE;
		count = Math.min(count, lstInfo.size());
		notifyDataSetChanged();
	}
	
	private static class EarthquakeHolder {
		TextView tvMagnitude;
		TextView tvPlace;
		TextView tvTime;
		TextView tvDepth;
		TextView tvDist;
	}
	
	public interface LoadDataCallback {
		public void onAfterDataLoad();		
	}

}
