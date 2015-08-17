package com.xb.mobilesafe.recevier;

import com.xb.mobilesafe.R;
import com.xb.mobilesafe.service.GPSService;
import com.xb.mobilesafe.utils.LogUtil;
import com.xb.mobilesafe.utils.SMSUtils;
import com.xb.mobilesafe.utils.ShowText;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.text.TextUtils;

/**
 * ���Ž���
 * @author baixb
 *
 */
public class SMSReceiver extends BroadcastReceiver {
	
	private static final String TAG = "SMSReceiver";
	private SharedPreferences sp;
	@Override
	public void onReceive(Context context, Intent intent) {
		sp =context.getSharedPreferences("config", Context.MODE_PRIVATE);
		ShowText.show("====");
		//���ն��ŵĴ���
		Object[] objs = (Object[]) intent.getExtras().get("pdus");
		for(Object ob :objs){
			SmsMessage message = SmsMessage.createFromPdu((byte[])ob);
			//���ͺ���
			String sender = message.getDisplayOriginatingAddress();
			String body   = message.getMessageBody();
			ShowText.show(sender);
			////�ж��Ƿ����˷�������
			boolean safeStatus = sp.getBoolean("safeStatus", false);
			ShowText.show(safeStatus+"");
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
						//Զ������
						LogUtil.i(TAG, "Զ������");
					}
					//��ֹ�㲥
					abortBroadcast();
				}
			}
			
		}
	}

}
