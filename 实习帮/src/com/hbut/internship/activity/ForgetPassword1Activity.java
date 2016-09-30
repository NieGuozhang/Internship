package com.hbut.internship.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.hbut.internship.R;
import com.hbut.internship.util.MyApplication;
import com.hbut.internship.util.ToastUtil;

public class ForgetPassword1Activity extends BaseActivity {

	private EditText inputaccount;

	private Button findpassword;
	private ImageButton back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgetpassword1);

		// 初始化控件
		back=(ImageButton) findViewById(R.id.imbt_findpassword1_back);
		inputaccount = (EditText) findViewById(R.id.et_fp_inputaccount);

		findpassword = (Button) findViewById(R.id.bt_fp1_findpassword);

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		findpassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final String email = inputaccount.getText().toString();
				if (email.equals("")) {
					ToastUtil.showToast(MyApplication.getContext(),
							"你输入的邮箱账号为空！请重新输入！");// 邮箱为空，提示重新输入。
				} else if (checkEmail(email)) {
					Intent intent = new Intent(ForgetPassword1Activity.this,
							ForgetPassword2Activity.class); // 邮箱格式正确，界面发生跳转。
					intent.putExtra("email", email);
					startActivity(intent);
				} else {
					ToastUtil.showToast(MyApplication.getContext(),
							"你输入的邮箱格式不正确，请重新输入！");
				}
			}
		});
	}

	// 检验邮箱的格式
	public boolean checkEmail(String email) {
		String format = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]"
				+ "{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))"
				+ "([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		if (email.matches(format)) {
			return true;
		} else {
			return false;
		}
	}

}
