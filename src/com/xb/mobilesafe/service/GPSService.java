package com.xb.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

/**
 * 定位服务
 * @author baixb
 *
 */
public class GPSService extends Service {
	
	private LocationManager lm;
	
	private MyLocationListener listener;

	/**
	 * 创建
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		lm  = (LocationManager) getSystemService(LOCATION_SERVICE);
		listener = new MyLocationListener();
		// 注册监听位置服务, 给位置提供者设置条件
		Criteria criteria= new Criteria();
		//精确的精度 设置为最大精度
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		//不要求海拔信息
		criteria.setAltitudeRequired(false);
		//不要求方位信息
		//criteria.setBearingRequired(false);
		//是否允许付费
		// criteria.setCostAllowed(true);
		//对电量的要求
		// criteria.setPowerRequirement(Criteria.POWER_LOW);
		
		//选择最好的定位方式
		String proveder =  lm.getBestProvider(criteria, true);
		
		lm.requestLocationUpdates(proveder, 6000, 6000, listener);
	}
	
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	
	class MyLocationListener implements LocationListener {
		
		/**
		 * 当位置改变的时候回调
		 */
		@Override
		public void onLocationChanged(Location location) {
			String longitude = "j:" + location.getLongitude() + "\n";
			String latitude = "w:" + location.getLatitude() + "\n";
			String accuracy = "a" + location.getAccuracy() + "\n";
			SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.putString("lastlocation", longitude + latitude + accuracy);
			editor.commit();

		}
		/**
		 * 当状态发生改变的时候回调 开启--关闭 ；关闭--开启
		 */
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			
		}
		
		/**
		 * 某一个位置提供者可以使用了
		 */
		@Override
		public void onProviderEnabled(String provider) {
			
		}
		/**
		 * 某一个位置提供者不可以使用了
		 */
		@Override
		public void onProviderDisabled(String provider) {
			
		}
		
	}

	/**
	 * 销毁
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		lm.removeUpdates(listener);
		listener =null;
	}
	
	
}
