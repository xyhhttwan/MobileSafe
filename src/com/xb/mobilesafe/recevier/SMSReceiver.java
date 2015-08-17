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
 * 短信接收
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
		//接收短信的代码
		Object[] objs = (Object[]) intent.getExtras().get("pdus");
		for(Object ob :objs){
			SmsMessage message = SmsMessage.createFromPdu((byte[])ob);
			//发送号码
			String sender = message.getDisplayOriginatingAddress();
			String body   = message.getMessageBody();
			ShowText.show(sender);
			////判断是否开启了防盗保护
			boolean safeStatus = sp.getBoolean("safeStatus", false);
			ShowText.show(safeStatus+"");
			if(safeStatus){
				//获取防盗安全号码
				String safePhone  = sp.getString("safePhone", "");
				//如果安全号码和咱们的发送者号码相同则进入
				if(!"".equals(safePhone) && safePhone.equals(sender)){
					//匹配发送内容
					if("#*location*#".equals(body)){
						//得到手机的GPS
						LogUtil.i(TAG, "得到手机的GPS");
						//启动GPS服务
						Intent gpsIntent  = new Intent(context,GPSService.class);
						context.startService(gpsIntent);
						//获取保存的最好一次更新的坐标
						String lastlocation = sp.getString("lastlocation", null);
						if(TextUtils.isEmpty(lastlocation)){
							SMSUtils.send(safePhone, "服务正在启动...");
						}else
						{
							SMSUtils.send(safePhone, lastlocation);
						}
						
					}else if("#*alarm*#".equals(body)){
						//播放报警影音
						LogUtil.i(TAG, "播放报警影音");
						MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
						//声音设置最高
						player.setVolume(1.0f, 1.0f);
						player.setLooping(false);
						player.start();
					}
					else if("#*wipedata*#".equals(body)){
						//远程清除数据
						LogUtil.i(TAG, "远程清除数据");
					}
					else if("#*lockscreen*#".equals(body)){
						//远程锁屏
						LogUtil.i(TAG, "远程锁屏");
					}
					//中止广播
					abortBroadcast();
				}
			}
			
		}
	}

}
