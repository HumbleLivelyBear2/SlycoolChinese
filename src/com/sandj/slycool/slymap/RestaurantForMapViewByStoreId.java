package com.sandj.slycool.slymap;


public class RestaurantForMapViewByStoreId implements
Comparable<RestaurantForMapViewByStoreId> {
	
	private String _storeId = null;
	private String _storeName = null;
	private String _storeAddress = null;
	private String _storeLatitude = null;
	private String _storeLongitude = null;
	private String _storeLogo = null;
	private String _storeDistance = null;
	

	public RestaurantForMapViewByStoreId(String storeId, String storeName,
			String storeAddress, String storeLatitude, String storeLongitude, String storeLogo, String storeDistance) {
		super();
		this._storeId = storeId;
		this._storeName = storeName;
		this._storeAddress = storeAddress;
		this._storeLatitude = storeLatitude;
		this._storeLongitude = storeLongitude;
		this._storeLogo = storeLogo;
		this._storeDistance = storeDistance;
	}

	@Override
	public int compareTo(RestaurantForMapViewByStoreId other) {
		return Integer.parseInt(_storeId)
		- Integer.parseInt(other.getStoreId());
	}

	public String getStoreId() {
		return _storeId;
	}

	public String getStoreName() {
		return _storeName;
	}

	public String getStoreAddress() {
		return _storeAddress;
	}

	public String getStoreLatitude() {
		return _storeLatitude;
	}

	public String getStoreLongitude() {
		return _storeLongitude;
	}
	public String getStoreLogo() {
		return _storeLogo;
	}

	public String getStoreDistance() {
		return _storeDistance;
	}
	
}
