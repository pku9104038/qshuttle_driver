/**
 * 涉及UI主线程的公共应用模块
 */
package com.qshuttle.car;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.widget.Toast;



/**
 * @author wangpeifeng
 *
 */
public class Utilities {
	
 
	/*
	 * public static 方法
	 */
	
	
	public static String MetersToString(int meters,Context context){
		
		String result = "";
		
		int km = (int)Math.floor(meters/1000);
		
		int m = meters%1000;
		
		int km_left = (int)Math.floor(m/100);
		
		
		if(km > 0){
			
			result += km + "." + km_left + context.getResources().getString(R.string.kilometers);
			
		
		}
		else{
			
			result += km + "." + km_left + context.getResources().getString(R.string.kilometers);
			
				
		}
		return result;
		
	}
	
	
	public static String MinutesToString(int time, Context context){
		
		String result = "";

		int hours = (int)Math.floor(time/60); 
		
		int minutes = time%60;
		
		if(hours > 0){
			
			result  += hours + context.getResources().getString(R.string.hours)
					+ minutes + context.getResources().getString(R.string.minutes);
			
			
		}
		else{
		
			 result += minutes + context.getResources().getString(R.string.minutes);
		
		}

		
		
		return result;
	
	}
	
	public static String StampToString(long stamp){
		
		Date date = null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(stamp);
		date =  calendar.getTime();
		
//    	String format = "yyyy-MM-dd HH:mm:ss.SSSZ"; 
    	String format = "yyyy-MM-dd HH:mm:ss.SSSZ"; 
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		//sdf.setTimeZone(TimeZone.getTimeZone("UTC"));	
		sdf.setTimeZone(TimeZone.getTimeZone("PRC"));	
		
		return sdf.format(date);
		
    }

	
	public static String StampToHHMM12(long stamp){
		
		String strStamp = StampToString(stamp);
		
		String strHHMM = strStamp.substring("yyyy-mm-dd ".length(),"yyyy-mm-dd hh:mm".length());
		
		String strHH = strHHMM.substring(0,"HH".length());
		
		String strMM = strHHMM.substring("HH:".length());
		
		int hh = Integer.parseInt(strHH);
		
		if(hh > 12){
			hh -= 12;
		}
		
		strHHMM = hh + ":" + strMM;
		
		return strHHMM;
		
    }

	
	public static String StampToDate(long stamp){
		
		return StampToString(stamp).substring(0, "yyyy-mm-dd".length());
		
    }
	

	
	
	public static String getDeviceInfo(Context context){
		
		String result = "";
		
	    String TOP = "\n";
	    
	    String BOTTOM = "\n";
	    
	    String LEFT = "    ";
	    
	    String RIGHT = "    \n";
	    
	    Resources resources = context.getResources();

	    result += TOP;

	    try {

	    	String pkName = context.getPackageName();
			
	    	String versionName = context.getPackageManager().getPackageInfo(pkName, 0).versionName;
	    	
	    	result += LEFT + resources.getString(R.string.app_name) + " Ver. " + versionName + RIGHT;
		    
	    } catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
	    }

	    int device_serial = PrefProxy.getDeviceSerial(context);
		
		String car_number = PrefProxy.getCarNumber(context);
		
		String driver_name = PrefProxy.getDriverName(context);
		
		String imei, imsi, brand, model, os, number;
		
		TelephonyManager mgrTelephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
	        
	    imei = mgrTelephony.getDeviceId();
	    imsi = mgrTelephony.getSubscriberId();
	    number = mgrTelephony.getLine1Number();
	    
	    brand = Build.BRAND;
	    model = Build.MODEL;
	    os = "Android "+Build.VERSION.RELEASE;
	    
	    
	    
	    
	    
	    
	    result += LEFT + resources.getString(R.string.device_serial) + device_serial + RIGHT;
	    result += LEFT + resources.getString(R.string.car_number) + car_number + RIGHT;
	    result += LEFT + resources.getString(R.string.driver) + driver_name + RIGHT;
	    result += LEFT + resources.getString(R.string.device_number) + number + RIGHT;
	    
	    result += LEFT + resources.getString(R.string.device_imei) + imei + RIGHT;
	    result += LEFT + resources.getString(R.string.device_imsi) + imsi + RIGHT;

	    
	    
	    //result += BOTTOM;
	    
		return result;
		
	}

}
