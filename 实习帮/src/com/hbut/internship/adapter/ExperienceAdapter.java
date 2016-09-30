package com.hbut.internship.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hbut.internship.R;
import com.hbut.internship.util.TextToDBCUtil;
import com.internship.model.Apply;

public class ExperienceAdapter extends ArrayAdapter<Apply> {

	private int resourceId;

	public ExperienceAdapter(Context context, int resource, List<Apply> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		resourceId = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Apply experienceApply = getItem(position);
		View view;
		ViewHolder viewHolder;
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.title = (TextView) view
					.findViewById(R.id.tv_experience_title);
			viewHolder.experience = (TextView) view
					.findViewById(R.id.tv_experience);
			viewHolder.time = (TextView) view
					.findViewById(R.id.tv_experience_time);
			viewHolder.enname = (TextView) view
					.findViewById(R.id.tv_experience_enname);
			view.setTag(viewHolder);
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.title.setText(experienceApply.getPosition().getPoName());
		viewHolder.experience.setText(TextToDBCUtil.toDBC(experienceApply
				.getSummary()));
		viewHolder.enname.setText(experienceApply.getPosition().getEnterprise()
				.getEnName());
		viewHolder.time.setText(experienceApply.getSettime().toString()
				.substring(0, 10));
		return view;
	}
	class ViewHolder {

		TextView title;
		TextView enname;
		TextView time;
		TextView experience;
	}

}
