package com.hbut.internship.activity;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.hbut.internship.R;
import com.hbut.internship.adapter.PositionListViewAdapter;
import com.hbut.internship.util.Internet;
import com.hbut.internship.view.ListViewRelayout;
import com.hbut.internship.view.RefreshableView;
import com.hbut.internship.view.RefreshableView.PullToRefreshListener;
import com.internship.model.Position;

public class SchoolRecommendActivity extends BaseActivity {

	RefreshableView refreshableView;// 下拉刷新
	private SharedPreferences pref;
	private List<Position> positionList = new ArrayList<Position>();
	private TextView schoolname;
	private String schname, schnum;
	private ListView positionListView;
	private PositionListViewAdapter adapter;
	
	private Handler handler = new Handler() {// 主线程中更新UI
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				schoolname.setText(schname + "推荐");
				break;
			default:
				break;
			}
		}
	};

	private Handler handler1 = new Handler() {// 刷新ListView
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			List<Position> mList = (List<Position>) msg.obj;
			adapter = new PositionListViewAdapter(SchoolRecommendActivity.this,
					R.layout.position_item, mList);
			positionListView.setAdapter(adapter);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schoolrecommend);

		/*
		 * 下拉刷新。
		 */
		refreshableView = (RefreshableView) findViewById(R.id.schoolrecommend_refreshable_view);
		refreshableView.setOnRefreshListener(new PullToRefreshListener() {
			@Override
			public void onRefresh() {

				try {
					Message msg = new Message();
					String account = pref.getString("account", "");
					schname = Internet.queryStudent(account).getSchool()
							.getSchName();
					schnum = Internet.queryStudent(account).getSchool()
							.getSchNum();
					positionList = Internet.querySchRecommend(schnum);
					msg.obj = positionList;
					handler1.sendMessage(msg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				refreshableView.finishRefreshing();
			}
		}, R.id.schoolrecommend_refreshable_view);

		pref = getSharedPreferences("Student", MODE_PRIVATE);

		schoolname = (TextView) findViewById(R.id.tv_schoolrecommend_schoolname);

		final String account = pref.getString("account", "");

		// 获取到学校的名称。
		class MyThread implements Runnable {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					schname = Internet.queryStudent(account).getSchool()
							.getSchName();
					schnum = Internet.queryStudent(account).getSchool()
							.getSchNum();
					positionList = Internet.querySchRecommend(schnum);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		MyThread myThread = new MyThread();
		Thread t = new Thread(myThread);
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 将学校的名称显示在界面上。
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message message = new Message();
				message.what = 0;
				handler.sendMessage(message);
			}
		}).start();

		adapter = new PositionListViewAdapter(SchoolRecommendActivity.this,
				R.layout.position_item, positionList);
		positionListView = (ListView) findViewById(R.id.lv_schoolrecommend);

		positionListView.setAdapter(adapter);
		// ListView的点击事件
		positionListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				Intent it = new Intent(SchoolRecommendActivity.this,
						PositionInformationActivity.class);
				// 通过Bundle传递对象。
				Bundle bundle = new Bundle();
				bundle.putSerializable("position", positionList.get(position));
				it.putExtras(bundle);
				startActivity(it);
			}
		});
		ListViewRelayout.setListViewHeightBasedOnChildren(positionListView);
	}
}
