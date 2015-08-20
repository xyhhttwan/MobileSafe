package com.xb.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xb.mobilesafe.utils.BlackNumberDBOpeanHelper;
import com.xb.mobilesafe.utils.LogUtil;
import com.xb.mobilesafe.utils.MyApplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.appcompat.R.styleable;

/**
 * 黑名单数据库的增删改查
 * @author baixb
 *
 */
public class BlackNumberDao {
	
	
	private static final String TAG = "BlackNumberDao";
	private BlackNumberDBOpeanHelper dbOpeanHelper;
	private SQLiteDatabase db; 
	public BlackNumberDao(){
		dbOpeanHelper = new BlackNumberDBOpeanHelper(MyApplication.getContext());
		
	}
	
	
	/**
	 * 新增黑名单
	 * @param number
	 * @param name
	 * @param mode
	 */
	public void addBlackNumber(String number,String name,Integer mode)throws Exception{
		LogUtil.e(TAG, "addBlackNumber[number:"+number+",name:"+name+",mode:]"+mode);
		db = dbOpeanHelper.getWritableDatabase();
		String sql = "insert into blackNumber(phone,name,mode) values(?,?,?)";
		db.execSQL(sql, new Object[]{number,name ,mode});
		db.close();
	}
	
	/**
	 * 查询有没有该号码
	 * @param number
	 * @return
	 */
	public boolean find(String number){
		LogUtil.e(TAG, "find :"+number);
		db = dbOpeanHelper.getReadableDatabase();
		String sql ="select count(*) from blackNumber where phone=?";
		Cursor cursor = db.rawQuery(sql,new String[]{number});
		boolean result =false;
		if(cursor.moveToNext()){
			result= true;
		}
		cursor.close();
		db.close();
		LogUtil.e(TAG, "find :"+result);
		return result;
	}
	
	public String getModeByPhone(String phone){
		LogUtil.e(TAG, "getModeByPhone :"+phone);
		db = dbOpeanHelper.getReadableDatabase();
		String sql ="selet mode from blackNumber where phone =?";
		Cursor cursor = db.rawQuery(sql,new String[]{phone});
		String  mode = null;
		if(cursor.moveToNext()){
			mode = cursor.getString(0);
		}
		cursor.close();
		db.close();
		LogUtil.e(TAG, "getModeByPhone-mode:"+mode);
		return mode;
	}
	
	/**
	 * 根据号码删除黑名单
	 * @param number
	 */
	public void delete(String number)throws Exception{
		LogUtil.e(TAG, "delete :"+number);
		db = dbOpeanHelper.getWritableDatabase();
		String sql ="delete from blackNumber where phone=?";
		db.execSQL(sql, new String[]{number});
		db.close();
	}
	
	/**
	 * 根据主键id 更新黑名单信息
	 * @param phone
	 * @param mode
	 * @param id
	 * @throws Exception
	 */
	public void update(String phone,int mode,int id)throws Exception{
		LogUtil.e(TAG, "update :phone->"+phone+",id->"+id);
		db = dbOpeanHelper.getWritableDatabase();
		String sql ="update blackNumber set phone=? ,mode=? where id=?";
		db.execSQL(sql, new Object[]{phone,mode,id});
		db.close();
		
	}
	
	/**
	 * 查询全部的黑名单信息
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> getALLBlankNumber(){
		db = dbOpeanHelper.getReadableDatabase();
		String sql ="select * from blackNumber order by id desc ";
		Cursor cursor = db.rawQuery(sql, null);
		Map<String, Object> map;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		while(cursor.moveToNext()){
			map = new HashMap<>();
			int id    = cursor.getInt(cursor.getColumnIndex("id"));
			LogUtil.e(TAG, "id:"+id);
			map.put("id", id);
			String number = cursor.getString(cursor.getColumnIndex("phone"));
			map.put("number", number);
			String name   = cursor.getString(cursor.getColumnIndex("name"));
			map.put("name", name);
			String mode   = cursor.getString(cursor.getColumnIndex("mode"));
			map.put("mode", mode);
			list.add(map);
		}
		LogUtil.e(TAG, list.size()+"");
		return list;
		
	}

}
