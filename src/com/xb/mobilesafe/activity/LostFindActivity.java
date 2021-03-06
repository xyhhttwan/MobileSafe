package com.xb.mobilesafe.activity;

import com.xb.mobilesafe.R;
import com.xb.mobilesafe.utils.LogUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LostFindActivity extends Activity {

	private static final String TAG = "LostFindActivity";

	private SharedPreferences sp;
	
	private ImageView img;
	
	private TextView tv_safe_phone;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//检查是否第一次访问该页面
		sp = getSharedPreferences("config", MODE_PRIVATE);
		boolean isVisit = sp.getBoolean("isVisit", false);
		if(isVisit){
			//手机防盗页面
			setContentView(R.layout.activity_lost_find);
			tv_safe_phone = (TextView) findViewById(R.id.tv_safe_phone);
			img = (ImageView) findViewById(R.id.iv_safe_lock);
			lostFind();
			//如果开启了防盗
			if(sp.getBoolean("safeStatus", false)){
				String safe_phone = sp.getString("safePhone", "");
				LogUtil.e(TAG, "safe_phone:"+safe_phone);
				tv_safe_phone.setText(safe_phone);
			}
			
		}else{
			//向导页面
			Setup1Activity.actionStart(this);
			finish();
		}
	}
	
	private void lostFind(){
		if(!TextUtils.isEmpty(sp.getString("sim", null))){
			img.setImageResource(R.drawable.lock);
		}
	}
	
	public static void actionStart(Context context){
		Intent intent  = new Intent (context,LostFindActivity.class);
		context.startActivity(intent);
	}
	/**
	 * 重新进入向导
	 */
	public void reEnterSetup(View view){
		Setup1Activity.actionStart(this);
		finish();
	}
	

}
