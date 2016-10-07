package com.hbut.internship.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;

import com.hbut.internship.R;
import com.hbut.internship.adapter.MyApplyAdapter;
import com.hbut.internship.util.Internet;
import com.hbut.internship.util.MyApplicationUtil;
import com.hbut.internship.util.ToastUtil;
import com.hbut.internship.view.SwipeMenu;
import com.hbut.internship.view.SwipeMenuCreator;
import com.hbut.internship.view.SwipeMenuItem;
import com.hbut.internship.view.SwipeMenuListView;
import com.hbut.internship.view.SwipeMenuListView.OnMenuItemClickListener;
import com.internship.model.Apply;

public class MyApplyActivity extends BaseActivity {

	private SharedPreferences preferences;

	private ImageButton back;
	private SwipeMenuListView mListView;
	private List<Apply> mList = new ArrayList<Apply>();
	private MyApplyAdapter adapter;

	private int studentID;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			mList.remove(msg.what);
			adapter.notifyDataSetChanged();
			mListView.invalidate();
			ToastUtil.showToast(MyApplicationUtil.getContext(), "撤销申请成功！");
		};
	};

	private Handler handler1 = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				ToastUtil.showToast(MyApplicationUtil.getContext(), "撤销申请失败！");
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myapply);
		// 返回按钮
		back = (ImageButton) findViewById(R.id.imbt_myapply_back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		class MyThread implements Runnable {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					preferences = getSharedPreferences("Student", MODE_PRIVATE);
					studentID = preferences.getInt("studentID", 0);
					mList = Internet.GetApply(studentID);
					int len = mList.size();
					for (int i = 0; i < len; i++) {
						// 将状态为正在实习的职位置顶
						if (mList.get(i).getApply_state() == 0) {
							mList.add(0, mList.get(i));
							mList.remove(i + 1);
							break;
						}
						// 将实习已经结束的实习不显示在“我的申请”界面上。
						if (mList.get(i).getApply_state() == 4) {
							mList.remove(i);
						}
					}
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

		mListView = (SwipeMenuListView) findViewById(R.id.lv_myapply);
		adapter = new MyApplyAdapter(MyApplyActivity.this,
				R.layout.myapplylistview_item, mList);

		mListView.setAdapter(adapter);
		initMenuListView();// 初始化滑动菜单
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MyApplyActivity.this,
						MyApplySpecificInfoActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("applyposition", mList.get(position));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		mListView.setEmptyView(findViewById(R.id.tv_emptyview));
	}

	private void initMenuListView() {
		// 创建一个SwipeMenuCreator供ListView使用
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// 创建一个侧滑菜单

				SwipeMenuItem openItem2 = new SwipeMenuItem(
						getApplicationContext());
				// 给该侧滑菜单设置背景
				openItem2.setBackground(new ColorDrawable(Color.rgb(0xFF, 0x3B,
						0x2F)));
				// 设置宽度
				openItem2.setWidth(dp2px(80));
				// 设置名称
				openItem2.setTitle("撤销申请");
				// 字体大小
				openItem2.setTitleSize(18);
				// 字体颜色
				openItem2.setTitleColor(Color.WHITE);
				// 加入到侧滑菜单中
				menu.addMenuItem(openItem2);
			}
		};

		mListView.setMenuCreator(creator);

		// 侧滑菜单的相应事件
		mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(final int position, SwipeMenu menu,
					int index) {
				switch (index) {
				case 0:// 第1个添加的菜单的响应时间(取消申请)
					new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Message msg = new Message();
							try {
								if (Internet.RevokeApply(mList.get(position)
										.getApplyId())) {
									msg.what = position;
									handler.sendMessage(msg);
								} else {
									msg.what = 0;
									handler1.sendMessage(msg);
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}).start();
					break;
				default:
					break;
				}
				return false;
			}
		});
	}

	// 将dp转化为px
	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}

}
