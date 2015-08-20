package com.xb.mobilesafe.utils;

import com.xb.mobilesafe.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
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
	  * 窗体管理者
	  */
	 private static WindowManager wm;
	 private static TextView tview;
	 private static Context context;
	 private static View view;
	 private static SharedPreferences sp;
	 static Point size = new Point();
	 /**
	  * 自定义显示
	  * @param str
	  */
	 @SuppressLint("NewApi")
	public static void myshow(String address,int length){
		 context  = MyApplication.getContext();
		 wm =(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		 myToast(address,length);
		wm.getDefaultDisplay().getSize(size);
	 }
	 
	private static  WindowManager.LayoutParams params;
	//"半透明","活力橙","卫士蓝","金属灰","苹果绿"
	final static int [] ids ={R.drawable.call_locate_white,R.drawable.call_locate_orange,R.drawable.call_locate_blue
		    ,R.drawable.call_locate_gray,R.drawable.call_locate_green};
	static long[] mHits = new long[2];
	 private static void myToast(String address,int length){
		    
		 sp = context.getSharedPreferences("config",Context.MODE_PRIVATE);
		 int phone_address_background = sp.getInt("phone_address_background",0);
		
		 view =View.inflate(context, R.layout.address_show, null);
		 view.setBackgroundResource(ids[phone_address_background]);
		 tview = (TextView) view.findViewById(R.id.tv_phone_address);
		 tview.setText(address);
		 
		 //双击居中
		 view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
				mHits[mHits.length - 1] = SystemClock.uptimeMillis();
				if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
					// 双击居中了。。。
					params.x = wm.getDefaultDisplay().getWidth()/2-view.getWidth()/2;
					wm.updateViewLayout(view, params);
					//Editor editor = sp.edit();
					//editor.putInt("lastx", params.x);
					//editor.commit();
				}
			}
		});
		 
		 //移动弹出的窗体
		 view.setOnTouchListener(new OnTouchListener() {
			int startX;//开始x
			int startY;//开始y
			
			@SuppressLint("NewApi")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN://手指按下
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE://手指移动
					int newX = (int) event.getRawX();
					int newY = (int) event.getRawY();
					int dx = newX - startX;
					int dy = newY - startY;
					
					//判断x,y的边界问题
					
					params.x+=dx;
					params.y+= dy;
					//垂直方向
					if(params.x<0){
						params.x=0;
					}
					if(params.y<0){
						params.y=0;
					}
			
					int x = size.x-view.getWidth();
					LogUtil.e("data", "x:"+x+",params.x:"+params.x);
					if(params.x>x){
						params.x= x;
					}
					int  y = size.y -v.getHeight();
					LogUtil.e("data", "y:"+y+",params.y:"+params.y);
					if(params.y>y){
						params.y=y;
					}
					
					wm.updateViewLayout(view, params);
					//重新初始化开始x,y
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_UP://手指离开
					//记录控件坐标
					//1.保存,2取值 3.显示
					//不做了
				default:
					break;
				}
				return false;// 事件处理完毕了。不要让父控件 父布局响应触摸事件了。
			}
		});
		 
		//窗体的参数就设置好了
		 params = new WindowManager.LayoutParams();
		 
         params.height = WindowManager.LayoutParams.WRAP_CONTENT;
         params.width = WindowManager.LayoutParams.WRAP_CONTENT;
         //没有焦点
         params.flags = 
        		 WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        		 //不能触摸
                // | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                 //不锁屏
                 |WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
         
         params.format = PixelFormat.TRANSLUCENT;
         //具有和电话一样权限的弹出窗体,需要添加权限
         params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
		 wm.addView(view, params);
	 }
	 
	 //移除自定义窗体
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
