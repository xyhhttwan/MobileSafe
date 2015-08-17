package com.xb.mobilesafe.activity;

import com.xb.mobilesafe.R;
import com.xb.mobilesafe.utils.LogUtil;
import com.xb.mobilesafe.utils.ShowText;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

public class Setup3Activity extends BaseSetupActivity {

	private static final String TAG = "Setup3Activity";
	private EditText et_phone;
	
	private String phone;	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
		et_phone = (EditText) findViewById(R.id.et_phone);
		
		if(!TextUtils.isEmpty(phone = sp.getString("safePhone", ""))){
			et_phone.setText(phone);
		}
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
		if(TextUtils.isEmpty(et_phone.getText())){
			ShowText.show("安全号码不能为空.");
			return;
		}
		Setup4Activity.actionStart(this);
		finish();
		ShowNextOverPendingAnim();
		Editor editor  = sp.edit();
		editor.putString("safePhone",phone);
		editor.commit();
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
	
	/**
	 * 回调
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 0:
			if(resultCode==RESULT_OK){
				phone = data.getStringExtra("phone");
				et_phone.setText(phone);
				LogUtil.e(TAG, "phone:-->"+phone);
			}
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
