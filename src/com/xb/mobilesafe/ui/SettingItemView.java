package com.xb.mobilesafe.ui;

import com.xb.mobilesafe.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingItemView extends RelativeLayout  {
	
	//ѡ��״̬
	private CheckBox cb_setting_checked;
	
	private TextView tv_setting_info;
	
	private TextView tv_setting_auto_update;
	
	private  String desc_on,desc_off;
	/**
	 * ��ʼ�������ļ� �Ѳ����ļ����ص� SettingItemView��
	 * ������� ����textView һ��checkBox ,һ��View
	 * @param context
	 */
	private void initView(Context context){
		View.inflate(context, R.layout.setting_item_view, this);
		cb_setting_checked = (CheckBox) findViewById(R.id.cb_setting_checked);
		tv_setting_info    = (TextView) findViewById(R.id.tv_setting_info);
		tv_setting_auto_update = (TextView) findViewById(R.id.tv_setting_auto_update);
		
	}
	
	@SuppressLint("NewApi")
	public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		initView(context);
	}

	public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}

	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
		
		String res ="http://schemas.android.com/apk/res/com.xb.mobilesafe";
		tv_setting_auto_update.setText(
				context.getString(
						attrs.getAttributeResourceValue(
								res, "mtitle",R.string.auto_update)
						)
				);
		desc_on= context.getString(
				attrs.getAttributeResourceValue(
						res, "desc_on",R.string.auto_update_open
						)
				);
		desc_off = context.getString(
				attrs.getAttributeResourceValue(
						res, "desc_off",R.string.auto_update_open
						)
				);
		setDes(desc_off);
	}

	public SettingItemView(Context context) {
		super(context);
		initView(context);
	}
	
	/**
	 * �ж��Ƿ�ѡ��
	 * @return boolean
	 */
	public boolean isChecked(){
		return cb_setting_checked.isChecked();
	}
	
	/**
	 * ����ѡ��״̬
	 * @param checked
	 */
	public void setChecked(boolean checked){
		if(checked){
			setDes(desc_on);
		}else{
			setDes(desc_off);
		}
		cb_setting_checked.setChecked(checked);
	}
	
	/**
	 * ��������
	 */
	public void setDes(String text){
		tv_setting_info.setText(text);
	}
	
	
}
