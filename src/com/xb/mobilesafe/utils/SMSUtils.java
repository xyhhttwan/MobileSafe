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
	 * ��Ϣ���ݻص�
	 * @author baixb
	 *
	 */
	public interface BackUpCallBack{
		
		/**
		 * ���ű���ǰ���õķ���
		 * @param total
		 */
		public void beforeSmsBackup(int total);
		
		/**
		 * ��Ϣ�����е��õķ���
		 * @param progress ��ǰ�Ľ���
		 */ 
		public void onSmsBackup(int progress);
		
	}
	
	private static  Context context;
	
	/**
	 * ���ݶ���
	 * @param backUpCallBack
	 * @throws Exception
	 */
	public static void backupSms(BackUpCallBack backUpCallBack)throws Exception{
		context = MyApplication.getContext();
		ContentResolver resolver = context.getContentResolver();
		
		//�Ѷ���һ��һ��������Ȼ��д��sdcard��
		//����Ҫд����ļ�
		File file  = new File(Environment.getExternalStorageDirectory(),"smsBack.xml");
		
		FileOutputStream fos= new FileOutputStream(file);

		//��ȡxml�ļ���������(ϵ����)
		XmlSerializer serializer = Xml.newSerializer();
		//��ʼ��������
		serializer.setOutput(fos,"utf-8");
		serializer.startDocument("utf-8", true);//true �Ƿ����
		serializer.startTag(null,"smss");
		
		Uri uri = Uri.parse("content://sms/");
		Cursor cursor = resolver.query(uri, new String[]{"body","address","type","date"}, null, null, null);
		int  total= cursor.getCount();
		backUpCallBack.beforeSmsBackup(total);
		int process = 0;
		//ѭ����ȡ
		while (cursor.moveToNext()) {
			Thread.sleep(500);
			//����
			String body = cursor.getString(0);
			//��ַ
			String address = cursor.getString(1);
			
			LogUtil.e("body", body);
			//����
			String type = cursor.getString(2);
			//����
			String date = cursor.getString(3);
			
			serializer.startTag(null, "sms");
			
			serializer.startTag(null, "body");
			//���������ַ��ᱨ��
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
	 * ��ԭ����
	 * @param flag
	 */
	public  static void restoreSms(boolean flag)throws Exception{
		
		Uri uri = Uri.parse("content://sms/");
		context = MyApplication.getContext();
		ContentResolver resolver = context.getContentResolver();
		//�Ƿ���������еĶ���
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
