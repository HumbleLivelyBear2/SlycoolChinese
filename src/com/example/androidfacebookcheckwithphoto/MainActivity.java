package com.example.androidfacebookcheckwithphoto;


import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;
import com.sandj.slycoolchinese.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	Button checkIn;
	public static Facebook facebook = new Facebook("101559133326399");
	public static AsyncFacebookRunner mAsyncRunner;
	private SharedPreferences mPrefs;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        
        // Instantiate the asynrunner object for asynchronous api calls.
        mAsyncRunner = new AsyncFacebookRunner(facebook);
        
        /*
         * Get existing access_token if any
         */
        mPrefs = getPreferences(MODE_PRIVATE);
        String access_token = mPrefs.getString("access_token", null);
        long expires = mPrefs.getLong("access_expires", 0);
        if(access_token != null) {
            facebook.setAccessToken(access_token);
        }
        if(expires != 0) {
            facebook.setAccessExpires(expires);
            Intent myIntent = new Intent(MainActivity.this, FbCheckInPlaces.class);
            startActivity(myIntent);
            finish();
        }
        /*
         * Only call authorize if the access_token has expired.
         */
        if (!facebook.isSessionValid()){
	        facebook.authorize(this, new String[]{"email", "publish_checkins","photo_upload"},
	          new DialogListener() {
	            @Override
	            public void onComplete(Bundle values) {
	            	SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putString("access_token", facebook.getAccessToken());
                    editor.putLong("access_expires", facebook.getAccessExpires());
                    editor.commit();
                    
                    Intent myIntent = new Intent(MainActivity.this, FbCheckInPlaces.class);
                    startActivity(myIntent);
                    finish();
	            }
	            	
	            @Override
	            public void onFacebookError(FacebookError error) {}
	
	            @Override
	            public void onError(DialogError e) {}
	
	            @Override
	            public void onCancel() {}
	        });
        }
        
        
//        checkIn = (Button) findViewById(R.id.check_in);
//        checkIn.setOnClickListener(new OnClickListener(){  
//            public void onClick(View v) {  
//            	Intent myIntent = new Intent(MainActivity.this, FbCheckInPlaces.class);
//                startActivity(myIntent); 
//            }
//        });
        
       
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        facebook.authorizeCallback(requestCode, resultCode, data);
    }
    
    public void onResume() {    
        super.onResume();
        facebook.extendAccessTokenIfNeeded(this, null);
    }
}
