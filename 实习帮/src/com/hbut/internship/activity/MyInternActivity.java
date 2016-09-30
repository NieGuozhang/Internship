package com.hbut.internship.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hbut.internship.R;
import com.hbut.internship.util.Internet;
import com.hbut.internship.util.MyApplication;
import com.hbut.internship.util.TextToDBCUtil;
import com.hbut.internship.util.ToastUtil;
import com.internship.model.Apply;

public class MyInternActivity extends BaseActivity implements OnClickListener {

	private SharedPreferences pref, preferences;
	private SharedPreferences.Editor editor;// 用于保存用户的评论及总结。

	private Button submit;
	private ImageButton back;

	private EditText commentEditText, summaryEditText;

	private TextView enterprise, positionname, salary, worktime, poaddress,
			poeducation, potype, peoplenumber, closingdate, jobdescription,
			postate;

	private RatingBar ratingbar;

	private Apply myinternship;

	private int studentID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myintern);

		pref = getSharedPreferences("Student", MODE_PRIVATE);
		editor = getSharedPreferences("UserComment", MODE_PRIVATE).edit();
		preferences = getSharedPreferences("UserComment", MODE_PRIVATE);

		class MyThread implements Runnable {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					studentID = pref.getInt("studentID", 0);
					myinternship = Internet.GetInternship(studentID);
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

		// 初始化控件
		back = (ImageButton) findViewById(R.id.imbt_myintern_back);
		positionname = (TextView) findViewById(R.id.tv_mi_poname);
		enterprise = (TextView) findViewById(R.id.tv_mi_enname);
		salary = (TextView) findViewById(R.id.tv_mi_salary);
		worktime = (TextView) findViewById(R.id.tv_mi_worktime);
		poaddress = (TextView) findViewById(R.id.tv_mi_poaddress);
		poeducation = (TextView) findViewById(R.id.tv_mi_poeducation);
		potype = (TextView) findViewById(R.id.tv_mi_potype);
		peoplenumber = (TextView) findViewById(R.id.tv_mi_popeoplenumber);
		closingdate = (TextView) findViewById(R.id.tv_mi_closingdate);
		jobdescription = (TextView) findViewById(R.id.tv_mi_jobdescription);
		postate = (TextView) findViewById(R.id.tv_mi_state);
		commentEditText = (EditText) findViewById(R.id.et_mi_comment);
		summaryEditText = (EditText) findViewById(R.id.et_mi_summary);
		ratingbar = (RatingBar) findViewById(R.id.mi_ratingbar);
		submit = (Button) findViewById(R.id.bt_mi_confirm);

		// 为每个控件赋值
		positionname.setText(myinternship.getPosition().getPoName());
		enterprise.setText(myinternship.getPosition().getEnterprise()
				.getEnName());
		salary.setText(myinternship.getPosition().getSalarymin() + "-"
				+ myinternship.getPosition().getSalarymax());
		worktime.setText(myinternship.getPosition().getWorkTime());
		poaddress.setText(myinternship.getPosition().getPoAddress());
		poeducation.setText(myinternship.getPosition().getPoEducation());
		potype.setText(myinternship.getPosition().getPoType());
		peoplenumber.setText(myinternship.getPosition().getNumbers() + "人");
		closingdate.setText(myinternship.getPosition().getClosingdate()
				.toString().substring(0, 10));
		jobdescription.setText(TextToDBCUtil.toDBC(myinternship.getPosition()
				.getDescription()));
		postate.setText("正在实习");

		commentEditText.setText(preferences.getString("comment", ""));
		summaryEditText.setText(preferences.getString("summary", ""));
		ratingbar.setRating(preferences.getFloat("starValue", 0));

		// 监听事件
		back.setOnClickListener(this);
		enterprise.setOnClickListener(this);
		submit.setOnClickListener(this);// 提交按钮
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imbt_myintern_back:// 退出
			finish();
			break;
		case R.id.tv_mi_enname:// 查看企业信息
			Intent intent1 = new Intent(MyInternActivity.this,
					EnterpriseInformationActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("position", myinternship.getPosition());
			intent1.putExtras(bundle);
			startActivity(intent1);
			break;
		case R.id.bt_mi_confirm:// 提交数据至服务器
			final String comment = commentEditText.getText().toString();// 获取评论框中的数据信息
			final String summary = summaryEditText.getText().toString();// 获取总结框中的数据信息
			final float starValue = ratingbar.getRating();// 获取星级评价数据。

			editor.putString("comment", comment);
			editor.putString("summary", summary);
			editor.putFloat("starValue", starValue);
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						if (Internet.EditApplyInformation(
								myinternship.getApplyId(), starValue, comment,
								summary)) {

							ToastUtil.showToast(MyApplication.getContext(),
									"提交成功！");
						} else {
							Message msg = new Message();
							msg.what = 0;// 提交失败
							handler.sendMessage(msg);
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
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(MyInternActivity.this, "距离上次设置实习不超过7天，提交失败！",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		};
	};
}
