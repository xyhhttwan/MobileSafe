package com.xb.mobilesafe.service;

import com.xb.mobilesafe.db.dao.BlackNumberDao;
import com.xb.mobilesafe.utils.ShowText;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

/**
 * ���������� ���ط���
 * @author baixb
 *
 */
public class CallSmsBlackNumberService extends Service {

	private InnerSmsReceiver innerSmsReceiver;
	
	private BlackNumberDao dao;
	
	//�绰�Ĺ�����
	private TelephonyManager tm;
	
	private MyListener listener;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	class InnerSmsReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			//���ն��ŵĴ���
			Object[] objs = (Object[]) intent.getExtras().get("pdus");
			for(Object ob :objs){
				SmsMessage message = SmsMessage.createFromPdu((byte[])ob);
				//���ͺ���
				String sender = message.getDisplayOriginatingAddress();
				//��ѯ����������û�иú���
				boolean result  = dao.find(sender);
				//����
				if(result){
					String mode =dao.getModeByPhone(sender);
					if(null!=mode){
						//����ȫ�����߶���
						if(mode.equals("1")||mode.equals("2")){
							abortBroadcast();
						}
					}
				}
				//���ܶ�Ϣ����
				//�õ����͵�����
				//String content  = message.getDisplayMessageBody();
				//���ܷ������� �����Ƿ�����
				
			}
		}
		
	}

	@Override
	public void onCreate() {
		
		innerSmsReceiver = new InnerSmsReceiver();
		//ע��㲥������
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		//���Ȩ��
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		//ע��
		registerReceiver(innerSmsReceiver, filter);
		//�绰
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new MyListener();
		tm.listen(listener,PhoneStateListener.LISTEN_CALL_STATE);
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(innerSmsReceiver);
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		innerSmsReceiver = null;
	}
	
	//�绰������
	class MyListener extends PhoneStateListener{

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			//����״̬
			case TelephonyManager.CALL_STATE_RINGING:
				if(dao.find(incomingNumber)){
					String mode = dao.getModeByPhone(incomingNumber);
					//���ص绰����ȫ������
					if(null!=mode && (mode.equals("0")||mode.equals("2"))){
						ShowText.show("��Ҫ�Ҷϵ绰");
					}
				}
				break;

			default:
				break;
			}
			super.onCallStateChanged(state, incomingNumber);
		}
		
	}
	

}
