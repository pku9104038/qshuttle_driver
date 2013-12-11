/**
 * 
 */
package com.qshuttle.car;

import java.util.ArrayList;
import java.util.Iterator;

import com.qshuttle.car.PrefProxy.Address;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;


/**
 * @author wangpeifeng
 *
 */
public class PrefProxy {
	
	/*
	 * public static final
	 */
	public static final String FILE_SHAREDPREFERENCES = "myPreferences";
	
	//--------------------------------------------------------------------
	//key
	public static final String KEY_MY_LATE6 = "my_late6";
	
	public static final String KEY_MY_LONGE6 = "my_longe6";
	
	public static final String KEY_CAR_NUMBER = "car_number";
	
	public static final String KEY_DRIVER_NAME = "driver_name";
	
	public static final String KEY_SERVICE_INFO_DATE = "service_info_date";
	
	public static final String KEY_MAP_TRAFFIC = "map_traffic";
	
	public static final String KEY_MAP_VECTOR = "map_vector";
	
	public static final String KEY_RECENT_ADDRESS_ = "my_address_";
	
	public static final String KEY_RECENT_LATE6_ = "recent_late6";
	
	public static final String KEY_RECENT_LONGE6_ = "recent_longe6";
	
	public static final String KEY_RECENT_CUSOUR = "recent_cusour";
	
	public static final String KEY_RECENT_COUNT = "recent_count";
	
	public static final String KEY_SCREEN_FILTER = "screen_filter";
	
	public static final String KEY_DEVICE_SERIAL = "device_serial";
	
	public static final String KEY_ZOOM_LEVEL = "zoom_level";
	
	//value
	public static final int VALUE_DEFAULT_MY_LATE6 = 31754648;//QUNKAI_LATE6;
	
	public static final int VALUE_DEFAULT_MY_LONGE6 = 120936981;//QUNKAI_LONGE6;
	
	public static final String VALUE_DEFAULT_DATE_NULL = "0000-00-00";
	
	public static final String VALUE_DEFAULT_INFO_NULL = "NULL";
	
	public static final int VALUE_RECENT_MIN = 1;
	
	public static final int VALUE_RECENT_MAX = 30;
	
	public static final int VALUE_RECENT_DEFAULT = 0;
	
	public static final int VALUE_DEVICE_SERIAL_DEFAULT = 0;
	
	public static final int VALUE_ZOOM_LEVEL_DEFAULT = ActivityMain.ZOOM_DEFAULT;
	
	public static final String KEY_HOST = "host";
	public static final String KEY_PORT = "port";
	public static final String VALUE_DEFAULT_HOST = "www.q-shuttle.com";
	//public static final String VALUE_DEFAULT_HOST = "www.jiesongche.com";
	public static final String VALUE_DEFAULT_PORT = "8088";
	
	
	
	public class Address{
		
		public String address;
		
		public int late6;
		
		public int longe6;
	
	}
	
	
	/*
	 * public static method
	 * 
	 * 
	 */
	
	public static void setHost(Context context, String host){
		
		SharedPreferences myPreferences = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);
		
		Editor editor = myPreferences.edit();
		
		editor.putString(KEY_HOST, host);
		
