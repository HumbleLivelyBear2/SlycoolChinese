package com.sandj.slycoolchinese;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;
import com.sandj.slycool.more.MoreActivity;
import com.sandj.slycool.slycard.ConfirmCardActivity;
import com.sandj.slycool.slypushme.SlyPushMeActivity;
import com.sandj.slycool.themes.SlyThemesActivity;
import com.techvalens.restaurant.gps.GpsListener;


public class TabActivity extends android.app.TabActivity  {

	private String str1;
	private GpsListener gpsListener = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabbarscreen);
		
		Bundle bData = this.getIntent().getExtras();
		int tabIndex = bData.getInt("tabIndex");
		
		//DEVICE ID OF THE CURRENT DEVICE
		TelephonyManager tManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
		Constants.DEVICE_ID = tManager.getDeviceId();
		System.out.println(Constants.DEVICE_ID+"===>>>>>>>>>");


		//USING SIMPLE DATE FORMATE CLASS
		SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		Constants.APPLICATION_LAUNCH_TIME =  sdfDateTime.format(new Date(System.currentTimeMillis()));
		System.out.println( Constants.APPLICATION_LAUNCH_TIME+"======>>>>>>>>>>>");


		TabHost mTabHost = getTabHost();

		mTabHost.addTab(mTabHost.newTabSpec("tab1")
				.setIndicator(null,getResources().getDrawable(R.drawable.icon_slycool))
				.setContent(new Intent(this, MapAndListActivity.class)));

		mTabHost.addTab(mTabHost.newTabSpec("tab2")
				.setIndicator(null,getResources().getDrawable(R.drawable.icon_themes))
				.setContent(new Intent(this, SlyThemesActivity.class)));

		mTabHost.addTab(mTabHost.newTabSpec("tab3")
				.setIndicator(null,getResources().getDrawable(R.drawable.icon_slycard))
				.setContent(new Intent(this, ConfirmCardActivity.class)));

		mTabHost.addTab(mTabHost.newTabSpec("tab4")
				.setIndicator(null,getResources().getDrawable(R.drawable.icon_slypushme))
				.setContent(new Intent(this, SlyPushMeActivity.class)));

		mTabHost.addTab(mTabHost.newTabSpec("tab5")
				.setIndicator(null,getResources().getDrawable(R.drawable.icon_more))
				.setContent(new Intent(this, MoreActivity.class)));

		mTabHost.setCurrentTab(tabIndex);
//		mTabHost.setCurrentTab(0);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();			
		inflater.inflate(R.layout.menu, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {

		case R.id.exit_MI:
			File cacheDir=null;
			if (android.os.Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED))
				cacheDir = new File(
						android.os.Environment.getExternalStorageDirectory(),
				"/ChineseRestaurant/");
			else
				cacheDir = this.getCacheDir();

			deleteDirectory(cacheDir);
			TabActivity.this.finish();
			System.exit(0);
			return true;
		default:
			return true;
		}
	}
	
	/** 離開時, 要刪除cache */
	static public boolean deleteDirectory(File path) {
		if( path.exists() ) {
			File[] files = path.listFiles();
			if (files == null) {
				return true;
			}
			for(int i=0; i<files.length; i++) {
				if(files[i].isDirectory()) {
					deleteDirectory(files[i]);
				}
				else {
					files[i].delete();
				}
			}
		}
		return( path.delete() );
	}

}


