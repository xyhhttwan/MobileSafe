package com.xb.mobilesafe.ui.adapter;

import java.util.List;
import java.util.Map;

import com.xb.mobilesafe.R;
import com.xb.mobilesafe.utils.LogUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SelectContactAdapter extends ArrayAdapter<Map<String,Object>> {
	
	private static final String TAG = "SelectContactAdapter";
	private int resourceId;
	
	public SelectContactAdapter(Context context, int resource, List<Map<String,Object>> objects) {
		super(context, resource, objects);
		this.resourceId = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Map<String, Object> map = getItem(position);
		View view ;
		ViewHolder viewHolder =null;
		String name = (String) map.get("name");
		String phone = (String) map.get("phone");
		LogUtil.e(TAG, "name:"+name);
		LogUtil.e(TAG, "phone:"+phone);
		if(null == convertView){
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder.name = (TextView) view.findViewById(R.id.tv_contact_name);
			viewHolder.phone =  (TextView) view.findViewById(R.id.tv_contact_phone);
			viewHolder.name.setText(name);
			viewHolder.phone.setText(phone);
			//存储在view 的tag中
			view.setTag(viewHolder);
		}else{
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		
		if(null !=viewHolder){
			viewHolder.name.setText(name);
			viewHolder.phone.setText(phone);
		}
		
		return view;
	}
	
	
	
	//内部类提升 性能 缓存  TextView,TextView
	class ViewHolder{
		
		TextView name ;
		TextView phone;
		
	}


	

}
