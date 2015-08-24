package com.xb.mobilesafe.recevier;

import com.xb.mobilesafe.R;
import com.xb.mobilesafe.service.GPSService;
import com.xb.mobilesafe.utils.LogUtil;
import com.xb.mobilesafe.utils.SMSUtils;
import com.xb.mobilesafe.utils.ShowText;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.view.View;

/**
 * ���Ž���
 * @author baixb
 *
 */
public class SMSReceiver extends BroadcastReceiver {
	
	private static final String TAG = "SMSReceiver";
	private SharedPreferences sp;
	
	private Context contexts;
	@Override
	public void onReceive(Context context, Intent intent) {
		sp =context.getSharedPreferences("config", Context.MODE_PRIVATE);
		dpm =(DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		contexts = context;
		//���ն��ŵĴ���
		Object[] objs = (Object[]) intent.getExtras().get("pdus");
		for(Object ob :objs){
			SmsMessage message = SmsMessage.createFromPdu((byte[])ob);
			//���ͺ���
			String sender = message.getDisplayOriginatingAddress();
			String body   = message.getMessageBody();
			////�ж��Ƿ����˷�������
			boolean safeStatus = sp.getBoolean("safeStatus", false);
			if(safeStatus){
				//��ȡ������ȫ����
				String safePhone  = sp.getString("safePhone", "");
				//�����ȫ��������ǵķ����ߺ�����ͬ�����
				if(!"".equals(safePhone) && safePhone.equals(sender)){
					//ƥ�䷢������
					if("#*location*#".equals(body)){
						//�õ��ֻ���GPS
						LogUtil.i(TAG, "�õ��ֻ���GPS");
						//����GPS����
						Intent gpsIntent  = new Intent(context,GPSService.class);
						context.startService(gpsIntent);
						//��ȡ��������һ�θ��µ�����
						String lastlocation = sp.getString("lastlocation", null);
						if(TextUtils.isEmpty(lastlocation)){
							SMSUtils.send(safePhone, "������������...");
						}else
						{
							SMSUtils.send(safePhone, lastlocation);
						}
						
					}else if("#*alarm*#".equals(body)){
						//���ű���Ӱ��
						LogUtil.i(TAG, "���ű���Ӱ��");
						MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
						//�����������
						player.setVolume(1.0f, 1.0f);
						player.setLooping(false);
						player.start();
					}
					else if("#*wipedata*#".equals(body)){
						//Զ���������
						LogUtil.i(TAG, "Զ���������");
					}
					else if("#*lockscreen*#".equals(body)){
						lockScrenn();
						//Զ������
						LogUtil.i(TAG, "Զ������");
					}
					//��ֹ�㲥
					abortBroadcast();
				}
			}
			
		}
	}
	
	private DevicePolicyManager dpm;
	private void lockScrenn(){
		ComponentName deviceComName = new ComponentName(contexts,LockScreenRecevier.class);
		if(dpm.isAdminActive(deviceComName)){
			dpm.lockNow();
			dpm.resetPassword("123", 0);
		}else{
			openDeviceAdmin(deviceComName);
		}
		
	}
	
	public void openDeviceAdmin(ComponentName deviceComName){
		//����һ����ͼ�������ǿ����豸�ĳ�������Ա
		  Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
          intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, deviceComName);
         //Ȱ˵�û���������Ա
          intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                contexts.getString(R.string.device_admin_lock_des));
         contexts.startActivity(intent);
	}
	
	
	public void uninstall(View view){
		 ComponentName cn = new ComponentName(contexts, LockScreenRecevier.class);
		//�����Ƴ�����Ա
		dpm.removeActiveAdmin(cn);
		Intent intent = new Intent();
		intent.setAction("android.intent.action.UNINSTALL_PACKAGE");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setData(Uri.parse("package:"+contexts.getPackageName()));
		contexts.startActivity(intent);
	}
	

}
