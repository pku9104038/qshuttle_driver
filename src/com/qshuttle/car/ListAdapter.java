/**
 * 
 */
package com.qshuttle.car;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.amap.mapapi.offlinemap.City;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
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
public class ListAdapter extends BaseAdapter {
	
	private ArrayList<City> listItems ;
	
	private ArrayList<String> listDownloadingCity;
	
	private Context context;

	/**
	 * @param listItems
	 * @param context
	 */
	public ListAdapter(ArrayList<City> listItems, ArrayList<String> listDownloadingCity, Context context) {
		super();
		this.listItems = listItems;
		this.listDownloadingCity = listDownloadingCity;
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

	
	
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
//		if(convertView==null){
			
			
			try {
				
				convertView =  LayoutInflater.from(context).inflate(R.layout.listitem_city, null);
			 
				City city = listItems.get(position);
				
				((TextView)convertView.findViewById(R.id.textViewCity)).setText(city.getCity());

				if(listDownloadingCity.contains(city.getCity())){
					
					((ImageView)convertView.findViewById(R.id.imageViewDownloading)).setVisibility(View.VISIBLE);
				
				}
				else{
					
					((ImageView)convertView.findViewById(R.id.imageViewDownloading)).setVisibility(View.INVISIBLE);
							
				}
				
			}
			catch (Exception e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
			}
//		}
		return convertView;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	

}
