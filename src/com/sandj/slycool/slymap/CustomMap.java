/***
 * Copyright (c) 2011 readyState Software Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.sandj.slycool.slymap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.sandj.slycoolchinese.Constants;
import com.sandj.slycoolchinese.R;
import com.sandj.slycoolchinese.Site;
import com.techvalens.restaurant.gps.GpsListener;


public class CustomMap extends MapActivity {

	MapView mapView;
	List<Overlay> mapOverlays;
	Drawable drawable2;
	CustomItemizedOverlay<CustomOverlayItem> itemizedOverlay;
	CustomItemizedOverlay<CustomOverlayItem> itemizedOverlay2;
	Drawable drawable;
	private MapController mapControl;
	private int latE6;
	private int lonE6;
	private GeoPoint gp;

	private ProgressDialog progressDialog=  null;
	public static ArrayList<RestaurantForMapViewByStoreId> data_RestaurantForMapViewByStoreId = null;
	private RestaurantForMapViewByStoreId _restaurantForMapViewByStoreId = null;
	private int i= 0;

	private ImageButton typeButton_a1 = null;
	private ImageButton typeButton_a2 = null;
	private ImageButton typeButton_a3 = null;
	private ImageButton typeButton_a4 = null;
	private ImageButton typeButton_a5 = null;
	private ImageButton typeButton_b1 = null;
	private ImageButton typeButton_b2 = null;
	private ImageButton typeButton_c = null;
	private ImageButton typeButton_d = null;
	private ImageButton typeButton_e = null;
	private ImageButton typeButton_f = null;
	private ImageButton typeButton_g = null;
	private ImageButton typeButton_h = null;
	private ImageButton typeButton_i = null;
	private ImageButton typeButton_j = null;
//	private ImageButton _refereshImageButton = null;
	private double location_latitude = 0;
	private double location_longitude = 0;
	private String latitudeStringValue= null;
	private String longitudeStringValue = null;

	private String hardcode="";
	String[] s= new String[20];
	private GoogleAnalyticsTracker tracker;
	private String slycool_file_name="slycool.csv";
	private boolean checkNet;
	
//	Site newSite;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapscreen);
		
		if(Constants.extStorageDirectory.equals("")){
			Constants.extStorageDirectory = Environment.getExternalStorageDirectory().toString();
			Constants.latitudeValue =25.090729;
			Constants.longitudeValue =121.522423;
		}
		
		
		checkNet = isOnline();
		
		if (checkNet){
			//	getCurrentLLOCATIONNAME();
			//	saveAppLaunchData();
			tracker = GoogleAnalyticsTracker.getInstance();
			 // Start the tracker in manual dispatch mode...
		    tracker.startNewSession("UA-34583559-1", this);
		    tracker.trackPageView("/slymap");
			
			
			
			if(Constants.latitudeValue != 0 && Constants.longitudeValue !=0){
				location_latitude = Constants.latitudeValue; //25.039797;
				location_longitude =Constants.longitudeValue; // 121.555553;
			}
			//CONVERT LOCATION FROM DOUBLE TO STRING TO PASS IN WEBSERVICE PARAMETER
			latitudeStringValue = Double.toString(location_latitude);
			longitudeStringValue = Double.toString(location_longitude);
	
			mapView = (MapView) findViewById(R.id.mapView);
			mapView.setBuiltInZoomControls(true);
	
			mapOverlays = mapView.getOverlays();
	
			mapView.setSatellite(false);
			mapView.setTraffic(false);
			mapView.setBuiltInZoomControls(true);   // Set android:clickable=true in main.xml
			int maxZoom = mapView.getMaxZoomLevel();
			int initZoom = maxZoom-2;
			mapControl = mapView.getController();
			mapControl.setZoom(initZoom);
			//Convert lat/long in degrees into integers in microdegrees
			/*latE6 =  (int) (35.955*1e6);
			lonE6 = (int) (-83.9265*1e6);
			gp = new GeoPoint(latE6, lonE6);
			mapControl.animateTo(gp);   */
	
			// first overlay
			//餐廳所在地
			//自己的所在地
			drawable = getResources().getDrawable(R.drawable.mappin); 
			drawable2=getResources().getDrawable(R.drawable.pinblue); 
				
			
			if (Constants.allSites.size()==0){
				new MyTask().execute();
			}else{
				setSitesByType("all");
			}
			
			
			/** set Button works */
			setButtons();
		
		}else{
			Toast.makeText(CustomMap.this, "資料載入錯誤,請檢查網路",Toast.LENGTH_SHORT).show();
			finish();
		}

	}
	
	
	public boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
	
	
	private void clearConstantsSites() {
		// TODO Auto-generated method stub
		Constants.allSites.clear();
	}
	
	private class MyTask extends AsyncTask{
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = ProgressDialog.show(CustomMap.this, "",Constants.PROCESSING_REQUEST);
		}

		@Override
		protected Object doInBackground(Object... params) {
			
			setSlyCoolData();
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);		
			progressDialog.dismiss();
			setSitesByType("all");
		}
	}
	
	private void setSlyCoolData() {
		// TODO Auto-generated method stub
		GeoPoint point= null;
		GeoPoint currentPoint=null;
		
		try {
	    	String strFile =  Constants.extStorageDirectory +"/Android_SlyCool/"+slycool_file_name;
			BufferedReader reader = new BufferedReader( new FileReader(strFile));
			LineNumberReader lineReader = new LineNumberReader(reader);
	        String line;
	        while ((line = lineReader.readLine()) != null) {
	        	if(lineReader.getLineNumber()!=1){
	             String[] RowData = Constants.parseCsvLine(line.toString());
	             int kk = RowData.length;
	             if (kk==19){
		             currentPoint = new GeoPoint((int) (Constants.latitudeValue * 1E6),(int) (Constants.longitudeValue * 1E6));
		             double latitude = 0;
					 double longitude = 0;
		             
					 String checkXLoc = RowData[14];
					 String checkyLoc = RowData[15];
					 
					 if (!checkXLoc.equals("N/A")){
							latitude = Double.parseDouble(checkXLoc);
							longitude = Double.parseDouble(checkyLoc);
							/** 算位置 */
							point = new GeoPoint((int) (latitude * 1E6),(int) (longitude * 1E6));
							Location locationA = new Location("point");
							locationA.setLatitude(point.getLatitudeE6() / 1E6);  
							locationA.setLongitude(point.getLongitudeE6() / 1E6);  
			
							Location locationB = new Location("current point");  
			
							locationB.setLatitude(currentPoint.getLatitudeE6() / 1E6);  
							locationB.setLongitude(currentPoint.getLongitudeE6() / 1E6);  
			
							double distanceKm = locationA.distanceTo(locationB)/1000;
							
							Site newSite = new Site(
									RowData[0],
									RowData[1],
									RowData[2],
									RowData[3],
									RowData[4],
									RowData[5],
									RowData[6],
									RowData[7],
									RowData[8],
									RowData[9],
									RowData[10],
									RowData[11],
									RowData[12],
									RowData[13],
									RowData[14],
									RowData[15],
									RowData[16],
									RowData[17],
									RowData[18],
									distanceKm
									);
							
							latitude = Double.parseDouble(newSite.xLoc);
							longitude = Double.parseDouble(newSite.yLoc);
							Constants.allSites.add(newSite);
							// 不同的Type塞到不同的ArrayList
							
						}
	             }
	        	}
	        }
		}catch (IOException ex) {
	       
	    }
	}
	
	
	private void setButtons() {
	// TODO Auto-generated method stub
		/** 
		 *  find view by id for classes pictures
		 * */
		typeButton_a1 = (ImageButton)findViewById(R.id.a1);
		typeButton_a1.setOnClickListener(new OnClickListener(){  
            public void onClick(View v) {
            	mapOverlays.clear();
            	mapView.invalidate();
            	itemizedOverlay.setFocus(null);
            	itemizedOverlay2.setFocus(null);
            	setSitesByType("a1");
            	
            }}); 
		typeButton_a2 = (ImageButton)findViewById(R.id.a2);
		typeButton_a2.setOnClickListener(new OnClickListener(){  
            public void onClick(View v) {  
            	mapOverlays.clear();
            	mapView.invalidate();
            	itemizedOverlay.setFocus(null);
            	itemizedOverlay2.setFocus(null);
            	setSitesByType("a2");
            }}); 
		typeButton_a3 = (ImageButton)findViewById(R.id.a3);
		typeButton_a3.setOnClickListener(new OnClickListener(){  
            public void onClick(View v) {  
            	mapOverlays.clear();
            	mapView.invalidate();
            	itemizedOverlay.setFocus(null);
            	itemizedOverlay2.setFocus(null);
            	setSitesByType("a3");
            }}); 
		typeButton_a4 = (ImageButton)findViewById(R.id.a4);
		typeButton_a4.setOnClickListener(new OnClickListener(){  
            public void onClick(View v) {  
            	mapOverlays.clear();
            	mapView.invalidate();
            	itemizedOverlay.setFocus(null);
            	itemizedOverlay2.setFocus(null);
            	setSitesByType("a4");
            }}); 
		typeButton_a5 = (ImageButton)findViewById(R.id.a5);
		typeButton_a5.setOnClickListener(new OnClickListener(){  
            public void onClick(View v) {  
            	mapOverlays.clear();
            	mapView.invalidate();
            	itemizedOverlay.setFocus(null);
            	itemizedOverlay2.setFocus(null);
            	setSitesByType("a5");
            }}); 
		typeButton_b1 = (ImageButton)findViewById(R.id.b1);
		typeButton_b1.setOnClickListener(new OnClickListener(){  
            public void onClick(View v) {  
            	mapOverlays.clear();
            	mapView.invalidate();
            	itemizedOverlay.setFocus(null);
            	itemizedOverlay2.setFocus(null);
            	setSitesByType("b1");
            }}); 
		typeButton_b2 = (ImageButton)findViewById(R.id.b2);
		typeButton_b2.setOnClickListener(new OnClickListener(){  
            public void onClick(View v) {  
            	mapOverlays.clear();
            	mapView.invalidate();
            	itemizedOverlay.setFocus(null);
            	itemizedOverlay2.setFocus(null);
            	setSitesByType("b2");
            }}); 
		typeButton_c = (ImageButton)findViewById(R.id.c);
		typeButton_c.setOnClickListener(new OnClickListener(){  
            public void onClick(View v) {  
            	mapOverlays.clear();
            	mapView.invalidate();
            	itemizedOverlay.setFocus(null);
            	itemizedOverlay2.setFocus(null);
            	setSitesByType("c");
            }}); 
		typeButton_d = (ImageButton)findViewById(R.id.d);
		typeButton_d.setOnClickListener(new OnClickListener(){  
            public void onClick(View v) {  
            	mapOverlays.clear();
            	mapView.invalidate();
            	itemizedOverlay.setFocus(null);
            	itemizedOverlay2.setFocus(null);
            	setSitesByType("d");
            }}); 
		typeButton_e = (ImageButton)findViewById(R.id.e);
		typeButton_e.setOnClickListener(new OnClickListener(){  
            public void onClick(View v) {  
            	mapOverlays.clear();
            	mapView.invalidate();
            	itemizedOverlay.setFocus(null);
            	itemizedOverlay2.setFocus(null);
            	setSitesByType("e");
            }}); 
		typeButton_f = (ImageButton)findViewById(R.id.f);
		typeButton_f.setOnClickListener(new OnClickListener(){  
            public void onClick(View v) {  
            	mapOverlays.clear();
            	mapView.invalidate();
            	itemizedOverlay.setFocus(null);
            	itemizedOverlay2.setFocus(null);
            	setSitesByType("f");
            }}); 
		typeButton_g = (ImageButton)findViewById(R.id.g);
		typeButton_g.setOnClickListener(new OnClickListener(){  
            public void onClick(View v) {  
            	mapOverlays.clear();
            	mapView.invalidate();
            	itemizedOverlay.setFocus(null);
            	itemizedOverlay2.setFocus(null);
            	setSitesByType("g");
            }}); 
		typeButton_h = (ImageButton)findViewById(R.id.h);
		typeButton_h.setOnClickListener(new OnClickListener(){  
            public void onClick(View v) {  
            	mapOverlays.clear();
            	mapView.invalidate();
            	itemizedOverlay.setFocus(null);
            	itemizedOverlay2.setFocus(null);
            	setSitesByType("h");
            }}); 
		typeButton_i = (ImageButton)findViewById(R.id.i);
		typeButton_i.setOnClickListener(new OnClickListener(){  
            public void onClick(View v) {  
            	mapOverlays.clear();
            	mapView.invalidate();
            	itemizedOverlay.setFocus(null);
            	itemizedOverlay2.setFocus(null);
            	setSitesByType("i");
            }}); 
		typeButton_j = (ImageButton)findViewById(R.id.j);
		typeButton_j.setOnClickListener(new OnClickListener(){  
            public void onClick(View v) {  
            	mapOverlays.clear();
            	mapView.invalidate();
            	itemizedOverlay.setFocus(null);
            	itemizedOverlay2.setFocus(null);
            	setSitesByType("j");
            }}); 
	}
	
	private void setSitesByType(String type) {
		// TODO Auto-generated method stub
		GeoPoint point= null;
		GeoPoint currentPoint=null;
		CustomOverlayItem overlayItem = null;
		final MapController mc = mapView.getController();

		
		itemizedOverlay = new CustomItemizedOverlay<CustomOverlayItem>(drawable, mapView); //餐廳位置
		itemizedOverlay2=new CustomItemizedOverlay<CustomOverlayItem>(drawable2, mapView); //自己位置
		
		mapOverlays.add(itemizedOverlay);//ADD THIS HERE TO AVOID EXCEPTION, DO NOT ADD BEFORE NETWORK CHECK OR BEFORE GETTING ATLEAST 1 PIN POINT FROM WEBSERVICE.
		mapOverlays.add(itemizedOverlay2);
		currentPoint = new GeoPoint((int) (location_latitude * 1E6),(int) (location_longitude * 1E6));
				
		/** 
		* 把餐廳物件一個個拿出來,再塞到itemizedOverlay
		* */
		
		double latitude = 0;
		double longitude = 0;
		int length = Constants.allSites.size();
		
		for (i=0;i<length;i++){
			Site usingSite =Constants.allSites.get(i);
			
			if (usingSite.englishType.indexOf(type)!=-1 || type.equals("all") ){
				latitude = Double.parseDouble(usingSite.xLoc);
				longitude = Double.parseDouble(usingSite.yLoc);
				
	
				String storeName =usingSite.siteName;
				String storeAddress = usingSite.siteAddress;
				String storeLogo = usingSite.photoUrl;
				String storeId = usingSite.id;
				
				point = new GeoPoint((int) (latitude * 1E6),(int) (longitude * 1E6));
				
				overlayItem = new CustomOverlayItem(point,storeName,storeAddress, storeLogo,storeId);
				itemizedOverlay.addOverlay(overlayItem);
			}
				
		}
		/** 
		 * 把現在所在位置塞進來
		 * */
		if(location_latitude!=0.0||location_longitude!=0.0)
		{
			overlayItem=new CustomOverlayItem(currentPoint, "", "", "", "");
			itemizedOverlay2.addOverlay(overlayItem);
			mc.animateTo(currentPoint);
			mc.setZoom(14);	
		}else
		{
			Toast.makeText(CustomMap.this,R.string.checkGPS,Toast.LENGTH_SHORT).show();
		}
		
		
	}
	
	
	/*@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
			new AlertDialog.Builder(this)
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setTitle("End of days...!")
			.setMessage("Do you wana walk out....?")
			.setPositiveButton("Yo man...!", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					//Stop the activity
					CustomMap.this.finish();    
				}

			})
			.setNegativeButton("No way..!", null)
			.show();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}*/
	//GET THE CURRENT LOCATION NAME FROM LAT-LONG
	/** 
	 * 把目前的經緯loc轉成文字位置
	 * */
	private void getCurrentLLOCATIONNAME() {
		if (Constants.latitudeValue != 0 && Constants.longitudeValue !=0) {
			double latitude = Constants.latitudeValue;
			double longitude = Constants.longitudeValue;

			GeoPoint point = new GeoPoint(
					(int) (latitude * 1E6), 
					(int) (longitude * 1E6));
			Constants.CURRENT_LOCATION = ConvertPointToLocation(point);
		}else{
			/*Toast.makeText(getBaseContext(), "Location = null", Toast.LENGTH_SHORT).show();*/
		}
	}
	/** 
	 * 利用Google的geoCoder來把位置轉成文字
	 * */
	public String ConvertPointToLocation(GeoPoint point) {   
		String address = "";
		Geocoder geoCoder = new Geocoder(
				getBaseContext(), Locale.getDefault());
		try {
			List<Address> addresses = geoCoder.getFromLocation(
					point.getLatitudeE6()  / 1E6, 
					point.getLongitudeE6() / 1E6, 1);

			if (addresses.size() > 0) {
				for (int index = 0; index < addresses.get(0).getMaxAddressLineIndex(); index++)
					address += addresses.get(0).getAddressLine(index) + " ";
			}
		}
		catch (IOException e) {                
			e.printStackTrace();
		}   

		return address;
	} 
    
	/** 
	 * 網路連不上時, 彈出dialog
	 * */
	private void networkFailed(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getParent());
		// set title
		alertDialogBuilder.setTitle(R.string.caution);
		// set dialog message
		alertDialogBuilder
		.setMessage(R.string.network_msg)
		.setCancelable(false)
		.setNegativeButton(R.string.ok,new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// if this button is clicked, just close
				// the dialog box and do nothing
				dialog.cancel();
			}
		});
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();
	}
	/** 
	 * isRouteDispalyed 通常在飛行模式才會使用
	 * */
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	//SAVE THE DEVICE ID, START TIME OF APP, LOCATION & LAT-LONG
	/** 
	 * 把打開App的地點傳回伺服器並記錄
	 * */
