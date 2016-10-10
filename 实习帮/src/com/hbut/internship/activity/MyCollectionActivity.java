package com.hbut.internship.activity;

import java.util.ArrayList;
import java.util.List;

import org.litepal.crud.DataSupport;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;

import com.hbut.internship.R;
import com.hbut.internship.adapter.PositionListViewAdapter;
import com.hbut.internship.util.TimeUtils;
import com.hbut.internship.util.Internet;
import com.hbut.internship.util.MyApplicationUtil;
import com.hbut.internship.util.ToastUtil;
import com.hbut.internship.view.SwipeMenu;
import com.hbut.internship.view.SwipeMenuCreator;
import com.hbut.internship.view.SwipeMenuItem;
import com.hbut.internship.view.SwipeMenuListView;
import com.hbut.internship.view.SwipeMenuListView.OnMenuItemClickListener;
import com.internship.model.Position;

public class MyCollectionActivity extends BaseActivity {

	private SharedPreferences pref;
	private List<Position> positionList = new ArrayList<Position>();
	private Button clear;
	private ImageButton back;
	private PositionListViewAdapter adapter;
	private SwipeMenuListView mListView;

	private String studentname;

	private Handler handler = new Handler() {// 获取取消单个职位收藏的信息。
		public void handleMessage(Message msg) {
			positionList.remove(msg.what);
			adapter.notifyDataSetChanged();
			mListView.invalidate();
			ToastUtil.showToast(MyApplicationUtil.getContext(), "取消收藏成功！");
		}
	};

	private Handler handler1 = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				ToastUtil.showToast(MyApplicationUtil.getContext(), "取消所有收藏成功！");
				positionList.removeAll(positionList);
				adapter.notifyDataSetChanged();
				mListView.invalidate();
				break;
			case 1:
				ToastUtil.showToast(MyApplicationUtil.getContext(), "取消所有收藏失败！");
				break;
			case 2:
				ToastUtil.showToast(MyApplicationUtil.getContext(), "取消失败！");
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
		setContentView(R.layout.activity_mycollection);

		pref = getSharedPreferences("Student", MODE_PRIVATE);

		class MyThread implements Runnable {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					pref = getSharedPreferences("Student", MODE_PRIVATE);
					studentname = pref.getString("studentname", "");
					List<Position> tempList = Internet.queryCollect(studentname);
					DataSupport.saveAll(tempList);//保存所有的数据至数据库
					int len = tempList.size();
					Time time = new Time("GMT+8");// 获取当前系统时间
					for (int i = 0; i < len; i++) {
						if (TimeUtils.compare_date(tempList.get(i)
								.getClosingdate().toString(), time.toString()) != 1) {
							tempList.add(tempList.get(i));// 将过期的收藏置于最后面
							tempList.remove(i);
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

		back = (ImageButton) findViewById(R.id.imbt_mycollection_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		// 清空按钮的点击事件
		clear = (Button) findViewById(R.id.bt_mycollection_clearall);
		clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder dialog = new AlertDialog.Builder(
						MyCollectionActivity.this);
				dialog.setTitle("清空收藏");
				dialog.setMessage("清空后收藏列表将不可恢复，是否清空？");
				dialog.setCancelable(false);
				dialog.setPositiveButton("确认",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								new Thread(new Runnable() {

									@Override
									public void run() {
										Message msg = new Message();
										// TODO Auto-generated method stub
										try {// 删除所有收藏,true删除成功，false删除失败
											if (Internet.deleteAllCollect(pref
													.getInt("studentID", 0))) {
												msg.what = 0;
												handler1.sendMessage(msg);
											} else {
												msg.what = 1;
												handler1.sendMessage(msg);
											}
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								}).start();
							}
						});
				dialog.setNegativeButton("取消", null);
				dialog.show();
			}
		});
		
		positionList=DataSupport.findAll(Position.class);
		mListView = (SwipeMenuListView) findViewById(R.id.lv_mycollection);
		adapter = new PositionListViewAdapter(MyCollectionActivity.this,
				R.layout.position_item, positionList);
		mListView.setAdapter(adapter);
		mListView.setEmptyView(findViewById(R.id.tv_emptyview_collect));
		initMenuListView();
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MyCollectionActivity.this,
						PositionInformationActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("position", positionList.get(arg2));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}

	private void initMenuListView() {
		// 创建一个SwipeMenuCreator供ListView使用
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {

				// 创建一个侧滑菜单
				SwipeMenuItem delItem = new SwipeMenuItem(
						getApplicationContext());
				// 给该侧滑菜单设置背景
				delItem.setBackground(android.R.color.holo_red_light);
				// 设置宽度
				delItem.setWidth(dp2px(80));
				// 设置图片
				delItem.setTitle("取消收藏");
				delItem.setTitleSize(18);
				delItem.setTitleColor(Color.WHITE);
				// 加入到侧滑菜单中
				menu.addMenuItem(delItem);
			}
		};

		mListView.setMenuCreator(creator);

		// 侧滑菜单的相应事件
		mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(final int position, SwipeMenu menu,
					int index) {
				switch (index) {
				case 1:// 第二个添加的菜单的响应时间(取消收藏)
					new Thread(new Runnable() {

						@Override
						public void run() {
							Message msg = new Message();
							// TODO Auto-generated method stub
							try { // 取消单个收藏,true取消成功，false取消失败
								if (Internet.deleteCollect(
										positionList.get(position).getPoId(),
										studentname)) {
									msg.what = position;
									handler.sendMessage(msg);
								} else {
									msg.what = 2;
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
