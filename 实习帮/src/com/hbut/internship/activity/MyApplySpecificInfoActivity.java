package com.hbut.internship.activity;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hbut.internship.R;
import com.hbut.internship.util.Internet;
import com.hbut.internship.util.MyApplication;
import com.hbut.internship.util.ObjectAndByteUtil;
import com.hbut.internship.util.ToastUtil;
import com.internship.model.Apply;

public class MyApplySpecificInfoActivity extends BaseActivity implements
		OnClickListener {

	private SharedPreferences.Editor editor;// 用于存对象。

	private SharedPreferences pref;

	private Button setinternship, revokeintern;
	private ImageButton back;
	private LinearLayout linearLayout;

	private TextView poname, enname, applytime, alltime, starttime, status;

	private Apply apply;
	private LinearLayout layout;
	private int studentID;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:// 该职位已被设为正在实习的职位
				setinternship.setVisibility(View.GONE);
				revokeintern.setVisibility(View.VISIBLE);
				revokeintern.setEnabled(true);
				status.setText("正在实习");
				ToastUtil.showToast(MyApplication.getContext(), "设置实习成功！");
				break;
			case 1:// 该职位撤销成功
				revokeintern.setVisibility(View.GONE);
				setinternship.setVisibility(View.VISIBLE);
				setinternship.setEnabled(true);
				setinternship.setText("设为实习");
				status.setText("审核通过");
				ToastUtil.showToast(MyApplication.getContext(), "撤销实习成功！");
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
		setContentView(R.layout.activity_myapply_specificinfor);

		pref = getSharedPreferences("Student", MODE_PRIVATE);
		editor = getSharedPreferences("SetPosition", MODE_PRIVATE).edit();

		// 初始化控件
		back = (ImageButton) findViewById(R.id.imbt_apply_back);
		linearLayout = (LinearLayout) findViewById(R.id.linearLayout_applypoinfor);
		poname = (TextView) findViewById(R.id.tv_apply_poname);
		enname = (TextView) findViewById(R.id.tv_apply_enname);
		applytime = (TextView) findViewById(R.id.tv_ma_applytime);
		starttime = (TextView) findViewById(R.id.tv_ma_internstarttime);
		alltime = (TextView) findViewById(R.id.tv_ma_interntime);
		status = (TextView) findViewById(R.id.tv_ma_applystatus);
		setinternship = (Button) findViewById(R.id.bt_setintern);// 设置实习
		layout = (LinearLayout) findViewById(R.id.linearLayout_issetintern);
		revokeintern = (Button) findViewById(R.id.bt_revokeintern);// 撤销实习

		// 获取上一层界面传过来的数据信息
		Bundle bundle = getIntent().getExtras();
		apply = (Apply) bundle.getSerializable("applyposition");

		// 为控件赋值
		poname.setText(apply.getPosition().getPoName());
		enname.setText(apply.getPosition().getEnterprise().getEnName());
		applytime.setText(apply.getApplytime().toString().substring(0, 10));
		alltime.setText(apply.getPosition().getWorkTime());
		starttime.setText(apply.getPosition().getWorkstarttime().toString()
				.substring(0, 10));

		switch (apply.getApply_state()) {
		case 1:
			layout.setVisibility(View.GONE);
			status.setText("申请中");
			status.setTextColor(Color.rgb(0x82, 0x7C, 0x79));
			break;
		case 2:
			status.setText("审核通过");
			revokeintern.setEnabled(false);
			revokeintern.setVisibility(View.GONE);
			status.setTextColor(Color.rgb(0x00, 0x00, 0xA0));
			break;
		case 3:
			status.setText("审核未通过");
			layout.setVisibility(View.GONE);
			status.setTextColor(Color.rgb(0xFF, 0x00, 0x00));
			break;
		default:
			break;
		}

		class MyThread implements Runnable {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					studentID = pref.getInt("studentID", 0);
					List<Apply> templist = Internet.GetApply(studentID);
					Bundle bundle = getIntent().getExtras();
					apply = (Apply) bundle.getSerializable("applyposition");
					int len = templist.size();
					for (int i = 0; i < len; i++) {
						if (templist.get(i).getApply_state() == 0) {// 表明存在正在实习的职位
							if (apply.getApply_state() == 0) {// 当前职位信息已是正在实习的
								revokeintern.setEnabled(true);
								setinternship.setVisibility(View.GONE);
								status.setText("正在实习");
								break;
							} else {// 如果当前职位信息不是正在实习的，就隐藏两个按钮
								layout.setVisibility(View.GONE);
							}
							break;
						}
						if (i == len - 1) {// 表明不存在正在实习的职位
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

		// back的点击事件
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("statue", 0);
				setResult(RESULT_OK, intent);
				finish();
			}
		});

		setinternship.setOnClickListener(this);
		revokeintern.setOnClickListener(this);
		linearLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.linearLayout_applypoinfor:
			Intent it = new Intent(MyApplySpecificInfoActivity.this,
					PositionInformationActivity.class);
			Bundle b = new Bundle();
			b.putSerializable("position", apply.getPosition());
			it.putExtras(b);
			startActivity(it);
			break;
		case R.id.bt_setintern:// 设置实习按钮的点击事件
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					MyApplySpecificInfoActivity.this);
			dialog.setIcon(android.R.drawable.ic_dialog_info);
			dialog.setTitle("提示");
			dialog.setMessage("确定将此职位设为自己的实习职位吗？设置完成后，半个月内将不能再设置其他实习职位了，如有需要可以撤销申请。");
			dialog.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							new Thread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									try {
										if (Internet.SetInternship(apply
												.getApplyId())) {
											Message msg = new Message();
											msg.what = 0;// 该职位设置成功为当前实习
											handler.sendMessage(msg);
										} else {
											ToastUtil.showToast(
													MyApplication.getContext(),
													"设置失败，请重试！");
										}
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}).start();
							byte[] applyposition = ObjectAndByteUtil
									.ObjectToByte(apply);
							editor.putString("setposition",
									applyposition.toString());
							editor.commit();

						}
					});
			dialog.setNegativeButton("取消", null);
			dialog.setCancelable(false);
			dialog.show();
			break;

		case R.id.bt_revokeintern:
			AlertDialog.Builder dialog2 = new AlertDialog.Builder(
					MyApplySpecificInfoActivity.this);
			dialog2.setIcon(android.R.drawable.ic_dialog_info);
			dialog2.setTitle("提示");
			dialog2.setMessage("确定要撤销实习吗？撤销成功后，该实习状态变更为之前的“审核已通过”。");
			dialog2.setCancelable(false);
			dialog2.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							new Thread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									try {
										if (Internet.RevokeInternship(apply
												.getApplyId())) {
											Message msg = new Message();
											msg.what = 1;// 撤销实习成功。
											handler.sendMessage(msg);
										} else {
											ToastUtil.showToast(
													MyApplication.getContext(),
													"撤销成功");
										}
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}).start();
							editor.clear();// 清除保存的设为实习的对象信息
						}
					});
			dialog2.setNegativeButton("取消", null);
			dialog2.show();
			break;
		default:
			break;
		}
	}

}
