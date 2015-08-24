package com.xb.mobilesafe.activity;


import com.xb.mobilesafe.R;
import com.xb.mobilesafe.service.AddressService;
import com.xb.mobilesafe.service.CallSmsBlackNumberService;
import com.xb.mobilesafe.ui.SettingItemView;
import com.xb.mobilesafe.ui.SettingPhoneBackground;
import com.xb.mobilesafe.utils.ServiceUtils;
import com.xb.mobilesafe.utils.ShowText;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 设置activity
 * @author baixb
 *
 */
public class SettingActivity extends Activity implements OnClickListener{
    
	//自动更新
	private SettingItemView siv_update;
	//归属地
	private SettingItemView show_address;
	
	private SettingPhoneBackground callAddressBackground;
	
	private SharedPreferences sp;
	//是否自动更新
	private boolean auto_uppdate;
	
	private SettingItemView siv_isBlackNumber;
	//来电归属地服务
	private Intent addressIntent ;
	//黑名单
	private Intent callSmsSafeIntent;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		/*********自动更新相关代码*************/
		siv_update = (SettingItemView) findViewById(R.id.siv_update);
		siv_update.setOnClickListener(this);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		auto_uppdate = sp.getBoolean("auto_update", false);		
		if(auto_uppdate)siv_update.setChecked(true);
			siv_update.setChecked(false);
		/*********自动更新相关代码*************/
		
		/*********号码归属地相关代码	*************/
		show_address = (SettingItemView) findViewById(R.id.siv_show_address);
		show_address.setOnClickListener(this);
		//判断号码归属地服务是否开启
		if(ServiceUtils.isRunningService("com.xb.mobilesafe.service.AddressService")){
			show_address.setChecked(true);
		}else{
			show_address.setChecked(false);
		}
		/*********号码归属地相关代码	*************/
		
		/********号码归属地提示框相关代码**********/
		int phone_address_background = sp.getInt("phone_address_background",0);
		callAddressBackground = (SettingPhoneBackground) findViewById(R.id.siv_phone_address_show);
		callAddressBackground.setDes(items[phone_address_background]);
		callAddressBackground.setOnClickListener(this);
		/********号码归属地提示框相关代码**********/
		
		/****黑名单***/
		siv_isBlackNumber  = (SettingItemView) findViewById(R.id.siv_isBlackNumber);
		siv_isBlackNumber.setOnClickListener(this);
		
		if(ServiceUtils.isRunningService("com.xb.mobilesafe.service.CallSmsBlackNumberService")){
			siv_isBlackNumber.setChecked(true);
		}else{
			siv_isBlackNumber.setChecked(false);
		}
		/****黑名单***/
	}
	
	public static void actionStart(Context context){
		Intent intent  = new Intent(context,SettingActivity.class);
		context.startActivity(intent);
	}
	
	
	//弹出一个对话框
	final String [] items={"半透明","活力橙","卫士蓝","金属灰","苹果绿"};

	@Override
	public void onClick(View v) {	
		Editor editor = sp.edit();
		switch (v.getId()) {
		case R.id.siv_update:
			//如果选中
			if(siv_update.isChecked()){
				siv_update.setChecked(false);
				editor.putBoolean("auto_update", false);
			}else{
				siv_update.setChecked(true);
				editor.putBoolean("auto_update", true);
			}
			editor.commit();
			break;
		case R.id.siv_show_address:
			addressIntent = new Intent(this,AddressService.class);
			if(show_address.isChecked()){
				show_address.setChecked(false);
				stopService(addressIntent);
				
			}else{
				show_address.setChecked(true);
				startService(addressIntent);
			}
			break;
			
		case R.id.siv_phone_address_show:
			AlertDialog.Builder builder = new Builder(SettingActivity.this);
			builder.setTitle(getString(R.string.phone_address_show_style));
			int which = sp.getInt("phone_address_background",0);
			builder.setSingleChoiceItems(items, which, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Editor editor = sp.edit();
					editor.putInt("phone_address_background", which);
					editor.commit();
					callAddressBackground.setDes(items[which]);
					dialog.dismiss();
				}
			});
			builder.setNegativeButton("取消", null);
			builder.show();
			break;
		case R.id.siv_isBlackNumber:
			callSmsSafeIntent = new Intent(this,CallSmsBlackNumberService.class);
			if(siv_isBlackNumber.isChecked()){
				siv_isBlackNumber.setChecked(false);
				stopService(callSmsSafeIntent);
				ShowText.show(ServiceUtils.isRunningService("com.xb.mobilesafe.service.CallSmsBlackNumberService")+"2");

			}else{
				siv_isBlackNumber.setChecked(true);
				startService(callSmsSafeIntent);
				ShowText.show(ServiceUtils.isRunningService("com.xb.mobilesafe.service.CallSmsBlackNumberService")+"1");
			}
			break;
		default:
			break;
		}
		
		
	}

	

}
