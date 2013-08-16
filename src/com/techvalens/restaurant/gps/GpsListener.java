package com.techvalens.restaurant.gps;




import com.sandj.slycoolchinese.Constants;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

public class GpsListener {
	
	public static GpsListener refrence = null ;
	public LocationManager locationManager = null;
	public LocationListener locationListener = null;
	public Location location = null;
	
	public static GpsListener getInstance(){
		if(refrence == null){
			refrence = new GpsListener();
		}
		return refrence;
	}
	
	public void startGpsCallBack(Context activityContext){
		locationManager = (LocationManager) activityContext.getSystemService(Context.LOCATION_SERVICE);
		locationListener = new mylocationlistener();
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		location = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if (location != null) {
			Constants.latitudeValue = location.getLatitude();
			Constants.longitudeValue = location.getLongitude();
			System.out.println(Constants.latitudeValue+"=============");
			
		}
	}
	
	public class mylocationlistener implements LocationListener {
		@Override
		public void onLocationChanged(Location location) {
			if (location != null) {
				Constants.latitudeValue = location.getLatitude();
				Constants.longitudeValue = location.getLongitude();
			}
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}
	
	public void stopGpsCallBack(){
		if (locationManager != null) {
			locationManager.removeUpdates(locationListener);
		}
	}
	
	public void startGpsCallbackAgain(Context activityContext){
		locationManager = (LocationManager) activityContext.getSystemService(Context.LOCATION_SERVICE);
		locationListener = new mylocationlistener();
		locationManager.requestLocationUpdates(
				LocationManager.GPS_PROVIDER, 0, 0, locationListener);
		location = locationManager
		.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location != null) {
			Constants.latitudeValue = location.getLatitude();
			Constants.longitudeValue = location.getLongitude();
		}
	}

}