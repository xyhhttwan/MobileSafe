package com.xb.mobilesafe.activity;


import com.xb.mobilesafe.R;
import com.xb.mobilesafe.ui.SettingItemView;

import android.app.Activity;
import android.content.Context;
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
    
	//自定义组合件
	private SettingItemView siv_update;
	
	private SharedPreferences sp;
	//是否自动更新
	private boolean auto_uppdate;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		siv_update = (SettingItemView) findViewById(R.id.siv_update);
		siv_update.setOnClickListener(this);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		auto_uppdate = sp.getBoolean("auto_update", false);
		
		if(auto_uppdate){
			siv_update.setChecked(true);
		}else{
			siv_update.setChecked(false);
		}
		
		
	}
	
	public static void actionStart(Context context){
		Intent intent  = new Intent(context,SettingActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void onClick(View v) {	
		Editor editor = sp.edit();
		//如果选中
		if(siv_update.isChecked()){
			siv_update.setChecked(false);
			editor.putBoolean("auto_update", false);
		}else{
			siv_update.setChecked(true);
			editor.putBoolean("auto_update", true);
		}
		editor.commit();
	}

	

}
