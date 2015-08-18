package com.xb.mobilesafe.activity;

import com.xb.mobilesafe.R;
import com.xb.mobilesafe.db.dao.NumberAddressQuery;
import com.xb.mobilesafe.utils.ShowText;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
/**
 * 号码归属地查询
 * @author baixb
 *
 */
public class NumberAddressQueryActivity extends Activity {
	
	private EditText ed_phone;
	
	private TextView result;
	//震动服务
	private Vibrator vibrator;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
 		setContentView(R.layout.activity_number_addres_query);
 		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
 		ed_phone = (EditText) findViewById(R.id.ed_phone);
 		result = (TextView) findViewById(R.id.result);
 		ed_phone.addTextChangedListener(new TextWatcher() {
			
 			/**
 			 * 文本发生变化回调
 			 * @param s
 			 * @param start
 			 * @param before
 			 * @param count
 			 */
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s!=null && s.length()>=3){
					//查询数据库显示结果
					String address  = NumberAddressQuery.queryNumber(s.toString());
					result.setText(address);
				}
			}
			/**
			 * 文本发生变化之前回调
			 * @param s
			 * @param start
			 * @param count
			 * @param after
			 */
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				
			}
			
			/**
			 * 文本发生变化之后回调
			 * @param s
			 */
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
 		
	}
	public static void actionStart(Context content){
		Intent intent = new Intent(content,NumberAddressQueryActivity.class);
		content.startActivity(intent);
	}
	
	/**
	 * 点击查询事件
	 * @param view
	 */
	public void numberAddressQuery(View view){
	
		String phone  = ed_phone.getText().toString();
		if(TextUtils.isEmpty(phone)){
			ShowText.show("查询内容不能为空");
			//当电话为空 开始震动
			vibrator.vibrate(200);
			//-1不重复
			long pattern[] ={200,200,300,300,100,200};
			vibrator.vibrate(pattern, -1);
			Animation animation =AnimationUtils.loadAnimation(this,R.anim.shake);
			ed_phone.startAnimation(animation);
			return;
		}else{
			//查询数据库
			String address = NumberAddressQuery.queryNumber(phone);
			result.setText(address);
		}
	}

}
