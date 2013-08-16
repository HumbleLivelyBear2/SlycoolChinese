package com.techvalens.restaurant.restaurantdetail;


import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.androidfacebookcheckwithphoto.MainActivity;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.sandj.coverflow.ImageLoader;
import com.sandj.slycool.more.MoreLink;
import com.sandj.slycool.slycard.ConfirmCardActivity;
import com.sandj.slycool.themes.SlyCoolGoodNewsDetail;
import com.sandj.slycoolchinese.Constants;
import com.sandj.slycoolchinese.R;
import com.sandj.slycoolchinese.Site;
import com.techvalens.restaurant.grouptabbar.TabGroupActivity;





public class RestaurantsDetail extends Activity implements OnClickListener{

	private ImageView _logoImageView = null;
	private ImageButton _menuImageButton = null;
	private ImageButton _mapRedirectImageButton = null;
	private ImageButton _callRedirectImageButton = null;
	private ImageButton _aboutRedirectImageButton = null;
	private Button buttonPartTime = null;
	private Button buttonFBCheckIn = null;
	private Button buttonFBlike = null;

	private TextView _addressTextView=null;
	private TextView _phoneTextView=null;
	private TextView _openCloseTimeTextView=null;
	private TextView _storeInfoTextView = null;
	private TextView _discountTextView=null;
	private TextView _storeTitle = null;

	
	private String _storeIDParameter = null;
	private Handler mHandler = new Handler();
	
	private Drawable image;
	private ProgressDialog progressDialog;
	
	private Site thisSite;
	private GoogleAnalyticsTracker tracker;
	
	private ImageLoader imageLoader;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.restaurants_detail);

		tracker = GoogleAnalyticsTracker.getInstance();
		 // Start the tracker in manual dispatch mode...
	    tracker.startNewSession("UA-34583559-1", this);
	    tracker.trackPageView("/store_detail");
		

		_storeIDParameter = getIntent().getStringExtra("STORE_ID");


		_storeTitle = (TextView)findViewById(R.id.textview_store_title);;
		_logoImageView = (ImageView)findViewById(R.id.restra_logo_ImageView);
//		_backImageButton=(ImageButton)findViewById(R.id.backimagebtn);
		/*_shareButton=(ImageButton)findViewById(R.id.sharebtn);*/
		_addressTextView=(TextView)findViewById(R.id.address_TxtView);
		_phoneTextView=(TextView)findViewById(R.id.phone_TxtView);
		_openCloseTimeTextView=(TextView)findViewById(R.id.openClosetime_TxtView);
		_menuImageButton  =(ImageButton)findViewById(R.id.menu_ImageButton);
		_storeInfoTextView = (TextView)findViewById(R.id.StoreInfo_textview);
		_discountTextView=(TextView)findViewById(R.id.discount_TxtView);

		_mapRedirectImageButton = (ImageButton)findViewById(R.id.redirect_toMap);
		_callRedirectImageButton = (ImageButton)findViewById(R.id.redirect_Call);
		_aboutRedirectImageButton  =(ImageButton)findViewById(R.id.redirect_AboutUs);

		buttonPartTime = (Button)findViewById(R.id.button_parttime);
		buttonFBCheckIn = (Button)findViewById(R.id.fb_checkin_button);
		buttonFBlike = (Button)findViewById(R.id.button_fb_like);
		
		buttonFBlike.setOnClickListener(this);
		buttonPartTime.setOnClickListener(this);
		buttonFBCheckIn.setOnClickListener(this);
		_addressTextView.setOnClickListener(this);
		_phoneTextView.setOnClickListener(this);
		_discountTextView.setOnClickListener(this);
		_menuImageButton.setOnClickListener(this);
		_mapRedirectImageButton.setOnClickListener(this);
		_callRedirectImageButton.setOnClickListener(this);
		_aboutRedirectImageButton.setOnClickListener(this);
		
		getData();
		
		
	}
	
	
	private void getData() {
		
		int length = Constants.allSites.size();
		for (int i= 0; i<length;i++){
			Site checkSite = Constants.allSites.get(i);
			if (checkSite.id.equals(_storeIDParameter)){
				thisSite = checkSite;
			}
		}
		setAllValues();
	}
	
	/** 
	 *  Set Values, we need change it
	 * */
	public void setAllValues() {
		
		if(thisSite != null){
			
//			new MyTask().execute();
			
			imageLoader=new ImageLoader(RestaurantsDetail.this);   
			imageLoader.DisplayImage(thisSite.photoUrl, _logoImageView);
			
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
//					Drawable imgDraw = LoadImageFromWebOperations(thisSite.photoUrl);
//					_logoImageView.setImageDrawable(imgDraw);
					setDatasView();//CREATE LIST VIEW OF THE NEW RESTAURANTS
				}

				
			});

		}else{
			Toast.makeText(RestaurantsDetail.this,  "網路繁忙,請重開.",Toast.LENGTH_SHORT).show();
		}
		                   
	}
	
	
	private class MyTask extends AsyncTask{

		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			try{
				progressDialog = ProgressDialog.show(RestaurantsDetail.this, "",Constants.PROCESSING_REQUEST);
			}catch(Exception e){
				progressDialog = ProgressDialog.show(getParent(), "",Constants.PROCESSING_REQUEST);
			}
		}

		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			
