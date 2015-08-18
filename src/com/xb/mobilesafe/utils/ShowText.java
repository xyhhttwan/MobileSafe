package com.xb.mobilesafe.utils;

import com.xb.mobilesafe.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class ShowText{
	
	
	  public   static void show(String str,int length) {  
		  
		  shows(str,length);
	        
	    }  
	  
	  public static void show(String str){
		  shows(str,-1);
	  }
	  
	 private static void shows(String str,int length){
		 Context context = MyApplication.getContext();
		 if(-1!=length){
			  Toast.makeText(context,str, length).show();  
		  }else{
			  Toast.makeText(context, str, Toast.LENGTH_SHORT).show(); 
		  }
	 }
	 
	 
	 /**
	  * ���������
	  */
	 private static WindowManager wm;
	 private static TextView tview;
	 private static Context context;
	 private static View view;
	 private static SharedPreferences sp;
	 
	 /**
	  * �Զ�����ʾ
	  * @param str
	  */
	 public static void myshow(String address,int length){
		 context  = MyApplication.getContext();
		 wm =(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		 myToast(address,length);
		
		 
	 }
	 
	//"��͸��","������","��ʿ��","������","ƻ����"
	final static int [] ids ={R.drawable.call_locate_white,R.drawable.call_locate_orange,R.drawable.call_locate_blue
		    ,R.drawable.call_locate_gray,R.drawable.call_locate_green};
	 
	 private static void myToast(String address,int length){
		    
		 sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
		 int phone_address_background = sp.getInt("phone_address_background",0);
		
		 view =View.inflate(context, R.layout.address_show, null);
		 view.setBackgroundResource(ids[phone_address_background]);
		 tview = (TextView) view.findViewById(R.id.tv_phone_address);
		 tview.setText(address);
		//����Ĳ��������ú���
		 WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		 
         params.height = WindowManager.LayoutParams.WRAP_CONTENT;
         params.width = WindowManager.LayoutParams.WRAP_CONTENT;
         //û�н���
         params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        		 //���ܴ���
                 | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                 //������
                 | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
         
         params.format = PixelFormat.TRANSLUCENT;
         params.type = WindowManager.LayoutParams.TYPE_TOAST;
		 wm.addView(view, params);
	 }
	 
	 //�Ƴ��Զ��崰��
	 public static void RemoveToast(){
		 if(null!=wm){
			 if(null!=view){
				 wm.removeView(view);
				 wm =null;
				 tview=null;
				 view=null;
			 }
		 }
		
	 }

}
