package com.techvalens.restaurant.networkavailablity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkCheck {
	
	public static NetworkCheck refrence = null ;
	
	public static NetworkCheck getInstence() {
		if( null == refrence)
			refrence = new NetworkCheck();
		return refrence ;
	}
	
	public boolean HaveNetworkConnection(Context context)
	{
	    boolean HaveConnectedWifi = false;
	    boolean HaveConnectedMobile = false;

	    ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo[] netInfo = cm.getAllNetworkInfo();
	    for (NetworkInfo ni : netInfo)
	    {
	        if (ni.getTypeName().equalsIgnoreCase("WIFI"))
	            if (ni.isConnected())
	                HaveConnectedWifi = true;
	        if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
	            if (ni.isConnected())
	                HaveConnectedMobile = true;
	    }
	    return HaveConnectedWifi || HaveConnectedMobile;
	}
}
