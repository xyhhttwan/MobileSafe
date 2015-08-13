package com.xb.mobilesafe.recevier;

import com.xb.mobilesafe.utils.LogUtil;
import com.xb.mobilesafe.utils.ShowText;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;

public class BootCompleteReceiver extends BroadcastReceiver {
	
	private SharedPreferences sp;
	private TelephonyManager tm;
	@Override
	public void onReceive(Context context, Intent intent) {
		//得到之前保存的sim卡信息
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		String oldsim = sp.getString("sim", null);
		if(null !=oldsim){
			//得到新的sim卡信息
			tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			String sim  = tm.getSimSerialNumber();
			
			if(!sim.equals(oldsim)){
				//发送广播
				ShowText.show("sim发生变更");
			}else{
				LogUtil.e("SIM", "sim卡没有发生变更");
				ShowText.show("sim卡没有发生变更");
			}
		}
		
		
	}

}
