package com.xb.mobilesafe.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.Looper;

public class HttpUtil {
	
	private static final String TAG="HttpUtil";
	
	public static void sendHttpRequest(final String address,final HttpCallbackListener listener){
		
		if(!NetworkUtils.isNetWorkConnect()){
			ShowText.show("ÍøÂçÁ¬½ÓÒì³£.");
			return;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				
				HttpURLConnection connection= null;
				try {
					LogUtil.e(TAG, "request address:----->"+address);
					URL url = new URL(address);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(10000);
					connection.setReadTimeout(10000);
					InputStream in = connection.getInputStream();
					String response  =StreamTools.readFromStream(in);
					if(listener !=null){
						listener.onFinish(response);
					}
					LogUtil.e(TAG, "response:----->"+response);
				} catch (MalformedURLException e) {
					e.printStackTrace();
					LogUtil.e(TAG,e.getMessage());
					listener.onError(e);
				} catch (IOException e) {
					e.printStackTrace();
					LogUtil.e(TAG,e.getMessage());
					listener.onError(e);
				}finally{
					if (connection != null) {
						connection.disconnect();
					}
				}
				
			}
		}).start();
	}

}
