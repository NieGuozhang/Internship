package com.hbut.internship.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.hbut.internship.R;
import com.hbut.internship.util.Internet;
import com.hbut.internship.util.ToastUtil;
import com.internship.model.Student;

public class LoginActivity extends BaseActivity {

	private SharedPreferences pref;
	private SharedPreferences.Editor editor, editor1;
	private EditText accountEditText;
	private EditText passwordEdittext;
	private CheckBox issavepassword;
	private Button loginButton;
	private Button forgetpassword;
	private LinearLayout linearLayout;

	private String account;
	private String password;
	private Student student;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				ToastUtil.showtoast("登录失败！");
				linearLayout.setVisibility(View.GONE);
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
		setContentView(R.layout.activity_login);

		editor1 = getSharedPreferences("Student", MODE_PRIVATE).edit();
		pref = PreferenceManager.getDefaultSharedPreferences(this);

		findView();

		boolean isRemember = pref.getBoolean("remember_password", false);
		if (isRemember) {
			// 将账号和密码都设置到文本框
			final String account = pref.getString("account", "");
			final String password = pref.getString("password", "");
			accountEditText.setText(account);
			passwordEdittext.setText(password);
			issavepassword.setChecked(true);
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						if (Internet.login(account, password)) {

							Intent intent = new Intent();
							intent.setClass(LoginActivity.this,
									MainActivity.class);
							startActivity(intent);
							finish();
						} else {
							Message msg = new Message();
							msg.what = 0;
							handler.sendMessage(msg);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
		}

		loginButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				linearLayout.setVisibility(View.VISIBLE);
				account = accountEditText.getText().toString();
				password = passwordEdittext.getText().toString();

				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							// Internet.login(account, password)
							if (true) {
								editor = pref.edit();
								if (issavepassword.isChecked()) {
									editor.putBoolean("remember_password", true);
									editor.putString("account", account);
									editor.putString("password", password);
								} else {
									editor.clear();
								}
								editor.commit();
								new Thread(new Runnable() {
									@Override
									public void run() {
										// TODO Auto-generated method stub
										try {
											student = Internet
													.queryStudent(account);
											editor1.putInt("studentID",
													student.getStuId());
											editor1.putString("studentname",
													student.getStuName());
											editor1.commit();
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								}).start();
								editor1.putString("account", account);
								editor1.commit();
								Intent intent = new Intent();
								intent.setClass(LoginActivity.this,
										MainActivity.class);
								startActivity(intent);
								finish();
							} else {
								Message msg = new Message();
								msg.what = 0;
								handler.sendMessage(msg);

							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).start();
			}
		});

		forgetpassword.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this,
						ForgetPassword1Activity.class);
				startActivity(intent);
			}
		});
	}

	private void findView() {
		accountEditText = (EditText) findViewById(R.id.et_inputaccount);
		passwordEdittext = (EditText) findViewById(R.id.et_inputpassword);
		loginButton = (Button) findViewById(R.id.bt_login);
		issavepassword = (CheckBox) findViewById(R.id.cb_issavapassword);
		forgetpassword = (Button) findViewById(R.id.bt_forgetpassword);
		linearLayout = (LinearLayout) findViewById(R.id.linearLayout_loginprogress);

	}
}