		editor.commit();
		
	}

	public static String getHost(Context context){
		
		SharedPreferences myPreferences = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);

		return myPreferences.getString(KEY_HOST, VALUE_DEFAULT_HOST);
		
	}
	/*
	 * public static method
	 * 
	 * 
	 */
	
	public static void setPort(Context context, String port){
		
		SharedPreferences myPreferences = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);
		
		Editor editor = myPreferences.edit();
		
		editor.putString(KEY_PORT, port);
		
		editor.commit();
		
	}

	public static String getPort(Context context){
		
		SharedPreferences myPreferences = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);

		return myPreferences.getString(KEY_PORT, VALUE_DEFAULT_PORT);
		
	}
	
	
	
	/*
	 * public static method
	 * 
	 * 
	 */
	
	public static void setZoomLevel(Context context, int zoom){
		
		SharedPreferences myPreferences = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);
		
		Editor editor = myPreferences.edit();
		
		editor.putInt(KEY_ZOOM_LEVEL, zoom);
		
		editor.commit();
		
	}

	public static int getZoomLevel(Context context){
		
		SharedPreferences myPreferences = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);

		return myPreferences.getInt(KEY_ZOOM_LEVEL, VALUE_ZOOM_LEVEL_DEFAULT);
		
	}
	
	public static void setDeviceSerial(Context context, int device_serial){

		SharedPreferences myPreferences = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);
		
		Editor editor = myPreferences.edit();
		
		editor.putInt(KEY_DEVICE_SERIAL, device_serial);
		
		editor.commit();
		
	}
	
	public static int getDeviceSerial(Context context){
	
		SharedPreferences myPreferences = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);

		return myPreferences.getInt(KEY_DEVICE_SERIAL, VALUE_DEVICE_SERIAL_DEFAULT);
	
	}
	
	
	
	public static void updateRecentAddress(Context context, Address address){
		
		
		int index = findAddress(context, address.address);
		
		if(index > 0){

			setRecentAddress(context, address, index);
			
		}
		else{
			
			addRecentAddress(context,address);
			
		}
		
		
	}
	
	/*
	 * write recent count
	 */
	
	public static void addRecentAddress(Context context, Address address){
		
		int cusour = getRecentCusour(context);
		
		if(cusour==VALUE_RECENT_MAX){
			
			cusour = VALUE_RECENT_MIN;
		
		}
		else{
			
			cusour++;
			
		}
		
		setRecentAddress(context, address, cusour);
		
		setRecentCusour(context, cusour);
		
		int count = getRecentCount(context);
		
		if(count < VALUE_RECENT_MAX){
			
			count++;
			
			setRecentCount(context, count);
		
		}
		
	}
	
	private static int findAddress(Context context, String address){
		
		int result = 0;
		
		ArrayList<Address> listAddress = getRecentAddressList(context);
		
		if(listAddress != null && listAddress.size() > 0){
		
			for (int i = 0; i < listAddress.size(); i++){
			
				if(listAddress.get(i).address.equals(address)){
				 
					result = i+1;
				 
					break;
			
				}
			
			}
		
		}
		return result;
		
	}
	
	public static ArrayList<Address> getRecentAddressList(Context context){
		
		ArrayList<Address> result = null;
		
		int count = getRecentCount(context);
		
		if(count > 0){
			
			result = new ArrayList<Address>();
			
			Address address;
			
			for(int i = 1; i <= count; i++){
				
				address = getRecentAddress(context, i);
				
				result.add(address);
				
			}
			
			
		}
		
		return result;
	}


	/*
	 * write recent count
	 */
	
	public static void setRecentAddress(Context context, Address address, int index){
		
		SharedPreferences myPreferences = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);
		
		Editor editor = myPreferences.edit();
		
		editor.putString(KEY_RECENT_ADDRESS_+index, address.address);
		
		editor.commit();

		editor.putInt(KEY_RECENT_LATE6_+index, address.late6);
		
		editor.commit();

		editor.putInt(KEY_RECENT_LONGE6_+index, address.longe6);
		
		editor.commit();
		
	}

	/*
	 * read recent count
	 */
	public static Address  getRecentAddress(Context context, int index){
		
		SharedPreferences myPreferences = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);

		Address address = (new PrefProxy()).new Address();
		
		address.address = myPreferences.getString(KEY_RECENT_ADDRESS_+index, VALUE_DEFAULT_INFO_NULL);
	
		address.late6 = myPreferences.getInt(KEY_RECENT_LATE6_+index, VALUE_DEFAULT_MY_LATE6);
		
		address.longe6 = myPreferences.getInt(KEY_RECENT_LONGE6_+index, VALUE_DEFAULT_MY_LONGE6);
		
		return address;
		
	}
	
	
	/*
	 * write recent count
	 */
	
	public static void setRecentCusour(Context context, int cusour){
		
		SharedPreferences myPreferences = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);
		
		Editor editor = myPreferences.edit();
		
		editor.putInt(KEY_RECENT_CUSOUR, cusour);
		
		editor.commit();
	}

	/*
	 * read recent count
	 */
	public static int  getRecentCusour(Context context){
		
		SharedPreferences myPreferences = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);

		return myPreferences.getInt(KEY_RECENT_CUSOUR, VALUE_RECENT_DEFAULT);
	
	}
	

	/*
	 * write recent count
	 */
	
	public static void setRecentCount(Context context, int count){
		
		SharedPreferences myPreferences = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);
		
		Editor editor = myPreferences.edit();
		
		editor.putInt(KEY_RECENT_COUNT, count);
		
		editor.commit();
	}

	/*
	 * read recent count
	 */
	public static int  getRecentCount(Context context){
		
		SharedPreferences myPreferences = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);

		return myPreferences.getInt(KEY_RECENT_COUNT, VALUE_RECENT_DEFAULT);
	
	}
	/*
	 * write map_traffic
	 */
	
	public static void setScreenFilter(Context context, boolean filter){
		
		SharedPreferences myPreferences = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);
		
		Editor editor = myPreferences.edit();
		
		editor.putBoolean(KEY_SCREEN_FILTER, filter);
		
		editor.commit();
	}

	/*
	 * read map_traffic
	 */
	public static boolean getScreenFilter(Context context){
		
		SharedPreferences myPreferences = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);

		return myPreferences.getBoolean(KEY_SCREEN_FILTER, false);
	
	}
	
	
	
	/*
	 * write map_traffic
	 */
	
	public static void setMapVector(Context context, boolean vector){
		
		SharedPreferences myPreferences = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);
		
		Editor editor = myPreferences.edit();
		
		editor.putBoolean(KEY_MAP_VECTOR, vector);
		
		editor.commit();
	}

	/*
	 * read map_traffic
	 */
	public static boolean getMapVector(Context context){
		
		SharedPreferences myPreferences = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);

		return myPreferences.getBoolean(KEY_MAP_VECTOR, true);
	
	}
	

	/*
	 * write map_traffic
	 */
	
	public static void setMapTraffic(Context context, boolean traffic){
		
		SharedPreferences myPreferences = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);
		
		Editor editor = myPreferences.edit();
		
		editor.putBoolean(KEY_MAP_TRAFFIC, traffic);
		
		editor.commit();
	}

	/*
	 * read map_traffic
	 */
	public static boolean getMapTraffic(Context context){
		
		SharedPreferences myPreferences = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);

		return myPreferences.getBoolean(KEY_MAP_TRAFFIC, false);
	
	}
	
	/*
	 * write ServiceInfo
	 */
	
	public static void setServiceInfo(Context context, String info){
		
		SharedPreferences myPreferences = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);
		
		Editor editor = myPreferences.edit();
		
		editor.putString(WebApi.API_JSON_SHUTTLE_ARRAY, info);
		
		editor.commit();
	}

	/*
	 * read ServiceInfo
	 */
	public static String getServiceInfo(Context context){
		
		if(getServiceInfoDate(context).equals(Utilities.StampToDate(System.currentTimeMillis()))){
			
			SharedPreferences myPreferences = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);

			return myPreferences.getString(WebApi.API_JSON_SHUTTLE_ARRAY, VALUE_DEFAULT_INFO_NULL);
		
		}
		else{
			
			return VALUE_DEFAULT_INFO_NULL;
		
		}
	
	}
	

	/*
	 * write ServiceInfo Date
	 */
	
	public static void setServiceInfoDate(Context context, String date){
		
		SharedPreferences myPreferences = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);
		
		Editor editor = myPreferences.edit();
		
		editor.putString(KEY_SERVICE_INFO_DATE, date);
		
		editor.commit();
	}

	/*
	 * read ServiceInfo Date
	 */
	public static String getServiceInfoDate(Context context){
		
		SharedPreferences myPreferences = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);

		return myPreferences.getString(KEY_SERVICE_INFO_DATE, VALUE_DEFAULT_DATE_NULL);
	
	}
	
	// write car_number
	public static void setCarNumber(Context context, String carnumber){
		
		SharedPreferences myPreferences = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);
		
		Editor editor = myPreferences.edit();
		
		editor.putString(KEY_CAR_NUMBER, carnumber);
		
		editor.commit();
		
	}
	
	// read car_number
	public static String getCarNumber(Context context){
		
		SharedPreferences myPreferences = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);

		return myPreferences.getString(KEY_CAR_NUMBER, context.getResources().getString(R.string.car_number_null));
	
	}
	
	// write driver_name
	public static void setDriverName(Context context, String drivername){
		
		SharedPreferences myPreferences = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);
		
		Editor editor = myPreferences.edit();
		
		editor.putString(KEY_DRIVER_NAME, drivername);
		
		editor.commit();
		
	}
	
	// read driver_name
	public static String getDriverName(Context context){
		
		SharedPreferences myPreferences = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);

		return myPreferences.getString(KEY_DRIVER_NAME, context.getResources().getString(R.string.driver_null));
	
	}
	
	
	// write myLate6
	public static void setMyLate6(Context context,int late6){
		
		SharedPreferences myPreferences = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);
		
		Editor editor = myPreferences.edit();
		
		editor.putInt(KEY_MY_LATE6, late6);
		
		editor.commit();
		
	}
	
	// read myLate6
	public static int getMyLate6(Context context){
		
		SharedPreferences myPreferences = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);

		return myPreferences.getInt(KEY_MY_LATE6, VALUE_DEFAULT_MY_LATE6);
	
	}
	

	// write myLonge6
	public static void setMyLonge6(Context context,int longe6){
		
		SharedPreferences myPreferences = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);
		
		Editor editor = myPreferences.edit();
		
		editor.putInt(KEY_MY_LONGE6, longe6);
		
		editor.commit();
		
	}
	
	// read myLonge6
	public static int getMyLonge6(Context context){
		
		SharedPreferences myPreferences = context.getSharedPreferences(FILE_SHAREDPREFERENCES, Context.MODE_PRIVATE);

		return myPreferences.getInt(KEY_MY_LONGE6, VALUE_DEFAULT_MY_LONGE6);
	
	}
	

}
