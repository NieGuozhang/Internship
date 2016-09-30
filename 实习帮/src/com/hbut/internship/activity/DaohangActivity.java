package com.hbut.internship.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.hbut.internship.R;
import com.hbut.internship.util.MyApplication;
import com.internship.model.Position;

/**
 * 路线导航
 * 
 * @author Nie
 * 
 */
public class DaohangActivity extends BaseActivity implements OnClickListener {

	private MapView mMapView = null;
	private BaiduMap baiduMap;

	private TextView enname, instance, poaddress;
	private ImageButton location, back, car, bus, walk;
	private Button daohang;

	private Position position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());// 关键
		setContentView(R.layout.activity_daohang);

		Bundle bundle = getIntent().getExtras();
		position = (Position) bundle.getSerializable("position");

		findView();
		init();
	}

	/**
	 * 初始化控件
	 */
	private void findView() {
		mMapView = (MapView) findViewById(R.id.mapview);

		enname = (TextView) findViewById(R.id.tv_daohang_enname);
		poaddress = (TextView) findViewById(R.id.tv_daohang_poaddress);
		instance = (TextView) findViewById(R.id.tv_daohang_instance);

		back = (ImageButton) findViewById(R.id.imbt_daohang_back);// 返回 按钮
		back.setOnClickListener(this);
		location = (ImageButton) findViewById(R.id.imbt_daohang_location);// 定位按钮
		location.setOnClickListener(this);
		daohang = (Button) findViewById(R.id.bt_daohang_daohang);// 导航按钮
		daohang.setOnClickListener(this);

		car = (ImageButton) findViewById(R.id.imbt_daohang_car);// 汽车导航
		bus = (ImageButton) findViewById(R.id.imbt_daohang_bus);// 公交导航
		walk = (ImageButton) findViewById(R.id.imbt_daohang_walk);// 步行导航
		car.setOnClickListener(this);
		bus.setOnClickListener(this);
		walk.setOnClickListener(this);
	}

	private void init() {
		// TODO Auto-generated method stub
		enname.setText(position.getEnterprise().getEnName());
		poaddress.setText(position.getPoAddress());
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imbt_daohang_back:
			finish();
			break;
		case R.id.imbt_daohang_location:// 定位服务

			break;
		case R.id.bt_daohang_daohang:// 导航

			break;
		case R.id.imbt_daohang_car:// 汽车导航
			break;
		case R.id.imbt_daohang_bus:// 公交导航
			break;
		case R.id.imbt_daohang_walk:// 步行导航
			break;

		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	@Override
	protected void onDestroy() {
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}

}
