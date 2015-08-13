package com.xb.mobilesafe.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xb.mobilesafe.R;
import com.xb.mobilesafe.ui.adapter.SelectContactAdapter;
import com.xb.mobilesafe.utils.LogUtil;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ListView;

/**
 * 选择练习人
 * @author baixb
 *
 */
public class SelectContanctActivity extends Activity {
	
	private static final String TAG = "SelectContanctActivity";

	private ListView lv_select_contact;
	
	private SelectContactAdapter adapter;
	
	List<Map<String, Object>> contactsList = new ArrayList<Map<String, Object>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_contact);
		lv_select_contact = (ListView) findViewById(R.id.lv_select_contact);
		readContacts();
		adapter =new SelectContactAdapter(this, R.layout.list_contact, contactsList);
		lv_select_contact.setAdapter(adapter);
		
	}
	
	private void readContacts(){
		Cursor cursor;
		cursor = getContentResolver()
				.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null, null, null, null);
	  if(cursor!=null){
		  try {
				while(cursor.moveToNext()){
					Map<String, Object> map = new HashMap<String, Object>();
						//姓名
						String displayName = cursor.getString(
								cursor.getColumnIndex(
										ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
						//电话
						String number = cursor.getString(
								cursor.getColumnIndex(
										ContactsContract.CommonDataKinds.Phone.NUMBER));
						map.put("name", displayName);
						map.put("phone", number);
						contactsList.add(map);
				}
			} catch (Exception e) {
				LogUtil.e(TAG, e.getMessage());
				e.printStackTrace();
			}finally{
				if(cursor!=null){
					if(!cursor.isClosed()){
						cursor.close();
					}
				}
			}
	  }
	}
	
	
	

}
