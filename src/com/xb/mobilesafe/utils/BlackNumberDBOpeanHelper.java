package com.xb.mobilesafe.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * ���������ݿ������
 * @author baixb
 *
 */
public class BlackNumberDBOpeanHelper extends SQLiteOpenHelper {
	
	//mode =0 ֻ���ص绰,=1ֻ���ض���=2���ص绰�Ͷ���
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
	 * ��ʼ�����ݿ�ı�ṹ
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(blackNumberSQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	

}
