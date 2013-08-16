package com.sandj.coverflow;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.sandj.slycool.more.MoreLink;
import com.sandj.slycoolchinese.Constants;
import com.sandj.slycoolchinese.PromoteData;
import com.sandj.slycoolchinese.R;
import com.sandj.slycoolchinese.SplashScreen;
import com.sandj.slycoolchinese.TabActivity;
import com.techvalens.restaurant.restaurantdetail.RestaurantsDetail;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class CoverFlowActivity extends Activity {
	
	private CoverFlow1 cover_flow;
	private CoverFlow2 cover_flow2;
	private ImageView imageview_bar;
	GoogleAnalyticsTracker tracker;
	private AlertDialog.Builder finishDialog;
	private AdView adView;
	
	private ImageAdapter2 imageAdapter2;
	private String promote_file_name="promote.csv";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_coverflow);
        
        if(Constants.extStorageDirectory.equals("")){
			Constants.extStorageDirectory = Environment.getExternalStorageDirectory().toString();
			Constants.latitudeValue =25.090729;
			Constants.longitudeValue =121.522423;
		}
        
       
            
        adView = new AdView(this, AdSize.BANNER, "a15067ff361900f ");
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.ad_banner_coverflow);
        layout.addView(adView);
        adView.loadAd(new AdRequest());
        
        
        tracker = GoogleAnalyticsTracker.getInstance();
	    tracker.startNewSession("UA-34583559-1", this);
	    tracker.trackPageView("/coverflowactivity");    
	    
        cover_flow = (CoverFlow1) findViewById(R.id.cover_flow);
        cover_flow2 = (CoverFlow2) findViewById(R.id.cover_flow2);
        imageview_bar = (ImageView) findViewById(R.id.imageview_bar);
        
        setPromoteStores();
    	

        ImageAdapter imageAdapter = new ImageAdapter(this);
		cover_flow.setAdapter(imageAdapter);
		// cf.setAlphaMode(false);
		// cf.setCircleMode(false);
		cover_flow.setSelection(2, true);
		cover_flow.setAnimationDuration(1000);
	
		
		cover_flow.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				Toast.makeText(CoverFlowActivity.this, "this is test"+position, Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(CoverFlowActivity.this, TabActivity.class);
      			intent.putExtra("tabIndex", position);
      			startActivity(intent);
//      			finish();
			}
		});
	}
    
  
	
    private void setPromoteStores() {
		// TODO Auto-generated method stub
    	
		
		imageAdapter2 = new ImageAdapter2(CoverFlowActivity.this);
		cover_flow2.setAdapter(imageAdapter2);
		cover_flow2.setSelection(0, true);
		cover_flow2.setAnimationDuration(1000);			
		
		cover_flow2.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				Toast.makeText(MainActivity.this, "this is coverflow2 at "+position, Toast.LENGTH_SHORT).show();
				
				if(Constants.allPromoteData.size()!=0){
				    Intent intent = new Intent(CoverFlowActivity.this, PromoteActivity.class);
					intent.putExtra("promoteID", position);
			      	startActivity(intent);
				}else{
					
				}
				
				
			}
		});
		
		cover_flow2.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if(position==0){
					imageview_bar.setBackgroundDrawable(getResources().getDrawable(R.drawable.galary_bar1));
				}else if(position==1){
					imageview_bar.setBackgroundDrawable(getResources().getDrawable(R.drawable.galary_bar2));
				}else if(position==2){
					imageview_bar.setBackgroundDrawable(getResources().getDrawable(R.drawable.galary_bar3));
				}
			}
			
			@Override  
			  public void onNothingSelected(AdapterView<?> arg0) {  
			   // TODO Auto-generated method stub  
			    
			  }  
		});
	}
    
	
   
    
	@Override
    public void  onBackPressed  () {  
			finishDialog = new AlertDialog.Builder(this).setTitle("結束執行")
					.setMessage("是否離開士林任我行?")
					.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish();
							System.exit(0);
						}
					})
					.setNegativeButton("No", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			finishDialog.show();          
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
	     imageAdapter2.imageLoader.clearCache();
	  }
}