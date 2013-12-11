/**
 * 
 */
package com.qshuttle.car;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.core.OverlayItem;
import com.amap.mapapi.map.ItemizedOverlay;
import com.amap.mapapi.map.MapActivity;
import com.amap.mapapi.map.MapView;
import com.amap.mapapi.route.Route;
import com.amap.mapapi.route.Segment;
import com.amap.mapapi.route.Route.FromAndTo;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;





/**
 * @author wangpeifeng
 *
 */
public class PassengerScheduleOverlay extends ItemizedOverlay {

	private static final String TAG = "PassengerScheduleOverlay";
	
	private Context context;
	
	private ArrayList<OverlayItem> listItems;
	private ArrayList<JSONObject>	listWaiting,
									listOnboard,
									listTickets,
									listSchedule;
	private List<GeoPoint>	listPassby;
	
	private GeoPoint startPoint, terminalPoint;
	
	private int scheduleMinutes, scheduleMeters , minutes, meters;
	
	private JSONObject instance_object;
	private String line_type = null;
	
	//private Drawable marker;

	public PassengerScheduleOverlay(Drawable marker, JSONObject jsonObj, Context context) throws JSONException{
		super(boundCenterBottom(marker));
		// TODO Auto-generated constructor stub
		
		this.context = context;
		
		//this.marker = marker;
		
		this.instance_object = jsonObj;
		this.listPassby = new ArrayList<GeoPoint>();
		
		int late6,longe6;
		
		late6 = jsonObj.getInt(DatabaseHelper.DB_COLUMN_LINE_TERMINAL_LATE6);
		
		longe6 = jsonObj.getInt(DatabaseHelper.DB_COLUMN_LINE_TERMINAL_LONGE6);
		
		terminalPoint = new GeoPoint(late6, longe6);
		/*
		startPoint = new GeoPoint( PrefProxy.getMyLate6(context),
									PrefProxy.getMyLonge6(context) );
		*/
		late6 = jsonObj.getInt(DatabaseHelper.DB_COLUMN_LINE_START_LATE6);
		
		longe6 = jsonObj.getInt(DatabaseHelper.DB_COLUMN_LINE_START_LONGE6);
		
		startPoint = new GeoPoint(late6, longe6);		
		
		listTickets = new ArrayList<JSONObject>();
		int i = 0;
		JSONArray ticketArray = jsonObj.getJSONArray(WebApi.API_JSON_TICKET_ARRAY);
		for (i = 0; i < ticketArray.length(); i++){
			
			listTickets.add(ticketArray.getJSONObject(i));
		
		};
		this.line_type = jsonObj.getString(DatabaseHelper.DB_COLUMN_LINE_OPERATION_TYPE);
		
		schedulePassenger();
		
		/*
		listItems = new ArrayList<OverlayItem>();
		
		listTickets = schedulePassengerItems(jsonObj, terminalPoint, startPoint); //
		
		int size = listTickets.size();

		OverlayItem overlayitem;
		
		GeoPoint point;
		
		JSONObject objItem;
		
		
		for(i = 0 ;i < size ; i++){
			
//			objItem = jsonArray.getJSONObject(i);
			
			objItem = listTickets.get(i);
			
			late6 = objItem.getInt(DatabaseHelper.DB_COLUMN_TICKET_PASSENGER_LATE6);
			
			longe6 = objItem.getInt(DatabaseHelper.DB_COLUMN_TICKET_PASSENGER_LONGE6);
			
			point = new GeoPoint(late6,longe6);
			
			String name = objItem.getString(DatabaseHelper.DB_COLUMN_TICKET_PASSENGER_NAME);
			
			String number = objItem.getString(DatabaseHelper.DB_COLUMN_TICKET_PASSENGER_PHONE);
			
			String address = objItem.getString(DatabaseHelper.DB_COLUMN_TICKET_PASSENGER_ADDRESS);
			
			//overlayitem = new OverlayItem(point,name, name);
			
			overlayitem = new OverlayItem(point,name, objItem.toString());
			
			listItems.add(overlayitem);
			
		}
		
		
		populate();	
		*/
		
	}
	
	
	
