package com.xb.mobilesafe.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xb.mobilesafe.R;
import com.xb.mobilesafe.db.dao.BlackNumberDao;
import com.xb.mobilesafe.ui.adapter.BlackNumberItemAdapter;
import com.xb.mobilesafe.utils.LogUtil;
import com.xb.mobilesafe.utils.ShowText;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 通讯卫士
 * @author baixb
 *
 */
public class CallSmsSafeActivity extends Activity implements OnItemClickListener {
	
	private static final String TAG = "CallSmsSafeActivity";

	private BlackNumberItemAdapter blackNumberItemAdapter;
    
	private ListView lv_call_sms_safe;
	
	private TextView tv_no_black_number;
	
	private BlackNumberDao dao;
	
	List<Map<String, Object>> list  ;
	
	private LinearLayout pb_process;
	
	private  int offset=0;
	private  int maxNo=10;
	
	private int counts =0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_sms_safe);
		lv_call_sms_safe = (ListView) findViewById(R.id.lv_call_sms_safe);
		tv_no_black_number = (TextView) findViewById(R.id.tv_no_black_number);
		
		dao = new BlackNumberDao();
		lv_call_sms_safe.setOnItemClickListener(this);
		
		//等待条
		pb_process = (LinearLayout) findViewById(R.id.pb_process);

		fillData();
		
		counts = dao.getCounts();
		LogUtil.e(TAG, "counts:"+counts);
		tv_no_black_number.setText("共"+counts+"个黑名单");
		
		
		//listView 滚动事件
		lv_call_sms_safe.setOnScrollListener(new OnScrollListener() {
			
			//滚动状态发生变化的时候调用
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				int lastposition = lv_call_sms_safe.getLastVisiblePosition();
				//滑动到最后一条数据了
				if(lastposition==(list.size()-1)){
					offset += maxNo;
					fillData();
				}
			}
			//滚动的时候调用
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				
			}
		});
		
		
	
		
	}

	private void fillData() {
		pb_process.setVisibility(View.VISIBLE);
		new Thread(){
			public void run() {
				if(null ==list){
					list = dao.getBlankNumberByPager(offset, maxNo);
				}else{
					list.addAll(dao.getBlankNumberByPager(offset, maxNo));
				}
				
				runOnUiThread(new Runnable() {
					public void run() {
						if(null==blackNumberItemAdapter){
							blackNumberItemAdapter = new BlackNumberItemAdapter
									(CallSmsSafeActivity.this,dao,list,handler);
							lv_call_sms_safe.setAdapter(blackNumberItemAdapter);
						}else{
							
							blackNumberItemAdapter.notifyDataSetChanged();
						}
						pb_process.setVisibility(View.INVISIBLE);
					}
				});
			}
			;
		}.start();
	}
	
	public static void actionStart(Context context){
		Intent  intent = new Intent(context,CallSmsSafeActivity.class);
		context.startActivity(intent);
	}
	
	
	private AlertDialog dialog;
	
	private Button cance;
	
	private Button ok;
	
	private EditText et_blacknumber;
	
	private CheckBox cb_phone;
	
	private CheckBox cb_sms;
	
	private View views;
	/**
	 * 新增黑名单点击事件
	 * @param view
	 */
	public void addBlanckNumber(View view ){
		add(0);
	}
	
	private void add(final int type){
		
		views  = View.inflate(this, R.layout.dialog_add_blacknumber,null);
		AlertDialog.Builder builder = new Builder(this);
		builder.setView(views);
		dialog = builder.create();
		dialog.setView(views, 0, 0, 0, 0);
		dialog.show();
		getValues(views);
		ok = (Button) views.findViewById(R.id.ok);
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
					if(saveBlackNum(views,type)){
						dialog.dismiss();
					}
				}
		});
		cance = (Button) views.findViewById(R.id.cancel);
		cance.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}
	
	
	private void getValues(View view){
		et_blacknumber = (EditText) view.findViewById(R.id.et_blacknumber);
		cb_phone       = (CheckBox) view.findViewById(R.id.cb_phone);
		cb_sms         = (CheckBox) view.findViewById(R.id.cb_sms);
	}
	private boolean saveBlackNum(View view,int type){
		
		String number  = et_blacknumber.getText().toString();
		boolean  phone = cb_phone.isChecked();
		boolean  sms   = cb_sms.isChecked();
		
		int  mode=0;
		if(TextUtils.isEmpty(number)){
			ShowText.show("号码不能为空.",0);
			return false;
		}
		
		if(!phone &&!sms){
			ShowText.show("请选择拦截模式",0);
			return false;
		}
		
		if(phone && sms){
			mode = 2;
		}
		else if(phone && (!sms)){
			mode=0;
		}
		else if((!phone) && sms){
			mode=1;
		}
		
		String msg =(type==0?"添加":"更新");
		try {

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("number", number);
			map.put("name",null);
			map.put("mode",mode);
			map.put("id", id);
			if(type==0){
				dao.addBlackNumber(number, null, mode);
				list.add(0, map);
			}else
			{
				dao.update(number,mode,id);
				list.set(postion, map);
			}
			adapterChage();
			ShowText.show(msg+"黑名单成功");
		} catch (Exception e) {
			e.printStackTrace();
			ShowText.show(msg+"黑名单失败");
			LogUtil.e(TAG, "addBlackNumber error:"+e.getMessage());
			return false;
		}
		
		return true;
	}
	
	private final int ADAPTER_CHAGE=110;
	@SuppressLint("HandlerLeak")
	public  Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			case ADAPTER_CHAGE:
				adapterChage();
				break;
			default:
				break;
			}
		}
		
	};
	public  void  adapterChage() {
		blackNumberItemAdapter.notifyDataSetChanged();
		counts = dao.getCounts();
		tv_no_black_number.setText("共"+counts+"个黑名单");
	}
	
	private  int id;
	private  int postion;
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		add(1);
		Map<String, Object> map = list.get(position);
		String number  = map.get("number").toString();
		String mode = map.get("mode").toString();
		et_blacknumber.setText(number);
		if(mode.equals("0"))
			cb_phone.setChecked(true);
		else if(mode.equals("1"))
			cb_sms.setChecked(true);
		else{
			cb_phone.setChecked(true);
			cb_sms.setChecked(true);
		}
		this.id = Integer.parseInt(map.get("id").toString());
		this.postion = position;
	}
	
	
	

}
