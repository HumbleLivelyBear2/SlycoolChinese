package com.example.androidfacebookcheckwithphoto;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONException;

import com.facebook.android.FacebookError;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.sandj.coverflow.CoverFlowActivity;
import com.sandj.slycoolchinese.Constants;
import com.sandj.slycoolchinese.R;
import com.sandj.slycoolchinese.SplashScreen;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


public class FbChenkInWithPhoto extends Activity {
	
	private static final int CAMERA_REQUEST = 1888; 
	private static final int BACK_REQUEST = 888; 
	Button upload_fb;
	EditText upload_text;
	ImageButton camera_image;
	String coordinates;
	String placeID;
	String name;
	private static int MAX_IMAGE_DIMENSION = 720;
	private final Handler mHandler= new Handler() {
        public void handleMessage(Message msg) {
        	if (msg.arg1 == 1){
        		Toast.makeText(getApplicationContext(),"已上傳至Fb", Toast.LENGTH_SHORT).show();
        	}else{
        		Toast.makeText(getApplicationContext(),"上傳至Fb失敗", Toast.LENGTH_SHORT).show();
        	}
        }
	};
    ProgressDialog dialog;
    Uri photoUri;
    private GoogleAnalyticsTracker tracker;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkin_with_photo);
        
        tracker = GoogleAnalyticsTracker.getInstance();
		 // Start the tracker in manual dispatch mode...
	    tracker.startNewSession("UA-34583559-1", this);
	    tracker.trackPageView("/fbcheck_upload");
        
        
        Intent intent= getIntent();
		name = intent.getStringExtra("name");
		placeID = intent.getStringExtra("place");
		coordinates = intent.getStringExtra("coordinates");
		
		upload_fb = (Button) findViewById (R.id.button_to_fb);
		upload_text = (EditText) findViewById (R.id.saying_box);
		camera_image = (ImageButton) findViewById (R.id.camera_image);
		
		// set Objects
		upload_text.setText("我想說..."+"\n"+"-在"+name);
		camera_image.setOnClickListener(new View.OnClickListener() {
			@Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
                startActivityForResult(cameraIntent, CAMERA_REQUEST); 
            }
        });
		
		upload_fb.setOnClickListener(new View.OnClickListener() {
			@Override
            public void onClick(View v) {
				 try {
					 Bundle params = new Bundle();
	                 try {
	                	 if(photoUri != null){
	                     params.putByteArray("photo",
	                             scaleImage(FbChenkInWithPhoto.this, photoUri));
	                	 }
	                 } catch (IOException e) {
	                     e.printStackTrace();
	                 }
	                
	                 String uploadMessage = upload_text.getText().toString();
	                 uploadMessage = uploadMessage.substring(0, uploadMessage.indexOf("-"));
	//                 params.putString("caption", "FbAPIs Sample App photo upload");
	                 params.putString("place", placeID);
	                 params.putString("message", uploadMessage);
	                 params.putString("coordinates", coordinates);
	                 if(photoUri != null){           	
	                	 MainActivity.mAsyncRunner.request("me/photos", params, "POST",
	                         new PhotoUploadListener(), null);	 
	                 }else{	                	
	                	 MainActivity.mAsyncRunner.request("me/checkins", params, "POST",
	                             new placesCheckInListener(), null);
	                 };
                	 
	                 
	                 
                 }catch(Exception e){
                 }
                 
                 
            }
        });
		
		
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (requestCode == CAMERA_REQUEST) {  
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            photoUri = data.getData();
            camera_image.setImageBitmap(photo);
        } 
        if (requestCode == BACK_REQUEST) {  
            finish();
        }  
    } 
    
    public class placesCheckInListener extends BaseRequestListener {
        @Override
        public void onComplete(final String response, final Object state) {
        	Message msg = mHandler.obtainMessage();
        	msg.arg1 = 1;
        	mHandler.sendMessage(msg);
        	finish();
        }

        public void onFacebookError(FacebookError error) {
        	Message msg = mHandler.obtainMessage();
        	msg.arg1 = 2; //2 means not succeed
        	mHandler.sendMessage(msg);
        }
    }
    
    public class PhotoUploadListener extends BaseRequestListener {

        @Override
        public void onComplete(final String response, final Object state) {
        	Message msg = mHandler.obtainMessage();
        	msg.arg1 = 1;
        	mHandler.sendMessage(msg);
            finish();
//            dialog.dismiss();
            
        }

        public void onFacebookError(FacebookError error) {
        	Message msg = mHandler.obtainMessage();
        	msg.arg1 = 2;
        	mHandler.sendMessage(msg);
        }
    }
    
    public static byte[] scaleImage(Context context, Uri photoUri) throws IOException {
        InputStream is = context.getContentResolver().openInputStream(photoUri);
        BitmapFactory.Options dbo = new BitmapFactory.Options();
        dbo.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, dbo);
        is.close();

        int rotatedWidth, rotatedHeight;
        int orientation = getOrientation(context, photoUri);

        if (orientation == 90 || orientation == 270) {
            rotatedWidth = dbo.outHeight;
            rotatedHeight = dbo.outWidth;
        } else {
            rotatedWidth = dbo.outWidth;
            rotatedHeight = dbo.outHeight;
        }

        Bitmap srcBitmap;
        is = context.getContentResolver().openInputStream(photoUri);
        if (rotatedWidth > MAX_IMAGE_DIMENSION || rotatedHeight > MAX_IMAGE_DIMENSION) {
            float widthRatio = ((float) rotatedWidth) / ((float) MAX_IMAGE_DIMENSION);
            float heightRatio = ((float) rotatedHeight) / ((float) MAX_IMAGE_DIMENSION);
            float maxRatio = Math.max(widthRatio, heightRatio);

            // Create the bitmap from file
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = (int) maxRatio;
            srcBitmap = BitmapFactory.decodeStream(is, null, options);
        } else {
            srcBitmap = BitmapFactory.decodeStream(is);
        }
        is.close();

        /*
         * if the orientation is not 0 (or -1, which means we don't know), we
         * have to do a rotation.
         */
        if (orientation > 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);

            srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(),
                    srcBitmap.getHeight(), matrix, true);
        }

        String type = context.getContentResolver().getType(photoUri);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (type.equals("image/png")) {
            srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        } else if (type.equals("image/jpg") || type.equals("image/jpeg")) {
            srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        }
        byte[] bMapArray = baos.toByteArray();
        baos.close();
        return bMapArray;
    }
    
    public static int getOrientation(Context context, Uri photoUri) {
        /* it's on the external media. */
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[] { MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);

        if (cursor.getCount() != 1) {
            return -1;
        }

        cursor.moveToFirst();
        return cursor.getInt(0);
    }
    
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

