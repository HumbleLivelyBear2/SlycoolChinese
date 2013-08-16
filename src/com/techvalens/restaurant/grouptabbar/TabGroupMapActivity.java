package com.techvalens.restaurant.grouptabbar;

import com.sandj.slycoolchinese.Constants;
import com.sandj.slycoolchinese.MapAndListActivity;

import android.content.Intent;
import android.os.Bundle;


public class TabGroupMapActivity extends TabGroupActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*Constants.setMapTabParentContext(this);
		startChildActivity("TabGroupMapActivity", new Intent(this,CustomMap.class));*/
	}
	
/** 
 * 在跑完onCreate()後, 會再跑onResume();
 * adnroid的生命週期是 onCreate() => onStart() => onResume() => onPause() => onStop() => onDestroy()
 * */
	@Override
	protected void onResume() {
		super.onResume();
		// setMapTabParentContext 把 constant裡的 tabMapContext 設為這個activity context
		Constants.setMapTabParentContext(this);
		startChildActivity("MapAndListActivity", new Intent(this,MapAndListActivity.class));
		setFromResume(true);
	}

}
