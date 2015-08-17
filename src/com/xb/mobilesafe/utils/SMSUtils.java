package com.xb.mobilesafe.utils;

import android.app.PendingIntent;
import android.content.Intent;
import android.telephony.SmsManager;

public class SMSUtils {
	
	private static final String TAG = "SMSUtils";
	public static void send(String phone, String message){
		LogUtil.e(TAG, "start->phone,"+phone+",messages"+message);
        PendingIntent pi = PendingIntent.getActivity(MyApplication.getContext(), 0, 
        			new Intent(MyApplication.getContext(), SMSUtils.class), 0);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phone, null, message, pi, null);
        LogUtil.e(TAG, "end->phone,"+phone+",messages"+message);
    }

}
