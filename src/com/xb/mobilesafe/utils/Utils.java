package com.xb.mobilesafe.utils;

import android.os.Environment;

public class Utils {
	
	
	/**
	 * �ж�sdcard�Ƿ����
	 * @return true ���� false������
	 */
	public static boolean isExistSDCard(){
		
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	

}
