package com.techvalens.restaurant.grouptabbar;

import android.content.Intent;
import android.os.Bundle;

import com.sandj.slycool.more.MoreActivity;


public class TabGroupMoreActivity extends TabGroupActivity{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//This calls the new List activity for every TAB EVENT CLICK....
		//Constants.setAboutTabParentContext(this);
		//startChildActivity("TabGroupAboutActivity", new Intent(this,AboutActivity.class));
	}
	@Override
	protected void onResume() {
		super.onResume();
		startChildActivity("MoreActivity", new Intent(this,MoreActivity.class));
		setFromResume(true);
	}
}
