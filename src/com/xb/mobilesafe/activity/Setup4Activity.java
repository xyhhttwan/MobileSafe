package com.xb.mobilesafe.activity;

import com.xb.mobilesafe.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

public class Setup4Activity extends BaseSetupActivity {
	 private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
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
		sp = getSharedPreferences("config", MODE_PRIVATE);
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
