package com.xb.mobilesafe.ui.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xb.mobilesafe.R;
import com.xb.mobilesafe.entity.HomeFuntionItem;
import com.xb.mobilesafe.utils.LogUtil;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeItemAdapter extends BaseAdapter {
	
	private  List<HomeFuntionItem> list ;
	Map<String, Integer> map= new HashMap<String,Integer>();
	
	private Context context;
	
	public HomeItemAdapter(Context context,List<HomeFuntionItem> items){
		this.list = items;
		this.context = context;
		map.put("safe", R.drawable.safe);
		map.put("callmsgsafe", R.drawable.callmsgsafe);
		map.put("app", R.drawable.app);
		map.put("taskmanager", R.drawable.taskmanager);
		map.put("netmanager", R.drawable.netmanager);
		map.put("trojan", R.drawable.trojan);
		map.put("sysoptimize", R.drawable.sysoptimize);
		map.put("atools", R.drawable.atools);
		map.put("settings", R.drawable.settings);
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
	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = View.inflate(context,R.layout.list_item_home, null);
		ImageView iv_item =(ImageView) view.findViewById(R.id.iv_item);
		TextView  tv_item = (TextView) view.findViewById(R.id.tv_item);
		HomeFuntionItem item = list.get(position);
		tv_item.setText(item.getName());
		if(map!=null){
			iv_item.setImageResource(map.get(item.getCode()));
		}else{
			iv_item.setImageResource(R.drawable.safe);
		}
		return view;
	}

}
