package com.sandj.coverflow;

import com.sandj.slycoolchinese.Constants;
import com.sandj.slycoolchinese.PromoteData;
import com.sandj.slycoolchinese.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class PromoteActivity extends Activity {
	/** Called when the activity is first created. */
	
	private Button button_promote_link;
	private TextView textview_promote_title;
	private TextView textview_promote_content;
	private int promoteID;	
	private PromoteData thisPromoteData;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_promote);
		
		Bundle bData = this.getIntent().getExtras();
		promoteID = bData.getInt("promoteID");
		
		searchData();
		setUIs();
		
		
	}

	private void searchData() {
		// TODO Auto-generated method stub
		thisPromoteData = Constants.allPromoteData.get(promoteID);
			
//		int length = Constants.moreData.size();
//		for (int i= 0; i<length;i++){
//			MoreData checkMoreData = Constants.moreData.get(i);
//			if (checkMoreData.id.equals(moreId)){
//				thisMoreData = checkMoreData;
//			}
//		}
	}

	private void setUIs() {
		// TODO Auto-generated method stub

		button_promote_link = (Button) findViewById(R.id.button_promote_link);
		textview_promote_title = (TextView) findViewById(R.id.textview_promote_title);
		textview_promote_content = (TextView) findViewById(R.id.textview_promote_content);
		
		textview_promote_title.setText(thisPromoteData.title);
		textview_promote_content.setText(thisPromoteData.content);
			
		button_promote_link.setOnClickListener(new OnClickListener(){  
	          public void onClick(View v) {
	            Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(thisPromoteData.other_link));
	            startActivity(viewIntent);	      				
	      		}         
	     });
		
		
	}
}

