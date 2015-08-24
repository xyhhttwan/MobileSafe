package com.xb.mobilesafe.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

/**
 * ����Ĺ�����
 * @author baixb
 *
 */
public class ServiceUtils {
	
	/**
	 * 
	 * У������Ƿ���
	 * @param serviceName �������� (com.xb.mobilesafe.xx)
	 * @return
	 */
	public static  boolean isRunningService(String serviceName){
		ActivityManager am =(ActivityManager) MyApplication.getContext().
				getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningServiceInfo > list = am.getRunningServices(100);
			for(RunningServiceInfo serviceInfo :list){
				if(serviceInfo.service.getClassName().equals(serviceName)){
					return true;
				}
			}
		return false;
	}

}
