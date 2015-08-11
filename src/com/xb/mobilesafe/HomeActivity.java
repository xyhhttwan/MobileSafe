package com.xb.mobilesafe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class HomeActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
	}
	
	public static void actionStart(Context context){
		Intent intent = new Intent(context,HomeActivity.class);
		context.startActivity(intent);
	}

}
