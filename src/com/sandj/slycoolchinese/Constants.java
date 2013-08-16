package com.sandj.slycoolchinese;

import java.util.ArrayList;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class Constants {

	public static final String MENU_FOLDER_PATH = "/mnt/sdcard/ChineseRestaurant/6MenuImageFolder/";
	public static Context tabMapContext = null;
	public static Context tabAboutContext = null;
	public static String extStorageDirectory="";
	public static double latitudeValue=25.090729;
	public static double longitudeValue=121.522423;
	public static String DEVICE_ID = null;
	public static String APPLICATION_LAUNCH_TIME=null;
	public static String CURRENT_LOCATION = null;
	public static int Display_WIDTH=0;
	public static int DEVICE_HEIGHT=0;
//	public static ArrayList<Site> typeA1 = new ArrayList<Site>(); 
//	public static ArrayList<Site> typeA2 = new ArrayList<Site>();
//	public static ArrayList<Site> typeA3 = new ArrayList<Site>();
//	public static ArrayList<Site> typeA4 = new ArrayList<Site>();
//	public static ArrayList<Site> typeA5 = new ArrayList<Site>();
//	public static ArrayList<Site> typeB1 = new ArrayList<Site>();
//	public static ArrayList<Site> typeB2 = new ArrayList<Site>();
//	public static ArrayList<Site> typeC = new ArrayList<Site>();
//	public static ArrayList<Site> typeD = new ArrayList<Site>();
//	public static ArrayList<Site> typeE = new ArrayList<Site>();
//	public static ArrayList<Site> typeF = new ArrayList<Site>();
//	public static ArrayList<Site> typeG = new ArrayList<Site>();
//	public static ArrayList<Site> typeH = new ArrayList<Site>();
//	public static ArrayList<Site> typeI = new ArrayList<Site>();
//	public static ArrayList<Site> typeJ = new ArrayList<Site>();
	public static ArrayList<Site> allSites = new ArrayList<Site>();
	public static ArrayList<Themes> allthemes = new ArrayList<Themes>();
//	public static ArrayList<CardData> allcard_data = new ArrayList<CardData>();
	public static ArrayList<PromoteData> allPromoteData = new ArrayList<PromoteData>();
	public static String currentSiteId = null;
	
	public static String PROCESSING_REQUEST = "加載...";
	
	public static void setMapTabParentContext(Context context){
		tabMapContext = context;
	}
	
	public static Context getMapTabParentContext(){
		return tabMapContext;
	}
	
	public static void setAboutTabParentContext(Context context){
		tabAboutContext = context;
	}
	
	public static Context getAboutTabParentContext(){
		return tabAboutContext;
	}
	
	public static String[] parseCsvLine(String line) {
        // Create a pattern to match breaks
        Pattern p =
            Pattern.compile(",(?=([^\"]*\"[^\"]*\")*(?![^\"]*\"))");
        // Split input with the pattern
        String[] fields = p.split(line);
        for (int i = 0; i < fields.length; i++) {
            // Get rid of residual double quotes
            fields[i] = fields[i].replace("\"", "");
        }
        return fields;
    }
	
}
