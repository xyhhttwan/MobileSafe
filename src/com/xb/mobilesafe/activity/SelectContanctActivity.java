package com.xb.mobilesafe.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xb.mobilesafe.R;
import com.xb.mobilesafe.ui.adapter.SelectContactAdapter;
import com.xb.mobilesafe.utils.LogUtil;
import com.xb.mobilesafe.utils.ShowText;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * ѡ����ϰ��
 * @author baixb
 *
 */
public class SelectContanctActivity extends Activity implements OnItemClickListener{
	
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
		lv_select_contact.setOnItemClickListener(this);
		
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
						//����
						String displayName = cursor.getString(
								cursor.getColumnIndex(
										ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
						//�绰
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
	
	/**
	 * ����绰�¼�
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
		Map<String, Object> map = contactsList.get(position);
		String phone  = (String)map.get("phone");
		LogUtil.e(TAG, "phone:-->"+phone);
		Intent  intent  = new Intent ();
		intent.putExtra("phone", phone);
		setResult(RESULT_OK,intent);
		finish();
	}
	
	
	
	
	
	
	
	

}
