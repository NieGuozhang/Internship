package com.hbut.internship.activity;

import java.util.List;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hbut.internship.R;
import com.hbut.internship.util.Internet;
import com.hbut.internship.util.PictureUtils;
import com.internship.model.Apply;

public class MyInternshipActivity extends BaseActivity implements
		OnClickListener {

	private SharedPreferences preferences;
	private static String path = "/sdcard/myHead/";// sd路径
	private ImageButton userheadImageButton;
	private Button myresumeButton, mycollectButton, myapplyButton,
			experienceButton, mysettingsButton, myintern;
	private TextView username, status, jobname;
	boolean isexit;

	private int studentID;
	private Typeface tf;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:// 存在正在实习的职位，跳转至正在实习的职位进行评论等操作。
				Intent intent = new Intent(MyInternshipActivity.this,
						MyInternActivity.class);
				startActivity(intent);
				break;
			case 1:// 不存在正在实习的职位 弹出一个对话框提示用户没有正在实习的职位。
				AlertDialog.Builder dialog = new AlertDialog.Builder(
						MyInternshipActivity.this);
				dialog.setIcon(android.R.drawable.ic_dialog_info);
				dialog.setTitle("提示：");
				dialog.setMessage("抱歉，你当前没有正在实习的职位，请在“我的申请”界面去设置实习。");
				dialog.setCancelable(false);
				dialog.setPositiveButton("取消", null);
				dialog.show();
				break;
			case 2:// 简历存在
				Intent intent2 = new Intent(MyInternshipActivity.this,
						MyResumeActivity.class);
				startActivity(intent2);
				break;
			case 3:// 简历为空
				Intent it = new Intent(MyInternshipActivity.this,
						NullMyResumeActivity.class);
				startActivity(it);
				break;
			case 4:// 不存在正在实习
				status.setText("未实习");
				jobname.setText("");
			default:
				break;
			}
		};
	};

	private Handler handler1 = new Handler() {
		public void handleMessage(Message msg) {
			Apply theApply = (Apply) msg.obj;
			status.setText("实习中");
			jobname.setText(theApply.getPosition().getPoName());
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myinternship);

		preferences = getSharedPreferences("Student", MODE_PRIVATE);
		studentID = preferences.getInt("studentID", 0);

		// 初始化按钮控件
		userheadImageButton = (ImageButton) findViewById(R.id.imgbt_userhead);
		username = (TextView) findViewById(R.id.tv_username);
		status = (TextView) findViewById(R.id.tv_status);
		jobname = (TextView) findViewById(R.id.tv_internposition);

		// 设置用户名、实习状态、实习岗位的字体
		tf = Typeface.createFromAsset(getAssets(),
				"fonts/fangzhengzhunyuan.TTF");
		username.setTypeface(tf);
		username.setText(preferences.getString("studentname", ""));
		status.setTypeface(tf);
		jobname.setTypeface(tf);

		myresumeButton = (Button) findViewById(R.id.bt_myresume);
		myintern = (Button) findViewById(R.id.bt_myintern);
		mycollectButton = (Button) findViewById(R.id.bt_mycollection);
		myapplyButton = (Button) findViewById(R.id.bt_myapply);
		experienceButton = (Button) findViewById(R.id.bt_internshipexperience);
		mysettingsButton = (Button) findViewById(R.id.bt_mysettings);

		userheadImageButton.setOnClickListener(this);
		myresumeButton.setOnClickListener(this);
		myintern.setOnClickListener(this);
		mycollectButton.setOnClickListener(this);
		myapplyButton.setOnClickListener(this);
		experienceButton.setOnClickListener(this);
		mysettingsButton.setOnClickListener(this);
		/*
		 * // 加载用户头像 Bitmap bt = BitmapFactory.decodeFile(path + "head.jpg");//
		 * 从Sd中找头像，转换成Bitmap if (bt != null) { Drawable drawable = new
		 * BitmapDrawable(bt);// 转换成drawable
		 * userheadImageButton.setImageDrawable(drawable); }
		 */
	}

	// 控件的监听器事件
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imgbt_userhead:// 跳转至”个人资料设置“
			Intent intent = new Intent(MyInternshipActivity.this,
					PersonalInformationActivity.class);
			startActivity(intent);
			break;
		case R.id.bt_myresume:// 跳转至”我的简历“
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						if (Internet.queryCv(studentID) != null) {// 简历存在
							Message msg = new Message();
							msg.what = 2;
							handler.sendMessage(msg);
						} else {// 简历不存在，跳转至新建简历
							Message msg = new Message();
							msg.what = 3;
							handler.sendMessage(msg);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
			break;
		case R.id.bt_mycollection:// 跳转至”我的收藏“
			Intent intent3 = new Intent(MyInternshipActivity.this,
					MyCollectionActivity.class);
			startActivity(intent3);
			break;
		case R.id.bt_myapply:// 跳转至”我的申请“
			Intent intent4 = new Intent(MyInternshipActivity.this,
					MyApplyActivity.class);
			startActivity(intent4);
			break;
		case R.id.bt_internshipexperience:// 跳转至”实习经历“
			Intent intent5 = new Intent(MyInternshipActivity.this,
					ExperienceActivity.class);
			startActivity(intent5);
			break;
		case R.id.bt_mysettings:// 跳转至”我的设置“
			Intent intent6 = new Intent(MyInternshipActivity.this,
					MySettingsActivity.class);
			startActivity(intent6);
			break;
		case R.id.bt_myintern:
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						List<Apply> templist = Internet.GetApply(studentID);
						int len = templist.size();
						for (int i = 0; i < len; i++) {
							if (templist.get(i).getApply_state() == 0) {
								Message msg = new Message();
								msg.what = 0;// 表明存在正在实习的职位
								handler.sendMessage(msg);
								break;
							}
							if (i == len - 1) {
								Message msg1 = new Message();
								msg1.what = 1;// 表明不存在正在实习的职位
								handler.sendMessage(msg1);
							}
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
		default:
			break;
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// 加载用户头像
		Bitmap bt = BitmapFactory.decodeFile(path + "head.jpg");// 从Sd中找头像，转换成Bitmap
		if (bt != null) {
			// Drawable drawable = new BitmapDrawable(bt);// 转换成drawable
			userheadImageButton.setImageBitmap(PictureUtils.ratio(bt, 120f,
					120f));
			//userheadImageButton.setImageDrawable(drawable);
		}

		class MyThread implements Runnable {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					preferences = getSharedPreferences("Student", MODE_PRIVATE);
					studentID = preferences.getInt("studentID", 0);
					List<Apply> templist = Internet.GetApply(studentID);
					int len = templist.size();
					for (int i = 0; i < len; i++) {
						if (templist.get(i).getApply_state() == 0) {
							Message msg = new Message();
							msg.obj = templist.get(i);// 表明存在正在实习的职位
							handler1.sendMessage(msg);
							break;
						}
						if (i == len - 1) {
							Message msg1 = new Message();
							msg1.what = 4;// 表明不存在正在实习的职位
							handler.sendMessage(msg1);
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

	}

}
