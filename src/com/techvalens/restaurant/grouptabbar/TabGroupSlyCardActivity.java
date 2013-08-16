package com.techvalens.restaurant.grouptabbar;

import com.sandj.slycool.slycard.ConfirmCardActivity;

import android.content.Intent;
import android.os.Bundle;



public class TabGroupSlyCardActivity extends TabGroupActivity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //This calls the new List activity for every TAB EVENT CLICK....
        //startChildActivity("ListOfNewRestaurantActivity", new Intent(this,ListOfNewRestaurantActivity.class));
    }
	
	@Override
	protected void onResume() {
		super.onResume();
		startChildActivity("ConfirmCardActivity", new Intent(this,ConfirmCardActivity.class));
		setFromResume(true);
	}
}
