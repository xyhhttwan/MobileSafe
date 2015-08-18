package com.xb.mobilesafe.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class NetworkUtils {
	private static String TAG = "NetworkUtils"; 
	
    /** û������ */
	private static final int NETWORKTYPE_INVALID = 0;
    /** wap���� */
	private static final int NETWORKTYPE_WAP = 1;
    /** 2G���� */
	private static final int NETWORKTYPE_2G = 2;
    /** 3G��3G�������磬��ͳ��Ϊ�������� */  
    private static final int NETWORKTYPE_3G = 3;  
    /** wifi���� */  
    private static final int NETWORKTYPE_WIFI = 4;
 
    
    private static boolean isFastMobileNetwork(Context context) { 
    	
    	TelephonyManager telephonyManager = (TelephonyManager)context.
    			getSystemService(Context.TELEPHONY_SERVICE);  
    	switch (telephonyManager.getNetworkType()) {  
    	       case TelephonyManager.NETWORK_TYPE_1xRTT:  
    	           return false; // ~ 50-100 kbps  
    	       case TelephonyManager.NETWORK_TYPE_CDMA:  
    	           return false; // ~ 14-64 kbps  
    	       case TelephonyManager.NETWORK_TYPE_EDGE:  
    	           return false; // ~ 50-100 kbps  
    	       case TelephonyManager.NETWORK_TYPE_EVDO_0:  
    	           return true; // ~ 400-1000 kbps  
    	       case TelephonyManager.NETWORK_TYPE_EVDO_A:  
    	           return true; // ~ 600-1400 kbps  
    	       case TelephonyManager.NETWORK_TYPE_GPRS:  
    	           return false; // ~ 100 kbps  
    	       case TelephonyManager.NETWORK_TYPE_HSDPA:  
    	           return true; // ~ 2-14 Mbps  
    	       case TelephonyManager.NETWORK_TYPE_HSPA:  
    	           return true; // ~ 700-1700 kbps  
    	       case TelephonyManager.NETWORK_TYPE_HSUPA:  
    	           return true; // ~ 1-23 Mbps  
    	       case TelephonyManager.NETWORK_TYPE_UMTS:  
    	           return true; // ~ 400-7000 kbps  
    	       case TelephonyManager.NETWORK_TYPE_EHRPD:  
    	           return true; // ~ 1-2 Mbps  
    	       case TelephonyManager.NETWORK_TYPE_EVDO_B:  
    	           return true; // ~ 5 Mbps  
    	       case TelephonyManager.NETWORK_TYPE_HSPAP:  
    	           return true; // ~ 10-20 Mbps  
    	       case TelephonyManager.NETWORK_TYPE_IDEN:  
    	           return false; // ~25 kbps  
    	       case TelephonyManager.NETWORK_TYPE_LTE:  
    	           return true; // ~ 10+ Mbps  
    	       case TelephonyManager.NETWORK_TYPE_UNKNOWN:  
    	           return false;  
    	       default:  
    	           return false;  
    	    }
    }
    
    /** 
     * ��ȡ����״̬��wifi,wap,2g,3g. 
     * 
     * @param context ������ 
     * @return int ����״̬ {@link #NETWORKTYPE_2G},{@link #NETWORKTYPE_3G},          *{@link #NETWORKTYPE_INVALID},{@link #NETWORKTYPE_WAP}* <p>{@link #NETWORKTYPE_WIFI} 
     */  
  
    private  static int NetWorkType() {  
    	Context context = MyApplication.getContext();
    	int mNetWorkType = NETWORKTYPE_INVALID;
        ConnectivityManager manager = (ConnectivityManager)context.
        		getSystemService(Context.CONNECTIVITY_SERVICE);  
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();  
  
        if (networkInfo != null && networkInfo.isConnected()) {  
            String type = networkInfo.getTypeName();  
  
            if (type.equalsIgnoreCase("WIFI")) {  
                mNetWorkType = NETWORKTYPE_WIFI;  
            } else if (type.equalsIgnoreCase("MOBILE")) {  
                String proxyHost = android.net.Proxy.getDefaultHost();  
  
                mNetWorkType = TextUtils.isEmpty(proxyHost)  
                        ? (isFastMobileNetwork(context) ? NETWORKTYPE_3G : NETWORKTYPE_2G)  
                        : NETWORKTYPE_WAP;  
            }  
        } else {  
            mNetWorkType = NETWORKTYPE_INVALID;  
        }
        
		return mNetWorkType;  
  
    }  
    
    public   static String getNetWorkTypeName(){
    	switch (getNetWorkType()) {
		case 0:
			return "û������";
		case 1:
			return "WAP����";
		case 2:
			return "2G����";
		case 3:
			return "3G����������";
		case 4:
			return "wifi����";
		default:
			return "δ֪������Ϣ";
		}
    }
    
    public static int getNetWorkType(){
    	return NetWorkType();
    }
    
    /**
     * �ж������Ƿ���ͬ
     * @return boolean �з���true û�з���false
     */
    public static boolean isNetWorkConnect(){
    	
    	if(0==NetWorkType()){
    		return false;
    	}
    	return true;
    }

}
