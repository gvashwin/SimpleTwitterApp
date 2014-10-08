package com.codepath.apps.SimpleTwitterApp.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {
	private static NetworkUtil netUtil;
	
	private NetworkUtil() {
		
	}
	
	public static NetworkUtil getInstance() {
		if(netUtil == null) {
			netUtil = new NetworkUtil();
		}
		return netUtil;
	}
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager 
        = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
	}
}
