package com.xb.mobilesafe.activity;

import com.xb.mobilesafe.R;
import com.xb.mobilesafe.ui.SettingItemView;
import com.xb.mobilesafe.utils.ShowText;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;

public class Setup2Activity extends BaseSetupActivity {

	private TelephonyManager tm;
	
	private CheckBox cb_setting_checked;
	
	private SettingItemView settingItemView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		cb_setting_checked = (CheckBox) findViewById(R.id.cb_setting_checked);
		settingItemView = (SettingItemView) findViewById(R.id.siv_bind);
		if(!TextUtils.isEmpty(sp.getString("sim", null))){
			settingItemView.setChecked(true);
		}else{
			settingItemView.setChecked(false);
		}
		
	}
	
	public static void actionStart(Context context){
		Intent intent  = new Intent (context,Setup2Activity.class);
		context.startActivity(intent);
	}
	
	@Override  
    public boolean onKeyDown(int keyCode, KeyEvent event)  
    {  
        if (keyCode == KeyEvent.KEYCODE_BACK )  
        {  
        	showPre();
        }  
        return false;  
    }

	@Override
	public void showNext() {
		if(TextUtils.isEmpty(sp.getString("sim", null))){
			ShowText.show("请先点击绑定sim卡");
			return ;
		}
		Setup3Activity.actionStart(this);
		finish();
		ShowNextOverPendingAnim();
	}

	@Override
	public void showPre() {
		Setup1Activity.actionStart(this);
		finish();
		ShowProOverPendingAnim();
	} 
	
	public void bind(View view){
		//得到sim序列号
		String num = tm.getSimSerialNumber();
		Editor editor = sp.edit();
		
		if(cb_setting_checked.isChecked()){
			settingItemView.setChecked(false);
			editor.putString("sim",null);
		}else
		{
			settingItemView.setChecked(true);
			editor.putString("sim",num);
		}
		editor.commit();
	}
}
