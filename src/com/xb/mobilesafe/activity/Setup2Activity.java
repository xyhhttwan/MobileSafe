package com.xb.mobilesafe.activity;

import com.xb.mobilesafe.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

public class Setup2Activity extends BaseSetupActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
		
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
}
