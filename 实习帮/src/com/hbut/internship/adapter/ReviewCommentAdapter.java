package com.hbut.internship.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hbut.internship.R;
import com.hbut.internship.util.TextToDBCUtil;
import com.internship.model.Apply;

public class ReviewCommentAdapter extends ArrayAdapter<Apply> {

	private int resourceId;

	public ReviewCommentAdapter(Context context, int resource,
			List<Apply> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		resourceId = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Apply mApply = getItem(position);
		View view;
		ViewHolder viewHolder;
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.time = (TextView) view
					.findViewById(R.id.tv_comment_time);
			viewHolder.account = (TextView) view
					.findViewById(R.id.tv_comment_account);
			viewHolder.content = (TextView) view.findViewById(R.id.tv_comment);
			// viewHolder.head=(ImageView)
			// view.findViewById(R.id.iv_comment_head);
			viewHolder.star = (RatingBar) view
					.findViewById(R.id.rb_commentratingbar);
			viewHolder.poname = (TextView) view
					.findViewById(R.id.tv_comment_poname);
			view.setTag(viewHolder);
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.time
				.setText(mApply.getSettime().toString().substring(0, 10));
		viewHolder.content.setText(TextToDBCUtil.toDBC(mApply.getEvaluation()));
		String mAccount = mApply.getStudent().getEmail();
		String str1 = mAccount.substring(0, 2);
		viewHolder.account.setText(mAccount.replace(str1, "***"));
		// viewHolder.head.setImageResource(comment.getImageviewResource());
		viewHolder.star.setRating(mApply.getStarevalu());// 设置星级评价。
		viewHolder.poname.setText(mApply.getPosition().getPoName());
		return view;
	}

	class ViewHolder {

		TextView time;

		TextView content;

		TextView poname;

		RatingBar star;

		TextView account;

		// ImageView head;
	}
}
