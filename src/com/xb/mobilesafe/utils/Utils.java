package com.xb.mobilesafe.utils;

import android.os.Environment;

public class Utils {
	
	
	/**
	 * 判断sdcard是否存在
	 * @return true 存在 false不存在
	 */
	public static boolean isExistSDCard(){
		
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	

}
