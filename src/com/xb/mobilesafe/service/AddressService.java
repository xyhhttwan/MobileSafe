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
 * ����,��ʾ����
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
		//��������
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		ml = new MyPhoneListener();
		tm.listen(ml, PhoneStateListener.LISTEN_CALL_STATE);
		//�ô���ȥע��㲥������
		outCallReceiver = new OutCallReceiver();
		//������ͼ
		IntentFilter filter = new IntentFilter();
		//��绰
		filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
		registerReceiver(outCallReceiver, filter);
		
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//ȥ����������
		tm.listen(ml, PhoneStateListener.LISTEN_NONE);
		//ȥ��ע��
		unregisterReceiver(outCallReceiver);
		outCallReceiver =null;
	}
	
	class MyPhoneListener extends PhoneStateListener{

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			
			//state�绰��״̬,incomingNumber������ĵ绰
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {//����
			case TelephonyManager.CALL_STATE_RINGING:
				//
				String address = NumberAddressQuery.queryNumber(incomingNumber);
				ShowText.show(address);
				break;
			case TelephonyManager.CALL_STATE_IDLE://�绰�Ҷ�
				ShowText.RemoveToast();
			default:
				break;
			}
		}

		
		
	}
	
	
	

}
