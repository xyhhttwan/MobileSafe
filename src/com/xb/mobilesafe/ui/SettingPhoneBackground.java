package com.xb.mobilesafe.ui;

import com.xb.mobilesafe.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingPhoneBackground extends RelativeLayout  {
	
	
	
	private TextView phone_address_show_style;
	
	private TextView phone_address_show_style_info;
	
	private  String desc_on,desc_off;
	/**
	 * 初始化布局文件 把布局文件加载到 SettingItemView中
	 * 里面包含 连个textView 一个checkBox ,一个View
	 * @param context
	 */
	private void initView(Context context){
		View.inflate(context, R.layout.choose_phone_address_show_background, this);
		phone_address_show_style    = (TextView) findViewById(R.id.tv_setting_phone_background);
		phone_address_show_style_info = (TextView) findViewById(R.id.tv_setting_phone_background_info);
		
	}
	
	@SuppressLint("NewApi")
	public SettingPhoneBackground(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		initView(context);
	}

	public SettingPhoneBackground(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}

	public SettingPhoneBackground(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
		String res ="http://schemas.android.com/apk/res/com.xb.mobilesafe";
		phone_address_show_style.setText(
				context.getString(
						attrs.getAttributeResourceValue(
								res, "mtitle",R.string.phone_address_show_style)
						)
				);
		desc_on= context.getString(
				attrs.getAttributeResourceValue(
						res, "desc_on",R.string.call_locate_blue
						)
				);
		desc_off = context.getString(
				attrs.getAttributeResourceValue(
						res, "desc_off",R.string.phone_address_show_style
						)
				);
		setDes(desc_off);
	}

	public SettingPhoneBackground(Context context) {
		super(context);
		initView(context);
	}
	
	/**
	 * 判断是否选中
	 * @return boolean
	 *//*
	public boolean isChecked(){
		//return cb_setting_checked.isChecked();
	}*/
	
	/**
	 * 设置选中状态
	 * @param checked
	 */
	public void setChecked(boolean checked){
		if(checked){
			setDes(desc_on);
		}else{
			setDes(desc_off);
		}
	}
	
	/**
	 * 设置描述
	 */
	public void setDes(String text){
		phone_address_show_style_info.setText(text);
	}
	
	
}
