/**
 * 
 */
package com.qshuttle.car;

import java.util.ArrayList;
import java.util.Iterator;

import com.amap.mapapi.map.MapActivity;
import com.amap.mapapi.offlinemap.City;
import com.amap.mapapi.offlinemap.MOfflineMapStatus;
import com.amap.mapapi.offlinemap.OfflineMapManager;
import com.amap.mapapi.offlinemap.OfflineMapManager.OfflineMapDownloadListener;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author wangpeifeng
 *
 */
public class ActivityOfflineMap extends MapActivity implements OfflineMapDownloadListener{
	
	private ArrayList<City> listCity ,listDownloading;
	
	private ArrayList<String> listDownloadingCity;
	
	private ListView listView;
	
	private ListAdapter listAdapter;
	
	public OfflineMapManager mapManager;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mapManager = new OfflineMapManager(this,this);
		
		Context context = getApplicationContext();

		listCity = mapManager.getOfflineCityList();
		
		if(listCity != null && listCity.size() > 0){
		
			setContentView(R.layout.citylist);
		
			listView = (ListView)findViewById(R.id.listView);
		
			listDownloadingCity = new ArrayList<String>();

			getDownloadingCity();
		
			listAdapter = new ListAdapter(listCity, listDownloadingCity, this);
		
			listView.setAdapter(listAdapter);
		
			listView.setOnItemClickListener(listener);
		
			((ImageButton)findViewById(R.id.imageButtonBack)).setOnClickListener(lsrButton);
		
		}
		
		else{

			Toast.makeText(context, R.string.address_list_null, Toast.LENGTH_LONG).show();
			
			finish();
			
		}
	}

	
	/* (non-Javadoc)
	 * @see com.amap.mapapi.map.MapActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		try{
			
//			mapManager.stop();
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		super.onDestroy();
	}

	public OnClickListener lsrButton = new OnClickListener(){

		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			finish();
			
		}
		
	};

	public OnItemClickListener listener = new OnItemClickListener(){

		/* (non-Javadoc)
		 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
		 */
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			
			mapManager.downloadByCityName(listCity.get(position).getCity());
			
			getDownloadingCity();
			
			listAdapter.notifyDataSetChanged();
       		
			listView.invalidate();
			

		}

		
		
	
	};
	
	public void onDownload(int status, int completeCode) {
		// TODO Auto-generated method stub
		switch(status)
		{
		case MOfflineMapStatus.LOADING:
			Toast.makeText(this,
					getApplicationContext().getResources().getString(R.string.map_downloading) + completeCode+"%",
					Toast.LENGTH_SHORT).show();
			break;
		
		case MOfflineMapStatus.ERROR:
			Toast.makeText(this,
					R.string.map_download_error,
					Toast.LENGTH_SHORT).show();
			break;
		
		case MOfflineMapStatus.PAUSE:
			Toast.makeText(this,
					R.string.map_download_pause,
					Toast.LENGTH_SHORT).show();
			break;
		
		case MOfflineMapStatus.STOP:
			Toast.makeText(this,
					R.string.map_download_stop,
					Toast.LENGTH_SHORT).show();
			break;

		case MOfflineMapStatus.SUCCESS:
			
			Toast.makeText(this,
					R.string.map_download_success,
					Toast.LENGTH_SHORT).show();
			
			getDownloadingCity();
			
			listAdapter.notifyDataSetChanged();
       		
			listView.invalidate();
		
			break;

		case MOfflineMapStatus.UNZIP:
			Toast.makeText(this,
					R.string.map_download_unzip,
					Toast.LENGTH_SHORT).show();
			break;

		case MOfflineMapStatus.WAITING:
			Toast.makeText(this,
					R.string.map_download_waiting,
					Toast.LENGTH_SHORT).show();
			break;

		}
		
	}
	
	
	private void getDownloadingCity(){
		
		listDownloading = mapManager.getDownloadingCityList();
		
		Iterator<City> i = listDownloading.iterator();
		
		listDownloadingCity.clear();
		
		while(i.hasNext()){
			
			City city = i.next();
			
			listDownloadingCity.add(city.getCity());
			
		}
	}

}
