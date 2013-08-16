package com.sandj.slycool.slycoupon;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.google.android.maps.GeoPoint;
import com.sandj.slycoolchinese.Constants;
import com.sandj.slycoolchinese.R;
import com.sandj.slycoolchinese.Site;
import com.techvalens.restaurant.restaurantdetail.RestaurantsDetail;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;



/** 
 * Change to 士林好康
 * */

public class ListOfRestaurantActivity extends Activity implements OnClickListener, OnItemClickListener
{
//	private ImageButton _refreshImageButton =null;
	private ListView listview=null;
	private ListViewCustomAdapter adapter;
	private ArrayList<Object> itemList;
	private ItemBean bean;
	private String str1=null;

	private Button button_a1 = null;
	private Button button_a2 = null;
	private Button button_a3 = null;
	private Button button_a4 = null;
	private Button button_a5 = null;
	private Button button_b1 = null;
	private Button button_b2 = null;
	private Button button_c = null;
	private Button button_d = null;
	private Button button_e = null;
	private Button button_f = null;
	private Button button_g = null;
	private Button button_h = null;
	private Button button_i = null;
	private Button button_j = null;

	private String methodFlag = null; 

	private ProgressDialog progressDialog= null;

	private int i= 0;
	private int j = 0;

	private double location_latitude = 0;
	private double location_longitude = 0;
	private String latitudeStringValue= null;
	private String longitudeStringValue = null;
	private String _hardCodeStoreId = "1";//by default it generate the list for 1st restaurant type
	
