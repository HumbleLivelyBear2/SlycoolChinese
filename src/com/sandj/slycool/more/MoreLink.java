package com.sandj.slycool.more;



import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.sandj.slycoolchinese.Constants;
import com.sandj.slycoolchinese.R;





public class MoreLink extends Activity{
	
	private WebView mWebView;
	private String url;
	private ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webinfo);
		
		Bundle bData = this.getIntent().getExtras();
		url = bData.getString("url");
        
        mWebView = (WebView)findViewById(R.id.mybrowser);
        
        mWebView.getSettings().setSupportZoom(true); 
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);   
    	mWebView.setWebViewClient(new WebViewClient());
//    	mWebView.loadUrl(url);
    	
    	
    	new MyTask().execute();
    	
    	
	}
	
	private class MyTask extends AsyncTask{

		Dialog dialog;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			try{
				progressDialog = ProgressDialog.show(getParent(), "",Constants.PROCESSING_REQUEST);
			}catch(Exception e){
				progressDialog = ProgressDialog.show(MoreLink.this, "",Constants.PROCESSING_REQUEST);
			}
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