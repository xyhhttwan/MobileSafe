package com.xb.mobilesafe.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.telephony.SmsManager;
import android.util.Xml;

public class SMSUtils {
	
	private static final String TAG = "SMSUtils";
	public static void send(String phone, String message){
		LogUtil.e(TAG, "start->phone,"+phone+",messages"+message);
        PendingIntent pi = PendingIntent.getActivity(MyApplication.getContext(), 0, 
        			new Intent(MyApplication.getContext(), SMSUtils.class), 0);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phone, null, message, pi, null);
        LogUtil.e(TAG, "end->phone,"+phone+",messages"+message);
    }
	
	/**
	 * 短息备份回调
	 * @author baixb
	 *
	 */
	public interface BackUpCallBack{
		
		/**
		 * 短信备份前调用的方法
		 * @param total
		 */
		public void beforeSmsBackup(int total);
		
		/**
		 * 短息备份中调用的方法
		 * @param progress 当前的进度
		 */ 
		public void onSmsBackup(int progress);
		
	}
	
	private static  Context context;
	
	/**
	 * 备份短信
	 * @param backUpCallBack
	 * @throws Exception
	 */
	public static void backupSms(BackUpCallBack backUpCallBack)throws Exception{
		context = MyApplication.getContext();
		ContentResolver resolver = context.getContentResolver();
		
		//把短信一条一条读出来然后写到sdcard中
		//创建要写入的文件
		File file  = new File(Environment.getExternalStorageDirectory(),"smsBack.xml");
		
		FileOutputStream fos= new FileOutputStream(file);

		//获取xml文件的生存期(系列器)
		XmlSerializer serializer = Xml.newSerializer();
		//初始化生成器
		serializer.setOutput(fos,"utf-8");
		serializer.startDocument("utf-8", true);//true 是否独立
		serializer.startTag(null,"smss");
		
		Uri uri = Uri.parse("content://sms/");
		Cursor cursor = resolver.query(uri, new String[]{"body","address","type","date"}, null, null, null);
		int  total= cursor.getCount();
		backUpCallBack.beforeSmsBackup(total);
		int process = 0;
		//循环读取
		while (cursor.moveToNext()) {
			Thread.sleep(500);
			//内容
			String body = cursor.getString(0);
			//地址
			String address = cursor.getString(1);
			
			LogUtil.e("body", body);
			//类型
			String type = cursor.getString(2);
			//日期
			String date = cursor.getString(3);
			
			serializer.startTag(null, "sms");
			
			serializer.startTag(null, "body");
			//出现特殊字符会报错
			serializer.text(body);
			serializer.endTag(null, "body");
			
			serializer.startTag(null,"address");
			serializer.text(address);
			serializer.endTag(null,"address");
			
			serializer.startTag(null,"type");
			serializer.text(type);
			serializer.endTag(null,"type");
			
			serializer.startTag(null,"date");
			serializer.text(date);
			serializer.endTag(null,"date");
			
			serializer.endTag(null,"sms");
			process++;
			backUpCallBack.onSmsBackup(process);
			
		}
		serializer.endTag(null,"smss");
		serializer.endDocument();
		fos.close();
	}
	
	/**
	 * 还原短信
	 * @param flag
	 */
	public  static void restoreSms(boolean flag)throws Exception{
		
		Uri uri = Uri.parse("content://sms/");
		context = MyApplication.getContext();
		ContentResolver resolver = context.getContentResolver();
		//是否清楚现在有的短信
		if(flag){
			resolver.delete(uri, null, null);
		}
		
		XmlPullParser pull = Xml.newPullParser();
		File file = new File(Environment.getExternalStorageDirectory(),"smsBack.xml");
		FileInputStream fis = new FileInputStream(file);
		pull.setInput(fis, "utf-8");
		
		int eventType = pull.getEventType();
		String body = null;
		String date = null;
		String type = null;
		String address = null;
		ContentValues values = null;
		while(eventType!=pull.END_DOCUMENT){
			String tagName = pull.getName();
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if("body".equals(tagName)){
					body = pull.nextText();
				}else if("date".equals(tagName)){
					date = pull.nextText();
				}else if("type".equals(tagName)){
					type = pull.nextText();
				}else if("address".equals(tagName)){
					address = pull.nextText();
				}
				break;
			case XmlPullParser.END_TAG:
				if("sms".equals(tagName)){
					values = new ContentValues();
					values.put("body", body);
					values.put("date",date);
					values.put("type",type);
					values.put("address",address);
					resolver.insert(uri, values);
				}
				break;
			default:
				break;
			}
			eventType = pull.next();
		}
		fis.close();
	}
	

}
