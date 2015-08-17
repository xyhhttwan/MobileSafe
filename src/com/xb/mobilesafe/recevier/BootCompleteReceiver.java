package com.xb.mobilesafe.recevier;

import com.xb.mobilesafe.utils.LogUtil;
import com.xb.mobilesafe.utils.SMSUtils;
import com.xb.mobilesafe.utils.ShowText;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;

public class BootCompleteReceiver extends BroadcastReceiver {
	
	private SharedPreferences sp;
	private TelephonyManager tm;
	@Override
	public void onReceive(Context context, Intent intent) {
		
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		//判断是否开启了防盗保护
		boolean safeStatus = sp.getBoolean("safeStatus", false);
		//开启了
		if(safeStatus){
			//得到之前保存的sim卡信息
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
					SMSUtils.send("15249284875", "防盗警告");
					ShowText.show("sim卡没有发生变更");
				}
			}
		}
	
		
		
	}

}
