package com.sandj.slycoolchinese;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.sandj.slycool.slycoupon.ListOfRestaurantActivity;
import com.sandj.slycool.slymap.CustomMap;
import com.techvalens.restaurant.grouptabbar.TabGroupActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class MapAndListActivity  extends Activity {
	
	private Button toMap;
	private Button toSlycool;
	private GoogleAnalyticsTracker tracker;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_and_list);
		
		tracker = GoogleAnalyticsTracker.getInstance();
		 // Start the tracker in manual dispatch mode...
	    tracker.startNewSession("UA-34583559-1", this);
	    tracker.trackPageView("/more_and_list");
		
		toMap = (Button) findViewById(R.id.button_to_map);
		toSlycool = (Button) findViewById(R.id.button_to_slycool);
		
		toMap.setOnClickListener(new OnClickListener(){  
            public void onClick(View v) {  
            	Intent intent = new Intent(MapAndListActivity.this, CustomMap.class);
//        		TabGroupActivity parentActivity = (TabGroupActivity)getParent();
//        		parentActivity.startChildActivity("CustomMap", intent);
            	startActivity(intent);
            }
         });
		toSlycool.setOnClickListener(new OnClickListener(){  
            public void onClick(View v) {  
            	Intent intent = new Intent(MapAndListActivity.this, ListOfRestaurantActivity.class);
//        		TabGroupActivity parentActivity = (TabGroupActivity)getParent();
//        		parentActivity.startChildActivity("ListOfRestaurantActivity", intent);
        		startActivity(intent);
            }
         });  
		
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
	  }
}