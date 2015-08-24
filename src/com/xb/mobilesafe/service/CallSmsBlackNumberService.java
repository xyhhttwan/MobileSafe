package com.xb.mobilesafe.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;
import com.xb.mobilesafe.db.dao.BlackNumberDao;
import com.xb.mobilesafe.utils.LogUtil;
import com.xb.mobilesafe.utils.ShowText;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.CallLog.Calls;
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

		private static final String TAG = "CallSmsBlackNumberService";

		@Override
		public void onReceive(Context context, Intent intent) {
			//���ն��ŵĴ���
			Object[] objs = (Object[]) intent.getExtras().get("pdus");
			for(Object ob :objs){
				SmsMessage message = SmsMessage.createFromPdu((byte[])ob);
				//���ͺ���
				String sender = message.getDisplayOriginatingAddress();
				LogUtil.e(TAG, "������Ϣ:����"+sender+",����:"+message.getDisplayMessageBody());
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
		
		dao = new BlackNumberDao();
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
						//�۲���ģʽ,�۲����ݿ� ͨ����¼�ı仯
						getContentResolver().
							registerContentObserver
									(Calls.CONTENT_URI, 
											true,new CallLogObService
											(incomingNumber, new Handler()));
						endCall();
					}
				}
				break;

			default:
				break;
			}
			super.onCallStateChanged(state, incomingNumber);
		}
		
	}
	
	private class CallLogObService extends ContentObserver{
		
		private String incomingNumber ;
		public CallLogObService(String incomingNumber, Handler handler) {
			super(handler);
			this.incomingNumber = incomingNumber;
		}
		@Override
		public void onChange(boolean selfChange) {
			deletCallLog(incomingNumber);
			super.onChange(selfChange);
		}
		
		
	}
	
	/**
	 *�Ҷϵ绰
	 * ����һ������ִ�е�Զ�̷���,������ʱ�������
	 * @param <T>
	 */
	private  void endCall(){
		//��ȡ��ʵ��iBinder 
		//IBinder iBinder = ServiceManager.getService(TELEPHONY_SERVICE);
		try {
			@SuppressWarnings("rawtypes")
			Class clazz =  CallSmsBlackNumberService.class.getClassLoader().loadClass("android.os.ServiceManager");
			Method method = clazz.getDeclaredMethod("getService", String.class);
			IBinder iBinder = (IBinder) method.invoke(null, TELEPHONY_SERVICE);
			ITelephony.Stub.asInterface(iBinder).endCall();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * ���������ṩ��ɾ��ͨ����¼
	 * @param incomingNumber
	 */
	public void deletCallLog(String incomingNumber) {
		ContentResolver resolver = getContentResolver();
		Uri uri =Uri.parse("content://call_log/calls");
		//����
		//Uri uris  = Calls.CONTENT_URI;
		resolver.delete(uri, "number=?", new String[]{incomingNumber});
	}
	

}
