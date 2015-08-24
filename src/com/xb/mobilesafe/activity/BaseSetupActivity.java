package com.xb.mobilesafe.activity;

import com.xb.mobilesafe.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

public abstract class BaseSetupActivity extends Activity {
	
	//1.0定义一个手势识别器
	private GestureDetector detector;
	
	protected SharedPreferences sp;
	
	//2.0实现处理事件
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		//实例化手势识别器
		detector = new GestureDetector(this,new SimpleOnGestureListener(){

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
				
				//Y轴滑动大于100就认为是竖滑动了 不进行任何操作
				if(Math.abs(e1.getRawY()-e2.getRawY())>100){
					return true;
				}
				//屏蔽滑动太慢
				if(Math.abs(velocityX)<200){
					return true;
				}
				//显示下一个页面：从右往左滑动
				if((e1.getRawX()-e2.getRawX())>200){
					showNext();
					return true;
				}
				//显示上一个页面从左往右滑
				else if((e2.getRawX()-e1.getRawX())>200){
					showPre();
					return true;
				}
				return super.onFling(e1, e2, velocityX, velocityY);
			}
				
			
			});
		}
	
	//3.重写onTouch事件
	//交由手势探测接口处理触摸事件
	public boolean onTouchEvent(MotionEvent event) {
		
			return detector.onTouchEvent(event);
	}
	//下一步
	public abstract void showNext();
	//上一步
	public abstract void showPre();
	
	/**
	 * 下一步点击事件
	 * @param view
	 */
	public  void next(View view){
		showNext();
	}
	
	public void pre(View view){
		showPre();
	}
	
	//finish 后执行或者startActivity之后执行
	protected void ShowNextOverPendingAnim(){
		overridePendingTransition(R.anim.tra_step_in, R.anim.tra_step_out);
	}
	protected void ShowProOverPendingAnim(){
		overridePendingTransition(R.anim.tra_step_pre_in, R.anim.tra_step_pre_out);
	}

	
}
