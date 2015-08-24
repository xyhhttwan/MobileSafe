package com.xb.mobilesafe.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 黑名单数据库帮助类
 * @author baixb
 *
 */
public class BlackNumberDBOpeanHelper extends SQLiteOpenHelper {
	
	//mode =0 只拦截电话,=1只拦截短信=2拦截电话和短信
	private final String blackNumberSQL=" create table blackNumber ("
			+ " id integer primary key autoincrement,"
			+ " phone text,"
			+ " name text,"
			+ " mode integer)";
	

	@SuppressLint("NewApi")
	public BlackNumberDBOpeanHelper(Context context) {
		super(context, "blackNumber.db", null, 1, null);
	}
	
	
	
	/**
	 * 初始化数据库的表结构
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(blackNumberSQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	

}
