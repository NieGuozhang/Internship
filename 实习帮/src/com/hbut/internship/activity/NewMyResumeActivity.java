package com.hbut.internship.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbut.internship.R;
import com.hbut.internship.util.Internet;
import com.hbut.internship.util.MyApplicationUtil;
import com.hbut.internship.util.ToastUtil;
import com.internship.model.CV;
import com.internship.model.Student;

public class NewMyResumeActivity extends BaseActivity {

	private SharedPreferences.Editor editor;

	private SharedPreferences pref, preferences;

	private static final int TAKE_PHOTO = 1;
	private static final int CROP_PHOTO = 2;

	private static String path = "/sdcard/myresume/myHead/";// sd路径

	private Bitmap head;

	private TextView title;
	private Button  save;
	private ImageButton back;
	private ImageView headpic;
	private EditText email, phone, interest, graduatetime, honour,
			introduction;

	private String mEmail, mPhone, mInterest, mGraduatetime, mHonour,
			mIntroduction;
	private CV cv = new CV();

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				editor.putString("email", mEmail);
				editor.putString("phone", mPhone);
				editor.putString("interest", mInterest);
				editor.putString("graduatetime", mGraduatetime);
				editor.putString("honour", mHonour);
				editor.putString("introduction", mIntroduction);
				editor.commit();
				ToastUtil.showToast(MyApplicationUtil.getContext(), "简历保存成功！");
				break;
			case 1:
				ToastUtil
						.showToast(MyApplicationUtil.getContext(), "简历保存失败，请重新提交！");
				break;
			case 2:
				editor.putString("email", mEmail);
				editor.putString("phone", mPhone);
				editor.putString("interest", mInterest);
				editor.putString("graduatetime", mGraduatetime);
				editor.putString("honour", mHonour);
				editor.putString("introduction", mIntroduction);
				editor.commit();
				ToastUtil.showToast(MyApplicationUtil.getContext(), "简历修改成功！");
				break;
			case 3:
				ToastUtil
						.showToast(MyApplicationUtil.getContext(), "简历修改失败，请重新提交！");
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
		setContentView(R.layout.activity_newmyresume);

		editor = getSharedPreferences("resume", MODE_PRIVATE).edit();
		pref = getSharedPreferences("resume", MODE_PRIVATE);
		preferences = getSharedPreferences("Student", MODE_PRIVATE);

		// 初始化新建简历中的各个控件
		back = (ImageButton) findViewById(R.id.imbt_newresume_back);
		title = (TextView) findViewById(R.id.tv_writeresume);
		Intent intent = getIntent();
		if (intent.getStringExtra("modifyresume").equals("modifyresume")) {
			title.setText("修改简历");
		}
		headpic = (ImageView) findViewById(R.id.iv_newresume_photo);
		email = (EditText) findViewById(R.id.et_newresume_email);
		phone = (EditText) findViewById(R.id.et_newresume_phone);
		interest = (EditText) findViewById(R.id.et_newresume_interest);
		graduatetime = (EditText) findViewById(R.id.et_newresume_graduatetime);
		honour = (EditText) findViewById(R.id.et_newresume_honour);
		introduction = (EditText) findViewById(R.id.et_newresume_introduction);
		save = (Button) findViewById(R.id.bt_newresume_save);

		setResume();

		// 返回按钮的点击事件
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			if (resultCode == RESULT_OK) {
				cropPhoto(data.getData());// 裁剪图片
			}

			break;
		case 2:
			if (resultCode == RESULT_OK) {
				File temp = new File(Environment.getExternalStorageDirectory()
						+ "/head.jpg");
				cropPhoto(Uri.fromFile(temp));// 裁剪图片
			}

