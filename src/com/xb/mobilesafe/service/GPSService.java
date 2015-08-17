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
 * ��λ����
 * @author baixb
 *
 */
public class GPSService extends Service {
	
	private LocationManager lm;
	
	private MyLocationListener listener;

	/**
	 * ����
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		lm  = (LocationManager) getSystemService(LOCATION_SERVICE);
		listener = new MyLocationListener();
		// ע�����λ�÷���, ��λ���ṩ����������
		Criteria criteria= new Criteria();
		//��ȷ�ľ��� ����Ϊ��󾫶�
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		//��Ҫ�󺣰���Ϣ
		criteria.setAltitudeRequired(false);
		//��Ҫ��λ��Ϣ
		//criteria.setBearingRequired(false);
		//�Ƿ�������
		// criteria.setCostAllowed(true);
		//�Ե�����Ҫ��
		// criteria.setPowerRequirement(Criteria.POWER_LOW);
		
		//ѡ����õĶ�λ��ʽ
		String proveder =  lm.getBestProvider(criteria, true);
		
		lm.requestLocationUpdates(proveder, 6000, 6000, listener);
	}
	
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	
	class MyLocationListener implements LocationListener {
		
		/**
		 * ��λ�øı��ʱ��ص�
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
		 * ��״̬�����ı��ʱ��ص� ����--�ر� ���ر�--����
		 */
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			
		}
		
		/**
		 * ĳһ��λ���ṩ�߿���ʹ����
		 */
		@Override
		public void onProviderEnabled(String provider) {
			
		}
		/**
		 * ĳһ��λ���ṩ�߲�����ʹ����
		 */
		@Override
		public void onProviderDisabled(String provider) {
			
		}
		
	}

	/**
	 * ����
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		lm.removeUpdates(listener);
		listener =null;
	}
	
	
}
