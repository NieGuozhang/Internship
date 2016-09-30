package com.hbut.internship.view;

import com.hbut.internship.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

/*
 * 继承RelativeLayout自定义标题栏
 */
public class TopBar extends RelativeLayout {

	private Context context;

	// 定义2个控件
	private ImageButton imageButton;
	private TextView tvTitle;

	public TopBar(Context context) {
		// TODO Auto-generated constructor stub
		this(context, null);
	}

	public TopBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TopBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		this.context = context;
		// 设置RelativeLayout的布局
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		// 设置默认的背景色
		setBackgroundColor(Color.parseColor("#00BFFF"));
		init();
	}

	private void init() {
		// TODO Auto-generated method stub

		// 初始化左边的imageButton
		imageButton = new ImageButton(context);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		// 垂直居中
		lp.addRule(RelativeLayout.CENTER_VERTICAL);
		// 设置距离左侧10dp
		lp.leftMargin = dp2px(context, 10);
		imageButton.setLayoutParams(lp);
		imageButton.setBackgroundColor(Color.parseColor("#00BFFF"));
		imageButton.setScaleType(ScaleType.FIT_XY);
		imageButton.setImageResource(R.drawable.zuo);
		imageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		// 初始化中间的标题控件
		tvTitle = new TextView(context);
		lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT);
		tvTitle.setLayoutParams(lp);
		tvTitle.setTextColor(Color.WHITE);
		tvTitle.setTextSize(18);

		addView(imageButton);
		addView(tvTitle);

	}

	// 设置标题栏背景颜色 string
	public void setBackColor(String backColors) {
		setBackgroundColor(Color.parseColor(backColors));
	}

	// 设置标题栏背景颜色 int
	public void setBackColor(int backColori) {
		setBackgroundColor(backColori);
	}

	// 设置中间控件的文字
	public void setTitleText(String titletext) {
		tvTitle.setText(titletext);
	}

	// 设置中间控件文字的大小
	public void setTitleTextSize(float titleTextSize) {
		tvTitle.setTextSize(titleTextSize);
	}

	// dp转px的方法
	private int dp2px(Context context, float dpVal) {
		// TODO Auto-generated method stub
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dpVal, context.getResources().getDisplayMetrics());
	}

}
