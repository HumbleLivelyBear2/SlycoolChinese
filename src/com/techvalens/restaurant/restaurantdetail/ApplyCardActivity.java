package com.techvalens.restaurant.restaurantdetail;



import com.sandj.slycoolchinese.Constants;
import com.sandj.slycoolchinese.R;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class ApplyCardActivity extends Activity{
	
	private WebView mWebView;
	private String url;
	private ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webinfo);
		
//		Bundle bData = this.getIntent().getExtras();
		url ="http://www.slycool.com/contact-us/seller";
        
        mWebView = (WebView)findViewById(R.id.mybrowser);
        
        mWebView.getSettings().setSupportZoom(true); 
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);   
    	mWebView.setWebViewClient(new WebViewClient());
//    	mWebView.loadUrl(url);
    	
    	
    	new MyTask().execute();
    	
//    	progressDialog = ProgressDialog.show(getParent(), "",Constants.PROCESSING_REQUEST);
//    	runOnUiThread(new Runnable() {
//			@Override
//			public void run() {
//				mWebView.loadUrl(url);
//		    	progressDialog.dismiss();//CREATE LIST VIEW OF THE NEW RESTAURANTS
//			}
//		});
    	
	}
	
	private class MyTask extends AsyncTask{

		Dialog dialog;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = ProgressDialog.show(getParent(), "",Constants.PROCESSING_REQUEST);
		}

		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			mWebView.loadUrl(url);
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			progressDialog.dismiss();
			
			
		}
	}

}
