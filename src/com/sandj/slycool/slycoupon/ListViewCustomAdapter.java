package com.sandj.slycool.slycoupon;

import java.util.ArrayList;

import com.sandj.slycoolchinese.R;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class ListViewCustomAdapter extends BaseAdapter{

	ArrayList<Object> itemList;

	private Activity _mContext;
	public LayoutInflater inflater;
	private Context _Context = null;
	
	public ImageLoaderForListView imageLoader =null;

	public ListViewCustomAdapter(Activity context,ArrayList<Object> itemList) {
		super();
		this._Context = context;
		this._mContext = context;
		this.itemList = itemList;
		imageLoader = new ImageLoaderForListView(_mContext);
		
		this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return itemList.size();
	}

	public Object getItem(int position) {
		return itemList.get(position);
	}

	public long getItemId(int position) {
		return 0;
	}

	public static class ViewHolder
	{
		private  ImageView _imgViewLogo=null;
		private TextView _txtName=null;
		private TextView _txtAddress=null;
		private  TextView  _txtDistances=null;
		private TextView _storeIdtTextView= null;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if(convertView==null)
		{
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.itemshowlist, null);

			holder._imgViewLogo = (ImageView) convertView.findViewById(R.id.imgViewLogo);
			holder._txtName = (TextView) convertView.findViewById(R.id.txtName);
			holder._txtAddress = (TextView) convertView.findViewById(R.id.txtAddress);
			holder._txtDistances=(TextView)convertView.findViewById(R.id.txtDistances);
			holder._storeIdtTextView=(TextView)convertView.findViewById(R.id.txtStoreId_itemlist);
			holder._storeIdtTextView.setVisibility(View.GONE);

			convertView.setTag(holder);
		}
		else
			holder=(ViewHolder)convertView.getTag();



		ItemBean bean = (ItemBean) itemList.get(position);

		try {
			if(holder._imgViewLogo !=null){
				holder._imgViewLogo.setTag(bean.getImage());
				imageLoader
				.displayImage(bean.getImage(), _mContext, holder._imgViewLogo, true);
			}
		} catch (Exception e) {
			System.out.println(e+"=====>>>>>");
			e.printStackTrace();
		}

		holder._txtName.setText(bean.getName());
		holder._txtAddress.setText(bean.getAddress());
		holder._txtDistances.setText(bean.getDistances()+"Km");
		holder._storeIdtTextView.setText(bean.getStoreId());

		return convertView;
	}

}
