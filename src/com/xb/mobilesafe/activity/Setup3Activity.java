package com.xb.mobilesafe.activity;

import com.xb.mobilesafe.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

public class Setup3Activity extends BaseSetupActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
	}
	
	public static void actionStart(Context context){
		Intent intent  = new Intent (context,Setup3Activity.class);
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
		Setup4Activity.actionStart(this);
		finish();
		ShowNextOverPendingAnim();
	}

	@Override
	public void showPre() {
		Setup2Activity.actionStart(this);
		finish();
		ShowProOverPendingAnim();
	} 
	/**
	 * 启动选择联系人
	 * @param view
	 */
	public void selectContact(View view){
		Intent intent  = new Intent(this,SelectContanctActivity.class);
		startActivityForResult(intent, 0);
	}

}
