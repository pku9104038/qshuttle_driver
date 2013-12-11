/**
 * 
 */
package com.qshuttle.car;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author wangpeifeng
 *
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private static final String TAG = "ExpandableListAdapter";
	
	private int action;
	
	private Context context;
	
	private JSONArray arrayList;
	
	private LayoutInflater inflater;
	
	private Resources resources;

	
	
	public ExpandableListAdapter(Context context, JSONArray arrayList, int action) {
		super();
		
		this.context = context;
		
		this.arrayList = arrayList;
		
		this.action = action;
		
		inflater = LayoutInflater.from(context);
		
		resources = context.getResources();
		
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getChild(int, int)
	 */
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub

		try {
			
			JSONObject groupObj = (JSONObject) arrayList.get(groupPosition);
			
			JSONArray childArray = groupObj.getJSONArray(WebApi.API_JSON_TICKET_ARRAY);
			
			JSONObject childObj = (JSONObject) childArray.get(childPosition);
			
			return childObj;
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getChildId(int, int)
	 */
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		
		return childPosition;
	}

	public String getChildNumber(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		String number = null;

		try {
			
			JSONObject groupObj = (JSONObject) arrayList.get(groupPosition);
			
			JSONArray childArray = groupObj.getJSONArray(WebApi.API_JSON_TICKET_ARRAY);
			
			JSONObject childObj = (JSONObject) childArray.get(childPosition);
			
			number = childObj.getString(DatabaseHelper.DB_COLUMN_TICKET_PASSENGER_PHONE);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//return null;
		}

		return number;
	}

	public int getChildLate6(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		
		int late6 = PrefProxy.getMyLate6(context);

		try {
			
			JSONObject groupObj = (JSONObject) arrayList.get(groupPosition);
			
			JSONArray childArray = groupObj.getJSONArray(WebApi.API_JSON_TICKET_ARRAY);
			
			JSONObject childObj = (JSONObject) childArray.get(childPosition);
			
			late6 = childObj.getInt(DatabaseHelper.DB_COLUMN_TICKET_PASSENGER_LATE6);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return late6;
	}
	public int getChildLonge6(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		
		int longe6 = PrefProxy.getMyLonge6(context);

		try {
			
			JSONObject groupObj = (JSONObject) arrayList.get(groupPosition);
			
			JSONArray childArray = groupObj.getJSONArray(WebApi.API_JSON_TICKET_ARRAY);
			
			JSONObject childObj = (JSONObject) childArray.get(childPosition);
			
			longe6 = childObj.getInt(DatabaseHelper.DB_COLUMN_TICKET_PASSENGER_LONGE6);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return longe6;
	}
	
	/*
	 * 读取 乘客上车状态
	 */
	
	public String getChildPickup(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		
		String pickup = DatabaseHelper.DB_VALUE_TICKET_SERVE_NO;
		
		try {
			
			JSONObject groupObj = (JSONObject) arrayList.get(groupPosition);
			
			JSONArray childArray = groupObj.getJSONArray(WebApi.API_JSON_TICKET_ARRAY);
			
			JSONObject childObj = (JSONObject) childArray.get(childPosition);
			
			pickup = childObj.getString(DatabaseHelper.DB_COLUMN_TICKET_SERVE_CONFIRM);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return pickup;
	}
	
	/*
	 * 
	 */
	public int getChildTicketSerial(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		int serial = 0;
		
		try {
			
			JSONObject groupObj = (JSONObject) arrayList.get(groupPosition);
			
			JSONArray childArray = groupObj.getJSONArray(WebApi.API_JSON_TICKET_ARRAY);
			
			JSONObject childObj = (JSONObject) childArray.get(childPosition);
			
			serial = childObj.getInt(DatabaseHelper.DB_COLUMN_TICKET_SERIAL);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return serial;
	}

	public String toggleChildPickup(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		
		String pickup = DatabaseHelper.DB_VALUE_TICKET_SERVE_NO;
		
		try {
			
			JSONObject groupObj = (JSONObject) arrayList.get(groupPosition);
			
			JSONArray childArray = groupObj.getJSONArray(WebApi.API_JSON_TICKET_ARRAY);
			
			JSONObject childObj = (JSONObject) childArray.get(childPosition);
			
			pickup = childObj.getString(DatabaseHelper.DB_COLUMN_TICKET_SERVE_CONFIRM);
			
			//pickup = (DatabaseHelper.DB_VALUE_TICKET_SERVE_YES.equals(pickup) ? DatabaseHelper.DB_VALUE_TICKET_SERVE_NO : DatabaseHelper.DB_VALUE_TICKET_SERVE_YES);
			if (DatabaseHelper.DB_VALUE_TICKET_SERVE_YES.equals(pickup)){
				pickup = DatabaseHelper.DB_VALUE_TICKET_SERVE_WAITING;
			}
			else if (DatabaseHelper.DB_VALUE_TICKET_SERVE_ONBOARD.equals(pickup)){
				pickup = DatabaseHelper.DB_VALUE_TICKET_SERVE_YES;
			}
			else{
				pickup = DatabaseHelper.DB_VALUE_TICKET_SERVE_ONBOARD;
			}
			
			childObj.put(DatabaseHelper.DB_COLUMN_TICKET_SERVE_CONFIRM, pickup);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return pickup;
	}
	
	
	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean, android.view.View, android.view.ViewGroup)
	 */
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
		// TODO Auto-generated method stub
		
		if(convertView == null){
			try{
			
				convertView = inflater.inflate(R.layout.listitem_passenger, null);
		
			}
			catch(InflateException e){
				e.printStackTrace();
			}
		}
		 
		ImageView ivAction = (ImageView)convertView.findViewById(R.id.imageViewAction);
		
		switch(action){
		
		case ActivityMain.ACTIVITY_REQUEST_CALL:
			
			ivAction.setImageResource(R.drawable.btn_passenger_call);
			
			break;
		
		case ActivityMain.ACTIVITY_REQUEST_PICKUP:
		
		case ActivityMain.ACTIVITY_REQUEST_SCHEDULE:
				
			if(DatabaseHelper.DB_VALUE_TICKET_SERVE_YES.equals(getChildPickup(groupPosition, childPosition))){
			
				ivAction.setImageResource(R.drawable.btn_passenger_arrived);
				
			}
			else if(DatabaseHelper.DB_VALUE_TICKET_SERVE_ONBOARD.equals(getChildPickup(groupPosition, childPosition))){
				
				ivAction.setImageResource(R.drawable.btn_passenger_onboard);
			}
			else{
				ivAction.setImageResource(R.drawable.btn_passenger_waiting);
					
			}
			
			break;
			
		case ActivityMain.ACTIVITY_REQUEST_ROUTE:
			
			ivAction.setImageResource(R.drawable.btn_passenger_route);
			
			break;
		}
		
		try {
			
			JSONObject groupObj = (JSONObject) arrayList.get(groupPosition);
			
			JSONArray childArray = groupObj.getJSONArray(WebApi.API_JSON_TICKET_ARRAY);
			
			JSONObject childObj = (JSONObject) childArray.get(childPosition);
			
			
			String name = childObj.getString(DatabaseHelper.DB_COLUMN_TICKET_PASSENGER_NAME);
			String number = childObj.getString(DatabaseHelper.DB_COLUMN_TICKET_PASSENGER_PHONE);
			String address = childObj.getString(DatabaseHelper.DB_COLUMN_TICKET_PASSENGER_ADDRESS);
			String late6 = childObj.getString(DatabaseHelper.DB_COLUMN_TICKET_PASSENGER_LATE6);
			String longe6 = childObj.getString(DatabaseHelper.DB_COLUMN_TICKET_PASSENGER_LONGE6);

			TextView tvName = (TextView)convertView.findViewById(R.id.textViewName);
			TextView tvNumber = (TextView)convertView.findViewById(R.id.textViewNumber);
			TextView tvAddress = (TextView)convertView.findViewById(R.id.textViewAddress);
			
			tvName.setText(name);
			//tvName.setTextColor(Color.WHITE);
			
			tvNumber.setText(number);
			//tvNumber.setTextColor(Color.RED);
			
			tvAddress.setText(address);
			//tvAddress.setTextColor(Color.YELLOW);
			
			return convertView;
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getChildrenCount(int)
	 */
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		
		int count = 0;
		
		try {
			
			JSONObject groupObj = (JSONObject) arrayList.get(groupPosition);
			
			JSONArray childArray = groupObj.getJSONArray(WebApi.API_JSON_TICKET_ARRAY);
			
			count =  childArray.length();
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return count;
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getGroup(int)
	 */
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		
		JSONObject groupObj = null;
		
		try {
			
			groupObj = (JSONObject) arrayList.get(groupPosition);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return groupObj;
		
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getGroupCount()
	 */
	public int getGroupCount() {
		// TODO Auto-generated method stub
		
		return arrayList.length();
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getGroupId(int)
	 */
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		
		return groupPosition;
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#getGroupView(int, boolean, android.view.View, android.view.ViewGroup)
	 */
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		if(convertView == null){
			try{
			
				convertView = inflater.inflate(R.layout.explistitem_instance, null);
		
			}
			catch(InflateException e){
				e.printStackTrace();
			}
		}
		 
		
		try {
			
			JSONObject groupObj = (JSONObject) arrayList.get(groupPosition);
			
			String linedes = groupObj.getString(DatabaseHelper.DB_COLUMN_LINE_DESCRIPTION);
			
			String departureadd = groupObj.getString(DatabaseHelper.DB_COLUMN_LINE_START_ADDRESS);
			
			String arriveadd = groupObj.getString(DatabaseHelper.DB_COLUMN_LINE_TERMINAL_ADDRESS);
			
			String number = groupObj.getString(DatabaseHelper.DB_COLUMN_CAR_NUMBER);
			
			String driver = groupObj.getString(DatabaseHelper.DB_COLUMN_DRIVER_NAME);
			
			String departuretime = groupObj.getString(DatabaseHelper.DB_COLUMN_SCHEDULE_DEPARTURE_TIME);
			
			String arrivetime = groupObj.getString(DatabaseHelper.DB_COLUMN_SCHEDULE_ARRIVE_TIME);

			
			String strLine = departureadd + resources.getString(R.string.from_to) + arriveadd;
			
			String strTime = departuretime + resources.getString(R.string.from_to) + arrivetime;
			
			((TextView)(convertView.findViewById(R.id.textViewLine))).setText(strLine);
			
			((TextView)(convertView.findViewById(R.id.textViewTime))).setText(strTime);
			
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return convertView;
		
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#hasStableIds()
	 */
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see android.widget.ExpandableListAdapter#isChildSelectable(int, int)
	 */
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}
	
	
}
