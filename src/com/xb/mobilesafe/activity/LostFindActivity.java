package com.xb.mobilesafe.activity;

import com.xb.mobilesafe.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class LostFindActivity extends Activity {

	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//����Ƿ��һ�η��ʸ�ҳ��
		sp = getSharedPreferences("config", MODE_PRIVATE);
		boolean isVisit = sp.getBoolean("isVisit", false);
		if(isVisit){
			//�ֻ�����ҳ��
			setContentView(R.layout.activity_lost_find);
		}else{
			//��ҳ��
			Setup1Activity.actionStart(this);
			finish();
		}
	}
	
	public static void actionStart(Context context){
		Intent intent  = new Intent (context,LostFindActivity.class);
		context.startActivity(intent);
	}
	/**
	 * ���½�����
	 */
	public void reEnterSetup(View view){
		Setup1Activity.actionStart(this);
	}
	

}
