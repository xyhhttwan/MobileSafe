package com.xb.mobilesafe.activity;

import com.xb.mobilesafe.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

/**
 * 向导第一步
 * @author baixb
 *
 */
public class Setup1Activity extends BaseSetupActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup1);
	}
	
	public static void actionStart(Context context){
		Intent intent  = new Intent (context,Setup1Activity.class);
		context.startActivity(intent);
	}

	
	@Override  
    public boolean onKeyDown(int keyCode, KeyEvent event)  
    {  
        if (keyCode == KeyEvent.KEYCODE_BACK )  
        {  
        	finish();
        }  
        return false;  
    } 

	
	public void showNext(){
		Setup2Activity.actionStart(this);
		finish();
		ShowNextOverPendingAnim();
	}

	@Override
	public void showPre() {
		
	}

}
