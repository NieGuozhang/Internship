package com.hbut.internship.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hbut.internship.R;
import com.internship.model.Apply;

public class MyApplyAdapter extends ArrayAdapter<Apply> {

	private int resourceId;

	public MyApplyAdapter(Context context, int resource, List<Apply> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		resourceId = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Apply apply = getItem(position);

		View view;
		ViewHolder viewHolder;

		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.enterprise = (TextView) view
					.findViewById(R.id.tv_myapply_enname);
			viewHolder.position = (TextView) view
					.findViewById(R.id.tv_myapply_poname);
			viewHolder.salary = (TextView) view
					.findViewById(R.id.tv_myapply_salary);
			viewHolder.status = (TextView) view
					.findViewById(R.id.tv_myapply_status);
			view.setTag(viewHolder);
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.enterprise.setText(apply.getPosition().getEnterprise()
				.getEnName());
		viewHolder.position.setText(apply.getPosition().getPoName());

		String mSalary = apply.getPosition().getSalarymin() + "-"
				+ apply.getPosition().getSalarymax();
		viewHolder.salary.setText(mSalary);

		// 根据获取status的类型，动态地设置其颜色。
		switch (apply.getApply_state()) {
		case 0:
			viewHolder.status.setTextColor(Color.rgb(0xFF, 0x00, 0x00));
			viewHolder.status.setText("正在实习");
			break;
		case 1:
			viewHolder.status.setTextColor(Color.rgb(0x82, 0x7C, 0x79));
			viewHolder.status.setText("申请中");
			break;
		case 2:
			viewHolder.status.setTextColor(Color.rgb(0x00, 0x00, 0xA0));
			viewHolder.status.setText("审核通过");
			break;
		case 3:
			viewHolder.status.setTextColor(Color.rgb(0xFF, 0x00, 0x00));
			viewHolder.status.setText("审核未通过");
		case 4:
			viewHolder.status.setTextColor(Color.rgb(0xEE, 0x7C, 0x80));
			viewHolder.status.setText("实习结束");
		default:
			break;
		}
		return view;
	}

	class ViewHolder {

		TextView enterprise;// 企业名称

		TextView position;// 职位名称

		TextView salary;// 薪资

		TextView status;// 申请状态
	}
}
