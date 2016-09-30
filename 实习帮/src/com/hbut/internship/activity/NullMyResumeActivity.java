package com.hbut.internship.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.hbut.internship.R;

public class NullMyResumeActivity extends BaseActivity {

	private ImageButton addresumeImgButton, back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nullmyresume);

		back = (ImageButton) findViewById(R.id.imbt_nullresume_back);
		addresumeImgButton = (ImageButton) findViewById(R.id.imgbt_addresume);

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		addresumeImgButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder dialog = new AlertDialog.Builder(
						NullMyResumeActivity.this);
				dialog.setTitle("提示：");
				dialog.setIcon(android.R.drawable.ic_dialog_info);
				dialog.setCancelable(false);
				dialog.setMessage("需要创建简历吗？");
				dialog.setPositiveButton("创建",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								Intent intent = new Intent(
										NullMyResumeActivity.this,
										NewMyResumeActivity.class);
								intent.putExtra("modifyresume", "newresume");
								startActivity(intent);
								dialog.dismiss();
								finish();
							}
						});

				dialog.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});
				dialog.show();
			}
		});
	}

}
