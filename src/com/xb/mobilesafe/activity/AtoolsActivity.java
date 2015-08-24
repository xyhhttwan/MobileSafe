package com.xb.mobilesafe.activity;

import com.xb.mobilesafe.R;
import com.xb.mobilesafe.utils.LogUtil;
import com.xb.mobilesafe.utils.SMSUtils;
import com.xb.mobilesafe.utils.SMSUtils.BackUpCallBack;
import com.xb.mobilesafe.utils.ShowText;
import com.xb.mobilesafe.utils.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * 高级工具
 * @author baixb
 *
 */
public class AtoolsActivity extends Activity {

	protected static final String TAG = "AtoolsActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atools);
	}
	
	public static void actionStart(Context content){
		Intent intent = new Intent(content,AtoolsActivity.class);
		content.startActivity(intent);
	}
	
	
	/**
	 * 号码归属地点查询
	 * @param view
	 */
	public void numberQuery(View view){
		NumberAddressQueryActivity.actionStart(this);
	}
	
	/**
	 * 短息备份
	 * @param view
	 */
	public void smsBackup(View view ){
		//判断sdcard是否存在
		if(Utils.isExistSDCard()){
			final ProgressDialog pd = new  ProgressDialog(this);
			pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pd.setMessage("正在备份...");
			pd.show();
			new Thread(){
				
				public void run(){
					try {
						SMSUtils.backupSms(new BackUpCallBack() {
							
							@Override
							public void onSmsBackup(int progress) {
								pd.setProgress(progress);
							}
							
							@Override
							public void beforeSmsBackup(int total) {
								pd.setMax(total);
							}
						});
					} catch (Exception e) {
						e.printStackTrace();
						LogUtil.e(TAG, e.getMessage());
					}
					pd.dismiss();
				}
				
			}.start();
			
		}else{
			ShowText.show("外部sd卡不可用");
		}
	}
	
	/**
	 * 短信还原
	 * @param view
	 */
	public void smsRestore(View view){
		try {
			SMSUtils.restoreSms(false);
			ShowText.show("还原成功！",0);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.e(TAG, e.getMessage());
		}
	}
	
	
	

}
