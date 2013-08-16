package com.sandj.slycool.slycoupon;

public class ItemBean 
{	
	String _storeId;
	String _storeType;
	String _storeName;
	String _storeAddress;
	String _storeLogo;
	String _storeDistances;



	public String getStoreId() {
		return _storeId;
	}

	public void setStoreId(String storeId) {
		this._storeId = storeId;
	}

	public String getStoreType() {
		return _storeType;
	}

	public void setStoreType(String storeType) {
		this._storeType = storeType;
	}

	public void setName(String storeName) {
		this._storeName = storeName;
	}
	public String getName() {
		return _storeName;
	}


	public void setAddress(String address) {
		this._storeAddress = address;
	}
	public String getAddress() {
		return _storeAddress;
	}

	public void setLogo(String image) {
		this._storeLogo = image;
	}	
	public String getImage() {
		return _storeLogo;
	}


	public String getDistances() {
		return _storeDistances;
	}

	public void setDistances(String distances) {
		this._storeDistances = distances;
	}
}