package com.xb.mobilesafe.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.xb.mobilesafe.R;
import com.xb.mobilesafe.entity.HomeFuntionItem;
import com.xb.mobilesafe.ui.adapter.HomeItemAdapter;
import com.xb.mobilesafe.utils.HttpCallbackListener;
import com.xb.mobilesafe.utils.HttpUtil;
import com.xb.mobilesafe.utils.LogUtil;
import com.xb.mobilesafe.utils.ShowText;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class HomeActivity extends Activity implements OnItemClickListener{
	
	protected static final String TAG = "HomeActivity";

	protected static final int ERROR = 0;

	protected static final int SUCCESS = 1;

	private GridView gv_function_item;
	
	private List<HomeFuntionItem> list;
    
	private HomeItemAdapter adapter;
	
	private Message msg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		gv_function_item = (GridView) findViewById(R.id.gv_function_item);
		gv_function_item.setOnItemClickListener(this);
		msg = new Message();
		getFuntionItem();
		
	}
	
	/**
	 * 获取全部的功能列表
	 */
	private void getFuntionItem() {
		HttpUtil.sendHttpRequest(getString(R.string.homeFuntionItemUrl), new HttpCallbackListener() {
			@Override
			public void onFinish(String response) {
				LogUtil.e(TAG, "<--成功回调-->");
				try {
					JSONArray jsonArray = new JSONArray(response);
					HomeFuntionItem homeitem;
					list = new ArrayList<HomeFuntionItem>();
					for(int i =0;i<jsonArray.length();i++){
						Object item = jsonArray.get(i);
						JSONObject obj = new JSONObject(item.toString());
						homeitem = new HomeFuntionItem();
						homeitem.setId(obj.getInt("id"));
						homeitem.setName(obj.getString("name"));
						homeitem.setCode(obj.getString("code"));
						list.add(homeitem);
					}
					msg.what=SUCCESS;
					handler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
					LogUtil.e(TAG, e.getMessage());
					ShowText.show("JSON解析出错");
				}
			}
			
			@Override
			public void onError(Exception e) {
				msg.what=ERROR;
				handler.sendMessage(msg);
				LogUtil.e(TAG, "回调失败-->"+e.getMessage());
			}
		});
	}
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SUCCESS:
				adapter = new HomeItemAdapter(HomeActivity.this,list);
				gv_function_item.setAdapter(adapter);
				break;
			case ERROR:
				//错误操作
				
				break;
			default:
				break;
			}
		}
			
	};

	public static void actionStart(Context context){
		Intent intent = new Intent(context,HomeActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		switch (position) {
		case 8:
			SettingActivity.actionStart(this);
			break;
		default:
			break;
		}
	}

}
