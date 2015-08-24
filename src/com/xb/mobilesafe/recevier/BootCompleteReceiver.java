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
		//�ж��Ƿ����˷�������
		boolean safeStatus = sp.getBoolean("safeStatus", false);
		//������
		if(safeStatus){
			//�õ�֮ǰ�����sim����Ϣ
			String oldsim = sp.getString("sim", null);
			if(null !=oldsim){
				//�õ��µ�sim����Ϣ
				tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
				String sim  = tm.getSimSerialNumber();
				
				if(!sim.equals(oldsim)){
					//���͹㲥
					ShowText.show("sim�������");
				}else{
					LogUtil.e("SIM", "sim��û�з������");
					SMSUtils.send("15249284875", "��������");
					ShowText.show("sim��û�з������");
				}
			}
		}
	
		
		
	}

}
