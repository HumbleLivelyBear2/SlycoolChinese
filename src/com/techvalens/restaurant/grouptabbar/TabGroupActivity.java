package com.techvalens.restaurant.grouptabbar;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.LocalActivityManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;

/**
 * The purpose of this Activity is to manage the activities in a tab.
 * Note: Child Activities can handle Key Presses before they are seen here.
 * @author Eric Harlow
 * 一開始先進來這個Group, 所以有些程式先在這裡整理
 */
public class TabGroupActivity extends ActivityGroup {

	private ArrayList<String> mIdList;
	private HashMap<String, Window> mWindowStorage = new HashMap<String, Window>(); //UPDATED
	private boolean _isFromResume;
	
	private AlertDialog.Builder finishDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);       
		if (mIdList == null) mIdList = new ArrayList<String>();
	}

	/**
	 * This is called when a child activity of this one calls its finish method. 
	 * This implementation calls {@link LocalActivityManager#destroyActivity} on the child activity
	 * and starts the previous activity.
	 * If the last child activity just called finish(),this activity (the parent),
	 * calls finish to finish the entire group.
	 * 
	 * 當子activity要結束時, 就會自動call這個method, 應該不用理它
	 */
	@Override
	public void finishFromChild(Activity child) {
		LocalActivityManager manager = getLocalActivityManager();
		int index = mIdList.size()-1;

		if (index < 1) {
			finish();
			return;
		}
		String actName = mIdList.get(index);
		manager.destroyActivity(actName, true);
		mWindowStorage.remove(actName);
		mIdList.remove(index); index--;
		String lastId = mIdList.get(index);
		Window newWindow = null;
		if(mWindowStorage.containsKey(lastId)){
			newWindow = mWindowStorage.get(lastId);
		}else{
			Intent lastIntent = manager.getActivity(lastId).getIntent();
			newWindow = manager.startActivity(lastId, lastIntent);
		}
		setContentView(newWindow.getDecorView());  
	}

	/**
	 * Starts an Activity as a child Activity to this.
	 * @param Id Unique identifier of the activity to be started.
	 * @param intent The Intent describing the activity to be started.
	 * @throws android.content.ActivityNotFoundException.
	 * 
	 * 用來call子activity的method, 問題是為何不照android原本內建的呢?
	 */
	public void startChildActivity(String Id, Intent intent) {
		Window window = null;
		if(mWindowStorage.containsKey(Id)){
			window = mWindowStorage.get(Id);
			if(_isFromResume){
				setFromResume(false);
				mIdList.clear();
				mIdList.add(Id);
				mWindowStorage.clear();
				mWindowStorage.put(Id, window);
			}
		}else{
			window = getLocalActivityManager().startActivity(Id,intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));	
			if (window != null) {
				mIdList.add(Id);
				mWindowStorage.put(Id, window);
			}
		}
		setContentView(window.getDecorView()); 
	}

	/**
	 * The primary purpose is to prevent systems before android.os.Build.VERSION_CODES.ECLAIR
	 * from calling their default KeyEvent.KEYCODE_BACK during onKeyDown.
	 * 
	 * 使onKeyDown,在tab層的時候失效 
	 * => 應該要想辦法改成在tab層onKeyDown, 會回到主畫面, 但不結束程式
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			//preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * Overrides the default implementation for KeyEvent.KEYCODE_BACK 
	 * so that all systems call onBackPressed().
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			onBackPressed();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	/**
	 * If a Child Activity handles KeyEvent.KEYCODE_BACK.
	 * Simply override and add this method.
	 */
	@Override
	public void  onBackPressed  () {
		int length = mIdList.size();
		if ( length > 1) {
			Activity current = getLocalActivityManager().getActivity(mIdList.get(length-1));
			current.finish();
		}else{
//			finishDialog = new AlertDialog.Builder(this).setTitle("結束執行")
//					.setMessage("是否離開士林任我行?")
//					.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							getParent().finish();
//							System.exit(0);
//						}
//					})
//					.setNegativeButton("No", new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							dialog.cancel();
//						}
//					});
//			finishDialog.show();
			finish();
		}  
	}

	public void setFromResume(boolean isFromResume) {
		_isFromResume = isFromResume;
	}
}

