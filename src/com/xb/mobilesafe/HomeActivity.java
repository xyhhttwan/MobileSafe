package com.xb.mobilesafe;

import org.json.JSONException;
import org.json.JSONObject;

import com.xb.mobilesafe.adapter.HomeItemAdapter;
import com.xb.mobilesafe.utils.HttpCallbackListener;
import com.xb.mobilesafe.utils.HttpUtil;
import com.xb.mobilesafe.utils.LogUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.GridView;

public class HomeActivity extends ActionBarActivity {
	
	protected static final String TAG = "HomeActivity";

	private GridView gv_function_item;
	
	private String itemArray[];
    
	private HomeItemAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		gv_function_item = (GridView) findViewById(R.id.gv_function_item);
		
		getFuntionItem();
		adapter = new HomeItemAdapter();
		gv_function_item.setAdapter(adapter);
		
	}
	
	/**
	 * 获取全部的功能列表
	 */
	private void getFuntionItem() {
		HttpUtil.sendHttpRequest(getString(R.string.homeFuntionItemUrl), new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				LogUtil.e(TAG, "成功回调-->"+response);
				try {
					JSONObject obj = new JSONObject(response);
					//
				} catch (JSONException e) {
					e.printStackTrace();
					LogUtil.e(TAG, e.getMessage());
				}
			}
			
			@Override
			public void onError(Exception e) {
				LogUtil.e(TAG, "回调失败-->"+e.getMessage());
			}
		});
	}

	public static void actionStart(Context context){
		Intent intent = new Intent(context,HomeActivity.class);
		context.startActivity(intent);
	}

}
