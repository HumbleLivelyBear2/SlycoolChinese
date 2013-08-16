package com.sandj.slycool.themes;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.sandj.coverflow.ImageLoader;
import com.sandj.slycool.more.MoreLink;
import com.sandj.slycoolchinese.Constants;
import com.sandj.slycoolchinese.R;
import com.sandj.slycoolchinese.Themes;
import com.techvalens.restaurant.grouptabbar.TabGroupActivity;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SlyCoolGoodNewsDetail  extends Activity{
	
	private TextView textview_title_1;
	private TextView textview_title_2;
	private TextView textview_title_3;
	private TextView textview_title_4;
	private TextView textview_title_5;
	private TextView textview_title_6;
	private TextView textview_content_1;
	private TextView textview_content_2;
	private TextView textview_content_3;
	private TextView textview_content_4;
	private TextView textview_content_5;
	private TextView textview_content_6;
	
	private LinearLayout linearlayout_1;
	private LinearLayout linearlayout_2;
	private LinearLayout linearlayout_3;
	private LinearLayout linearlayout_4;
	private LinearLayout linearlayout_5;
	private LinearLayout linearlayout_6;
	private LinearLayout linearlayout_info_link;
	
	private Button button_more_info_link;
	
	private String imgUrl;
	private Drawable image;
	
	ImageView news_image;
	private ProgressDialog progressDialog;
	Drawable imgDraw;
	Handler handler;
	private Themes currentTheme;
	int rowId;
	
	private ImageLoader imageLoader;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		news_image = (ImageView) findViewById(R.id.slycool_news_image);
		
		setContentView(R.layout.slycool_news_deatil);
		
		textview_title_1 = (TextView) findViewById(R.id.textview_title_1);
		textview_title_2 = (TextView) findViewById(R.id.textview_title_2);
		textview_title_3 = (TextView) findViewById(R.id.textview_title_3);
		textview_title_4 = (TextView) findViewById(R.id.textview_title_4);
		textview_title_5 = (TextView) findViewById(R.id.textview_title_5);
		textview_title_6 = (TextView) findViewById(R.id.textview_title_6);
		textview_content_1 = (TextView) findViewById(R.id.textview_content_1);
		textview_content_2 = (TextView) findViewById(R.id.textview_content_2);
		textview_content_3 = (TextView) findViewById(R.id.textview_content_3);
		textview_content_4 = (TextView) findViewById(R.id.textview_content_4);
		textview_content_5 = (TextView) findViewById(R.id.textview_content_5);
		textview_content_6 = (TextView) findViewById(R.id.textview_content_6);
		linearlayout_1 = (LinearLayout) findViewById(R.id.linearlayout_1);
		linearlayout_2 = (LinearLayout) findViewById(R.id.linearlayout_2);
		linearlayout_3 = (LinearLayout) findViewById(R.id.linearlayout_3);
		linearlayout_4 = (LinearLayout) findViewById(R.id.linearlayout_4);
		linearlayout_5 = (LinearLayout) findViewById(R.id.linearlayout_5);
		linearlayout_6 = (LinearLayout) findViewById(R.id.linearlayout_6);
		linearlayout_info_link = (LinearLayout) findViewById(R.id.linearlayout_info_link);
		button_more_info_link = (Button) findViewById(R.id.more_info_link);
		
		Intent intent= getIntent();
		String id = intent.getStringExtra("ROW_ID");
		
		searchData(id);
		
	}
	

	private void searchData(String index) {
		// TODO Auto-generated method stub
		int length = Constants.allthemes.size();
		for(int i=0;i<length;i++){
			String checkIndex = Constants.allthemes.get(i).themeIndex;
			if(checkIndex.equals(index)){
				currentTheme =  Constants.allthemes.get(i);
				setAllValues();
			}
		}
	}
	
	private void setAllValues() {
		// TODO Auto-generated method stub
		imgUrl = "";
		
		String deadLine = "";
		String title = "";
		String title_a = "";
		String title_b = "";
		String title_c = "";
		String content ="";
		String content_a ="";
		String content_b ="";
		String content_c ="";
		String moreinfo ="";
		String more_info_link ="";
	        
	    imgUrl = currentTheme.photo_url;
	    title = currentTheme.title;
	    content =currentTheme.content;
	    title_a = currentTheme.title_a;
	    content_a = currentTheme.content_a;
	    title_b = currentTheme.title_b;
	    content_b = currentTheme.content_b;
	    title_c = currentTheme.title_c;
	    content_c = currentTheme.content_c;
	    moreinfo = currentTheme.more_info;
	    deadLine = currentTheme.deadline;
	    more_info_link =currentTheme.more_info_link;
	    
	    textview_title_1.setText(title);
	    textview_content_1.setText(content);
	    textview_title_2.setText(title_a);
	    textview_content_2.setText(content_a);
	    textview_title_3.setText(title_b);
	    textview_content_3.setText(content_b);
	    textview_title_4.setText(title_c);
	    textview_content_4.setText(content_c);
	    textview_content_5.setText(moreinfo);
	    textview_content_6.setText(deadLine);
	    
	    if (!title.equals("N/A")){ linearlayout_1.setVisibility(View.VISIBLE);}
	    if (!title_a.equals("N/A")){ linearlayout_2.setVisibility(View.VISIBLE);}
	    if (!title_b.equals("N/A")){ linearlayout_3.setVisibility(View.VISIBLE);}
	    if (!title_c.equals("N/A")){ linearlayout_4.setVisibility(View.VISIBLE);}
	    if (!moreinfo.equals("N/A")){ linearlayout_5.setVisibility(View.VISIBLE);}
	    if (!more_info_link.equals("N/A")){ linearlayout_info_link.setVisibility(View.VISIBLE);
	    	button_more_info_link.setOnClickListener(new OnClickListener(){  
	            public void onClick(View v) {  
	            	Intent intent = new Intent(SlyCoolGoodNewsDetail.this, MoreLink.class);
//	      			TabGroupActivity parentActivity = (TabGroupActivity)getParent();
	      			intent.putExtra("url", currentTheme.more_info_link);
//	      			parentActivity.startChildActivity("MoreLink", intent);
	      			startActivity(intent);
	            }
	        });  
	    
	    }
	    if (!deadLine.equals("N/A")){ linearlayout_6.setVisibility(View.VISIBLE);}
	    
	    
	    try {
			SetContent(imgUrl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void SetContent(String imgUrl) throws IOException {
		// TODO Auto-generated method stub
		news_image =new ImageView(this);
		news_image = (ImageView) findViewById(R.id.slycool_news_image);
//		new MyTask().execute();
        
		imageLoader=new ImageLoader(SlyCoolGoodNewsDetail.this);   
		imageLoader.DisplayImage(currentTheme.photo_url, news_image);
		
	}

	
	private class MyTask extends AsyncTask{

		Dialog dialog;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = ProgressDialog.show(SlyCoolGoodNewsDetail.this, "",Constants.PROCESSING_REQUEST);
		}

		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			
//			image = LoadImageFromWebOperations(imgUrl);
			imageLoader=new ImageLoader(SlyCoolGoodNewsDetail.this);   
			imageLoader.DisplayImage(currentTheme.photo_url, news_image);	
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
//			news_image.setImageDrawable(image);
			progressDialog.dismiss();
			
			
		}
	}
	
	@Override
	  protected void onDestroy() {
	    super.onDestroy();
		 // clear cache in arraylist
	    imageLoader.clearCache();
	  }
	 
	 
}	
