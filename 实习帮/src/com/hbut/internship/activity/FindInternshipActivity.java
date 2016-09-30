package com.hbut.internship.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.hbut.internship.R;
import com.hbut.internship.adapter.PositionListViewAdapter;
import com.hbut.internship.util.Internet;
import com.hbut.internship.view.RefreshableView;
import com.hbut.internship.view.RefreshableView.PullToRefreshListener;
import com.internship.model.Position;

public class FindInternshipActivity extends BaseActivity {

	RefreshableView refreshableView;// 下拉刷新
	private EditText searchposition;
	private ListView jobListView;
	private List<Position> positionList = new ArrayList<Position>();
	private PositionListViewAdapter adapter;

	// 下拉刷新ListView的数据。
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			List<Position> mList = (List<Position>) msg.obj;
			adapter = new PositionListViewAdapter(FindInternshipActivity.this,
					R.layout.position_item, mList);
			jobListView.setAdapter(adapter);

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_findinternship);
		/*
		 * 下拉刷新。
		 */
		refreshableView = (RefreshableView) findViewById(R.id.findintren_refreshable_view);
		refreshableView.setOnRefreshListener(new PullToRefreshListener() {
			@Override
			public void onRefresh() {
				try {
					Message msg = new Message();
					msg.obj = positionList;
					handler.sendMessage(msg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				refreshableView.finishRefreshing();
			}
		}, R.id.findintren_refreshable_view);

		// 获取所有的实习职位数据
		class MyThread implements Runnable {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					
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

		searchposition = (EditText) findViewById(R.id.et_search);
		searchposition.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FindInternshipActivity.this,
						FindInternshipSearchActivity.class);
				startActivity(intent);
			}
		});

		adapter = new PositionListViewAdapter(FindInternshipActivity.this,
				R.layout.position_item, positionList);

		jobListView = (ListView) findViewById(R.id.lv_findinternship);
		jobListView.setAdapter(adapter);

		// ListView的点击事件
		jobListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				Intent it = new Intent(FindInternshipActivity.this,
						PositionInformationActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("position", positionList.get(position));
				it.putExtras(bundle);
				startActivity(it);
			}
		});

	}
}
