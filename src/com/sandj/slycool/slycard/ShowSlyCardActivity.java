package com.sandj.slycool.slycard;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.sandj.slycoolchinese.R;
import com.techvalens.restaurant.grouptabbar.TabGroupActivity;

public class ShowSlyCardActivity extends Activity{
	
	private String cardType;
	private String cardName;
	private String cardId;
	private String cardDeadLine;
	
	private TextView cardOwner;
	private TextView textViewCardDeadLine;
	private ImageView cardView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_slycard);
		
		
		Bundle bData = this.getIntent().getExtras();
		cardType = bData.getString("cardType");
		cardName = bData.getString("cardName");
		cardId = bData.getString("cardId");
		cardDeadLine = bData.getString("cardDeadLine");
		
		cardOwner = (TextView) findViewById(R.id.textview_card_owner);
		textViewCardDeadLine = (TextView) findViewById(R.id.textview_card_deadline);
		cardView = (ImageView) findViewById(R.id.imageview_card);
		
		cardOwner.setText("持卡人:"+cardName+"  "+"卡號:"+cardId);
		textViewCardDeadLine.setText("卡片到期日:"+cardDeadLine+"         "+"卡片到期日:"+cardDeadLine+"         "+"卡片到期日:"+cardDeadLine);
//		textViewCardDeadLine.setFocusable(true);
		textViewCardDeadLine.setSelected(true);
		
		if (cardId.equals("J")){
			cardView.setBackgroundResource(R.drawable.card_j);
		}else if(cardId.equals("EST")){
			cardView.setBackgroundResource(R.drawable.card_est);
		}else if(cardId.equals("B")){
			cardView.setBackgroundResource(R.drawable.card_b);
		}else if(cardId.equals("U")){
			cardView.setBackgroundResource(R.drawable.card_u);
		}else if(cardId.equals("E")){
			cardView.setBackgroundResource(R.drawable.card_e);
		}else if(cardId.equals("G")){
			cardView.setBackgroundResource(R.drawable.card_g);
		}
	}
	
}
