package com.hbut.internship.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hbut.internship.R;
import com.internship.model.Position;

public class PositionListViewAdapter extends ArrayAdapter<Position> {
	
	private int resourceId;

	public PositionListViewAdapter(Context context, int resource,
			List<Position> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		resourceId = resource;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Position job = getItem(position);

		View view;
		ViewHolder viewHolder;

		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.enterprise = (TextView) view
					.findViewById(R.id.tv_enterprisename);
			viewHolder.position = (TextView) view
					.findViewById(R.id.tv_positionname);
			viewHolder.salary = (TextView) view
					.findViewById(R.id.tv_closingdate);
			viewHolder.education = (TextView) view
					.findViewById(R.id.tv_education);
			viewHolder.closingdate = (TextView) view
					.findViewById(R.id.tv_closingdate);
			viewHolder.salary = (TextView) view.findViewById(R.id.tv_salary);
			view.setTag(viewHolder);
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}

		viewHolder.enterprise.setText(job.getEnterprise().getEnName());
		viewHolder.position.setText(job.getPoName());
		
		String date=job.getClosingdate().toString();//获取时间
		viewHolder.closingdate.setText(date.substring(0, 10));//截取日期显示出来。

		String mSalary = job.getSalarymin() + "-" + job.getSalarymax();
		viewHolder.salary.setText(mSalary+"元");
		viewHolder.education.setText(job.getPoEducation());
		return view;
	}

	class ViewHolder {
		TextView enterprise;// 企业名称
		TextView position;// 职位名称
		TextView salary;// 薪水
		TextView closingdate;// 截止日期
		TextView education;// 学历要求
	}
}