//	private void saveAppLaunchData() {
//
//		if (NetworkCheck.getInstence().HaveNetworkConnection(
//				CustomMap.this)) {
//			Thread thread = new Thread(){
//				@Override
//				public void run() {
//					getCurrentLLOCATIONNAME();
//					String response = callWebServiceForSaveAppData(latitudeStringValue,longitudeStringValue);
//					// TODO Auto-generated method stub
//					super.run();
//				}
//			};thread.start();
//		}
//	}
	/** 
	 * onDestroy時, 就把GpsListener關掉, 不用回報了
	 * */
	@Override
	protected void onDestroy() {
		GpsListener.getInstance().stopGpsCallBack();
		super.onDestroy();
		try{
		    tracker.dispatch();
		    tracker.stopSession();
		    }catch(Exception e){
		    	
		}
	}
	
	/** 
	 * Connect to 伺服器, 把經緯度傳回去
	 * */
//	private String callWebServiceForSaveAppData(String latitude, String longitude){
//		String responseData = "";
//		WebServerCommunicator webServerCommunicator = WebServerCommunicator
//		.getInstance();
//		webServerCommunicator.setMethodName(SAVE_APPLICATION_LAUNCH_DATA);
//		SoapObject soapObject = webServerCommunicator.getSoapObject();
//		soapObject.addProperty("Latitude", latitude);
//		soapObject.addProperty("Longitude",longitude);
//		soapObject.addProperty("ResponseType", "attribute");
//		responseData = webServerCommunicator.connectToWebService(soapObject);
//		return responseData;
//	}

}