	private void  schedulePassenger() throws JSONException{
		
		int late6,longe6;
		
		
		listItems = new ArrayList<OverlayItem>();
		
		this.listSchedule = schedulePassengerItems(terminalPoint, startPoint); //
		
		
		
		
//		int size = this.listSchedule.size();
		int size = this.listTickets.size();

		OverlayItem overlayitem;
		
		GeoPoint point;
		
		JSONObject objItem;
		
		int i;
		for(i = 0 ;i < size ; i++){
			
//			objItem = jsonArray.getJSONObject(i);
			
//			objItem = this.listSchedule.get(i);
			objItem = this.listTickets.get(i);
					
			late6 = objItem.getInt(DatabaseHelper.DB_COLUMN_TICKET_PASSENGER_LATE6);
			
			longe6 = objItem.getInt(DatabaseHelper.DB_COLUMN_TICKET_PASSENGER_LONGE6);
			
			point = new GeoPoint(late6,longe6);
			
			String name = objItem.getString(DatabaseHelper.DB_COLUMN_TICKET_PASSENGER_NAME);
			
			String number = objItem.getString(DatabaseHelper.DB_COLUMN_TICKET_PASSENGER_PHONE);
			
			String address = objItem.getString(DatabaseHelper.DB_COLUMN_TICKET_PASSENGER_ADDRESS);
			
			//overlayitem = new OverlayItem(point,name, name);
			
			overlayitem = new OverlayItem(point,name, objItem.toString());
			
			listItems.add(overlayitem);
			Log.i(TAG, "items:"+i+listItems.get(i).getTitle());
				
		}
		
		Log.i(TAG, "items:"+listItems.size());
		
		populate();	

	}

	/* (non-Javadoc)
	 * @see com.mapabc.mapapi.ItemizedOverlay#createItem(int)
	 */
	@Override
	protected OverlayItem createItem(int index) {
		// TODO Auto-generated method stub

		return listItems.get(index);

	}


	/* (non-Javadoc)
	 * @see com.mapabc.mapapi.ItemizedOverlay#size()
	 */
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return listItems.size();
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.amap.mapapi.map.ItemizedOverlay#setFocus(com.amap.mapapi.core.OverlayItem)
	 */
	@Override
	public void setFocus(OverlayItem item) {
		// TODO Auto-generated method stub
		super.setFocus(item);
	}

	
	/* (non-Javadoc)
	 * @see com.amap.mapapi.map.ItemizedOverlay#draw(android.graphics.Canvas, com.amap.mapapi.map.MapView, boolean)
	 */
	@Override
	public void draw(Canvas canvas, MapView mapview, boolean shadow) {
		// TODO Auto-generated method stub
		//super.draw(canvas, mapview, shadow);
		
		try{
			
		if(!shadow){
		
//			Iterator<OverlayItem> iterator = listItems.iterator();
			int size = listItems.size();
			int count = 0;
//			while(iterator.hasNext()){
				
//				OverlayItem item = iterator.next();

			for( int i = size-1; i >= 0; i-- ){
				
				OverlayItem item = listItems.get(i);

//				count++;
				count = i+1;
				//Log.i(TAG, "drawItem:"+i);
				drawItem(canvas, mapview, item, count);
			
			}
			if(this.getFocus()!=null){
				drawItem(canvas, mapview, this.getFocus(), this.getLastFocusedIndex()+1);
			}
		}
		
		}
		catch(Exception e){
			
			e.printStackTrace();
		}
		
	}
	
	

