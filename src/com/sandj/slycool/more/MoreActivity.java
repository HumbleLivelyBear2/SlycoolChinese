package com.sandj.slycool.more;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.sandj.slycoolchinese.Constants;
import com.sandj.slycoolchinese.R;
import com.techvalens.restaurant.grouptabbar.TabGroupActivity;



public class MoreActivity extends Activity{
	
	String extStorageDirectory;
	String more_URL="https://docs.google.com/spreadsheet/pub?key=0AquU7odNIm9edFJnX0RBbnhpWFJFXzVGQzgtTGs2Wnc&output=csv";
	String more_file_name="more.csv";
	
	private GoogleAnalyticsTracker tracker;
	
	private ProgressDialog progressDialog;
//	ArrayList<NameToContent> moreArray = new ArrayList<NameToContent>();
	private ListView listview=null;
	private SimpleAdapter adapter;
	ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutscreen);
		
		tracker = GoogleAnalyticsTracker.getInstance();
		 // Start the tracker in manual dispatch mode...
	    tracker.startNewSession("UA-34583559-1", this);
	    tracker.trackPageView("/more_activity");
		
		listview =(ListView)findViewById(R.id.mainListView);
		extStorageDirectory = Environment.getExternalStorageDirectory().toString();
		new MyTask().execute();
		
		
	}

	private class MyTask extends AsyncTask{

		Dialog dialog;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = ProgressDialog.show(MoreActivity.this, "",Constants.PROCESSING_REQUEST);
		}

		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			
			try {
		    	String strFile =  extStorageDirectory +"/Android_SlyCool/"+more_file_name;
				BufferedReader reader = new BufferedReader( new FileReader(strFile));
				LineNumberReader lineReader = new LineNumberReader(reader);
		        String line;
		        while ((line = lineReader.readLine()) != null) {
		        	if(lineReader.getLineNumber()!=1){
		        		String[] RowData = Constants.parseCsvLine(line.toString());
		        		if (RowData.length==2){
		        			HashMap<String,String> item = new HashMap<String,String>();
		    				item.put( "title", RowData[0]);
		    				item.put( "content",RowData[1]);
		    				list.add( item );
		        		}
		        	}       	
		        }
			}catch (IOException ex) {
		       
		    }
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			setListView();
		}

		private void setListView() {
			// TODO Auto-generated method stub
			adapter = new SimpleAdapter( 
					 MoreActivity.this, 
					 list,
					 R.layout.morelistview,
					 new String[] { "title","content" },
					 new int[] { R.id.textView1, R.id.textView2 } );
			listview.setAdapter(adapter);
			listview.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					TextView v1 = (TextView)view.findViewById(R.id.textView1);
					TextView v2 = (TextView)view.findViewById(R.id.textView2);
					
					String url = v2.getText().toString();
	      			if(position==7){
	      				Intent intent = new Intent(MoreActivity.this, RunningTeamActivity.class);
//		      			TabGroupActivity parentActivity = (TabGroupActivity)getParent();     				
//		      			parentActivity.startChildActivity("RunningTeamActivity", intent);
	      				startActivity(intent);
	      			}else if(position==9){
	      				Intent intent = new Intent(MoreActivity.this, BePartnerActivity.class);
//		      			TabGroupActivity parentActivity = (TabGroupActivity)getParent();
//		      			parentActivity.startChildActivity("BePartnerActivity", intent);
		      			startActivity(intent);
	      			}else{
		      			Intent intent = new Intent(MoreActivity.this, MoreLink.class);
//		      			TabGroupActivity parentActivity = (TabGroupActivity)getParent();
		      			intent.putExtra("url", url);
//		      			parentActivity.startChildActivity("MoreLink", intent);
		      			startActivity(intent);
	      			}

				}
			});
		}
		
	}
	
	/** onKeyDwon => Do nothing! */
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			//preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}
	
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
