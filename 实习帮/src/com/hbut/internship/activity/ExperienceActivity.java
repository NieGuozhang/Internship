package com.hbut.internship.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.hbut.internship.R;
import com.hbut.internship.adapter.ExperienceAdapter;
import com.hbut.internship.util.Internet;
import com.internship.model.Apply;

public class ExperienceActivity extends BaseActivity implements
		OnClickListener {

	private SharedPreferences pref;// 获取学生的ID

	private ImageButton back;

	private ListView listview;

	private List<Apply> mList = new ArrayList<Apply>();

	private ExperienceAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_experience);
		pref = getSharedPreferences("Student", MODE_PRIVATE);
		back=(ImageButton) findViewById(R.id.imbt_experience_back);
		back.setOnClickListener(this);

		class MyThread implements Runnable {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					mList = Internet.GetExperience(pref.getInt("studentID", 0));
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

		adapter = new ExperienceAdapter(ExperienceActivity.this,
				R.layout.experiencelistview_item, mList);
		listview = (ListView) findViewById(R.id.lv_experience);
		listview.setAdapter(adapter);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.imbt_experience_back:
			finish();
			break;

		default:
			break;
		}
	}

}