	/* (non-Javadoc)
	 * @see com.mapabc.mapapi.ItemizedOverlay#onTap(int)
	 */
	@Override
	protected boolean onTap(int index) {
		// TODO Auto-generated method stub
		
		setFocus(listItems.get(index));
		
		/*
		Toast.makeText(context, (index+1) 
								+ "\n" + getItemName(listItems.get(index)) 
								+ "\n" + getItemPhone(listItems.get(index))
								+ "\n" + getItemAddress(listItems.get(index)), 
								Toast.LENGTH_LONG).show();
		*/
		
		GeoPoint point = this.getItemPoint(getItem(index));
			
		Message msg = new Message();
		
		msg.what = ActivityMain.HANDLER_MSG_WHAT_TAP_PASSENGER;
			
		Bundle bundle = new Bundle();
			
		bundle.putInt(ActivityMain.BUNDLE_KEY_LATE6, point.getLatitudeE6());
			
		bundle.putInt(ActivityMain.BUNDLE_KEY_LONGE6, point.getLongitudeE6());
		
		bundle.putString(ActivityMain.BUNDLE_KEY_PASSENGER_NAME, getItemName(listItems.get(index)));
		bundle.putString(ActivityMain.BUNDLE_KEY_PASSENGER_NUMBER, getItemPhone(listItems.get(index)));
			
		msg.setData(bundle);
			
		ActivityMain.handlerRef.sendMessage(msg);
		
		
		//return true;
		return super.onTap(index);
	}
	
	public OverlayItem getStopItem(){
	
		return listItems.get(0);
	
	}
	
	public OverlayItem getStartItem(){
	
		return listItems.get(size()-1);
	
	}
	
	
	public int getScheduleMeters(){
		
		return scheduleMeters;
		
	}
	
	public int getScheduleMinutes(){
		
		return scheduleMinutes;
		
	}
	
	public void backFocus(){
	
		int index = this.getLastFocusedIndex();
		
		if(index==size()-1){
		
			index = 0;
		
		}
		else{
		
			index++;
		
		}
		setFocus(this.getItem(index));
		
	}
	
	public void forwardFocus(){
		int index = this.getLastFocusedIndex();
		if(index==0){
			index = size()-1;
		}
		else{
			index--;
		}
		
		this.setFocus(this.getItem(index));
	}
	
	
	
	/*
	 * 
	 */
	