			break;
		case 3:
			if (data != null) {
				Bundle extras = data.getExtras();
				head = extras.getParcelable("data");
				if (head != null) {

					/*
					 * 上传服务器代码
					 */
					setPicToView(head);// 保存在SD卡中
					headpic.setImageBitmap(head);// 用ImageView显示出来
				}

			}
			break;
		default:
			break;

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 调用系统的裁剪
	 * 
	 * @param uri
	 */
	public void cropPhoto(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 100);
		intent.putExtra("outputY", 100);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}

	private void setPicToView(Bitmap mBitmap) {
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
			return;
		}
		FileOutputStream b = null;
		File file = new File(path);
		file.mkdirs();// 创建文件夹
		String fileName = path + "head.jpg";// 图片名字
		try {
			b = new FileOutputStream(fileName);
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭流
				b.flush();
				b.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void getResume() {
		mEmail = email.getText().toString();
		mPhone = phone.getText().toString();
		mInterest = interest.getText().toString();
		mGraduatetime = graduatetime.getText().toString();
		mHonour = honour.getText().toString();
		mIntroduction = introduction.getText().toString();
	}

	private void setResume() {

		// 将每个控件之前保存的内容加载进去。
		email.setText(pref.getString("resume", ""));
		phone.setText(pref.getString("resume", ""));
		interest.setText(pref.getString("resume", ""));
		graduatetime.setText(pref.getString("resume", ""));
		honour.setText(pref.getString("resume", ""));
		introduction.setText(pref.getString("resume", ""));
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		setResume();
		// 保存简历按钮的点击事件
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getResume();
				if (mEmail.equals("") || mPhone.equals("")
						|| mInterest.equals("") || mGraduatetime.equals("")
						|| mHonour.equals("") || mIntroduction.equals("")) {
					ToastUtil.showToast(MyApplicationUtil.getContext(),
							"请将简历填写完整后再提交！");
				} else {
					cv.setEmail(mEmail);
					cv.setPhone(mPhone);
					cv.setIntroduction(mIntroduction);
					cv.setInterestion(mInterest);
					cv.setHonour(mHonour);
					cv.setGraduatetime(Timestamp.valueOf(mGraduatetime
							+ " 00:00:00"));
					Student student = new Student();
					student.setStuId(preferences.getInt("studentID", 0));
					cv.setStudent(student);

					new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								Intent intent = getIntent();
								if (intent.getStringExtra("modifyresume")
										.equals("modifyresume")) {
									if (Internet.ModifyResume(cv)) {
										new Thread(new Runnable() {
											@Override
											public void run() {
												Message msg = new Message();
												msg.what = 2;
												handler.sendMessage(msg);
											}
										}).start();
									} else {
										new Thread(new Runnable() {
											@Override
											public void run() {
												Message msg = new Message();
												msg.what = 3;
												handler.sendMessage(msg);
											}
										}).start();
									}
								} else {
									if (Internet.SetResume(cv)) {
										new Thread(new Runnable() {
											@Override
											public void run() {
												Message msg = new Message();
												msg.what = 0;
												handler.sendMessage(msg);
											}
										}).start();
									} else {
										new Thread(new Runnable() {
											@Override
											public void run() {
												Message msg = new Message();
												msg.what = 1;
												handler.sendMessage(msg);
											}
										}).start();
									}
								}

							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}).start();
				}
			}
		});

		headpic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder dialog = new AlertDialog.Builder(
						NewMyResumeActivity.this);
				dialog.setTitle("提示：");
				dialog.setMessage("请选择是打开相机进行拍照还是从相册选择？");
				// dialog.setCancelable(false);
				dialog.setPositiveButton("拍照片",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								Intent intent2 = new Intent(
										MediaStore.ACTION_IMAGE_CAPTURE);
								intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri
										.fromFile(new File(Environment
												.getExternalStorageDirectory(),
												"head.jpg")));
								startActivityForResult(intent2, CROP_PHOTO);// 采用ForResult打开
							}
						});
				dialog.setNeutralButton("从相册选择",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

								Intent intent1 = new Intent(Intent.ACTION_PICK,
										null);
								intent1.setDataAndType(
										MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
										"image/*");
								startActivityForResult(intent1, TAKE_PHOTO);

							}
						});

				dialog.setNegativeButton("取消", null);
				dialog.show();

			}
		});
		Bitmap bt = BitmapFactory.decodeFile(path + "head.jpg");// 从Sd中找头像，转换成Bitmap
		if (bt != null) {
			Drawable drawable = new BitmapDrawable(bt);// 转换成drawable
			headpic.setImageDrawable(drawable);
		}
	}
}
