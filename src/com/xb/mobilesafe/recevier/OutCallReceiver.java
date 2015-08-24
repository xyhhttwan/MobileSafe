package com.xb.mobilesafe.recevier;

import com.xb.mobilesafe.db.dao.NumberAddressQuery;
import com.xb.mobilesafe.utils.LogUtil;
import com.xb.mobilesafe.utils.ShowText;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 去电Receiver
 * @author baixb
 *
 */
public class OutCallReceiver extends BroadcastReceiver {
	
	private static final String TAG = "OutCallReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {
	
		//得到打出去的电话
		String phone  = getResultData();
		LogUtil.e(TAG, "有电话打出去了:"+phone);
		//查询归属地
		String address = NumberAddressQuery.queryNumber(phone);
		//显示出来
		ShowText.myshow(address,1);
		
	}

}
