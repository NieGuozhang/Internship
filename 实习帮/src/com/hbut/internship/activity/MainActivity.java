package com.hbut.internship.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

import com.hbut.internship.R;
import com.hbut.internship.broadcast.NetReceiver;
import com.hbut.internship.util.ToastUtil;

public class MainActivity extends TabActivity {

	NetReceiver mReceiver = new NetReceiver();
	IntentFilter mFilter = new IntentFilter();

	private boolean isExit = false;// 用于判断连续两次按back键
	private Handler handler = new Handler();

	private TabHost tabhost;
	private RadioGroup main_radiogroup;
	private RadioButton schoolrecommend, myinternship, findinternship;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tabhost);
		ActivityCollector.addActivity(this);

		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mReceiver, mFilter);

		main_radiogroup = (RadioGroup) findViewById(R.id.main_radiogroup);

		// 往TabWidget添加Tab
		schoolrecommend = (RadioButton) findViewById(R.id.tab_schrecommend);
		myinternship = (RadioButton) findViewById(R.id.tab_myintern);
		findinternship = (RadioButton) findViewById(R.id.tab_findintern);

		tabhost = getTabHost();
		tabhost.addTab(tabhost.newTabSpec("tag1").setIndicator("0")
				.setContent(new Intent(this, SchoolRecommendActivity.class)));
		tabhost.addTab(tabhost.newTabSpec("tag2").setIndicator("1")
				.setContent(new Intent(this, FindInternshipActivity.class)));
		tabhost.addTab(tabhost.newTabSpec("tag3").setIndicator("2")
				.setContent(new Intent(this, MyInternshipActivity.class)));

		// 设置监听事件
		main_radiogroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// TODO Auto-generated method stub
						switch (checkedId) {
						case R.id.tab_schrecommend:
							tabhost.setCurrentTab(0);
							break;
						case R.id.tab_findintern:
							tabhost.setCurrentTab(1);
							break;
						case R.id.tab_myintern:
							tabhost.setCurrentTab(2);
							break;
						}
					}
				});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(mReceiver);
		ActivityCollector.removeActivity(this);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		moveTaskToBack(true);
		if (isExit) {
			ActivityCollector.finishAll();// 退出所有的Activity，退出APP
		} else {
			ToastUtil.showtoast("再按一次退出");
			isExit = true;// 两秒之内为true;
			handler.postDelayed(new Runnable() {
				// 两秒之后将isExit重新置为false

				@Override
				public void run() {
					// TODO Auto-generated method stub
					isExit = false;
				}
			}, 2000);
		}
	}
}