package com.xb.mobilesafe;

import com.xb.mobilesafe.utils.LogUtil;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.TextView;

public class SplashActivity extends Activity {

	private final static String TAG="SplashActivity";
	
	private TextView tv_splash_version;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		//设置版本号
		tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
		tv_splash_version.setText(getString(R.string.version)+getVersionName());
	}
	
	/**
	 * 得到版本号
	 * @return String
	 */
	private String getVersionName(){
		//管理手机apk
		PackageManager pm = getPackageManager();
		//得到指定apk的功能清单文件
		try {
			return pm.getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			LogUtil.e(TAG, e.getMessage());
		}
		return "";
	}
	
}
