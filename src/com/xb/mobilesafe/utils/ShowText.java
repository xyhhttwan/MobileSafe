package com.xb.mobilesafe.utils;

import android.content.Context;
import android.widget.Toast;

public class ShowText{
	
	
	  public   static void show(String str,int length) {  
		  
		  shows(str,length);
	        
	    }  
	  
	  public static void show(String str){
		  shows(str,-1);
	  }
	  
	 private static void shows(String str,int length){
		 Context context = MyApplication.getContext();
		 if(-1!=length){
			  Toast.makeText(context,str, length).show();  
		  }else{
			  Toast.makeText(context, str, Toast.LENGTH_SHORT).show(); 
		  }
	 }

}
