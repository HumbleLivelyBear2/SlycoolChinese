package com.sandj.slycoolchinese;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import com.flurry.android.FlurryAgent;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.sandj.coverflow.CoverFlowActivity;
import com.sandj.slycool.slycoupon.ListOfRestaurantActivity;
import com.sandj.slycoolchinese.MyLocation.LocationResult;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

public class SplashScreen extends Activity {
	/** Called when the activity is first created. */
	public static int screenHeight;
	public static int screenWidth;
	/*public static int imageSetWidth;
	public static int imageSetHeight;*/
	
//	String extStorageDirectory;
	String check_URL="https://docs.google.com/spreadsheet/pub?key=0Am8sCHFg0R71dHFpcFliek5hdVNmTHJPVnRDWENvTkE&output=csv";
	String check_file_name="check.csv";
	String promote_URL="https://docs.google.com/spreadsheet/pub?key=0AquU7odNIm9edE5XYlZ3Nm9DU1NGYkNTT3FPMDlCdnc&output=csv";
	String promote_file_name="promote.csv";
	String slycool_URL="https://docs.google.com/spreadsheet/pub?key=0AquU7odNIm9edHBuNTlkbm5ZS0o5U3RFakJoYXRjNFE&output=csv";
	String slycool_file_name="slycool.csv";
	String theme_URL="https://docs.google.com/spreadsheet/pub?key=0AquU7odNIm9edE5DejVYN2YxZTNzRFJDdUpGQnQ1MXc&output=csv";
	String theme_file_name="theme.csv";
	String more_URL="https://docs.google.com/spreadsheet/pub?key=0AquU7odNIm9edFJnX0RBbnhpWFJFXzVGQzgtTGs2Wnc&output=csv";
	String more_file_name="more.csv";
	
