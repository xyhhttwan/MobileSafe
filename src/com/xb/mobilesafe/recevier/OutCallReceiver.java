package com.xb.mobilesafe.recevier;

import com.xb.mobilesafe.db.dao.NumberAddressQuery;
import com.xb.mobilesafe.utils.LogUtil;
import com.xb.mobilesafe.utils.ShowText;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * ȥ��Receiver
 * @author baixb
 *
 */
public class OutCallReceiver extends BroadcastReceiver {
	
	private static final String TAG = "OutCallReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {
	
		//�õ����ȥ�ĵ绰
		String phone  = getResultData();
		LogUtil.e(TAG, "�е绰���ȥ��:"+phone);
		//��ѯ������
		String address = NumberAddressQuery.queryNumber(phone);
		//��ʾ����
		ShowText.myshow(address,1);
		
	}

}
