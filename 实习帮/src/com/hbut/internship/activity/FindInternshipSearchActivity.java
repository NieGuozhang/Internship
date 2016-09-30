package com.hbut.internship.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.hbut.internship.R;
import com.hbut.internship.adapter.PositionListViewAdapter;
import com.hbut.internship.util.Internet;
import com.internship.model.Position;

public class FindInternshipSearchActivity extends BaseActivity implements
		OnClickListener {


	private List<Position> mList = new ArrayList<Position>();
	private List<Position> positionList;
	private PositionListViewAdapter positionListViewAdapter;

	
	private String[] fitername = { "企业", "职位" };
	
	private TextView fiternumber;
	private Button search,fiter;
	private ImageButton back;
	private Spinner spinner;
	private EditText searchEdittext;// 输入框搜索内容
	private ListView mListView;

	private String getFitername;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			positionList = (List<Position>) msg.obj;
			positionListViewAdapter = new PositionListViewAdapter(
					FindInternshipSearchActivity.this, R.layout.position_item,
					positionList);
			mListView.setAdapter(positionListViewAdapter);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_findinternshipsearch);

		findView();
		
	}
	
	private void findView(){
		back = (ImageButton) findViewById(R.id.imbt_findinternship_back);
		back.setOnClickListener(this);

		search = (Button) findViewById(R.id.bt_findinternship_search);
		search.setOnClickListener(this);

		searchEdittext = (EditText) findViewById(R.id.et_searchinternship);

		spinner = (Spinner) findViewById(R.id.spinner_fitername);
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
				FindInternshipSearchActivity.this, R.layout.fitername_item,
				R.id.tv_fitername, fitername);
		spinner.setAdapter(arrayAdapter);

		mListView = (ListView) findViewById(R.id.lv_jobfilter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FindInternshipSearchActivity.this,
						PositionInformationActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("position", positionList.get(position));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		
		//筛选按钮
		fiter=(Button) findViewById(R.id.bt_searchposition_fiter);
		fiter.setOnClickListener(this);
		//筛选后的数量统计
		fiternumber=(TextView) findViewById(R.id.tv_searchresultnumber);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imbt_findinternship_back:
			finish();
			break;
		case R.id.bt_findinternship_search:// 查找实习
			switch (spinner.getSelectedItem().toString()) {
			case "企业":
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							getFitername = searchEdittext.getText().toString();
							mList = Internet.FindIntershipWithEn(getFitername);
							new Thread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									Message msg = new Message();
									msg.obj = mList;
									handler.sendMessage(msg);
								}
							}).start();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).start();
				break;
			case "职位":
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							getFitername = searchEdittext.getText().toString();
							mList = Internet.FindIntershipWithPo(getFitername);
							new Thread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									Message msg = new Message();
									msg.obj = mList;
									handler.sendMessage(msg);
								}
							}).start();
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
			break;
			
		case R.id.bt_searchposition_fiter://筛选按钮
			break;
		default:
			break;
		}
	}
}