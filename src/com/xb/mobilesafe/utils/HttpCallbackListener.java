package com.xb.mobilesafe.utils;

public interface HttpCallbackListener {
	
	
	void onFinish(String response);
	
	void onError(Exception e);

}
