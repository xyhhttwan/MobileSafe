package com.xb.mobilesafe.activity;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import com.xb.mobilesafe.R;
import com.xb.mobilesafe.utils.HttpCallbackListener;
import com.xb.mobilesafe.utils.HttpUtil;
import com.xb.mobilesafe.utils.LogUtil;
import com.xb.mobilesafe.utils.ShowText;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

public class SplashActivity extends Activity {

	private final static String TAG="SplashActivity";
	private TextView tv_splash_version;
	//版本号
	private String version;
	//描述
	private String description;
	//下载地址
	private String apkurl;
	//是否强制升级
	private boolean isForcedUpdate;
	
	private TextView tv_uplaod_process;
	
	private static final int SHOW_UPDATE_DIALOG=0;
	
	private static final int ENTER_HOME=1;
	
	private static final int CHECK_ERROR=2;
	
	private static final int JSON_ERROR=3;
	
	Message msg ;
	
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		//设置版本号
		tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
		tv_splash_version.setText(getString(R.string.version)+getVersionName());
		tv_uplaod_process = (TextView) findViewById(R.id.tv_uplaod_process);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		boolean update = sp.getBoolean("auto_update", false);
		if(update){
			//检查版本信息
			checkUpdate();
		}else
		{
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					enterHome();
				}
			}, 2000);
		}
		
		//动画效果 类似荷塘月色
		AlphaAnimation aa = new AlphaAnimation(0.2f, 1.0f);
		aa.setDuration(800);
		findViewById(R.id.rl_root_splash).startAnimation(aa);
	}
	
	/**
	 * 显示升级对话框
	 */
	private void showUpdateDialog(){
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("新版本升级");
		//强制升级
		if(isForcedUpdate){
			builder.setCancelable(false);
		}else{
			builder.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					enterHome();
					dialog.dismiss();
				}
			});
		}
		
		
		builder.setMessage(description);
		builder.setPositiveButton("立刻升级", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//下载apk
				//判断SDK是否存在
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
					//存在afinal下载
					String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/mobilesafe2.0.apk";
					upload(apkurl,path);
				}else{
					
					//不存在
					ShowText.show("没有检测到sdcard,请先安装后重试.");
					return ;
				}
			}
		});
		builder.setNegativeButton("下次再说", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				enterHome();
			}
		});
		builder.show();
	}	
	/**
	 * 检查版本更新
	 */
	private void checkUpdate() {
		msg=Message.obtain();
		final long startTime  = System.currentTimeMillis();
		
		HttpUtil.sendHttpRequest(getString(R.string.seviverurl),new HttpCallbackListener() {
			@Override
			public void onFinish(String response) {
				
				LogUtil.e(TAG, "回调成功."+response);
				try {
					JSONObject obj = new JSONObject(response);
					version = (String) obj.get("version");
					description = (String) obj.get("description");
					apkurl = (String) obj.get("apkurl");
					isForcedUpdate = obj.getBoolean("isForcedUpdate");
					//没有新版本
					if(getVersionName().equals(version)){
						msg.what = ENTER_HOME;
					}else
					{
						msg.what = SHOW_UPDATE_DIALOG;
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
					LogUtil.e(TAG, e.getMessage());
					msg.what = JSON_ERROR;
				}
				long endTime =System.currentTimeMillis();
				ThreadTime(startTime,endTime);
				handler.sendMessage(msg);
			}
			
			@Override
			public void onError(Exception e) {
				msg.what = CHECK_ERROR;
				long endTime =System.currentTimeMillis();
				ThreadTime(startTime,endTime);
				handler.sendMessage(msg);
			}
			
		});
	}
	
	private void ThreadTime(long startTime,long endTime){
		long dtime = endTime-startTime;
		if(dtime<2000){
			try {
				Thread.sleep(2000-dtime);
			} catch (InterruptedException e) {
				e.printStackTrace();
				Log.e(TAG, e.getMessage());
			}
		}
	}

	/**
	 * 得到版本号
	 * @return String
	 */
	private String getVersionName(){
		//管理手机apk
		PackageManager pm = getPackageManager();
		//得到指定apk的功能清单文件
		try {
			return pm.getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			LogUtil.e(TAG, e.getMessage());
		}
		return "";
	}
	
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			//新版本
			case SHOW_UPDATE_DIALOG:
				LogUtil.e(TAG, "开始升级......");
				showUpdateDialog();
				break;
			case CHECK_ERROR:
				ShowText.show("网络异常");
				enterHome();
				break;
			case ENTER_HOME:
				enterHome();
				break;
			case JSON_ERROR:
				ShowText.show("json解析出错.");
				enterHome();
				break;
			default:
				enterHome();
				break;
			}
		}
		
	};
	/**
	 * 进入home activity
	 */
	private void enterHome(){
		HomeActivity.actionStart(SplashActivity.this);
		finish();
	}
	
	private  void upload(String url,String path){
		FinalHttp finalHttp = new FinalHttp();
		
		finalHttp.download(url, path, new AjaxCallBack<File>() {
			
			//下载失败
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				ShowText.show("下载失败");
				LogUtil.e(TAG, strMsg);
				super.onFailure(t, errorNo, strMsg);
			}
			//进度
			@Override
			public void onLoading(long count, long current) {
				super.onLoading(count, current);
				Log.e(TAG, "onLoading-->"+count+","+current);
				int process =(int) (current*100/count);
				tv_uplaod_process.setVisibility(View.VISIBLE);
				tv_uplaod_process.setText("当前进度:"+process+"%");
				
			}
			
			//成功
			@Override
			public void onSuccess(File t) {
				tv_uplaod_process.setVisibility(View.GONE);
				super.onSuccess(t);
				installAPK(t);
				
			}
			
			/**
			 * 安装APK
			 * @param t
			 */
			private void installAPK(File t) {
			  Intent intent = new Intent();
			  intent.setAction("android.intent.action.VIEW");
			  intent.addCategory("android.intent.category.DEFAULT");
			  intent.setDataAndType(Uri.fromFile(t), "application/vnd.android.package-archive");
			  startActivity(intent);
			  
			}
			  
		});
	}
	
}
