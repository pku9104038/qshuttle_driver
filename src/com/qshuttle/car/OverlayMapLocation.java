/**
 * 
 */
package com.qshuttle.car;

import android.content.Context;
import android.graphics.Canvas;
import android.location.Location;
import android.location.LocationListener;

import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.map.MapView;
import com.amap.mapapi.map.MyLocationOverlay;

/**
 * @author wangpeifeng
 *
 */
public class OverlayMapLocation extends MyLocationOverlay {
	
	/*
	 * fields
	 */
	
	private Context context;

	private MapView mapView;
	
	private LocationListener listener = null;
	
	
	/**
	 * @param arg0
	 * @param arg1
	 */
	public OverlayMapLocation(Context context, MapView mapView) {
		super(context, mapView);
		// TODO Auto-generated constructor stub
		
		this.context = context;
		
		this.mapView = mapView;
		
	}


	/* (non-Javadoc)
	 * @see com.amap.mapapi.map.MyLocationOverlay#onLocationChanged(android.location.Location)
	 */
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		super.onLocationChanged(location);
		
		if(this.listener != null){
			
			listener.onLocationChanged(location);
			
		}
	}
	
	
	/*
	 * 
	 */
	
	/* (non-Javadoc)
	 * @see com.amap.mapapi.map.MyLocationOverlay#drawMyLocation(android.graphics.Canvas, com.amap.mapapi.map.MapView, android.location.Location, com.amap.mapapi.core.GeoPoint, long)
	 */
	@Override
	protected void drawMyLocation(Canvas canvas, MapView mapview, Location location,
			GeoPoint point, long arg4) {
		// TODO Auto-generated method stub
//		super.drawMyLocation(canvas, mapview, location, point, arg4);
	}


	public void registerLocationListener(LocationListener listener){
		
		this.listener = listener;
		
	}
	
	/*
	 * 
	 */
	
	public void removeLocationListener(Location listener){
		
		if(this.listener.equals(listener)){
			
			this.listener = null;
			
		}
		
	}
	
	
	

}
