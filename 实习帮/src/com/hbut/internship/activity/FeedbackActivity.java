package com.hbut.internship.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

import com.hbut.internship.R;

public class FeedbackActivity extends BaseActivity implements OnClickListener {

	private Button send;
	private ImageButton back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);

		back = (ImageButton) findViewById(R.id.imbt_feedback_back);
		back.setOnClickListener(this);

		send = (Button) findViewById(R.id.bt_feedback_send);
		send.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imbt_feedback_back:
			finish();
			break;
		case R.id.bt_feedback_send:

		default:
			break;
		}
	}
}
