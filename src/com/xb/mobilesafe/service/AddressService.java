package com.xb.mobilesafe.service;

import com.xb.mobilesafe.db.dao.NumberAddressQuery;
import com.xb.mobilesafe.recevier.OutCallReceiver;
import com.xb.mobilesafe.utils.ShowText;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

/**
 * 来电,显示服务
 * @author baixb
 *
 */
public class AddressService extends Service {
	
	private TelephonyManager tm;
	
	private MyPhoneListener ml;
	
	private OutCallReceiver outCallReceiver;
	


	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		//监听来电
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		ml = new MyPhoneListener();
		tm.listen(ml, PhoneStateListener.LISTEN_CALL_STATE);
		//用代码去注册广播接收者
		outCallReceiver = new OutCallReceiver();
		//定义意图
		IntentFilter filter = new IntentFilter();
		//打电话
		filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
		registerReceiver(outCallReceiver, filter);
		
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//去掉监听来电
		tm.listen(ml, PhoneStateListener.LISTEN_NONE);
		//去掉注册
		unregisterReceiver(outCallReceiver);
		outCallReceiver =null;
	}
	
	class MyPhoneListener extends PhoneStateListener{

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			
			//state电话的状态,incomingNumber打进来的电话
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {//来电
			case TelephonyManager.CALL_STATE_RINGING:
				//
				String address = NumberAddressQuery.queryNumber(incomingNumber);
				ShowText.show(address);
				break;
			case TelephonyManager.CALL_STATE_IDLE://电话挂断
				ShowText.RemoveToast();
			default:
				break;
			}
		}

		
		
	}
	
	
	

}