//			image = LoadImageFromWebOperations(thisSite.photoUrl);
			
			imageLoader=new ImageLoader(RestaurantsDetail.this);   
			imageLoader.DisplayImage(thisSite.photoUrl, _logoImageView);
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
//			_logoImageView.setImageDrawable(image);
			progressDialog.dismiss();
			
			
		}
	}
	
	public static Drawable LoadImageFromWebOperations(String url) {
	    try {
	        InputStream is = (InputStream) new URL(url).getContent();
	        Drawable d = Drawable.createFromStream(is, "src name");
	        return d;
	    } catch (Exception e) {
	        return null;
	    }
	}
	
	private void setDatasView() {
		// TODO Auto-generated method stub
		_addressTextView.setText(thisSite.siteAddress);
		_phoneTextView.setText(thisSite.sitePhone);
		//Menu
		_openCloseTimeTextView.setText(thisSite.siteTime);
		//pictures
		_storeInfoTextView.setText("出示士林卡或任一聯名卡:");
		_discountTextView.setText(thisSite.siteCoupon);
		_storeTitle.setText(thisSite.siteName);
		
	}

	
	
	
	
	/** 
	 *  On Click
	 * */
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.button_parttime:
			String url = thisSite.partTimeJob;
			if (!url.equals("N/A")){
				try{
					Intent intent = new Intent(getParent(), MoreLink.class);
		  			TabGroupActivity parentActivity = (TabGroupActivity)getParent();
		  			intent.putExtra("url", url);
		  			parentActivity.startChildActivity("MoreLink", intent);
				}catch(Exception e){
					Intent intent = new Intent(this, MoreLink.class);
					startActivity(intent);
				}
			}else{
				Toast.makeText(this, "目前無缺額", Toast.LENGTH_LONG).show();
			}
			break;
			
		case R.id.fb_checkin_button:
			Boolean check = isOnline();
			if(check){
				Intent intent = new Intent(RestaurantsDetail.this, MainActivity.class);
				startActivity(intent);
			}else{
				Toast.makeText(this, "無網路", Toast.LENGTH_LONG).show();
			}
			break;
		
		case R.id.button_fb_like:
			 if (!thisSite.fbUrl.equals("N/A")){
				String fb_url = thisSite.fbUrl;
				Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fb_url));
		        startActivity(myIntent);
			 }else{
				Toast.makeText(this, "此商家無粉絲專頁", Toast.LENGTH_LONG).show();
			 };
			break;
			
		case R.id.menu_ImageButton:
			 if (thisSite.menuUrl.equals("N/A")){
				Toast.makeText(this, "目前無此商家Menu", Toast.LENGTH_LONG).show();
			 }else{
				 String menu_url = thisSite.fbUrl;
				 try{
					 Intent intentMenu = new Intent(getParent(), MoreLink.class);
			  		 TabGroupActivity parentActivity = (TabGroupActivity)getParent();
			  		 intentMenu.putExtra("url", menu_url);
			  	     parentActivity.startChildActivity("MoreLink", intentMenu);
				 }catch(Exception e){
					 Intent intentMenu = new Intent(RestaurantsDetail.this, MoreLink.class);
					 intentMenu.putExtra("url", menu_url);
					 startActivity(intentMenu);
				 }			 
			 }
			break;

		case R.id.redirect_toMap:
			double xLoc1 = Double.parseDouble(thisSite.xLoc);
			double yLoc1 = Double.parseDouble(thisSite.yLoc);
			clickOnAndroidRoute(Constants.latitudeValue,Constants.longitudeValue,xLoc1,yLoc1);
			break;

		case R.id.address_TxtView:
			double xLoc = Double.parseDouble(thisSite.xLoc);
			double yLoc = Double.parseDouble(thisSite.yLoc);
			clickOnAndroidRoute(Constants.latitudeValue,Constants.longitudeValue,xLoc,yLoc);
			break;

		case R.id.redirect_Call:
			String phone_number1 = (String) _phoneTextView.getText();
			clickOnAndroidPhone(phone_number1);
			break;

		case R.id.phone_TxtView:
			String phone_number = (String) _phoneTextView.getText();
			clickOnAndroidPhone(phone_number);
			break;

		case R.id.redirect_AboutUs:
			try{
			Intent aboutIntent =  new Intent(this,ConfirmCardActivity.class);
//			aboutIntent.putExtra("restaurantDetailFlag", true);
			TabGroupActivity parentActivityAbout = (TabGroupActivity)getParent();
			parentActivityAbout.startChildActivity("ShowCardActivity",aboutIntent);	
			}catch(Exception e){
				Intent aboutTxtIntent =  new Intent(this,ConfirmCardActivity.class);
				startActivity(aboutTxtIntent);
			}
			break;
		case R.id.discount_TxtView:
			try{
				Intent aboutTxtIntent =  new Intent(this,ConfirmCardActivity.class);
				aboutTxtIntent.putExtra("restaurantDetailFlag", true);
				TabGroupActivity parentActivityAboutText = (TabGroupActivity)getParent();
				parentActivityAboutText.startChildActivity("AboutActivity",aboutTxtIntent);
			}catch(Exception e){
				Intent aboutTxtIntent =  new Intent(this,ConfirmCardActivity.class);
				startActivity(aboutTxtIntent);
			}
			break;

		default:
			break;

		}


	}
	
	

	
	public void clickOnAndroidPhone(String phonenumber) {
    	final String myPhoneNumber = phonenumber;
        mHandler.post(new Runnable() {
            public void run() {
            	Uri uri = Uri.parse("tel:"+myPhoneNumber);  
            	Intent it = new Intent(Intent.ACTION_DIAL, uri);  
            	RestaurantsDetail.this.startActivity(it);                      
            }
        });
	} 

	 public void clickOnAndroidRoute(double startLat, double startLng, double endLat, double endLng) {        	
	    	final String routeUrl = "http://maps.google.com/maps?f=d&saddr="+startLat+","+startLng+
									"&daddr="+endLat+","+endLng+"&hl=tw";        	
	        mHandler.post(new Runnable() {
	            public void run() {                	
	            	Uri uri = Uri.parse(routeUrl);  
	            	Intent it = new Intent(Intent.ACTION_VIEW, uri);  
	            	RestaurantsDetail.this.startActivity(it);            	                      
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
		    imageLoader.clearCache();
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
	
}