	private GoogleAnalyticsTracker tracker;
	private String slycool_file_name="slycool.csv";
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listrestaurantscreen);
		
		if(Constants.extStorageDirectory.equals("")){
			Constants.extStorageDirectory = Environment.getExternalStorageDirectory().toString();
			Constants.latitudeValue =25.090729;
			Constants.longitudeValue =121.522423;
		}
		
		tracker = GoogleAnalyticsTracker.getInstance();
		 // Start the tracker in manual dispatch mode...
	    tracker.startNewSession("UA-34583559-1", this);
	    tracker.trackPageView("/store_list");

		if(Constants.latitudeValue != 0 && Constants.longitudeValue !=0){
			location_latitude = Constants.latitudeValue;
			location_longitude = Constants.longitudeValue;
		}
		
		//CONVERT LOCATION FROM DOUBLE TO STRING TO PASS IN WEBSERVICE PARAMETER
		/** 
		 * This need to be change because we will only use the cell phone's loc, won't call server
		 * */
		latitudeStringValue = Double.toString(location_latitude);
		longitudeStringValue = Double.toString(location_longitude);
		
		setUIs();
		
		if(Constants.allSites.size()==0){
			new MyTask().execute();
		}else{
			sitesArraySorting(Constants.allSites);
			prepareArrayLits("a1");
		}
		
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
				progressDialog = ProgressDialog.show(ListOfRestaurantActivity.this, "",Constants.PROCESSING_REQUEST);
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
				sitesArraySorting(Constants.allSites);
				prepareArrayLits("a1");
			}
	}
	
	
	private void setUIs() {
		// TODO Auto-generated method stub
		button_a1 = (Button)findViewById(R.id.logo_a1);
		button_a1.setOnClickListener(this);
		button_a2 = (Button)findViewById(R.id.logo_a2);
		button_a2.setOnClickListener(this);
		button_a3 = (Button)findViewById(R.id.logo_a3);
		button_a3.setOnClickListener(this);
		button_a4 = (Button)findViewById(R.id.logo_a4);
		button_a4.setOnClickListener(this);
		button_a5 = (Button)findViewById(R.id.logo_a5);
		button_a5.setOnClickListener(this);
		button_b1 = (Button)findViewById(R.id.logo_b1);
		button_b1.setOnClickListener(this);
		button_b2 = (Button)findViewById(R.id.logo_b2);
		button_b2.setOnClickListener(this);
		button_c = (Button)findViewById(R.id.logo_c);
		button_c.setOnClickListener(this);
		button_d = (Button)findViewById(R.id.logo_d);
		button_d.setOnClickListener(this);
		button_e = (Button)findViewById(R.id.logo_e);
		button_e.setOnClickListener(this);
		button_f = (Button)findViewById(R.id.logo_f);
		button_f.setOnClickListener(this);
		button_g = (Button)findViewById(R.id.logo_g);
		button_g.setOnClickListener(this);
		button_h = (Button)findViewById(R.id.logo_h);
		button_h.setOnClickListener(this);
		button_i = (Button)findViewById(R.id.logo_i);
		button_i.setOnClickListener(this);
		button_j = (Button)findViewById(R.id.logo_j);
		button_j.setOnClickListener(this);
		
		listview =(ListView)findViewById(R.id.listView1);
		listview.setOnItemClickListener(this);
		
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
	
	
	/** 
	 *  onClickListener, call RestaurantDetail
	 * */
	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
		TextView _storeIdTextView = (TextView)view.findViewById(R.id.txtStoreId_itemlist);

		String _storeId = _storeIdTextView.getText().toString();
		System.out.println(_storeId);

		Intent intent = new Intent(ListOfRestaurantActivity.this, RestaurantsDetail.class);
//		TabGroupActivity parentActivity = (TabGroupActivity)getParent();
		intent.putExtra("STORE_ID", _storeId);
//		parentActivity.startChildActivity("RestaurantDetail", intent);
		startActivity(intent);
	}
	
	

	
	/** 
	 *  prepareArrayList for listview, won't change
	 * */
	private void prepareArrayLits(String type){	
			
//			progressDialog.dismiss();
		
			// StoreId, StoreName, StoreAddress, StoreLogo, distance, toreId
//			AddObjectToList("","(百年老店)吉利台灣傳統美食", 
//					"新士林市場55,56號(士林區基河路101號)","http://www.slycool.com/app/1/storeA0001A.png","11",
//					"");
		    itemList = new ArrayList<Object>();
			int length = Constants.allSites.size();
			
			for (int i=0;i<length;i++){
				Site usingSite =Constants.allSites.get(i);
				if(usingSite.englishType.indexOf(type) != -1)
				// StoreType, StoreName, StoreAddress, StoreLogo, distance, StoreId
				AddObjectToList(usingSite.englishType,usingSite.siteName, 
						usingSite.siteAddress,usingSite.photoUrl,String.valueOf(usingSite.siteDistance).substring(0, 3),
						usingSite.id);
			}
			
			setListView();
		
	}

	
	private void setListView() {
		// TODO Auto-generated method stub
		adapter = new ListViewCustomAdapter(this, itemList);
		listview.setAdapter(adapter);
	}
	
	// Add one item into the Array List
	public void AddObjectToList(String storeType, String storeName, String storeAddress,
			String urlString, String storeDistance, String storeId){
		bean = new ItemBean();
		bean.setStoreType(storeType);
		bean.setName(storeName);
		bean.setAddress(storeAddress);
		bean.setLogo(urlString);
		bean.setDistances(storeDistance);
		bean.setStoreId(storeId);

		itemList.add(bean);
	}
	
	
	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			//preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}


	@Override
	public void onClick(View v) {
		if(v == button_a1){
			prepareArrayLits("a1");
		}else if(v== button_a2){
			prepareArrayLits("a2");
		}else if(v == button_a3){
			prepareArrayLits("a3");
		}else if(v == button_a4){
			prepareArrayLits("a4");
		}else if(v == button_a5){
			prepareArrayLits("a5");
		}else if(v == button_b1){
			prepareArrayLits("b1");
		}else if(v == button_b2){
			prepareArrayLits("b2");
		}else if(v == button_c){
			prepareArrayLits("c");
		}else if(v == button_d){
			prepareArrayLits("d");
		}else if(v == button_e){
			prepareArrayLits("e");
		}else if(v == button_f){
			prepareArrayLits("f");
		}else if(v == button_g){
			prepareArrayLits("g");
		}else if(v == button_h){
			prepareArrayLits("h");
		}else if(v == button_i){
			prepareArrayLits("i");
		}else if(v == button_j){
			prepareArrayLits("j");
		}
	}

	private void sitesArraySorting(ArrayList<Site> newSitesArray) {
		ComparatorSite Comparator = new ComparatorSite();
		// TODO Auto-generated method stub
		Collections.sort(newSitesArray, Comparator);
	}
	public class ComparatorSite implements Comparator<Site>{
		
		@Override 
		 public int compare(Site siteA, Site siteB) {
			 double distanceA = siteA.getDistance();
			 double distanceB = siteB.getDistance();
			 
			 if (distanceA>distanceB){
				 return 1;
			 }
			 else if(distanceA < distanceB){
				 return -1;
			 }
			 else{
				 return 0;
			 }

		 }
	}
	
	@Override
	  protected void onDestroy() {
	    super.onDestroy();
	    // Stop the tracker when it is no longer needed.
		    try{
		    tracker.dispatch();
		    tracker.stopSession();
		    }catch(Exception e){
		    	
		    }
		 // clear cache in arraylist
		    adapter.imageLoader.clearCache();
	  }
}
