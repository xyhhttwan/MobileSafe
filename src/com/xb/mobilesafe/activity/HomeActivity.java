package com.xb.mobilesafe.activity;

import java.util.ArrayList;
import java.util.List;

import com.xb.mobilesafe.R;
import com.xb.mobilesafe.entity.HomeFuntionItem;
import com.xb.mobilesafe.ui.adapter.HomeItemAdapter;
import com.xb.mobilesafe.utils.LogUtil;
import com.xb.mobilesafe.utils.ShowText;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

public class HomeActivity extends Activity implements OnItemClickListener{
	
	protected static final String TAG = "HomeActivity";

	protected static final int ERROR = 0;

	protected static final int SUCCESS = 1;

	private GridView gv_function_item;
	
	private List<HomeFuntionItem> list;
    
	private HomeItemAdapter adapter;
	
	private Message msg;
	
	private SharedPreferences sp;
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		gv_function_item = (GridView) findViewById(R.id.gv_function_item);
		gv_function_item.setOnItemClickListener(this);
		msg = new Message();
		getFuntionItem();
		sp = getSharedPreferences("config", MODE_PRIVATE);
		
	}
	
	String   result ="["
			+"{\"id\":1,\"name\":\"手机防盗\",\"code\":\"safe\"},"+
			"{\"id\":2,\"name\":\"通讯卫士\",\"code\":\"callmsgsafe\"},"+
			"{\"id\":3,\"name\":\"软件管理\",\"code\":\"app\"},"+
			"{\"id\":4,\"name\":\"进程管理\",\"code\":\"taskmanager\"},"+
			"{\"id\":5,\"name\":\"流量统计\",\"code\":\"netmanager\"},"+
			"{\"id\":6,\"name\":\"缓存清理\",\"code\":\"sysoptimize\"},"+
			"{\"id\":7,\"name\":\"高级工具\",\"code\":\"atools\"},"+
			"{\"id\":8,\"name\":\"手机杀毒\",\"code\":\"trojan\"},"+
			"{\"id\":9,\"name\":\"设置中心\",\"code\":\"settings\"}]";
	
	/**
	 * 获取全部的功能列表
	 */
	private void getFuntionItem() {
		
		list = new ArrayList<HomeFuntionItem>();
		HomeFuntionItem  homeitem = new HomeFuntionItem();
		homeitem.setCode("safe");
		homeitem.setId(1);
		homeitem.setName("手机防盗");
		list.add(homeitem);
		homeitem = new HomeFuntionItem();
		homeitem.setCode("callmsgsafe");
		homeitem.setId(2);
		homeitem.setName("通讯卫士");
		list.add(homeitem);
		
		homeitem = new HomeFuntionItem();
		homeitem.setCode("app");
		homeitem.setId(3);
		homeitem.setName("软件管理");
		list.add(homeitem);
		homeitem = new HomeFuntionItem();
		homeitem.setCode("taskmanager");
		homeitem.setId(4);
		homeitem.setName("进程管理");
		list.add(homeitem);
		homeitem = new HomeFuntionItem();
		homeitem.setCode("netmanager");
		homeitem.setId(5);
		homeitem.setName("流量统计");
		list.add(homeitem);
		homeitem = new HomeFuntionItem();
		homeitem.setCode("sysoptimize");
		homeitem.setId(6);
		homeitem.setName("缓存清理");
		list.add(homeitem);
		homeitem = new HomeFuntionItem();
		homeitem.setCode("atools");
		homeitem.setId(7);
		homeitem.setName("高级工具");
		list.add(homeitem);
		homeitem = new HomeFuntionItem();
		homeitem.setCode("trojan");
		homeitem.setId(8);
		homeitem.setName("手机杀毒");
		list.add(homeitem);
		homeitem = new HomeFuntionItem();
		homeitem.setCode("settings");
		homeitem.setId(9);
		homeitem.setName("设置中心");
		list.add(homeitem);
		
		msg.what=SUCCESS;
		handler.sendMessage(msg);
		
		
	/*	HttpUtil.sendHttpRequest(getString(R.string.homeFuntionItemUrl), new HttpCallbackListener() {
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
				try {
					JSONArray jsonArray = new JSONArray(result);
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
				} catch (JSONException ex) {
					ex.printStackTrace();
					LogUtil.e(TAG, ex.getMessage());
					ShowText.show("JSON解析出错");
				}
				msg.what=ERROR;
				handler.sendMessage(msg);
				LogUtil.e(TAG, "回调失败-->"+e.getMessage());
			}
		});*/
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
				LogUtil.e(TAG,list.size()+"");
				adapter = new HomeItemAdapter(HomeActivity.this,list);
				gv_function_item.setAdapter(adapter);
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
		case 8://进入设置
			SettingActivity.actionStart(this);
			break;
		case 0://进入手机防盗
			showLostFindDialog();
			break;
		case 1://通讯卫士
			CallSmsSafeActivity.actionStart(this);
			break;
		case 6://高级工具
			AtoolsActivity.actionStart(this);
			break;
		default:
			break;
		}
	}
	
	private void showLostFindDialog(){
		//判断是否设置过密码
		if(isSetPwd()){
			//设置过密码
			showEnterDialog();
		}else{
			//没有设置过密码
			showSetPwdDialog();
		}
	}
	/**
	 * 弹出输入密码对话窗
	 */
	private void showEnterDialog() {
		View view = View.inflate(this, R.layout.dialog_enter_pwd, null);
		AlertDialog.Builder builder = new Builder(this);
		builder.setView(view);
		dialog = builder.create();
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
		getValues(view);
		//取消
				bt_set_pwd_cancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				//确定
				bt_set_pwd_ok.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						String password = et_set_password.getText().toString().trim();
						if(TextUtils.isEmpty(password)){
							ShowText.show("密码不能为空.");
							return ;
						}
						String pwd = sp.getString("password", null);
						//如果出现pwd 为空情况
						if(TextUtils.isEmpty(pwd)){
							dialog.dismiss();
							showLostFindDialog();
						}else if(pwd.equals(password)){
							LostFindActivity.actionStart(HomeActivity.this);
							dialog.dismiss();
							return;
						}else{
							ShowText.show("密码错误.");
							return ;
						}
						
					}
				});
	}
	
	private void getValues(View view){
		bt_set_pwd_ok     = (Button) view.findViewById(R.id.bt_set_pwd_ok);
		bt_set_pwd_cancel = (Button) view.findViewById(R.id.bt_set_pwd_cancel);
		et_set_password   = (EditText) view.findViewById(R.id.et_set_password);
	}

	//确定按钮
	private Button bt_set_pwd_ok;
	//取消按钮
	private Button bt_set_pwd_cancel;
	//密码
	private EditText et_set_password;
	//确认密码
	private EditText et_re_password;
	
	private AlertDialog dialog;
	/**
	 * 弹出设置密码的对话框
	 */
	private void showSetPwdDialog() {
		
		View view = View.inflate(this, R.layout.dialog_set_pwd, null);
		AlertDialog.Builder builder = new Builder(this);
		
		dialog = builder.create();
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
		getValues(view);
		et_re_password   = (EditText) view.findViewById(R.id.et_re_password);
		//取消
		bt_set_pwd_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		//确定
		bt_set_pwd_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String password = et_set_password.getText().toString().trim();
				String repassword=et_re_password.getText().toString().trim();
				if(TextUtils.isEmpty(password)||TextUtils.isEmpty(repassword)){
					ShowText.show("密码不能为空.");
					return ;
				}
				if(password.equals(repassword)){
					Editor editor = sp.edit();
					editor.putString("password", password);
					editor.commit();
					ShowText.show("设置成功.");
					dialog.dismiss();
					
				}else{
					ShowText.show("两次密码不一致.");
					return ;
				}
				
			}
		});
	}

	/**
	 * 判断是否设置过密码
	 * @return 如果设置了 返回 true,否则返回false
	 */
	private boolean isSetPwd(){
		String password = sp.getString("password", null);
		return !TextUtils.isEmpty(password);
	}
}
