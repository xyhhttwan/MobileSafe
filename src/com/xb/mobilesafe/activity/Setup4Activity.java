package com.xb.mobilesafe.activity;

import com.xb.mobilesafe.R;
import com.xb.mobilesafe.utils.ShowText;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Setup4Activity extends BaseSetupActivity {
	private SharedPreferences sp;
	
	private CheckBox cb_safe_status;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
		cb_safe_status = (CheckBox) findViewById(R.id.cb_safe_status);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		
		if(sp.getBoolean("safeStatus", false)){
			cb_safe_status.setChecked(true);
			setDes(0);
		}
		cb_safe_status.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					setDes(0);
				}else{
					setDes(1);
				}
				sp = getSharedPreferences("config", MODE_PRIVATE);
				Editor editor = sp.edit();
				editor.putBoolean("safeStatus", true);
				editor.commit();
			}
		});
	}
	
	private void setDes(int type){
		if(0==type)
			cb_safe_status.setText("手机防盗已经开启");
		else
			cb_safe_status.setText("手机防盗没有开启");
	}
	
	public static void actionStart(Context context){
		Intent intent  = new Intent (context,Setup4Activity.class);
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
		
		
	}
	
	@Override
	public void next(View view) {
		if(!cb_safe_status.isChecked()){
			ShowText.show("请开启防盗保护");
			return;
		}
		Editor editor = sp.edit();
		editor.putBoolean("isVisit", true);
		editor.commit();
		finish();
		LostFindActivity.actionStart(this);
	}

	@Override
	public void showPre() {
		Setup3Activity.actionStart(this);
		finish();
		ShowProOverPendingAnim();
		
	} 

}
