package com.xb.mobilesafe.utils;

import com.xb.mobilesafe.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
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
	 
	private static  WindowManager.LayoutParams params;
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
		 
		 //�ƶ������Ĵ���
		 view.setOnTouchListener(new OnTouchListener() {
			int startX;//��ʼx
			int startY;//��ʼy
			
			@SuppressLint("NewApi")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN://��ָ����
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE://��ָ�ƶ�
					int newX = (int) event.getRawX();
					int newY = (int) event.getRawY();
					int dx = newX - startX;
					int dy = newY - startY;
					
					//�ж�x,y�ı߽�����
					
					params.x+=dx;
					params.y+= dy;
					//��ֱ����
					if(params.x<0){
						params.x=0;
					}
					if(params.y<0){
						params.y=0;
					}
					
					Point size = new Point();
					wm.getDefaultDisplay().getSize(size);
					LogUtil.e("size", "x:"+ size.x+"y:"+ size.y+"view:"+view.getWidth());
					int x = size.x-view.getWidth();
					if(params.x>x){
						params.x= x;
					}
					int  y = size.y -v.getHeight();
					if(params.y>y){
						params.y=y;
					}
					wm.updateViewLayout(view, params);
					//���³�ʼ����ʼx,y
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_UP://��ָ�뿪
					//��¼�ؼ�����
					//1.����,2ȡֵ 3.��ʾ
					//������
				default:
					break;
				}
				return false;// �¼���������ˡ���Ҫ�ø��ؼ� ��������Ӧ�����¼��ˡ�
			}
		});
		 
		//����Ĳ��������ú���
		 params = new WindowManager.LayoutParams();
		 
         params.height = WindowManager.LayoutParams.WRAP_CONTENT;
         params.width = WindowManager.LayoutParams.WRAP_CONTENT;
         //û�н���
         params.flags = 
        		 WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        		 //���ܴ���
                // | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                 //������
                 |WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
         
         params.format = PixelFormat.TRANSLUCENT;
         //���к͵绰һ��Ȩ�޵ĵ�������,��Ҫ���Ȩ��
         params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
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
