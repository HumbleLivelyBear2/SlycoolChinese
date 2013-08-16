package com.sandj.slycool.themes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.sandj.slycool.slycoupon.ListOfRestaurantActivity;
import com.sandj.slycoolchinese.Constants;
import com.sandj.slycoolchinese.R;
import com.sandj.slycoolchinese.Site;
import com.sandj.slycoolchinese.Themes;
import com.techvalens.restaurant.grouptabbar.TabGroupActivity;



/** 
 * Change to 主題好康
 * */
public class SlyThemesActivity extends Activity implements OnItemClickListener 
{

//	private ImageButton _refreshImageButton =null;
	private ListView listview=null;
	private ListViewNewRestaurantAdapter adapter;
	private ArrayList<Object> itemList=new ArrayList<Object>();
	private ItemNewRestaurants bean;
	private ProgressDialog progressDialog;
	private ProgressDialog progressDialog_1;
	private GoogleAnalyticsTracker tracker;
	private String theme_file_name="theme.csv";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_new_restaurants);
		
		if(Constants.extStorageDirectory.equals("")){
			Constants.extStorageDirectory = Environment.getExternalStorageDirectory().toString();
			Constants.latitudeValue =25.090729;
			Constants.longitudeValue =121.522423;
		}
		
		
//		Constants.allthemes.clear();
		
		tracker = GoogleAnalyticsTracker.getInstance();
		 // Start the tracker in manual dispatch mode...
	    tracker.startNewSession("UA-34583559-1", this);
	    tracker.trackPageView("/themelist");
		
		listview =(ListView)findViewById(R.id.listView1);
		listview.setOnItemClickListener(this);
		
		if (Constants.allthemes.size()==0){
			new MyTask().execute();
		}else{
			setAllValues();
		}

		

	}
	
	
	
	private class MyTask extends AsyncTask{
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = ProgressDialog.show(SlyThemesActivity.this, "",Constants.PROCESSING_REQUEST);
		}

		@Override
		protected Object doInBackground(Object... params) {
			setThemesData();
			return null;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);		
			progressDialog.dismiss();
			setAllValues();
		}
	}
	
	
	
	private void setThemesData() {
		// TODO Auto-generated method stub
		int index = 0;
		try {
	    	String strFile =  Constants.extStorageDirectory +"/Android_SlyCool/"+theme_file_name;
			BufferedReader reader = new BufferedReader( new FileReader(strFile));
			LineNumberReader lineReader = new LineNumberReader(reader);
	        String line;
	        while ((line = lineReader.readLine()) != null) {
	        	if(lineReader.getLineNumber()!=1){
	        		String[] RowData = Constants.parseCsvLine(line.toString());
	        		if (RowData.length==14){
	        			for(int j=0; j<13;j++){	
	        				String currentString = RowData[j];
	        				currentString = currentString.replaceAll("\\\\n", "\n");
	        				RowData[j] = currentString;
	        			}
		        		Themes currentTheme = new Themes(String.valueOf(index),RowData[0],RowData[1],RowData[2],RowData[3],RowData[4],RowData[5],RowData[6],RowData[7],RowData[8],RowData[9],RowData[10],RowData[11],RowData[12],RowData[13]);
				    	Constants.allthemes.add(currentTheme);
				    	index++;
	        		}
	        	}       	
	        }
		}catch (IOException ex) {
	       
	    }
	}
	
	
	/**
	 *  Add new restaurants one by one to itemList 
	 *  We need put data to data_newRestaurantDetailsByStoreId or itemList !!
	 * */
	public void setAllValues() {
//		NewRestaurantDetailsByStoreId newRestaurantDetailsByStoreId = null;
		
		if (Constants.allthemes!=null){
		    int length = Constants.allthemes.size();
		    for(int i=0;i<length;i++){
		    	Themes currentTheme = Constants.allthemes.get(i);
		    	AddObjectToList(currentTheme.photo_url,
		    			currentTheme.title, 
						"~"+currentTheme.content,
						"",
						currentTheme.themeIndex);
		    }
		    
		    
		    runOnUiThread(new Runnable() {
				@Override
				public void run() {
					prepareArrayLits();//CREATE LIST VIEW OF THE NEW RESTAURANTS
				}
			});
		}else{
			Toast.makeText(SlyThemesActivity.this,  "無資料,請重開App.",Toast.LENGTH_SHORT).show();
		}
	}

	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
		
		TextView _storeIdTextView = (TextView)view.findViewById(R.id.txtStoreId_itemlist);

		/*RelativeLayout _relativeLayout = (RelativeLayout) view;
		TextView _storeIdTextView = (TextView)_relativeLayout.getChildAt(4);*/

		String row_id = _storeIdTextView.getText().toString();
		System.out.println(row_id);

		Intent intent = new Intent(SlyThemesActivity.this, SlyCoolGoodNewsDetail.class);
//		TabGroupActivity parentActivity = (TabGroupActivity)getParent();
		intent.putExtra("ROW_ID", row_id);
//		parentActivity.startChildActivity("SlyCoolGoodNewsDetail", intent);
		startActivity(intent);

	}
	
	/** 
	 *  This run on a thread. 
	 *  It will set listview when got datas from server
	 * */
	private void prepareArrayLits(){	
		progressDialog_1 = ProgressDialog.show(SlyThemesActivity.this, "",Constants.PROCESSING_REQUEST);

		adapter = new ListViewNewRestaurantAdapter(this, itemList);
		listview.setAdapter(adapter);
		progressDialog_1.dismiss();
	}

	// Add one item into the Array List
	public void AddObjectToList(String image, String name, String address,String distances, String storeId)
	{
		bean = new ItemNewRestaurants();
		bean.setAddress(address);
		bean.setImage(image);
		bean.setName(name);
		bean.setDistances(distances);
		bean.setStoreId(storeId);
		itemList.add(bean);
	}
	
	
	/** Show Alert When Net Fails*/
//	private void networkFailedALERT(){
//		/*new AlertDialog.Builder(getParent()).setMessage(“Hello world”).show();*/
//		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getParent());
//		// set title
//		alertDialogBuilder.setTitle(R.string.caution);
//		// set dialog message
//		alertDialogBuilder
//		.setMessage(R.string.network_msg)
//		.setCancelable(false)
//		.setNegativeButton(R.string.ok,new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog,int id) {
//				// if this button is clicked, just close
//				// the dialog box and do nothing
//				dialog.cancel();
//			}
//		});
//		// create alert dialog
//		AlertDialog alertDialog = alertDialogBuilder.create();
//		// show it
//		alertDialog.show();
//	}
	/** onKeyDwon => Do nothing! */
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			//preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}

	/*	@Override
	public void onGPSUpdate(Location location) {
		location_latitude = location.getLatitude();
		location_longitude = location.getLongitude();
	}*/

	@Override
	  protected void onDestroy() {
	    super.onDestroy();
	    // Stop the tracker when it is no longer needed.
		    try{
		    tracker.dispatch();
		    tracker.stopSession();
		    }catch(Exception e){
		    	
		    }
	  }

}
