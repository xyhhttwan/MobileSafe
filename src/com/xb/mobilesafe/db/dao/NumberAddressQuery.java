package com.xb.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 查询电话号码的归属地
 * @author baixb
 *
 */
public class NumberAddressQuery {
	
	private static String path="data/data/com.xb.mobilesafe/files/address.db";
	/**
	 * 查询电话号码的归属地
	 * @param phone
	 * @return
	 */
	public static String queryNumber(String phone){
		 SQLiteDatabase database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		 String address =phone;
		 
		 if(phone.matches("^1[34568]\\d{9}$")){
				Cursor cursor = database.rawQuery("select location from data2 where id =(select outkey from data1 where id=?) ",
						new String[]{phone.substring(0,7)});
				
				if(null !=cursor){
					while(cursor.moveToNext()){
						String postion  = cursor.getString(0);
						address = postion;
					}
					cursor.close();
				}
			}// /处理长途电话 10else 
		 else {
			 if (phone.length() > 10 && phone.startsWith("0")) {
					// 010-59790386
					Cursor cursor = database.rawQuery(
							"select location from data2 where area = ?",
							new String[] { phone.substring(1, 3) });
		
					while (cursor.moveToNext()) {
						String location = cursor.getString(0);
						address = location.substring(0, location.length() - 2);
					}
					cursor.close();
					// 0855-59790386
					cursor = database.rawQuery(
							"select location from data2 where area = ?",
							new String[] { phone.substring(1, 4) });
					while (cursor.moveToNext()) {
						String location = cursor.getString(0);
						address = location.substring(0, location.length() - 2);
		
					}
				}
		 }
		return address;
	}

}
