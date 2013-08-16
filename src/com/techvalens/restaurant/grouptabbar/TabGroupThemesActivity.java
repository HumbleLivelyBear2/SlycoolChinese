package com.techvalens.restaurant.grouptabbar;



import com.sandj.slycool.themes.SlyThemesActivity;

import android.content.Intent;
import android.os.Bundle;

public class TabGroupThemesActivity extends TabGroupActivity{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//startChildActivity("TabGroupshowRestaurantActivity", new Intent(this,ListOfRestaurantActivity.class));
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		startChildActivity("SlyThemesActivity", new Intent(this,SlyThemesActivity.class));
		setFromResume(true);
	}

}
