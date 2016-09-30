package com.hbut.internship.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.hbut.internship.R;
import com.hbut.internship.util.Internet;
import com.hbut.internship.util.MyApplication;
import com.hbut.internship.util.ToastUtil;

public class ModifyPasswordActivity extends BaseActivity implements
		OnClickListener {

	private SharedPreferences pref;

	private EditText oldpasswordET, newpasswordET, newpasswordaginET;

	private String oldpassword, newpassword, newpassword2;

	private Button submit;// 提交按钮
	private ImageButton back;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				ToastUtil.showToast(MyApplication.getContext(), "密码修改成功！");
				break;
			case 1:
				ToastUtil.showToast(MyApplication.getContext(), "修改失败，请重新操作！");
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
		setContentView(R.layout.activity_modifypassword);

		pref = PreferenceManager.getDefaultSharedPreferences(this);

		back = (ImageButton) findViewById(R.id.imbt_modifypassword_back);
		oldpasswordET = (EditText) findViewById(R.id.et_oldpassword);
		newpasswordET = (EditText) findViewById(R.id.et__newpassword1);
		newpasswordaginET = (EditText) findViewById(R.id.et_newpassword2);
		submit = (Button) findViewById(R.id.bt_submit);
		submit.setOnClickListener(this);
		back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_submit:
			// 获取输入框中的密码
			oldpassword = oldpasswordET.getText().toString();
			newpassword = newpasswordET.getText().toString();
			newpassword2 = newpasswordaginET.getText().toString();
			// 验证
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						if (Internet.CheckPassword(
								pref.getString("account", ""), oldpassword,
								newpassword)) {
							new Thread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									Message msg = new Message();
									msg.what = 0;
									handler.sendMessage(msg);
								}
							}).start();

						} else {
							new Thread(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									Message msg = new Message();
									msg.what = 1;
									handler.sendMessage(msg);
								}
							}).start();
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
			break;
		case R.id.imbt_modifypassword_back:
			finish();
		default:
			break;
		}
	}
}
