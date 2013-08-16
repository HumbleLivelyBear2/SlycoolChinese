package com.techvalens.restaurant.imageloader;

/*
 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements.  See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.    
 */
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.sandj.slycoolchinese.R;



import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.Contacts;
import android.widget.ImageView;



public class DrawableManager {
	private final Map<String, Drawable> drawableMap;
	private Drawable serverImage = null;
	private Vector<ImageView> _queuedObj;


	public DrawableManager() {
		_queuedObj = new Vector<ImageView>();
		drawableMap = new HashMap<String, Drawable>();
	}

	private InputStream fetch(String urlString) throws MalformedURLException,
	IOException {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet request = new HttpGet(urlString);
		HttpResponse response = httpClient.execute(request);
		return response.getEntity().getContent();
	}

	public Drawable fetchDrawable(String urlString) {
		if (drawableMap.containsKey(urlString)) {
			return drawableMap.get(urlString);
		}

		try {
			InputStream is = fetch(urlString);
			Drawable drawable = getRoundedCornerImage(Drawable
					.createFromStream(is, "src"));

			if (drawable != null) {
				drawableMap.put(urlString, drawable);
				/*
				 * Log.d(this.getClass().getSimpleName(),
				 * "got a thumbnail drawable: " + drawable.getBounds() + ", " +
				 * drawable.getIntrinsicHeight() + "," +
				 * drawable.getIntrinsicWidth() + ", " +
				 * drawable.getMinimumHeight() + "," +
				 * drawable.getMinimumWidth());
				 */
			} else {
				/*
				 * Log.w(this.getClass().getSimpleName(),
				 * "could not get thumbnail");
				 */
			}

			return drawable;
		} catch (MalformedURLException e) {
			// Log.e(this.getClass().getSimpleName(), "fetchDrawable failed",
			// e);
			return null;
		} catch (IOException e) {
			// Log.e(this.getClass().getSimpleName(), "fetchDrawable failed",
			// e);
			return null;
		}
	}

	public void fetchDrawableOnThread(final String urlString,
			final ImageView imageView, final Activity activity)	throws Exception {

		/*
		 * if(_queuedObj.contains(imageView)){ return; }else{
		 * _queuedObj.add(imageView); }
		 */
		urlString.replaceAll(" ", "%20");
		if (drawableMap.containsKey(urlString)) {
			serverImage = drawableMap.get(urlString);
			imageView.setImageDrawable(drawableMap.get(urlString));
			return;
		}

		if (urlString.startsWith("content:")) {
			Uri photoUri = Uri.parse(urlString);
			Cursor cursor = activity.getContentResolver().query(photoUri,
					new String[] { Contacts.Photo.DATA15 }, null, null, null);
			byte[] data = null;
			if (cursor != null) {
				try {
					if (cursor.moveToFirst()) {
						data = cursor.getBlob(0);
						if (data != null) {
							Bitmap bitmap = BitmapFactory.decodeByteArray(data,
									0, data.length);
							if (bitmap != null) {
								Drawable imageDrawable = new BitmapDrawable(
										bitmap);
								imageView
								.setImageDrawable(getRoundedCornerImage(imageDrawable));
								bitmap.recycle();
							} else
								imageView
								.setImageResource(R.drawable.app_icon);
							return;
						}
					}
				} finally {
					cursor.close();
				}
			}
		} else {
			final Handler handler = new Handler() {
				@Override
				public void handleMessage(Message message) {
					try {
						serverImage = (Drawable) message.obj;
						// imageView.setImageDrawable((Drawable) message.obj);
						// Bitmap bitmap =
						// ((BitmapDrawable)serverImage).getBitmap();
						imageView.setImageDrawable(serverImage);

					} catch (Exception e) {
						imageView.setImageResource(R.drawable.app_icon);
					}
				}
			};
			Thread thread = new Thread() {
				@Override
				public void run() {
					Drawable drawable = fetchDrawable(urlString);
					if (drawable != null) {
						Message message = handler.obtainMessage(1, drawable);
						handler.sendMessage(message);
					} else {
						activity.runOnUiThread(new Runnable() {
							public void run() {
								imageView
								.setImageResource(R.drawable.app_icon);
							}
						});
					}
				}
			};
			thread.start();
		}
	}

	public Drawable getImageDrawable() {
		return serverImage;
	}

	public Drawable getRoundedCornerImage(Drawable bitmapDrawable) {
		try{
			Bitmap bitmap = ((BitmapDrawable) bitmapDrawable).getBitmap();
			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
					bitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);

			final int color = 0xff424242;
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			final RectF rectF = new RectF(rect);
			final float roundPx = 10;

			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);
			Drawable image = new BitmapDrawable(output);
			return image;
		}catch (OutOfMemoryError outEx) {
			outEx.printStackTrace();
			return null;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

}