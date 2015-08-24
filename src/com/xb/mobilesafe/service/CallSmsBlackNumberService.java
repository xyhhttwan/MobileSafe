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
 * 黑名单短信 拦截服务
 * @author baixb
 *
 */
public class CallSmsBlackNumberService extends Service {

	private InnerSmsReceiver innerSmsReceiver;
	
	private BlackNumberDao dao;
	
	//电话的管理者
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
			//接收短信的代码
			Object[] objs = (Object[]) intent.getExtras().get("pdus");
			for(Object ob :objs){
				SmsMessage message = SmsMessage.createFromPdu((byte[])ob);
				//发送号码
				String sender = message.getDisplayOriginatingAddress();
				LogUtil.e(TAG, "发来短息:号码"+sender+",内容:"+message.getDisplayMessageBody());
				//查询黑名单中有没有该号码
				boolean result  = dao.find(sender);
				//存在
				if(result){
					String mode =dao.getModeByPhone(sender);
					if(null!=mode){
						//拦截全部或者短信
						if(mode.equals("1")||mode.equals("2")){
							abortBroadcast();
						}
					}
				}
				//智能短息拦截
				//得到发送的内容
				//String content  = message.getDisplayMessageBody();
				//智能分析内容 决定是否拦截
				
			}
		}
		
	}

	@Override
	public void onCreate() {
		
		innerSmsReceiver = new InnerSmsReceiver();
		//注册广播接受者
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		//最高权限
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		//注册
		registerReceiver(innerSmsReceiver, filter);
		//电话
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
	
	//电话监听类
	class MyListener extends PhoneStateListener{

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			//响铃状态
			case TelephonyManager.CALL_STATE_RINGING:
				if(dao.find(incomingNumber)){
					String mode = dao.getModeByPhone(incomingNumber);
					//拦截电话或者全部拦截
					if(null!=mode && (mode.equals("0")||mode.equals("2"))){
						ShowText.show("需要挂断电话");
						//观察者模式,观察数据库 通话记录的变化
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
	 *挂断电话
	 * 另外一个进程执行的远程方法,可能有时间差问题
	 * @param <T>
	 */
	private  void endCall(){
		//获取真实的iBinder 
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
	 * 利用内容提供器删除通话记录
	 * @param incomingNumber
	 */
	public void deletCallLog(String incomingNumber) {
		ContentResolver resolver = getContentResolver();
		Uri uri =Uri.parse("content://call_log/calls");
		//或者
		//Uri uris  = Calls.CONTENT_URI;
		resolver.delete(uri, "number=?", new String[]{incomingNumber});
	}
	

}
