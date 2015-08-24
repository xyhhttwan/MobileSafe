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
	
	//1.0����һ������ʶ����
	private GestureDetector detector;
	
	protected SharedPreferences sp;
	
	//2.0ʵ�ִ����¼�
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		//ʵ��������ʶ����
		detector = new GestureDetector(this,new SimpleOnGestureListener(){

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
				
				//Y�Ử������100����Ϊ���������� �������κβ���
				if(Math.abs(e1.getRawY()-e2.getRawY())>100){
					return true;
				}
				//���λ���̫��
				if(Math.abs(velocityX)<200){
					return true;
				}
				//��ʾ��һ��ҳ�棺�������󻬶�
				if((e1.getRawX()-e2.getRawX())>200){
					showNext();
					return true;
				}
				//��ʾ��һ��ҳ��������һ�
				else if((e2.getRawX()-e1.getRawX())>200){
					showPre();
					return true;
				}
				return super.onFling(e1, e2, velocityX, velocityY);
			}
				
			
			});
		}
	
	//3.��дonTouch�¼�
	//��������̽��ӿڴ������¼�
	public boolean onTouchEvent(MotionEvent event) {
		
			return detector.onTouchEvent(event);
	}
	//��һ��
	public abstract void showNext();
	//��һ��
	public abstract void showPre();
	
	/**
	 * ��һ������¼�
	 * @param view
	 */
	public  void next(View view){
		showNext();
	}
	
	public void pre(View view){
		showPre();
	}
	
	//finish ��ִ�л���startActivity֮��ִ��
	protected void ShowNextOverPendingAnim(){
		overridePendingTransition(R.anim.tra_step_in, R.anim.tra_step_out);
	}
	protected void ShowProOverPendingAnim(){
		overridePendingTransition(R.anim.tra_step_pre_in, R.anim.tra_step_pre_out);
	}

	
}
