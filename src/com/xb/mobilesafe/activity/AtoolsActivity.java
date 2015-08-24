package com.xb.mobilesafe.activity;

import com.xb.mobilesafe.R;

import android.app.Activity;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atools);
	}
	
	public static void actionStart(Context content){
		Intent intent = new Intent(content,AtoolsActivity.class);
		content.startActivity(intent);
	}
	
	
	public void numberQuery(View view){
		NumberAddressQueryActivity.actionStart(this);
	}
	
	
	
	
	

}
