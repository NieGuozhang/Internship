package com.hbut.internship.activity;

import java.text.DecimalFormat;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption.DrivingPolicy;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRoutePlanOption.TransitPolicy;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.hbut.internship.R;
import com.hbut.internship.baidumap.overlayutil.DrivingRouteOverlay;
import com.hbut.internship.baidumap.overlayutil.TransitRouteOverlay;
import com.hbut.internship.baidumap.overlayutil.WalkingRouteOverlay;
import com.hbut.internship.util.LocationUtils;
import com.hbut.internship.util.ObjectUtils;
import com.hbut.internship.util.TimeUtils;
import com.hbut.internship.util.ToastUtil;
import com.hbut.internship.view.MyOrientationListener;
import com.hbut.internship.view.MyOrientationListener.OnOrientationListener;
import com.internship.model.Position;

/**
 * 路线导航界面
 * 
 * @author Nie
 * 
 */
public class DaohangActivity extends BaseActivity implements OnClickListener,
		OnGetGeoCoderResultListener, OnGetRoutePlanResultListener {

	private NaviParaOption paraOption;
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private GeoCoder mSearch;// 反地理编码查询

	// 定位相关
	private LocationClient mLocationClient;
	private MyLocationListener mLocationListener;
	private boolean isFirstIn = true;
	private boolean flag = false;// 标记是否已经定位过
	private double mLatitude;// 定位得到的纬度
	private double mLongtitude;// 定位得到的经度
	private double lat;// 实习地点的纬度
	private double lon;// 实习地点的经度

	// 路线
	private RouteLine route = null;
	// 路线搜索相关
	private RoutePlanSearch routePlanSearch = null;
	private PlanNode stNode = null;// 起始点
	private PlanNode enNode = null;// 终点

	// 自定义定位图标
	private BitmapDescriptor mIconLocation;
	private MyOrientationListener myOrientationListener = null;
	private float mCurrentX;
	private LocationMode mLocationMode;

	private ImageButton imbt_location;
	private TextView poaddress, instance;
	private Button daohang, car, bus, walk;
	private Position position;

	private String ecity;// 实习地点城市。
	private double juli;// 实习点和定位点的之间的距离
	private DecimalFormat df = new DecimalFormat("#.00");// 将double数只保留两位小数。

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_daohang);

		position = (Position) getIntent().getSerializableExtra("Position");
		//将Position中的byte数组转化为LatLng对象，得到经纬度。
		LatLng positionLatLng = (LatLng) ObjectUtils.ByteToObject(position
				.getAddressBytes());
		lat = positionLatLng.latitude;
		lon = positionLatLng.longitude;

		findView();

		// 初始化定位
		initMyLocation();

		initView();

	}

	private void findView() {
		mMapView = (MapView) findViewById(R.id.mapview);
		mBaiduMap = mMapView.getMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
		mBaiduMap.setMapStatus(msu);
		imbt_location = (ImageButton) findViewById(R.id.imbt_daohang_location);
		imbt_location.setOnClickListener(this);
		poaddress = (TextView) findViewById(R.id.tv_daohang_poaddress);
		instance = (TextView) findViewById(R.id.tv_daohang_instance);
		car = (Button) findViewById(R.id.bt_daohang_car);
		bus = (Button) findViewById(R.id.bt_daohang_bus);
		walk = (Button) findViewById(R.id.bt_daohang_walk);
		daohang = (Button) findViewById(R.id.bt_daohang_daohang);
		daohang.setOnClickListener(this);

		car.setOnClickListener(this);
		bus.setOnClickListener(this);
		walk.setOnClickListener(this);

	}

	private void initView() {

		// 根据实习地点的位置给poaddress赋值
		LatLng latLng1 = new LatLng(lat, lon);// 实习地点
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(latLng1));// 反地理编码查询。

		// 路线查询初始化及结果监听
		routePlanSearch = RoutePlanSearch.newInstance();
		routePlanSearch.setOnGetRoutePlanResultListener(this);
	}

	/**
	 * 初始化定位
	 */
	private void initMyLocation() {

		mLocationMode = LocationMode.NORMAL;
		mLocationClient = new LocationClient(this);

		mLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mLocationListener);

		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度
		option.setIsNeedAddress(true);// 位置
		option.setOpenGps(true);// 打开GPS
		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 定位模式——高精度
		option.setScanSpan(1000);// 多长时间进行一次请求
		mLocationClient.setLocOption(option);
		// 初始化图标
		mIconLocation = BitmapDescriptorFactory
				.fromResource(R.drawable.navi_map_gps_locked);
		myOrientationListener = new MyOrientationListener(
				getApplicationContext());

		myOrientationListener
				.setOnOrientationListener(new OnOrientationListener() {
					@Override
					public void onOrientationChanged(float x) {
						mCurrentX = x;
					}
				});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imbt_daohang_location:// 定位按钮
			flag = true;// 设置成已经定位
			mBaiduMap.setMyLocationEnabled(true);
			if (!mLocationClient.isStarted())
				mLocationClient.start();
			// 开启方向传感器
			myOrientationListener.start();
			LocationUtils.centerToMyLocation(mBaiduMap, mLatitude, mLongtitude);
			break;

		case R.id.bt_daohang_car:// 驾车路线
			if (flag) {
				// 重置浏览节点的路线数据
				if (route != null)
					route = null;
				mBaiduMap.clear();
				routePlanSearch.drivingSearch((new DrivingRoutePlanOption())
						.from(stNode).to(enNode)
						.policy(DrivingPolicy.ECAR_DIS_FIRST));// 距离优先
			} else {
				ToastUtil.showtoast("抱歉，请先定位");
			}
			break;
		case R.id.bt_daohang_bus:// 换乘路线
			if (flag) {
				// 重置浏览节点的路线数据
				if (route != null)
					route = null;
				mBaiduMap.clear();
				routePlanSearch.transitSearch((new TransitRoutePlanOption())
						.from(stNode).city(ecity).to(enNode)
						.policy(TransitPolicy.EBUS_WALK_FIRST));// 少步行
			} else {
				ToastUtil.showtoast("抱歉，请先定位");
			}
			break;
		case R.id.bt_daohang_walk:// 步行路线
			if (flag) {
				// 重置浏览节点的路线数据
				if (route != null)
					route = null;
				mBaiduMap.clear();
				routePlanSearch.walkingSearch((new WalkingRoutePlanOption())
						.from(stNode).to(enNode));
			} else {
				ToastUtil.showtoast("抱歉，请先定位");
			}
			break;
		case R.id.bt_daohang_daohang:// 调用百度地图导航
			if (flag) {
				paraOption = new NaviParaOption();
				LatLng latLng1 = new LatLng(mLatitude, mLongtitude);
				paraOption.startPoint(latLng1);
				paraOption.startName("从这里开始");
				LatLng latLng2 = new LatLng(lat, lon);
				paraOption.endPoint(latLng2);
				paraOption.endName(ecity);
				try {

					BaiduMapNavigation.openBaiduMapNavi(paraOption, this);

				} catch (BaiduMapAppNotSupportNaviException e) {
					e.printStackTrace();
					Toast.makeText(DaohangActivity.this, "未找到应用",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				ToastUtil.showtoast("抱歉，请先定位");
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 定位监听器
	 * 
	 * @author Nie
	 * 
	 */
	private class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {

			MyLocationData data = new MyLocationData.Builder()//
					.direction(mCurrentX)//
					.accuracy(location.getRadius())// 误差半径
					.latitude(location.getLatitude())//
					.longitude(location.getLongitude())//
					.build();
			mBaiduMap.setMyLocationData(data);
			// 设置自定义图标
			MyLocationConfiguration config = new MyLocationConfiguration(
					mLocationMode, true, mIconLocation);
			mBaiduMap.setMyLocationConfigeration(config);

			// 更新经纬度
			mLatitude = location.getLatitude();
			mLongtitude = location.getLongitude();
			// 开启定位
			if (isFirstIn) {
				LatLng latLng = new LatLng(mLatitude, mLongtitude);
				MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
				mBaiduMap.animateMapStatus(msu);
				isFirstIn = false;
			}

			LatLng poLatLng = new LatLng(mLatitude, mLongtitude);
			LatLng latLng = new LatLng(lat, lon);
			juli = new DistanceUtil().getDistance(latLng, poLatLng);// 查询两地间的距离,单位是m，返回-1时表示转换错误。
			if (juli == -1) {
				instance.setText("转换错误");
			} else {
				df = new DecimalFormat("#.00");// 将double数只保留两位小数。
				instance.setText("距离该实习地点" + df.format(juli / 1000) + "千米");
			}
			setNode();
		}
	}

	// 地理编码：地址到经纬度
	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		// TODO Auto-generated method stub

	}

	// 反地理编码结果 经纬度到地址
	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		// TODO Auto-generated method stub
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			return;
		} else {
			poaddress.setText(result.getAddress());// 给实习地点赋值
			ecity = result.getAddressDetail().city;// 得到实习地点所在的城市。
		}
	}

	/**
	 * 设置路线规划的起始点。
	 */
	private void setNode() {
		// 根据坐标查询 我的坐标 ---> 目标marker
		stNode = PlanNode.withLocation(new LatLng(mLatitude, mLongtitude));
		enNode = PlanNode.withLocation(new LatLng(lat, lon));
	}

	/**
	 * 路线规划监听器
	 * 
	 * 步行1分钟 0.06公里 驾车1分钟 0.55公里 公交1分钟 0.15公里
	 * 
	 * @param minute
	 * @return
	 */
	// 驾车路线规划
	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult result) {
		// TODO Auto-generated method stub
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(DaohangActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
					.show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {

			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {

			route = result.getRouteLines().get(0);
			DrivingRouteOverlay overlay = new DrivingRouteOverlay(mBaiduMap);

			mBaiduMap.setOnMarkerClickListener(overlay);

			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap(); // 将所有Overlay 添加到地图上
			overlay.zoomToSpan(); // 缩放地图，使所有Overlay都在合适的视野内 注：
									// 该方法只对Marker类型的overlay有效
			int cartime = (int) (juli / (1000 * 0.55));
			car.setText(TimeUtils.timeFormatter(cartime));
		}

	}

	// 换乘路线规划
	@Override
	public void onGetTransitRouteResult(TransitRouteResult result) {
		// TODO Auto-generated method stub
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			bus.setText("无");
			Toast.makeText(DaohangActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
					.show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {

			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {

			route = result.getRouteLines().get(0);
			TransitRouteOverlay overlay = new TransitRouteOverlay(mBaiduMap);
			// routeOverlay = overlay;

			mBaiduMap.setOnMarkerClickListener(overlay);

			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap(); // 将所有Overlay 添加到地图上
			overlay.zoomToSpan(); // 缩放地图，使所有Overlay都在合适的视野内 注：
									// 该方法只对Marker类型的overlay有效

			int bustime = (int) (juli / (1000 * 0.15));
			bus.setText(TimeUtils.timeFormatter(bustime));
		}
	}

	// 步行路线规划
	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult result) {
		// TODO Auto-generated method stub
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(DaohangActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
					.show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {

			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {

			route = result.getRouteLines().get(0);
			WalkingRouteOverlay overlay = new WalkingRouteOverlay(mBaiduMap);
			// routeOverlay = overlay;
			mBaiduMap.setOnMarkerClickListener(overlay);

			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap(); // 将所有Overlay 添加到地图上
			overlay.zoomToSpan(); // 缩放地图，使所有Overlay都在合适的视野内 注：
									// 该方法只对Marker类型的overlay有效
			int walktime = (int) (juli / (1000 * 0.06));
			walk.setText(TimeUtils.timeFormatter(walktime));
		}
	}

	// 自行车路线——没有。
	@Override
	public void onGetBikingRouteResult(BikingRouteResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		mSearch.destroy();
		if (routePlanSearch != null) {
			routePlanSearch.destroy();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mMapView.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		// centerToMyLocation(45.0, 45.0);
		LocationUtils.initLocation(mBaiduMap, lat, lon);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		// 停止定位
		mBaiduMap.setMyLocationEnabled(false);
		mLocationClient.stop();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mMapView.onPause();
	}

}
