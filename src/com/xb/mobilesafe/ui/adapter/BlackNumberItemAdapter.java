package com.xb.mobilesafe.ui.adapter;

import java.util.List;
import java.util.Map;

import com.xb.mobilesafe.R;
import com.xb.mobilesafe.db.dao.BlackNumberDao;
import com.xb.mobilesafe.utils.LogUtil;
import com.xb.mobilesafe.utils.ShowText;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ������Adapter
 * @author baixb
 *
 */
public class BlackNumberItemAdapter extends BaseAdapter {
	
	private static final String TAG = "BlackNumberItemAdapter";
	private Context context;
	private List<Map<String, Object>> list ;
	private BlackNumberDao dao;
	
	private Handler handler;
	
	public BlackNumberItemAdapter(Context context,BlackNumberDao dao, List<Map<String, Object>> objects,Handler handler) {
		this.context = context;
		this.list = objects;
		this.dao = dao;
		this.handler=handler;
	}
	
	private View view;
	private ViewHolder holder;
	private TextView number;
	private TextView mode;
	@SuppressLint("ViewHolder")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		//1.�����ڴ���view���󴴽��ĸ���
		if(null == convertView){
			holder = new ViewHolder();
			//�����ڴ��еĿ���
			view  = View.inflate(context,R.layout.list_item_black_number, null);
			//����Ѱ���Ӻ��ӵ�Ч��
			number  = (TextView) view.findViewById(R.id.tv_black_number);
			mode    = (TextView) view.findViewById(R.id.tv_block_mode);
			holder.iv_delete = (ImageView)view.findViewById(R.id.iv_delete);
			holder.number = number;
			holder.mode = mode;
			holder.view= view;
			view.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
			view = convertView;
		}
		Map<String, Object> map = list.get(position);
		Object phone = map.get("number");
		Object mode = map.get("mode");
		LogUtil.e(TAG, "�绰����:"+phone+"����ģʽ:"+getModeName(mode)+",mode:"+mode+",id:"+map.get("id"));
		holder.number.setText(phone.toString());
		holder.mode.setText(getModeName(mode));
		holder.iv_delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				AlertDialog.Builder builder = new Builder(context);
				builder.setTitle("����");
				builder.setMessage("ȷ��Ҫɾ���ú���?");
				builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Map<String, Object> map =list.get(position);
						try {
							dao.delete(map.get("number").toString());
							ShowText.show("ɾ���������ɹ�.");
							list.remove(position);
							Message message = new Message();
							message.what=110;
							handler.sendMessage(message);
						} catch (Exception e) {
							e.printStackTrace();
							LogUtil.e(TAG, "delete:"+e.getMessage());
							ShowText.show("ɾ������������.");
						}
					}
				});
				builder.setNegativeButton("ȡ��", null);
				builder.show();
				
			}
		});
		return view;
	}
	
	private String getModeName(Object mode){
		String modes = mode.toString().trim();
		if(modes.equals("0")){
			return "ֻ���ص绰";
		} 
		else if(modes.equals("1")){
			return "ֻ���ض���";
		} 
		else{
			return  "ȫ������";
		}
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	class ViewHolder {
		View view ;
		TextView number;
		TextView mode;
		
		ImageView iv_delete;
	}
	
}
