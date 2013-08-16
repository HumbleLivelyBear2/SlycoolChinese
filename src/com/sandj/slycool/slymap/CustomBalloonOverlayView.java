/***
 * Copyright (c) 2011 readyState Software Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.sandj.slycool.slymap;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.maps.OverlayItem;

import com.sandj.slycoolchinese.Constants;
import com.sandj.slycoolchinese.R;
import com.techvalens.readystatesoftware.mapviewballoons.BalloonOverlayView;

import com.techvalens.restaurant.grouptabbar.TabGroupActivity;
import com.techvalens.restaurant.imageloader.ImageLoader;
//import com.techvalens.restaurant.restaurantdetail.MenuDrawableManager;
import com.techvalens.restaurant.restaurantdetail.RestaurantMapLocation;
import com.techvalens.restaurant.restaurantdetail.RestaurantsDetail;

public class CustomBalloonOverlayView<Item extends OverlayItem> extends BalloonOverlayView<CustomOverlayItem> {

	private TextView title;
	private TextView snippet;
	private ImageView image;
	TextView storeIdTextView;
	private Context mcontext;
	private String _transferStoreId;//STORE ID TO CALL THE RESTAURANT DEATILS WEBSERVICE
	private ImageLoader imageLoader = null;
//	private MenuDrawableManager _menuDrawable = null;
	public CustomBalloonOverlayView(Context context, int balloonBottomOffset) {
		super(context, balloonBottomOffset);
//		_menuDrawable = new MenuDrawableManager();
		imageLoader = new ImageLoader(mcontext);
	}
	@Override
	protected void setupView(Context context, final ViewGroup parent) {
		mcontext = context;



		// inflate our custom layout into parent
		LayoutInflater inflater = (LayoutInflater) context
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.custom_balloon_overlay, parent);

		// setup our fields
		title = (TextView) v.findViewById(R.id.balloon_item_title);
		snippet = (TextView) v.findViewById(R.id.balloon_item_snippet);
		image = (ImageView) v.findViewById(R.id.balloon_item_image);

		//IMPLEMENTING BALLOON DETAILS
		ImageButton details = (ImageButton) v.findViewById(R.id.balloon_details);
		if(mcontext instanceof RestaurantMapLocation){
			details.setBackgroundResource(R.drawable.icon_close);
		}else{
			details.setBackgroundResource(R.drawable.detail);
		}
		details.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mcontext instanceof RestaurantMapLocation){
					parent.setVisibility(View.GONE);

				}else{
					Intent intent = new Intent(mcontext, RestaurantsDetail.class);
//					TabGroupActivity parentActivity = (TabGroupActivity)Constants.getMapTabParentContext();
					intent.putExtra("STORE_ID", _transferStoreId);
//					parentActivity.startChildActivity("RestaurantDetail", intent);
					mcontext.startActivity(intent);

				}

			}
		});

		/*		if(mcontext instanceof RestaurantMapLocation){
			details.setVisibility(View.GONE);
		}
		 */		
		storeIdTextView = (TextView)parent.findViewById(R.id.balloon_storeId_custom);
		storeIdTextView.setVisibility(View.GONE);

	}

	@Override
	protected void setBalloonData(CustomOverlayItem item, ViewGroup parent) {
		// map our custom item data to fields
		title.setText(item.getTitle());
		snippet.setText(item.getSnippet());
		storeIdTextView.setText(item.getStoreId());

		_transferStoreId = storeIdTextView.getText().toString();

		System.out.println(storeIdTextView.getText().toString()+"=======>>>>>>");
		// get remote _image from network.
		// bitmap results would normally be cached, but this is good enough for demo purpose.
		/*image.setImageResource(R.drawable.icon);*/
		/*new FetchImageTask() { 
			protected void onPostExecute(Bitmap result) {
				if (result != null) {
					image.setImageBitmap(result);
				}
			}
		}.execute(item.getImageURL());
		 */
		try {
			String url = item.getImageURL();
			if(url.length()>0){
				image.setTag(item.getImageURL());
				imageLoader
				.displayImage(url, (Activity)mcontext, image, true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private class FetchImageTask extends AsyncTask<String, Integer, Bitmap> {
		@Override
		protected Bitmap doInBackground(String... arg0) {
			Bitmap b = null;
			try {
				b = BitmapFactory.decodeStream((InputStream) new URL(arg0[0]).getContent());
			}catch (OutOfMemoryError outEx) {
				outEx.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
			return b;
		}	
	}
}
