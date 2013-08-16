package com.sandj.coverflow;


import com.sandj.slycoolchinese.Constants;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class ImageAdapter2 extends BaseAdapter {
	int mGalleryItemBackground;
	private Context mContext;
	private int flowLength = 3 ;
	public ImageLoader imageLoader; 
//	private Integer[] mImageIds = { 
//			R.drawable.a1,
//			R.drawable.a2, 
//			R.drawable.a3};
	
	public ImageAdapter2(Context c) {
		mContext = c;
		imageLoader=new ImageLoader(c);
	}

	public int getCount() {
		return 3;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView = new ImageView(mContext);
//		ImageView imageView = (ImageView) convertView;
		try {
			imageLoader.DisplayImage(Constants.allPromoteData.get(position).photo_link, imageView);
		}catch(Exception e){
			
		}
		
//		if(position==0){
//			imageView.setImageDrawable(Constants.promote0);
//		}else if(position==1){
//			imageView.setImageDrawable(Constants.promote1);
//		}else if(position==2){
//			imageView.setImageDrawable(Constants.promote2);
//		}
		
		if (Constants.Display_WIDTH > 600){
			imageView.setLayoutParams(new Gallery.LayoutParams(300, 210));
		}else{
			imageView.setLayoutParams(new Gallery.LayoutParams(200, 140));
		}
		imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		return imageView;

	}

//	public float getScale(boolean focused, int offset) {
//		return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
//	}	

}

