package com.techvalens.restaurant.imageloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Stack;

import android.app.Activity;
import android.content.Context;
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
import android.provider.ContactsContract.Contacts;
import android.widget.ImageView;

public class ImageLoader {

	// Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable {
		Drawable bitmapDrawable;
		ImageView imageView;

		public BitmapDisplayer(Drawable b, ImageView i) {
			bitmapDrawable = b;
			imageView = i;
		}

		public void run() {
			if (bitmapDrawable != null) {
				_returnImage = bitmapDrawable;
				imageView.setImageDrawable(bitmapDrawable);
			} else {
				/*imageView.setImageResource(stub_id);*/
			}
		}
	}

	class PhotosLoader extends Thread {
		public void run() {
			try {
				while (true) {
					// thread waits until there are any images to load in the
					// queue
					if (photosQueue.photosToLoad.size() == 0)
						synchronized (photosQueue.photosToLoad) {
							photosQueue.photosToLoad.wait();
						}
					if (photosQueue.photosToLoad.size() != 0) {
						PhotoToLoad photoToLoad;
						synchronized (photosQueue.photosToLoad) {
							photoToLoad = photosQueue.photosToLoad.pop();
						}
						Drawable bmp = getBitmap(photoToLoad.url);
						cache.put(photoToLoad.url, bmp);
						Object tag = photoToLoad.imageView.getTag();
						if (tag != null
								&& ((String) tag).equals(photoToLoad.url)) {
							BitmapDisplayer bd = new BitmapDisplayer(bmp,photoToLoad.imageView);
							Activity a = (Activity) photoToLoad.imageView
							.getContext();
							a.runOnUiThread(bd);
						}
					}
					if (Thread.interrupted())
						break;
				}
			} catch (OutOfMemoryError outEx) {
				outEx.printStackTrace();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// stores list of photos to download
	class PhotosQueue {
		private Stack<PhotoToLoad> photosToLoad = new Stack<PhotoToLoad>();

		// removes all instances of this ImageView
		public void Clean(ImageView image) {
			for (int j = 0; j < photosToLoad.size();) {
				if (photosToLoad.get(j).imageView == image)
					photosToLoad.remove(j);
				else
					++j;
			}
		}
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;

		public PhotoToLoad(String u, ImageView i) {
			url = u;
			imageView = i;
		}
	}

	// the simplest in-memory cache implementation. This should be replaced with
	// something like SoftReference or BitmapOptions.inPurgeable(since 1.6)
	private HashMap<String, Drawable> cache = new HashMap<String, Drawable>();
	private File cacheDir;

	private Drawable _returnImage = null;

	/*final int stub_id = R.drawable.app_icon;

	final int stub_id1 = R.drawable.app_icon; // Default image
	 */
	PhotosQueue photosQueue = new PhotosQueue();

	PhotosLoader photoLoaderThread = new PhotosLoader();

	public ImageLoader(Context context) {
		// Make the background thead low priority. This way it will not affect
		// the UI performance
		photoLoaderThread.setPriority(Thread.NORM_PRIORITY - 1);

		// Find the dir to save cached images
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			cacheDir = new File(
					android.os.Environment.getExternalStorageDirectory(),
			/*Constants.APP_DIRECTOR_NAME+*/ "/ChineseRestaurant/ImageCache/");
		else
			cacheDir = context.getCacheDir();
		if (!cacheDir.exists())
			cacheDir.mkdirs();
	}

	public void clearCache() {
		// clear memory cache
		cache.clear();

		// clear SD cache
		File[] files = cacheDir.listFiles();
		for (File f : files)
			f.delete();
	}

	// decodes _image and scales it to reduce memory consumption
	private Drawable decodeFile(File f) {
		try {
			// decode _image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 70;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			Drawable image = new BitmapDrawable(BitmapFactory.decodeStream(
					new FileInputStream(f), null, o2));
			return getRoundedCornerImage(image);
		} catch (OutOfMemoryError outEx) {
			outEx.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	// MEHTOD USED IN PARSING THE IMAGES FROM URL
	public void displayImage(String url, Activity activity,
			ImageView imageView, boolean isWish) {

		Drawable drawable = null;
		try {

			if (url.startsWith("content:")) {
				Uri photoUri = Uri.parse(url);
				Cursor cursor = activity.getContentResolver().query(photoUri,
						new String[] { Contacts.Photo.DATA15 }, null, null,
						null);
				byte[] data = null;
				if (cursor != null) {
					try {
						if (cursor.moveToFirst()) {
							data = cursor.getBlob(0);
							if (data != null) {
								Bitmap bitmap = BitmapFactory.decodeByteArray(
										data, 0, data.length);
								if (bitmap != null) {
									Drawable imageDrawable = new BitmapDrawable(
											bitmap);
									imageView
									.setImageDrawable(getRoundedCornerImage(imageDrawable));
								}
								return;
							}
						}
					} finally {
						cursor.close();
					}
				}
			} else if (cache.containsKey(url)) {
				drawable = cache.get(url);
				if (drawable != null)
					imageView.setImageDrawable(drawable);
				return;
			}

			queuePhoto(url, activity, imageView);
			// bitmap= BitmapFactory.decodeResource(activity.getResources(),
			// stub_id);
			/*if (isWish) {
				imageView.setImageResource(stub_id1);
			} else {
				imageView.setImageResource(stub_id);
			}*/
		}  catch (OutOfMemoryError outEx) {
			outEx.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}/*
		 * finally{ if(bitmap != null){ bitmap.recycle(); } }
		 */
	}

	private Drawable getBitmap(String url) {
		// I identify images by hashcode. Not a perfect solution, good for the
		// demo.
		String filename = String.valueOf(url.hashCode());
		File f = new File(cacheDir, filename);

		// from SD cache
		Drawable b = decodeFile(f);
		if (b != null)
			return b;

		// from web
		try {
			Drawable bitmapDrawable = null;
			InputStream is = new URL(url).openStream();
			OutputStream os = new FileOutputStream(f);
			Utils.CopyStream(is, os);
			os.close();
			bitmapDrawable = decodeFile(f);
			return bitmapDrawable;
		}catch (OutOfMemoryError outEx) {
			outEx.printStackTrace();
			return null;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}
	//MEHTOD USED IN MENU SCREEN ACTIVITY TO RESIZE THE IMAGE.......!
	public Drawable getBimapFromURl(String url, Activity activity, ImageView imageview) {
		// I identify images by hashcode. Not a perfect solution, good for the
		// demo.
		String filename = String.valueOf(url.hashCode());
		File f = new File(cacheDir, filename);
		// from web
		try {
			Drawable bitmapDrawable = null;
			InputStream is = new URL(url).openStream();
			OutputStream os = new FileOutputStream(f);
			Utils.CopyStream(is, os);
			os.close();
			bitmapDrawable = decodeFile(f);

			imageview.setImageDrawable(bitmapDrawable);

			return bitmapDrawable;
		}
		catch (OutOfMemoryError outEx) {
			outEx.printStackTrace();
			return null;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}


	public Drawable getImageBitmap() {
		return _returnImage;
	}

	public Drawable getRoundedCornerImage(Drawable bitmapDrawable)
	{
		try{
			Bitmap bitmap = ((BitmapDrawable) bitmapDrawable).getBitmap();
			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
					bitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);

			final int color = 0xff424242;
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			final RectF rectF = new RectF(rect);
			final float roundPx = 0;//for value 0 it will show sharp edges.....!

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
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	private void queuePhoto(String url, Activity activity, ImageView imageView) {
		// This ImageView may be used for other images before. So there may be
		// some old tasks in the queue. We need to discard them.
		photosQueue.Clean(imageView);
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		synchronized (photosQueue.photosToLoad) {
			photosQueue.photosToLoad.push(p);
			photosQueue.photosToLoad.notifyAll();
		}

		// start thread if it's not started yet
		if (photoLoaderThread.getState() == Thread.State.NEW)
			photoLoaderThread.start();
	}

	public void stopThread() {
		photoLoaderThread.interrupt();
	}
}
