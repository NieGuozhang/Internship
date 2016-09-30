package com.hbut.internship.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hbut.internship.R;
import com.hbut.internship.adapter.ReviewCommentAdapter;
import com.hbut.internship.util.Internet;
import com.hbut.internship.util.MyApplication;
import com.hbut.internship.util.TextToDBCUtil;
import com.hbut.internship.view.ListViewRelayout;
import com.internship.model.Apply;
import com.internship.model.Position;

public class EnterpriseInformationActivity extends BaseActivity implements
		OnClickListener {

	private TextView enterprisenameTextView, enTypeTV, enHandler, enTel,
			enAddress, enTro;
	private ImageButton back;
	private ListView comment;
	private ScrollView scrollView;

	private List<Apply> commentList = new ArrayList<Apply>();
	private ReviewCommentAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enterpriseinformation);

		class MyThread implements Runnable {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					// 获取上个界面传递下来的对象
					final Bundle bundle = getIntent().getExtras();
					Position position = (Position) bundle
							.getSerializable("position");
					commentList = Internet.GetComment(position.getEnterprise()
							.getEnId());
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
		back = (ImageButton) findViewById(R.id.imbt_enterpriseinfor_back);// 返回按钮
		enterprisenameTextView = (TextView) findViewById(R.id.tv_enname);// 企业名字
		enTypeTV = (TextView) findViewById(R.id.tv_entype);// 企业类型
		enHandler = (TextView) findViewById(R.id.tv_enhandler);// 企业联系人
		enTel = (TextView) findViewById(R.id.tv_entel);// 企业联系方式
		enAddress = (TextView) findViewById(R.id.tv_enaddress);// 企业地址
		enTro = (TextView) findViewById(R.id.tv_enintroduction);// 企业简介
		comment = (ListView) findViewById(R.id.lv_comment);// 评论
		scrollView = (ScrollView) findViewById(R.id.sv_enterpriseinfor);

		// 获取上个界面传递下来的对象
		final Bundle bundle = getIntent().getExtras();
		Position position = (Position) bundle.getSerializable("position");
		// 为每个控件赋值
		enterprisenameTextView.setText(position.getEnterprise().getEnName());// 企业名字
		enTypeTV.setText(position.getEnterprise().getEnType());// 企业类型
		enHandler.setText(position.getEnterprise().getEnHandler());// 企业联系人
		enTel.setText(position.getEnterprise().getEnTel());// 企业联系方式
		enAddress.setText(position.getEnterprise().getEnAdd());// 企业地址
		enTro.setText(TextToDBCUtil
				.toDBC(position.getEnterprise().getEnIntro()));// 企业简介

		adapter = new ReviewCommentAdapter(MyApplication.getContext(),
				R.layout.commentlistview_item, commentList);
		comment.setAdapter(adapter);
		ListViewRelayout.setListViewHeightBasedOnChildren(comment);
		back.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imbt_enterpriseinfor_back:
			finish();
			break;
		default:
			break;
		}
	}

}
