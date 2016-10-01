package com.hbut.internship.util;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.hbut.internship.R;

/**
 * 定位工具类
 * 
 * @author Nie
 * 
 */
public class LocationUtils {

	/**
	 * 定位到指定点
	 * 
	 * @param lat
	 *            纬度
	 * @param lon
	 *            经度
	 */
	public static void initLocation(BaiduMap mBaiduMap, double lat, double lon) {
		LatLng cenpt = new LatLng(lat, lon);
		MyLocationData data = new MyLocationData.Builder().latitude(lat)//
				.longitude(lon)//
				.build();
		mBaiduMap.setMyLocationData(data);
		// 定义Maker坐标点
		LatLng point = new LatLng(lat, lon);

		// 构建Marker图标
		BitmapDescriptor bitmap = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_gcoding);
		// 构建MarkerOption，用于在地图上添加Marker
		OverlayOptions option = new MarkerOptions().position(point)
				.icon(bitmap);
		// 在地图上添加Marker，并显示
		mBaiduMap.addOverlay(option);

		MapStatus mapStatus = new MapStatus.Builder().target(cenpt).zoom(16)
				.build();
		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
				.newMapStatus(mapStatus);
		// 改变地图状态
		mBaiduMap.setMapStatus(mMapStatusUpdate);
	}

	/**
	 * 定位到指定的位置
	 * 
	 * @param lat
	 *            指定位置的纬度
	 * @param lon
	 *            指定位置的经度
	 */
	public static void centerToMyLocation(BaiduMap mBaiduMap, double lat,
			double lon) {
		LatLng latLng = new LatLng(lat, lon);
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
		mBaiduMap.animateMapStatus(msu);
	}
}
