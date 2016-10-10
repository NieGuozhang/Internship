package com.hbut.internship.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hbut.internship.R;
import com.hbut.internship.util.IdentifyingCodeUtil;
import com.hbut.internship.util.Internet;
import com.hbut.internship.util.MyApplicationUtil;
import com.hbut.internship.util.ObjectUtils;
import com.hbut.internship.util.SendCodeToEmailUtil;
import com.hbut.internship.util.ToastUtil;
import com.hbut.internship.view.CountDownTimerUtils;

public class ForgetPassword2Activity extends BaseActivity implements
		OnClickListener {

	private EditText inputVFcode, newpassword, newpasswordAgain;

	private Button confirm;
	private ImageButton back;

	private TextView sendCode;

	private String Code;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				ToastUtil.showToast(MyApplicationUtil.getContext(), "密码修改成功!");
				finish();
				break;
			case 1:
				ToastUtil.showToast(MyApplicationUtil.getContext(),
						"您输入的邮箱不存在！请重新操作。");
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
		setContentView(R.layout.activity_forgetpassword2);

		// 初始化控件
		back = (ImageButton) findViewById(R.id.imbt_findpassword2_back);
		inputVFcode = (EditText) findViewById(R.id.et_fp2_vfcode);
		newpassword = (EditText) findViewById(R.id.et_fp2_newpassword1);
		newpasswordAgain = (EditText) findViewById(R.id.et_fp2_newpassword2);
		confirm = (Button) findViewById(R.id.bt_fp2_confirm);
		sendCode = (TextView) findViewById(R.id.tv_fp2_sendcode);

		// 返回按钮的监听事件
		back.setOnClickListener(this);

		// 发送验证码
		sendCode.setText("发送验证码");
		sendCode.setTextColor(Color.WHITE);
		sendCode.setOnClickListener(this);
		confirm.setOnClickListener(this);// 确认按钮的监听器

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imbt_findpassword2_back:
			finish();
			break;
		case R.id.tv_fp2_sendcode:// 发送验证码的按钮点击事件
			CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(
					sendCode, 600000, 1000);
			mCountDownTimerUtils.start();// 验证码倒计时开始。

			/*
			 * 验证码发送至上一层界面的输入的邮箱。
			 */
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						Code = IdentifyingCodeUtil.getInstance().createCode();
						SendCodeToEmailUtil.sendEmail(getIntent(), Code);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
			break;
		case R.id.bt_fp2_confirm:// 确认按钮的点击事件
			final String newpwd,
			newpwd1;
			newpwd = newpassword.getText().toString();// 获取新密码
			newpwd1 = newpasswordAgain.getText().toString();// 再次获取新密码

			final String code = inputVFcode.getText().toString();// 获取编辑框中输入的验证码。
			if (newpwd.equals("") || newpwd1.equals("")) {
				ToastUtil.showtoast("请输入新密码");
			} else if (code.equals("")) {
				ToastUtil.showtoast("请登录邮箱，输入相应的验证码");
			} else if (!newpwd.equals(newpwd1)) {// 新密码不同，。提醒用户从新输入新密码。
				ToastUtil.showToast(MyApplicationUtil.getContext(),
						"您两次输入的新密码不一致，请重新输入新密码！");
			} else {// 若两次输入的新密码相同，可以将新密码保存至后台
				if (ObjectUtils.isNullOrEmpty(Code)) {
					ToastUtil.showtoast("请点击发送验证码");
				}else if (!Code.equalsIgnoreCase(code)) {

					ToastUtil.showToast(MyApplicationUtil.getContext(),
							"您输入的验证码不对，修改密码失败！");
				} else {
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								// 若返回成功，提醒用户密码保存成功。
								if (Internet.sendemail(getIntent()
										.getStringExtra("email"), newpwd)) {
									new Thread(new Runnable() {
										@Override
										public void run() {
											// TODO Auto-generated method stub
											Message msg = new Message();
											msg.what = 0;
											handler.sendMessage(msg);
										}
									}).start();

								} else {// 若返回false 提示用户密码修改失败
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
				}
			}
			break;
		default:
			break;
		}
	}
}
