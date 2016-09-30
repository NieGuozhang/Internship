package com.hbut.internship.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FiterViewHolder {
	private SparseArray<View> mViews;
	private LayoutInflater layoutInflater;
	private View mContentView;

	public FiterViewHolder(int itemLayoutId, ViewGroup parent, Context mContext) {
		mViews = new SparseArray<View>();
		layoutInflater = LayoutInflater.from(mContext);
		mContentView = layoutInflater.inflate(itemLayoutId, parent, false);
		mContentView.setTag(this);
	}

	public static FiterViewHolder get(int position, View convertView,
			ViewGroup parent, int layoutId, Context context) {
		FiterViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new FiterViewHolder(layoutId, parent, context);
		} else {
			viewHolder = (FiterViewHolder) convertView.getTag();
		}
		return viewHolder;
	}

	public <T extends View> T getView(int viewId) {
		View v = mViews.get(viewId);
		if (v == null) {
			v = mContentView.findViewById(viewId);
			mViews.put(viewId, v);
		}
		return (T) v;
	}

	public FiterViewHolder setTvText(int viewId, String str) {
		TextView v = getView(viewId);
		v.setText(str);
		return this;
	}

	public FiterViewHolder setBtnText(int viewId, String str) {
		TextView v = getView(viewId);
		v.setText(str);
		return this;
	}

	public View getmContentView() {
		return mContentView;
	}
}
