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
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
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
import android.widget.TextView;

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
	
	/**
	 * ��ȡȫ���Ĺ����б�
	 */
	private void getFuntionItem() {
		HttpUtil.sendHttpRequest(getString(R.string.homeFuntionItemUrl), new HttpCallbackListener() {
			@Override
			public void onFinish(String response) {
				LogUtil.e(TAG, "<--�ɹ��ص�-->");
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
					ShowText.show("JSON��������");
				}
			}
			
			@Override
			public void onError(Exception e) {
				msg.what=ERROR;
				handler.sendMessage(msg);
				LogUtil.e(TAG, "�ص�ʧ��-->"+e.getMessage());
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
				//�������
				
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
		case 8://��������
			SettingActivity.actionStart(this);
			break;
		case 0://�����ֻ�����
			showLostFindDialog();
			break;
		default:
			break;
		}
	}
	
	private void showLostFindDialog(){
		//�ж��Ƿ����ù�����
		if(isSetPwd()){
			//���ù�����
			showEnterDialog();
		}else{
			//û�����ù�����
			showSetPwdDialog();
		}
	}
	/**
	 * ������������Ի���
	 */
	private void showEnterDialog() {
		View view = View.inflate(this, R.layout.dialog_enter_pwd, null);
		AlertDialog.Builder builder = new Builder(this);
		builder.setView(view);
		dialog = builder.create();
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
		getValues(view);
		//ȡ��
				bt_set_pwd_cancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				//ȷ��
				bt_set_pwd_ok.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						String password = et_set_password.getText().toString().trim();
						if(TextUtils.isEmpty(password)){
							ShowText.show("���벻��Ϊ��.");
							return ;
						}
						String pwd = sp.getString("password", null);
						//�������pwd Ϊ�����
						if(TextUtils.isEmpty(pwd)){
							dialog.dismiss();
							showLostFindDialog();
						}else if(pwd.equals(password)){
							LostFindActivity.actionStart(HomeActivity.this);
							dialog.dismiss();
							return;
						}else{
							ShowText.show("�������.");
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

	//ȷ����ť
	private Button bt_set_pwd_ok;
	//ȡ����ť
	private Button bt_set_pwd_cancel;
	//����
	private EditText et_set_password;
	//ȷ������
	private EditText et_re_password;
	
	private AlertDialog dialog;
	/**
	 * ������������ĶԻ���
	 */
	private void showSetPwdDialog() {
		
		View view = View.inflate(this, R.layout.dialog_set_pwd, null);
		AlertDialog.Builder builder = new Builder(this);
		
		dialog = builder.create();
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
		getValues(view);
		et_re_password   = (EditText) view.findViewById(R.id.et_re_password);
		//ȡ��
		bt_set_pwd_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		//ȷ��
		bt_set_pwd_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String password = et_set_password.getText().toString().trim();
				String repassword=et_re_password.getText().toString().trim();
				if(TextUtils.isEmpty(password)||TextUtils.isEmpty(repassword)){
					ShowText.show("���벻��Ϊ��.");
					return ;
				}
				if(password.equals(repassword)){
					Editor editor = sp.edit();
					editor.putString("password", password);
					editor.commit();
					ShowText.show("���óɹ�.");
					dialog.dismiss();
					
				}else{
					ShowText.show("�������벻һ��.");
					return ;
				}
				
			}
		});
	}

	/**
	 * �ж��Ƿ����ù�����
	 * @return ��������� ���� true,���򷵻�false
	 */
	private boolean isSetPwd(){
		String password = sp.getString("password", null);
		return !TextUtils.isEmpty(password);
	}
}