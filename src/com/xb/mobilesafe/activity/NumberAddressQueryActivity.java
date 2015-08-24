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
 * ��������ز�ѯ
 * @author baixb
 *
 */
public class NumberAddressQueryActivity extends Activity {
	
	private EditText ed_phone;
	
	private TextView result;
	//�𶯷���
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
 			 * �ı������仯�ص�
 			 * @param s
 			 * @param start
 			 * @param before
 			 * @param count
 			 */
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s!=null && s.length()>=3){
					//��ѯ���ݿ���ʾ���
					String address  = NumberAddressQuery.queryNumber(s.toString());
					result.setText(address);
				}
			}
			/**
			 * �ı������仯֮ǰ�ص�
			 * @param s
			 * @param start
			 * @param count
			 * @param after
			 */
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				
			}
			
			/**
			 * �ı������仯֮��ص�
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
	 * �����ѯ�¼�
	 * @param view
	 */
	public void numberAddressQuery(View view){
	
		String phone  = ed_phone.getText().toString();
		if(TextUtils.isEmpty(phone)){
			ShowText.show("��ѯ���ݲ���Ϊ��");
			//���绰Ϊ�� ��ʼ��
			vibrator.vibrate(200);
			//-1���ظ�
			long pattern[] ={200,200,300,300,100,200};
			vibrator.vibrate(pattern, -1);
			Animation animation =AnimationUtils.loadAnimation(this,R.anim.shake);
			ed_phone.startAnimation(animation);
			return;
		}else{
			//��ѯ���ݿ�
			String address = NumberAddressQuery.queryNumber(phone);
			result.setText(address);
		}
	}

}
