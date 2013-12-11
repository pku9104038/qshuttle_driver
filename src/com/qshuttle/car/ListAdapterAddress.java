/**
 * 
 */
package com.qshuttle.car;

import java.util.ArrayList;

import com.amap.mapapi.offlinemap.City;
import com.qshuttle.car.PrefProxy.Address;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author wangpeifeng
 *
 */
public class ListAdapterAddress extends BaseAdapter {
	
	private ArrayList<Address> listItems;
	
	private Context context;
	
	

	/**
	 * @param listItems
	 * @param context
	 */
	public ListAdapterAddress(ArrayList<Address> listItems, Context context) {
		super();
		this.listItems = listItems;
		this.context = context;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return listItems.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listItems.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		try {
			
			convertView =  LayoutInflater.from(context).inflate(R.layout.listitem_address, null);
		 
			((TextView)convertView.findViewById(R.id.textViewCity)).setText(listItems.get(position).address);
			//((TextView)convertView.findViewById(R.id.textViewCity)).setTextColor(Color.BLUE);
		
		}
		catch (Exception e) {
		
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}

		return convertView;
	}

}
