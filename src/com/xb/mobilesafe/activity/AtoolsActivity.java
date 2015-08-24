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
 * �߼�����
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
	 * ��������ص��ѯ
	 * @param view
	 */
	public void numberQuery(View view){
		NumberAddressQueryActivity.actionStart(this);
	}
	
	/**
	 * ��Ϣ����
	 * @param view
	 */
	public void smsBackup(View view ){
		//�ж�sdcard�Ƿ����
		if(Utils.isExistSDCard()){
			final ProgressDialog pd = new  ProgressDialog(this);
			pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pd.setMessage("���ڱ���...");
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
			ShowText.show("�ⲿsd��������");
		}
	}
	
	/**
	 * ���Ż�ԭ
	 * @param view
	 */
	public void smsRestore(View view){
		try {
			SMSUtils.restoreSms(false);
			ShowText.show("��ԭ�ɹ���",0);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.e(TAG, e.getMessage());
		}
	}
	
	
	

}
