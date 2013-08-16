package com.sandj.slycool.slycard;


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
import java.util.Calendar;

import com.flurry.android.FlurryAgent;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.sandj.slycool.more.MoreLink;
import com.sandj.slycoolchinese.Constants;
import com.sandj.slycoolchinese.R;
import com.techvalens.restaurant.grouptabbar.TabGroupActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ConfirmCardActivity extends Activity{
	
	String extStorageDirectory;
	String check_URL="https://docs.google.com/spreadsheet/pub?key=0Am8sCHFg0R71dHFpcFliek5hdVNmTHJPVnRDWENvTkE&output=csv";
	String check_file_name="check.csv";
	
	private EditText edit_name;
	private EditText edit_card_id;
	private Button button_confirm;
	private Button button_applycard;
	private Button button_opencard;
	
	private String cardType = "";
	private String cardDeadLine ="";
	private String cardName;
	private String cardId;
	
	private TextView cardOwner;
	private TextView textViewCardDeadLine;
	private ImageView cardView;
	private LinearLayout layoutConfirm;
	
	private SharedPreferences settings;
	private int currentTime;
	private GoogleAnalyticsTracker tracker;
	private AdView adView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comfirm_card);
		
		
		adView = new AdView(this, AdSize.BANNER, "a15067ff361900f ");
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.ad_banner_slycard);
        layout.addView(adView);
        adView.loadAd(new AdRequest());
		
		tracker = GoogleAnalyticsTracker.getInstance();
		 // Start the tracker in manual dispatch mode...
	    tracker.startNewSession("UA-34583559-1", this);
	    tracker.trackPageView("/confirmcard");
		
		extStorageDirectory = Environment.getExternalStorageDirectory().toString();
		
		edit_name = (EditText) findViewById(R.id.edit_name);
		edit_card_id = (EditText) findViewById(R.id.edit_card_id);
		button_confirm = (Button) findViewById(R.id.button_confirm);
		button_applycard = (Button) findViewById(R.id.button_applycard);
		button_opencard = (Button) findViewById(R.id.button_opencard);
		cardOwner = (TextView) findViewById(R.id.textview_card_owner);
		textViewCardDeadLine = (TextView) findViewById(R.id.textview_card_deadline);
		cardView = (ImageView) findViewById(R.id.imageview_card);
		layoutConfirm = (LinearLayout) findViewById(R.id.layout_confirm);
		
		settings = getSharedPreferences("Preference", 0);
		cardId = settings.getString("cardId", "");
		cardName = settings.getString("cardName", "");
		cardType = settings.getString("cardType", "");
		cardDeadLine = settings.getString("cardDeadLine", "0");
		
		setButtons();
		
		Calendar c = Calendar.getInstance();
	    currentTime =  c.get(Calendar.YEAR)*10000 + (c.get(Calendar.MONTH)+1)*100+ c.get(Calendar.DATE);
	    if (Integer.parseInt(cardDeadLine) > currentTime){
	    	setShowCardLayout();
	    }else{
	    	
	    }		
		
		
	}
	
	
	private void setShowCardLayout() {
		layoutConfirm.setVisibility(View.GONE);
		// TODO Auto-generated method stub
		cardOwner.setText("持卡人:"+cardName+"  "+"卡號:"+cardId);
		textViewCardDeadLine.setText("卡片到期日:"+cardDeadLine+"         "+"卡片到期日:"+cardDeadLine+"         "+"卡片到期日:"+cardDeadLine);
		textViewCardDeadLine.setSelected(true);
		
		if (cardType.equals("J")){
			cardView.setBackgroundResource(R.drawable.card_j);
		}else if(cardType.equals("EST")){
			cardView.setBackgroundResource(R.drawable.card_est);
		}else if(cardType.equals("B")){
			cardView.setBackgroundResource(R.drawable.card_b);
		}else if(cardType.equals("U")){
			cardView.setBackgroundResource(R.drawable.card_u);
		}else if(cardType.equals("E")){
			cardView.setBackgroundResource(R.drawable.card_e);
		}else if(cardType.equals("G")){
			cardView.setBackgroundResource(R.drawable.card_g);
		}    		
	}
		
	private void setButtons() {
		
		button_confirm.setOnClickListener(new OnClickListener(){  
            public void onClick(View v) {
            	String enterName = edit_name.getText().toString();
            	String enterCardId = edit_card_id.getText().toString();
//            	int length = Constants.allcard_data.size();
            	boolean check = false;
            	
            	check = checkData(enterName,enterCardId);
            	
            	if (check){
            		if (Integer.parseInt(cardDeadLine) > currentTime){
            			settings.edit().putString("cardId", cardId).commit();
            			settings.edit().putString("cardName", cardName).commit();
            			settings.edit().putString("cardType", cardType).commit();
            			settings.edit().putString("cardDeadLine", cardDeadLine).commit();
            			// set showCard layout
            			setShowCardLayout();
            		}else{
            			Toast.makeText(ConfirmCardActivity.this, "您的卡已經過期了!", Toast.LENGTH_LONG).show();
            		}
            				
            	}else{
            		Toast.makeText(ConfirmCardActivity.this, "姓名或卡號有錯誤,請修正", Toast.LENGTH_LONG).show();
            	}
            }

			
        });
		
		button_applycard.setOnClickListener(new OnClickListener(){  
            public void onClick(View v) {  
            	try{
            	Intent intent = new Intent(getParent(), MoreLink.class);
      			TabGroupActivity parentActivity = (TabGroupActivity)getParent();
      			intent.putExtra("url", "https://docs.google.com/spreadsheet/viewform?formkey=dG1PS1RtWEFPNmZsMmFFNG12S3JfU0E6MQ");
      			parentActivity.startChildActivity("MoreLink", intent);
            	}catch(Exception e){
      				Intent intent = new Intent(ConfirmCardActivity.this, MoreLink.class);
      				intent.putExtra("url", "https://docs.google.com/spreadsheet/viewform?formkey=dG1PS1RtWEFPNmZsMmFFNG12S3JfU0E6MQ");
      				startActivity(intent);
      				}
      			}         
        });
		
		button_opencard.setOnClickListener(new OnClickListener(){  
            public void onClick(View v) {
            	try{
	            	Intent intent = new Intent(getParent(), MoreLink.class);
	      			TabGroupActivity parentActivity = (TabGroupActivity)getParent();
	      			intent.putExtra("url", "https://docs.google.com/spreadsheet/viewform?formkey=dEJYTXYzTXVrY1FyUWlhY3BiRUNGU3c6MQ");
	      			parentActivity.startChildActivity("MoreLink", intent);
            	}catch(Exception e){
	  				Intent intent = new Intent(ConfirmCardActivity.this, MoreLink.class);
	  				intent.putExtra("url", "https://docs.google.com/spreadsheet/viewform?formkey=dG1PS1RtWEFPNmZsMmFFNG12S3JfU0E6MQ");
	  				startActivity(intent);
  				}
            }
        });
		
	}
	
	private boolean checkData(String checkName, String checkId) {
		// get check.csv and see if name and cardId matched
		boolean returnCheck = false;
		
		try {
	    	String strFile =  extStorageDirectory +"/Android_SlyCool/"+check_file_name;
			BufferedReader reader = new BufferedReader( new FileReader(strFile));
			LineNumberReader lineReader = new LineNumberReader(reader);
	        String line;
	        while ((line = lineReader.readLine()) != null) {
	        	String[] RowData = Constants.parseCsvLine(line.toString());
	        	if (RowData[1].equals(checkId) && RowData[2].equals(checkName)){
	        		returnCheck =true;
	        		cardId =  RowData[1];
	        		cardName = RowData[2];
	        		cardDeadLine = RowData[3];
	        		cardType = RowData[4];
	        		
	        	}
	        }

		}catch (IOException ex) {
	       
	    }		
		return returnCheck;
	}
	
	/** onKeyDwon => Do nothing! */
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			//preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
//			
//				
//				if( (TabGroupActivity)getParent()==null){
//					finish();
//				}
//				
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

