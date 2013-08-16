package com.sandj.slycoolchinese;

public class Site{
	public String id;
	public String chineseType;
	public String englishType;
	public String photoUrl;
	public String logoUrl;
	public String siteName;
	public String siteAddress;
	public String sitePhone;
	public String siteTime;
	public String siteCoupon;
	public String fbUrl;
	public String partTimeJob;
	public String menuUrl;
	public String experienceUrl;
	public String xLoc;
	public String yLoc;
	public String couponTime;
	public String memberValueFbUrl;
	public String otherUrl;
	public double siteDistance;
	public Site(String id
			,String chineseType
			,String englishType
			,String photoUrl
			,String logoUrl
			,String siteName
			,String siteAddress
			,String sitePhone
			,String siteTime
			,String siteCoupon
			,String fbUrl
			,String partTimeJob
			,String menuUrl
			,String experienceUrl
			,String xLoc
			,String yLoc
			,String couponTime
			,String memberValueFbUrl
			,String otherUrl
			,double siteDistance){
		this.id = id;
		this.chineseType = chineseType;
		this.englishType = englishType;
		this.photoUrl = photoUrl;
		this.logoUrl = logoUrl;
		this.siteName = siteName;
		this.siteAddress = siteAddress;
		this.sitePhone = sitePhone;
		this.siteTime = siteTime;
		this.siteCoupon = siteCoupon;
		this.fbUrl = fbUrl;
		this.partTimeJob = partTimeJob;
		this.menuUrl = menuUrl;
		this.experienceUrl = experienceUrl;
		this.xLoc = xLoc;
		this.yLoc = yLoc;
		this.couponTime = couponTime;
		this.memberValueFbUrl = memberValueFbUrl;
		this.otherUrl = otherUrl;
		this.siteDistance = siteDistance;
	}
	public Double getDistance(){
		return siteDistance;
	}
}
