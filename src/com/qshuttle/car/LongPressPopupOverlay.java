/**
 * 
 */
package com.qshuttle.car;

import java.util.ArrayList;
import java.util.List;

import com.amap.mapapi.core.OverlayItem;
import com.amap.mapapi.map.ItemizedOverlay;
import com.amap.mapapi.map.MapView;
import com.amap.mapapi.map.ItemizedOverlay.*;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * @author wangpeifeng
 *
 */
public class LongPressPopupOverlay extends ItemizedOverlay implements OnFocusChangeListener{
	
	private static final String TAG = "LongPressPopupOverlay";  
	
	private List<OverlayItem> lstItems = new ArrayList<OverlayItem>();  
	
	private Context context;

	private MapView mapView;
	
	private View popView;
	
	private OverlayItem focusItem;
	 
	private Drawable marker;
	
	private OnClickListener listener;
	
	    
	
	public LongPressPopupOverlay(Context context, MapView mapView, Drawable marker) {
		
		super(boundCenterBottom(marker));
		// TODO Auto-generated constructor stub
		
		this.context = context;
		
		this.mapView = mapView;
		
		this.marker = marker;
		
		this.listener = listenerButton;
		
	    initPopView();
	    
	    this.setOnFocusChangeListener((OnFocusChangeListener) this);
	    
	}
	
	
	public LongPressPopupOverlay(Context context, MapView mapView, Drawable marker, OnClickListener listener) {
		
		super(boundCenterBottom(marker));
		// TODO Auto-generated constructor stub
		
		this.context = context;
		
		this.mapView = mapView;
		
		this.marker = marker;
		
		this.listener = listener;
		
	    initPopView();
	    
	    this.setOnFocusChangeListener((OnFocusChangeListener) this);
	    
	}

		private void initPopView(){  
	        
		 if(null == popView){  

			 popView = (((Activity) context).getLayoutInflater().inflate(R.layout.addressbubble, null));  
	         
			 mapView.addView(popView, new MapView.LayoutParams(  
	                    MapView.LayoutParams.WRAP_CONTENT,  
	                    MapView.LayoutParams.WRAP_CONTENT, null,  
	                    MapView.LayoutParams.BOTTOM_CENTER));  
	         
			 popView.setVisibility(View.GONE);  

			 ((RelativeLayout)(popView.findViewById(R.id.relativeLayoutGo))).setOnClickListener(listener);  
	           
		 }  
	         
	    
	 }  

	 
	 public View getPopView(){
	
		 return popView;
	 
	 }
	 
	 @Override
	
	 protected OverlayItem createItem(int index) {
		// TODO Auto-generated method stub
		return lstItems.get(index);
	
	 }

	
	 @Override
	
	 public int size() {
		// TODO Auto-generated method stub
	
		 return lstItems.size();
	
	 }
	
	 
	
	 public void addItem(OverlayItem item) {  
	
		 lstItems.add(item);  
		
		 populate();  
	
	 }  
	
	
	 public void removeItem(int index) {  
	
		 lstItems.remove(index);  
	
	 }  

	 
	 public void onFocusChanged(ItemizedOverlay<?> overlay, OverlayItem newFocus) {
	
		 // TODO Auto-generated method stub
		 if (null != newFocus) {  
        
			 MapView.LayoutParams params = (MapView.LayoutParams) popView.getLayoutParams();  
            
			 params.x = marker.getBounds().centerX();// 
            
			 params.y =  - marker.getBounds().height();//  
            
			 params.point = newFocus.getPoint();  
            
			 TextView title_TextView = (TextView) popView.findViewById(R.id.map_bubbleTitle);  
            
			 title_TextView.setText(newFocus.getTitle());  
            
			 TextView desc_TextView = (TextView) popView.findViewById(R.id.map_bubbleText);  
            
			 desc_TextView.setText(newFocus.getSnippet());  
             
			 mapView.updateViewLayout(popView, params);  
            
			 popView.setVisibility(View.VISIBLE);  
            
			 mapView.getController().animateTo(newFocus.getPoint());  
	            
			 focusItem = newFocus;  
        
		 }  	
        
	
	 }
	
	 public void registerListener(OnClickListener listener){
		 
		 this.listener = listener;
		 
	 }
	
	 public OnClickListener listenerButton = new OnClickListener(){

		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			Log.i("click", "click!");
			
			
			popView.setVisibility(View.GONE);  
			
			Message msg = new Message();
			msg.what = ActivityMain.HANDLER_MSG_WHAT_START_ROUTE;
			
			Bundle bundle = new Bundle();
			bundle.putInt(ActivityMain.BUNDLE_KEY_LATE6, focusItem.getPoint().getLatitudeE6());
			bundle.putInt(ActivityMain.BUNDLE_KEY_LONGE6, focusItem.getPoint().getLongitudeE6());
			msg.setData(bundle);
			
			ActivityMain.handlerRef.sendMessage(msg);

			PrefProxy.Address address = (new PrefProxy()).new Address();
			
			address.address = focusItem.getTitle();
			
			address.late6 = focusItem.getPoint().getLatitudeE6();
			
			address.longe6 = focusItem.getPoint().getLongitudeE6();
			
			PrefProxy.updateRecentAddress(context, address);
		}
		 
	 };


}