	private GeoPoint getItemPoint(OverlayItem item){
		
		GeoPoint point = null;
		
		try {
			JSONObject obj = new JSONObject(item.getSnippet());
			
			int late6 = obj.getInt(DatabaseHelper.DB_COLUMN_TICKET_PASSENGER_LATE6);
			
			int longe6 = obj.getInt(DatabaseHelper.DB_COLUMN_TICKET_PASSENGER_LONGE6);
			
			point = new GeoPoint(late6, longe6);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return point;
		
	}
	
	public int getPointCount(){
		return this.listItems.size();
	}
	
	
	public GeoPoint getPointByIndex(int index){
		
		OverlayItem item = this.listItems.get(index);
		
		GeoPoint point = null;
		
		try {
			JSONObject obj = new JSONObject(item.getSnippet());
			
			int late6 = obj.getInt(DatabaseHelper.DB_COLUMN_TICKET_PASSENGER_LATE6);
			
			int longe6 = obj.getInt(DatabaseHelper.DB_COLUMN_TICKET_PASSENGER_LONGE6);
			
			point = new GeoPoint(late6, longe6);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return point;
		
	}
	
	
	public GeoPoint getFrom(){
		return this.startPoint;
	}
	
	public GeoPoint getTo(){
		return this.terminalPoint;
	}
	
	public List<GeoPoint> getPassby(){
		if(this.listPassby.size()>0){
			return this.listPassby;
		}
		else{
			return null;
		}
	}
	
	public GeoPoint getRouteEnd(){
		if(DatabaseHelper.DB_VALUE_LINE_OPERATION_TYPE_FROM.equals(line_type)){
			if(this.listPassby.size()>0){
				return this.listPassby.get(this.listPassby.size()-1);
			}
			else{
				return this.startPoint;
			}
		}
		else{
			return this.terminalPoint;
		}
	}
	
	/*
	 * 
	 */
	private String getItemAddress(OverlayItem item){
		
		String result = "";
		
		try {
			JSONObject obj = new JSONObject(item.getSnippet());
			
			result = obj.getString(DatabaseHelper.DB_COLUMN_TICKET_PASSENGER_ADDRESS);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
		
	}
	

	/*
	 * 
	 */
	private String getItemPhone(OverlayItem item){
		
		String result = "";
		
		try {
			JSONObject obj = new JSONObject(item.getSnippet());
			
			result = obj.getString(DatabaseHelper.DB_COLUMN_TICKET_PASSENGER_PHONE);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
		
	}
	
	/*
	 * 
	 */
	private String getItemName(OverlayItem item){
		
		String result = "";
		
		try {
			JSONObject obj = new JSONObject(item.getSnippet());
			
			result = obj.getString(DatabaseHelper.DB_COLUMN_TICKET_PASSENGER_NAME);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
		
	}
	
	
	/*
	 * 
	 */
	private boolean isItemPassengerWaiting(OverlayItem item){
		
		boolean result = true;
		
		try {
			JSONObject obj = new JSONObject(item.getSnippet());
			
			String pickup = obj.getString(DatabaseHelper.DB_COLUMN_TICKET_SERVE_CONFIRM);
			
			if(DatabaseHelper.DB_VALUE_TICKET_SERVE_YES.equals(pickup)){
				
				result = false;
			}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return result;
		
	}
	
	private int checkItemPassengerStatus(OverlayItem item){
		
		int result = 0;
		
		try {
			JSONObject obj = new JSONObject(item.getSnippet());
			
			String pickup = obj.getString(DatabaseHelper.DB_COLUMN_TICKET_SERVE_CONFIRM);
			
			if(DatabaseHelper.DB_VALUE_TICKET_SERVE_YES.equals(pickup)){
				
				result = 2;
			}
			else if(DatabaseHelper.DB_VALUE_TICKET_SERVE_ONBOARD.equals(pickup)){
				
				result = 1;
			}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return result;
		
	}
	/*
	 * 鐢籭tem
	 */
	
	private void drawItem(Canvas canvas, MapView mapview, OverlayItem item, int count){
		
		Point out = new Point();
		
		Bitmap bitmap = null;

		Paint paint = new Paint();

		Matrix matrix; 
		
		GeoPoint itemPoint = getItemPoint(item);

		float scale;

		mapview.getProjection().toPixels(itemPoint, out);
		
		Drawable marker = context.getResources().getDrawable(R.drawable.marker_passenger_waiting);
		
		switch(checkItemPassengerStatus(item)){
		
		case 0://waiting
			
			break;
			
		case 1://onboard
			marker = context.getResources().getDrawable(R.drawable.marker_passenger_onboard);
			break;
			
		case 2://arrived
			marker = context.getResources().getDrawable(R.drawable.marker_passenger_arrived);
			break;
		
		}
		bitmap = ((BitmapDrawable)marker).getBitmap();
		
		/*
		if(isItemPassengerWaiting(item)){
			
			Drawable marker_wainting = context.getResources().getDrawable(R.drawable.marker_passenger_waiting);
			
			bitmap = ((BitmapDrawable)marker_wainting).getBitmap() ;
				
		}
		else{

			Drawable marker_onboard = context.getResources().getDrawable(R.drawable.marker_passenger_onboard);
			
			bitmap = ((BitmapDrawable)marker_onboard).getBitmap() ;
			
		}
		*/
		
		/*
		if(count-1 == this.getLastFocusedIndex()){
			
			Drawable marker_onboard = context.getResources().getDrawable(R.drawable.btn_passenger_waiting);
			
			bitmap = ((BitmapDrawable)marker_onboard).getBitmap() ;
			
		}
		*/
		matrix = new Matrix();

		scale = (float)1;

		matrix.postTranslate(-bitmap.getWidth()*(scale/2), -bitmap.getHeight()*scale); //boundCenterBottom(marker)

		matrix.postScale(scale, scale);

		float degrees = 0;
	
		matrix.postRotate(degrees);

		matrix.postTranslate(out.x, out.y);

		canvas.drawBitmap(bitmap, matrix, paint);
		
		
		
		Drawable marker_count = context.getResources().getDrawable(R.drawable.marker_count_n);
		
		switch(count){
		
		case 1:
			
			marker_count = context.getResources().getDrawable(R.drawable.marker_count_1);
								
			break;
			
		case 2:
		
			marker_count = context.getResources().getDrawable(R.drawable.marker_count_2);
			
			break;
		
		case 3:
			
			marker_count = context.getResources().getDrawable(R.drawable.marker_count_3);
			
			break;
			
		case 4:
			
			marker_count = context.getResources().getDrawable(R.drawable.marker_count_4);
			
			break;
			
		case 5:
			
			marker_count = context.getResources().getDrawable(R.drawable.marker_count_5);
			
			break;
			
		case 6:
			
			marker_count = context.getResources().getDrawable(R.drawable.marker_count_6);
			
			break;
			
		case 7:
			
			marker_count = context.getResources().getDrawable(R.drawable.marker_count_7);
			
			break;
			
		case 8:
			
			marker_count = context.getResources().getDrawable(R.drawable.marker_count_8);
			
			break;
			
		case 9:
			
			marker_count = context.getResources().getDrawable(R.drawable.marker_count_9);
			
			break;
			
		}
		
		bitmap = ((BitmapDrawable)marker_count).getBitmap() ;

		canvas.drawBitmap(bitmap, matrix, paint);
		
		
		
	}
	
	/*
	 * 
	 */
	
	private ArrayList<JSONObject> schedulePassengerItems(GeoPoint target, GeoPoint start){
		
		ArrayList<JSONObject>  listResult = null;
		
		try{
			GeoPoint point = null;
			if(DatabaseHelper.DB_VALUE_LINE_OPERATION_TYPE_FROM.equals(line_type)){
				point = new GeoPoint(start.getLatitudeE6(), start.getLongitudeE6());
			}
			else{
				point = new GeoPoint(target.getLatitudeE6(), target.getLongitudeE6());
			}

			listResult = new ArrayList<JSONObject>();
			
			ArrayList<JSONObject> listSchedule = new ArrayList<JSONObject>();
			ArrayList<JSONObject> listToBeSchedule = new ArrayList<JSONObject>();
			ArrayList<JSONObject> listWaiting = new ArrayList<JSONObject>();
			ArrayList<JSONObject> listOnboard = new ArrayList<JSONObject>();
			for(int i = 0; i<this.listTickets.size();i++){
				JSONObject ticket = this.listTickets.get(i);
				listToBeSchedule.add(ticket);
				if (DatabaseHelper.DB_VALUE_TICKET_SERVE_ONBOARD.equals(ticket.getString(DatabaseHelper.DB_COLUMN_TICKET_SERVE_CONFIRM))){
					listOnboard.add(ticket);
					Log.i(TAG, "onboard:"+listOnboard.size());
					}
				else if(DatabaseHelper.DB_VALUE_TICKET_SERVE_WAITING.equals(ticket.getString(DatabaseHelper.DB_COLUMN_TICKET_SERVE_CONFIRM))
						|| DatabaseHelper.DB_VALUE_TICKET_SERVE_NO.equals(ticket.getString(DatabaseHelper.DB_COLUMN_TICKET_SERVE_CONFIRM))){
					listWaiting.add(ticket);
					Log.i(TAG, "waiting:"+listWaiting.size());
				}
			}

			Iterator<JSONObject> iterator = listToBeSchedule.iterator();
			scheduleMinutes = 0;
			scheduleMeters = 0;
			
			/* 
			while(iterator.hasNext()){
				JSONObject obj = null;
				//obj = scheduleShortestItem(listTickets, point);
				if(DatabaseHelper.DB_VALUE_LINE_OPERATION_TYPE_FROM.equals(line_type)){
					obj = scheduleFirstItem(listToBeSchedule, point);
				}
				else{
					obj = scheduleLatestItem(listToBeSchedule, point);
				}
				if(obj != null){
					point = getTicketPoint(obj);
					Log.i(TAG, "point:"+point.getLatitudeE6()+","+point.getLongitudeE6());
					listSchedule.add(obj);
					Log.i(TAG, "size:"+listSchedule.size());
					
					listToBeSchedule.remove(obj);
					iterator  = listToBeSchedule.iterator();

					scheduleMinutes += minutes;
					scheduleMeters += meters;
					
				}
			}
			
			
			if(DatabaseHelper.DB_VALUE_LINE_OPERATION_TYPE_TO.equals(line_type)){
				for( int i = 0; i < listSchedule.size(); i++ ){
					listResult.add(listSchedule.get(listSchedule.size()-1-i));
				}
			}
			else{
				for( int i = 0; i < listSchedule.size(); i++ ){
					listResult.add(listSchedule.get(i));
				}
			}
			
			*/
			
			
			this.listPassby = new ArrayList<GeoPoint>();
			if(DatabaseHelper.DB_VALUE_LINE_OPERATION_TYPE_FROM.equals(line_type)){
				listToBeSchedule = listOnboard;
				point = new GeoPoint(start.getLatitudeE6(), start.getLongitudeE6());
			}
			else{
				listToBeSchedule = listWaiting;
				point = new GeoPoint(target.getLatitudeE6(), target.getLongitudeE6());
			}
			iterator = listToBeSchedule.iterator();
			scheduleMinutes = 0;
			scheduleMeters = 0;
			Log.i(TAG, "to be pass by:"+listToBeSchedule.size());
			while(iterator.hasNext()){
				JSONObject obj = null;
				//obj = scheduleShortestItem(listTickets, point);
				if(DatabaseHelper.DB_VALUE_LINE_OPERATION_TYPE_FROM.equals(line_type)){
					obj = scheduleFirstItem(listToBeSchedule, point);
				}
				else{
					obj = scheduleLatestItem(listToBeSchedule, point);
				}
				if(obj != null){
					point = getTicketPoint(obj);
					this.listPassby.add(point);
					
					listToBeSchedule.remove(obj);
					iterator  = listToBeSchedule.iterator();

					scheduleMinutes += minutes;
					scheduleMeters += meters;
					
				}
			}
			Log.i(TAG,"passby:"+listPassby.size());
			
		}
		catch(Exception e){
			
			e.printStackTrace();
			
		}
		
		return listResult;
	}
	
	/*
	 *
	 */
	
	private JSONObject scheduleShortestItem(ArrayList<JSONObject> listTickets, GeoPoint targetPoint){
		
		
		JSONObject result = null;
		
		result = listTickets.get(0);
		
		GeoPoint point = this.getTicketPoint(result);
		
		int distance = getDistance(point, targetPoint);
		
		int meters = distance;
		
		int minutes = this.minutes;
		
		for(int i = 1; i < listTickets.size(); i++){
			
			point = getTicketPoint(listTickets.get(i));
			
			int length = getDistance(point, targetPoint);
			
			if(length < distance){
				
				result = listTickets.get(i);
				
				distance = length;
				
				meters = distance;
				
				minutes = this.minutes;
			}
		}
		
		this.minutes = minutes;
		this.meters = meters;
		
		return result;
		
	}
	

	/*
	 * 
	 */
	
	private JSONObject scheduleFirstItem(ArrayList<JSONObject> listTickets, GeoPoint start){
		
		JSONObject result = null;
		
		result = listTickets.get(0);
		
		GeoPoint point = this.getTicketPoint(result);
		
		int distance = getDistance(start, point);

		int meters = distance;
		
		int minutes = this.minutes;
		
		for(int i = 1; i < listTickets.size(); i++){
			
			point = getTicketPoint(listTickets.get(i));
			
			int length = getDistance(start, point);
			
			if(length < distance){
				
				result = listTickets.get(i);
				
				distance = length;

				meters = distance;
				
				minutes = this.minutes;
			}
		}
		
		this.minutes = minutes;
		this.meters = meters;
		
		Log.i(TAG, "meters="+meters+"minutes="+minutes);
		
		return result;
		
	}
	
	
	/*
	 * 
	 */
	
	private JSONObject scheduleLatestItem(ArrayList<JSONObject> listTickets, GeoPoint targetPoint){
		
		
		JSONObject result = null;
		
		result = listTickets.get(0);
		
		GeoPoint point = this.getTicketPoint(result);
		
		int distance = getDistance(point, targetPoint);
		
		int meters = distance;
		
		int minutes = this.minutes;
		
		for(int i = 1; i < listTickets.size(); i++){
			
			point = getTicketPoint(listTickets.get(i));
			
			int length = getDistance(point, targetPoint);
			
			if(length < distance){
				
				result = listTickets.get(i);
				
				distance = length;
				
				meters = distance;
				
				minutes = this.minutes;
			}
		}
		
		this.minutes = minutes;
		
		this.meters = meters;
		
		Log.i(TAG, "meters="+meters+"minutes="+minutes);
		
		return result;
		
	}
	
	/*
	 * 
	 */
	
	private GeoPoint getTicketPoint(JSONObject ticket){
		
		GeoPoint point = null;
		
		try {
			
			int late6 = ticket.getInt(DatabaseHelper.DB_COLUMN_TICKET_PASSENGER_LATE6);
			
			int longe6 = ticket.getInt(DatabaseHelper.DB_COLUMN_TICKET_PASSENGER_LONGE6);
			
			point = new GeoPoint(late6, longe6);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return point;
		
	}
	
	private String getTicketStatus(JSONObject ticket){
		
		String status = null;
		
		try {
			status = ticket.getString(DatabaseHelper.DB_COLUMN_TICKET_SERVE_CONFIRM);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return status;
		
	}

	/*
	 * 
	 */
	
	private int getDistance(GeoPoint start, GeoPoint target){
		
		int result = 0;
		
		this.meters = 0;
		
		this.minutes = 0;
		
		if(Math.abs((start.getLatitudeE6() - target.getLatitudeE6())) < 10 
				&& Math.abs((start.getLongitudeE6() - target.getLongitudeE6())) < 10 ){
			return 1;
		}
		
		FromAndTo fromAndTo = new FromAndTo(start, target,Route.DrivingDefault);
		
		List<Route> listRoute = null;

		try {
			
			listRoute = Route.calculateRoute((MapActivity) context, fromAndTo, Route.DrivingDefault);
		
			if(listRoute.size()>0){
				
				Route route = listRoute.get(0);
				
				result = route.getLength();
				
				meters = result;
				
				minutes = routeMinutes(route);
				
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    		
		return result;
	
	}
	
	
	
	private int routeMinutes(Route route){
		
		int i = 0;
		
		int time = 0;
		
		int steps = route.getStepCount();
				
		String HOUR = context.getResources().getString(R.string.HOUR);
		
		String MINUTE = context.getResources().getString(R.string.MINUTE);
		
		do{
		
			Segment segment = route.getStep(i);
			
			String strTime = segment.getConsumeTime();
			
			String strHour;
			
			if(strTime.contains(HOUR)){
			
				strHour = strTime.substring(0, strTime.indexOf(HOUR));
				
				time += Integer.parseInt(strHour.replace(HOUR, "").trim())*60;
				
				strTime = strTime.substring(strTime.indexOf(HOUR)+2);
				
			}
			
			if(strTime.contains(MINUTE)){
				
				time += Integer.parseInt(strTime.replace(MINUTE, "").trim());
			
			}
		
			i++;
		
		}while(i<steps);
		
		return time;
	}
}
