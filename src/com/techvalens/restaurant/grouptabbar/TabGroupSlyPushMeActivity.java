package com.techvalens.restaurant.grouptabbar;



import com.sandj.slycool.slypushme.SlyPushMeActivity;

import android.content.Intent;
import android.os.Bundle;

public class TabGroupSlyPushMeActivity extends TabGroupActivity{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//startChildActivity("TabGroupRecommendActivity", new Intent(this,ListOfRecommendRestaurantActivity.class));
	}

	@Override
	protected void onResume() {
		super.onResume();
		startChildActivity("SlyPushMeActivity", new Intent(this,SlyPushMeActivity.class));
		setFromResume(true);
	}
}
