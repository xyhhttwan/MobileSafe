package com.xb.mobilesafe;

import com.xb.mobilesafe.utils.HttpCallbackListener;
import com.xb.mobilesafe.utils.HttpUtil;
import com.xb.mobilesafe.utils.LogUtil;
import com.xb.mobilesafe.utils.ShowText;

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
		//���ð汾��
		tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
		tv_splash_version.setText(getString(R.string.version)+getVersionName());
		
		//���汾��Ϣ
		checkUpdate();
	}
	
	/**
	 * ���汾����
	 */
	private void checkUpdate() {
		ShowText.show("�����°汾");
		HttpUtil.sendHttpRequest(getString(R.string.seviverurl),new HttpCallbackListener() {
			@Override
			public void onFinish(String response) {
				//���̲߳�����UI����
				ShowText.show("�����°汾");
			}
			
			@Override
			public void onError(Exception e) {
				//���̲߳�����UI����
				ShowText.show("�����쳣,���Ժ�����.");
			}
		});
	}

	/**
	 * �õ��汾��
	 * @return String
	 */
	private String getVersionName(){
		//�����ֻ�apk
		PackageManager pm = getPackageManager();
		//�õ�ָ��apk�Ĺ����嵥�ļ�
		try {
			return pm.getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			LogUtil.e(TAG, e.getMessage());
		}
		return "";
	}
	
}
