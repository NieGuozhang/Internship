package com.hbut.internship.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbut.internship.R;
import com.hbut.internship.util.Internet;
import com.hbut.internship.util.MyApplicationUtil;
import com.hbut.internship.util.TextToDBCUtil;
import com.hbut.internship.util.ToastUtil;
import com.internship.model.CV;

public class MyResumeActivity extends BaseActivity {

	private SharedPreferences pref;

	private CV cv;

	private Button modify, delete;
	private ImageButton back;

	private ImageView head;

	private TextView name, sex, telephone, email, address, IDNum, education,
			major, graduatetime, introduction, honour, interestion,
			graduateschool, age;

	private int studentID;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				ToastUtil.showToast(MyApplicationUtil.getContext(), "简历删除成功！");
				finish();
				break;
			case 1:
				ToastUtil
						.showToast(MyApplicationUtil.getContext(), "删除简历失败，请重新操作！");
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
		setContentView(R.layout.activity_myresume);

		pref = getSharedPreferences("Student", MODE_PRIVATE);
		studentID = pref.getInt("studentID", 0);

		class MyThread implements Runnable {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					cv = Internet.queryCv(studentID);
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

		// 初始化按钮
		back = (ImageButton) findViewById(R.id.imbt_myresume_back);
		modify = (Button) findViewById(R.id.bt_myresume_modify);
		delete = (Button) findViewById(R.id.bt_myresume_delete);

		// 初始化简历上的资料控件
		head = (ImageView) findViewById(R.id.iv_myresume_photo);
		name = (TextView) findViewById(R.id.tv_myresume_name);
		sex = (TextView) findViewById(R.id.tv_myresume_sex);
		telephone = (TextView) findViewById(R.id.tv_myresume_phone);
		email = (TextView) findViewById(R.id.tv_myresume_email);
		address = (TextView) findViewById(R.id.tv_myresume_address);
		IDNum = (TextView) findViewById(R.id.tv_myresume_idcard);
		education = (TextView) findViewById(R.id.tv_myresume_education);
		major = (TextView) findViewById(R.id.tv_myresume_major);
		graduatetime = (TextView) findViewById(R.id.tv_myresume_graduatetime);
		introduction = (TextView) findViewById(R.id.tv_myresume_introduction);
		honour = (TextView) findViewById(R.id.tv_myresume_honour);
		interestion = (TextView) findViewById(R.id.tv_myresume_interestion);
		graduateschool = (TextView) findViewById(R.id.tv_myresume_graduateschool);
		age = (TextView) findViewById(R.id.tv_myresume_age);

		// 为控件赋值
		name.setText(cv.getStudent().getStuName());// 姓名
		sex.setText(cv.getStudent().getStuSex());// 性别
		telephone.setText(cv.getPhone());
		email.setText(cv.getEmail());
		address.setText(cv.getStudent().getAddress());// 籍贯地址
		IDNum.setText(cv.getStudent().getIdNum());// 身份证号
		major.setText(cv.getStudent().getMajor());
		education.setText(cv.getStudent().getEducation());
		graduatetime.setText(cv.getGraduatetime().toString().substring(0, 10));
		// age.setText(CalculateAgeUtil.IdNOToAge(cv.getStudent().getIdNum()));//根据身份证号得出年龄
		graduateschool.setText(cv.getStudent().getSchool().getSchName());
		interestion.setText(TextToDBCUtil.toDBC(cv.getInterestion()));
		introduction.setText(TextToDBCUtil.toDBC(cv.getIntroduction()));
		honour.setText(TextToDBCUtil.toDBC(cv.getHonour()));
		
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		modify.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder dialog = new AlertDialog.Builder(
						MyResumeActivity.this);
				dialog.setTitle("提示");
				dialog.setIcon(android.R.drawable.ic_dialog_info);
				dialog.setMessage("确定要修改简历内容吗？");
				dialog.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								Intent intent = new Intent(
										MyResumeActivity.this,
										NewMyResumeActivity.class);
								intent.putExtra("modifyresume", "modifyresume");
								startActivity(intent);
							}
						});
				dialog.setNegativeButton("取消", null);
				dialog.setCancelable(false);
				dialog.show();
			}
		});

		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder dialog = new AlertDialog.Builder(
						MyResumeActivity.this);
				dialog.setTitle("提示");
				dialog.setIcon(android.R.drawable.ic_dialog_info);
				dialog.setMessage("确定要删除该简历吗？");
				dialog.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								new Thread(new Runnable() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										try {
											// 删除简历成功
											if (Internet.deleteCv(studentID)) {
												new Thread(new Runnable() {

													@Override
													public void run() {
														// TODO Auto-generated
														// method stub
														Message msg = new Message();
														msg.what = 0;
														handler.sendMessage(msg);
													}
												}).start();

											} else {
												new Thread(new Runnable() {

													@Override
													public void run() {
														// TODO Auto-generated
														// method stub
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
						});
				dialog.setNegativeButton("取消", null);
				dialog.setCancelable(false);
				dialog.show();
			}
		});
	}
}