	private boolean checkNet;
	GoogleAnalyticsTracker tracker;
//	private AdController myController;
	private AlertDialog.Builder alertDialog;
	private ProgressDialog progressDialog= null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);
		Constants.extStorageDirectory = Environment.getExternalStorageDirectory().toString();
		
		
		checkNet = isOnline();
		
		if (checkNet){
			
//			myController = new AdController(getApplicationContext(), "439115843");
//			myController.loadNotification();
			
			tracker = GoogleAnalyticsTracker.getInstance();
			 // Start the tracker in manual dispatch mode...
		    tracker.startNewSession("UA-34583559-1", this);
		    tracker.trackPageView("/splashscreen");
			
			
			// Download promote.csv
			new DownloadPromoteTask().execute();
			new DownloadSlyCoolTask().execute();		 
			new DownloadThemeTask().execute();
			new DownloadMoreTask().execute();      
	        new DownloadCheckTask().execute();
			
			
			/** 開始GpsListener */
			LocationResult locationResult = new LocationResult(){
			    @Override
			    public void gotLocation(Location location){
			        //Got the location!
			    	Constants.latitudeValue = location.getLatitude();
			    	Constants.longitudeValue = location.getLongitude();
			    }
			};
			try {
				MyLocation myLocation = new MyLocation();
				myLocation.getLocation(this, locationResult);
			}catch(Exception e){
				// let location at slilin
			}
			
			
			Constants.Display_WIDTH = SplashScreen.this.getWindowManager().getDefaultDisplay().getWidth();
			Constants.DEVICE_HEIGHT = SplashScreen.this.getWindowManager().getDefaultDisplay().getHeight();
		
		}else{
//			Toast.makeText(SplashScreen.this, "資料載入錯誤,請檢查網路",Toast.LENGTH_SHORT).show();
//			finish();
			
			alertDialog = new AlertDialog.Builder(this).setTitle("無法連線")
					.setMessage("是否離線使用?")
					.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intentToHomeScreenActivity = new Intent(SplashScreen.this,CoverFlowActivity.class);
							intentToHomeScreenActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
							startActivity(intentToHomeScreenActivity);
							SplashScreen.this.finish();
						}
					})
					.setNegativeButton("No", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					});
			alertDialog.show();    
		}
		
	}
	
	public boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
	
	// download promote.csv
	private class DownloadPromoteTask extends AsyncTask{
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
		}

		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			
			
			File Directory = new File(Constants.extStorageDirectory+"/Android_SlyCool/");
			Directory.mkdirs();
			File file = new File(Directory, promote_file_name);
			try {
				URL url = new URL(promote_URL);
				URLConnection connection = url.openConnection();
				connection.connect();
				InputStream input = new BufferedInputStream(url.openStream());
//				OutputStream output = new FileOutputStream("/sdcard/file_name.extension");
				OutputStream output = new FileOutputStream(file);
				byte data[] = new byte[1];
				while ((input.read(data)) != -1) {
	               
	                output.write(data);
	            }			
				output.flush();
	            output.close();
	            input.close();
			}catch (Exception e) {
				e.toString();
	        }
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);		
//			setPromoteValues();
			
		}
	}
	
	
	
	// download check.csv
	private class DownloadCheckTask extends AsyncTask{
	    
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	       
	        
	    }
	    
	    @Override
	    protected String doInBackground(Object... params) {
	    	
	    	File Directory = new File(Constants.extStorageDirectory+"/Android_SlyCool/");
			Directory.mkdirs();
			File file = new File(Directory, check_file_name);
			try {
				URL url = new URL(check_URL);
				URLConnection connection = url.openConnection();
				connection.connect();
				InputStream input = new BufferedInputStream(url.openStream());
				OutputStream output = new FileOutputStream(file);
				byte data[] = new byte[1];
				while ((input.read(data)) != -1) {
	               
	                output.write(data);
	            }			
				output.flush();
	            output.close();
	            input.close();
				
			}catch (Exception e) {
	        }
	        return null;
	    }
	    
	    @Override
	    protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			
		}
		
	}
	
	// download more.csv
	private class DownloadMoreTask extends AsyncTask{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
		}

		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			
			
			File Directory = new File(Constants.extStorageDirectory+"/Android_SlyCool/");
			Directory.mkdirs();
			File file = new File(Directory, more_file_name);
			try {
				URL url = new URL(more_URL);
				URLConnection connection = url.openConnection();
				connection.connect();
				InputStream input = new BufferedInputStream(url.openStream());
//				OutputStream output = new FileOutputStream("/sdcard/file_name.extension");
				OutputStream output = new FileOutputStream(file);
				byte data[] = new byte[1];
				while ((input.read(data)) != -1) {
	               
	                output.write(data);
	            }			
				output.flush();
	            output.close();
	            input.close();
			}catch (Exception e) {
	        }
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);		
			
		}
	}
	
	// download slycool.csv
	private class DownloadSlyCoolTask extends AsyncTask{
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = ProgressDialog.show(SplashScreen.this, "","加載中,請稍候,...");
			
		}

		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub		
			File Directory = new File(Constants.extStorageDirectory+"/Android_SlyCool/");
			Directory.mkdirs();
			File file = new File(Directory, slycool_file_name);
			try {
				URL url = new URL(slycool_URL);
				URLConnection connection = url.openConnection();
				connection.connect();
				InputStream input = new BufferedInputStream(url.openStream());
				OutputStream output = new FileOutputStream(file);
				byte data[] = new byte[1];
				while ((input.read(data)) != -1) {
	               
	                output.write(data);
	            }			
				output.flush();
	            output.close();
	            input.close();
			}catch (Exception e) {
	        }
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			setPromoteValues();
			Intent intentToHomeScreenActivity = new Intent(SplashScreen.this,CoverFlowActivity.class);
			intentToHomeScreenActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
			startActivity(intentToHomeScreenActivity);
			SplashScreen.this.finish();
			//GET THE RESOLUTION OF THE DEVICE
			Constants.Display_WIDTH = SplashScreen.this.getWindowManager().getDefaultDisplay().getWidth();
			Constants.DEVICE_HEIGHT = SplashScreen.this.getWindowManager().getDefaultDisplay().getHeight();
		}

		
	}
	
	 public void setPromoteValues() {
			
			try {
		    	String strFile =  Constants.extStorageDirectory +"/Android_SlyCool/"+promote_file_name;
				BufferedReader reader = new BufferedReader( new FileReader(strFile));
				LineNumberReader lineReader = new LineNumberReader(reader);
		        String line;
		        while ((line = lineReader.readLine()) != null) {
		        	if(lineReader.getLineNumber()!=1){
		        		String[] RowData = Constants.parseCsvLine(line.toString());
		        		if (RowData.length==5){
		        			PromoteData currentData = new PromoteData(RowData[0],RowData[1],RowData[2],RowData[3],RowData[4]);
		    				Constants.allPromoteData.add(currentData);
						}
					 
		        	}
		           
		        	
		        }
		    }
		    catch (IOException ex) {
		        // handle exception
		    	Toast.makeText(SplashScreen.this, "找不到資料",Toast.LENGTH_SHORT).show();
		    }		
		}
	
	//download theme.csv
	private class DownloadThemeTask extends AsyncTask{
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
		}

		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			
			
			File Directory = new File(Constants.extStorageDirectory+"/Android_SlyCool/");
			Directory.mkdirs();
			File file = new File(Directory, theme_file_name);
			try {
				URL url = new URL(theme_URL);
				URLConnection connection = url.openConnection();
				connection.connect();
				InputStream input = new BufferedInputStream(url.openStream());
				OutputStream output = new FileOutputStream(file);
				byte data[] = new byte[1];
				while ((input.read(data)) != -1) {
	               
	                output.write(data);
	            }			
				output.flush();
	            output.close();
	            input.close();
			}catch (Exception e) {
	        }
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);		
		}	
	}
	
	
	
	@Override
	protected void onStart()
	{
		super.onStart();
		FlurryAgent.onStartSession(this, "G7JM8RNP6GDTJDSCJ3BM");
	}
	
	
	@Override
	protected void onStop()
	{
		super.onStop();		
		FlurryAgent.onEndSession(this);
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