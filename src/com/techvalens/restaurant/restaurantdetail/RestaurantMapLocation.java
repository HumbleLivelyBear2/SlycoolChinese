package com.techvalens.restaurant.restaurantdetail;

import java.util.List;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import com.sandj.slycool.slymap.CustomItemizedOverlay;
import com.sandj.slycool.slymap.CustomOverlayItem;
import com.sandj.slycoolchinese.R;



public class RestaurantMapLocation extends MapActivity {

	MapView mapView;
	List<Overlay> mapOverlays;
	Drawable drawable2;
	CustomItemizedOverlay<CustomOverlayItem> itemizedOverlay;
	CustomItemizedOverlay<CustomOverlayItem> itemizedOverlay2;
	Drawable drawable;
	private MapController mapControl;
	private int latE6;
	private int lonE6;
	private GeoPoint gp;

	private ProgressDialog progressDialog=  null;

	private String _storeLatitude = null;
	private String _storeLongitude = null;
	private String _storeId = null;
	private String _storeName= null;
	private String _storeAddress = null;
	private String _storeLogo = null;
	
//	private ImageButton _refreshImageButton= null;

	double latitude=0;
	double longitude=0;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapscreen);
		
//		_refreshImageButton = (ImageButton)findViewById(R.id.refreshMap_ImageButton);
//		_refreshImageButton.setVisibility(View.GONE);
		
		_storeLatitude = getIntent().getStringExtra("LATITUDE");
		_storeLongitude = getIntent().getStringExtra("LOGITUDE");
		_storeName = getIntent().getStringExtra("STORE_NAME");
		_storeAddress = getIntent().getStringExtra("STORE_ADDRESS");
		_storeLogo = getIntent().getStringExtra("STORE_LOGO");
		_storeId = getIntent().getStringExtra("STORE_ID");
		
		mapView = (MapView) findViewById(R.id.mapView);
		mapView.setBuiltInZoomControls(true);

		mapOverlays = mapView.getOverlays();

		mapView.setSatellite(false);
		mapView.setTraffic(false);
		mapView.setBuiltInZoomControls(true);   // Set android:clickable=true in main.xml
		int maxZoom = mapView.getMaxZoomLevel();
		int initZoom = maxZoom-2;
		mapControl = mapView.getController();
		mapControl.setZoom(initZoom);
		//Convert lat/long in degrees into integers in microdegrees
		latE6 =  (int) (35.955*1e6);
		lonE6 = (int) (-83.9265*1e6);
		gp = new GeoPoint(latE6, lonE6);
		mapControl.animateTo(gp);   

		//LOCATION OF THE RESTAURANT.........
		latitude = Double.parseDouble(_storeLatitude);
		longitude = Double.parseDouble(_storeLongitude);

		// first overlay
		drawable = getResources().getDrawable(R.drawable.mappin);
		itemizedOverlay = new CustomItemizedOverlay<CustomOverlayItem>(drawable, mapView);
		//GENERATING GEO POINT FOR THE LOCATION
		GeoPoint point = new GeoPoint((int) (latitude * 1E6),(int) (longitude * 1E6));
		//GENERATING CUSTOM OVERLAY
		CustomOverlayItem overlayItem = new CustomOverlayItem(point, _storeName,_storeAddress,_storeLogo,_storeId);
		itemizedOverlay.addOverlay(overlayItem);
		mapOverlays.add(itemizedOverlay);

		final MapController mc = mapView.getController();
		mc.animateTo(point);
		mc.setZoom(14);
	}


	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
