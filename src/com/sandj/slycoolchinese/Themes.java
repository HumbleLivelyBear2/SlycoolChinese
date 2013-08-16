package com.sandj.slycoolchinese;

public class Themes {
	public String themeIndex;
	public String order;
	public String title;
	public String title_a;
	public String title_b;
	public String title_c;
	public String content;
	public String content_a;
	public String content_b;
	public String content_c;
	public String photo_url;
	public String small_photo_url;
	public String more_info;
	public String more_info_link;
	public String deadline;
	
	public Themes(String themeIndex,String order
			,String title, String content
			,String title_a, String content_a
			,String title_b, String content_b
			,String title_c, String content_c
			,String photo_url, String small_photo_url
			,String more_info, String more_info_link
			,String deadline
			){
		this.themeIndex = themeIndex;
		this.order = order;
		this.title = title;
		this.title_a = title_a;
		this.title_b = title_b;
		this.title_c = title_c;
		this.content= content;
		this.content_a= content_a;
		this.content_b= content_b;
		this.content_c= content_c;
		this.photo_url = photo_url;
		this.small_photo_url= small_photo_url;
		this.more_info = more_info;
		this.more_info_link = more_info_link;
		this.deadline = deadline;
		
	}

}
