/**
 * 
 */
package com.qshuttle.car;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;


/**
 * @author wangpeifeng
 *
 */
public class WebApi {
	
	
	/*
	 * public 常量
	 */

	//api url
	public static final String HTTP_PROTOCOL = "http://";
	public static final String API_ROOT = "/qshuttle/webapi/";
	
   
	
	
	//api method
	public static final String API_CAR_POS_REPORTER = "api/car_tracker_reporter.php";
	public static final String API_CAR_ROUTE_QUERY = "api/car_tracker_query.php";
	public static final String API_CAR_NUMBER_QUERY = "api/car_number_query.php";
	public static final String API_DEVICE_REGISTER = "api/device_register.php";
	//public static final String API_SHUTTLE_TICKET_QUERY = "api/shuttle_ticket_query.php";
	//public static final String API_SHUTTLE_TICKET_QUERY = "api/shuttle_ticket_monkey.php";
	public static final String API_INSTANCE_TICKET_QUERY = "api/instance_tickets_query.php";
	public static final String API_TICKET_SERVE_CONFIRM = "api/ticket_serve_confirm.php";

    
	//api params
	public static final String API_PARAM_CAR_ROUTE_START_TIME = "car_route_start_stamp";
	public static final String API_PARAM_CAR_ROUTE_END_TIME = "car_route_end_stamp";
	
	//api response 
	public static final String API_RESP_JSON_CAR_POS_ARRAY = "car_pos_array";
	public static final String API_JSON_TICKET_ARRAY = "ticket_array";
	public static final String API_JSON_SHUTTLE_ARRAY = "shuttle_array";
	public static final String API_RESP_SUCCESS = "Success";


	/*
	 * private 常量
	 */
	private static final String TAG = "WebApi";
	
	private static final int CALLER_ID_LOGIN_DEVICE = 1;
	
	private static final int CALLER_ID_REPORT_POSITION = 1 + CALLER_ID_LOGIN_DEVICE;
	
	private static final int CALLER_ID_REGISTER_DEVICE = 1 + CALLER_ID_REPORT_POSITION;
	
	private static final int CALLER_ID_QUERY_SERVICE_INFO = 1 + CALLER_ID_REGISTER_DEVICE;
	
	private static final int CALLER_ID_REPORT_PICKUP = 1 + CALLER_ID_QUERY_SERVICE_INFO;
	
	/*
	 * private 变量
	 */

	
	private Context context;
	
	private DataOperator operator;
	
	private Uri uri;
	
	private int rowid;
	
	
	

	
	
	/**
	 * @param context
	 */
	public WebApi(Context context) {
		super();
		this.context = context;
	}


	/************************************************************************
	 * public API
	 */
	
	/*
	 * WebApi响应结果处理
	 */
	
	public void checkResponse(String response, int caller_id){
		
		switch(caller_id){
		
		case CALLER_ID_LOGIN_DEVICE:
			
			if(checkLoginDevice(response)){

				ActivityMain.handlerRef.sendEmptyMessage(ActivityMain.HANDLER_MSG_WHAT_DEVICE_LOGINED);

			}
			
			break;
			
		case CALLER_ID_REPORT_POSITION:
			
			if(checkReportPosition(response)){

				ActivityMain.handlerRef.sendEmptyMessage(ActivityMain.HANDLER_MSG_WHAT_POSITION_REPORTED);
				
			}
			
			break;
			
		case CALLER_ID_REGISTER_DEVICE:
			
			if(isRespSuccess(response)){

				ActivityMain.handlerRef.sendEmptyMessage(ActivityMain.HANDLER_MSG_WHAT_DEVICE_REGISTER);
				
			}
			else{

				ActivityMain.handlerRef.sendEmptyMessage(ActivityMain.HANDLER_MSG_WHAT_DEVICE_REGISTER_FAILED);
				
			}
			
			break;
			
		case CALLER_ID_QUERY_SERVICE_INFO:

//			if(isServiceInfo(response)){
			if(isRespSuccess(response)){
				
				PrefProxy.setServiceInfo(context, response);
				
				PrefProxy.setServiceInfoDate(context, Utilities.StampToDate(System.currentTimeMillis()));
				
				ActivityMain.handlerRef.sendEmptyMessage(ActivityMain.HANDLER_MSG_WHAT_SERVICE_INFO_READY);
				
			}
			else {
				
				if(!isServiceInfoReady(context)){
					
					ActivityMain.handlerRef.sendEmptyMessage(ActivityMain.HANDLER_MSG_WHAT_SERVICE_INFO_NULL);

				}
				else{
					ActivityMain.handlerRef.sendEmptyMessage(ActivityMain.HANDLER_MSG_WHAT_SERVICE_INFO_READY);
					
				}
				
				
			}
			
			break;
			
			
		case CALLER_ID_REPORT_PICKUP:

			if(isRespSuccess(response)){

//				ActivityMain.handlerRef.sendEmptyMessage(ActivityMain.HANDLER_MSG_WHAT_SERVICE_INFO_UPDATE);
				
			}
			
			break;
			
		}
		
	}
	
	
	/*
	 * 是否当日 车次/乘客信息乘客信息
	 */
	
	public static boolean isServiceInfoReady(Context context){
		
		boolean result = false;
		
		try{
		
//		if(PrefProxy.getServiceInfoDate(context).equals(Convertor.StampToDate(System.currentTimeMillis()))){
		
			if(isServiceInfo(PrefProxy.getServiceInfo(context))){
				
				result = true;
			
			}
//		}
		
		}
		catch(Exception e){
			
			e.printStackTrace();
			
		}
		return result;
	}
	
	/*
	 * 查询 车次/乘客信息
	 */
	
	public void queryServiceInfo(){

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

		String strUrl = HTTP_PROTOCOL + PrefProxy.getHost(context) + ":" + PrefProxy.getPort(context) + API_ROOT + API_INSTANCE_TICKET_QUERY;
		
		postApi(strUrl,params, CALLER_ID_QUERY_SERVICE_INFO);
		
	}
		
	/*
	 * 
	 */
	
	public void reportPickup(int ticket_serial, String pickup){

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair(DatabaseHelper.DB_COLUMN_TICKET_SERIAL, ""+ticket_serial));
		params.add(new BasicNameValuePair(DatabaseHelper.DB_COLUMN_TICKET_SERVE_CONFIRM, pickup));

		String strUrl = HTTP_PROTOCOL + PrefProxy.getHost(context) + ":" + PrefProxy.getPort(context) + API_ROOT + API_TICKET_SERVE_CONFIRM;
		
		postApi(strUrl, params, CALLER_ID_REPORT_PICKUP);

	}

	/*
	 * 上报位置信息
	 */
	
	public void reportPosition(String late6, String longe6, String number, String type, String stamp,
				String speed, String bearing, String accuracy, Uri uri, int rowid, DataOperator operator){

		this.uri = uri;
		this.rowid = rowid;
		this.operator = operator;
		
		
		String strUrl = HTTP_PROTOCOL + PrefProxy.getHost(context) + ":" + PrefProxy.getPort(context) + API_ROOT+API_CAR_POS_REPORTER;
		
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair(DatabaseHelper.DB_COLUMN_CAR_NUMBER, number));
		params.add(new BasicNameValuePair(DatabaseHelper.DB_COLUMN_CAR_POS_LATE6, ""+late6));
		params.add(new BasicNameValuePair(DatabaseHelper.DB_COLUMN_CAR_POS_LONGE6, ""+longe6));
		params.add(new BasicNameValuePair(DatabaseHelper.DB_COLUMN_CAR_POS_PROVIDER, type));
		params.add(new BasicNameValuePair(DatabaseHelper.DB_COLUMN_CAR_POS_STAMP, stamp.substring(0, "0000-00-00 00:00:00".length())));
		params.add(new BasicNameValuePair(DatabaseHelper.DB_COLUMN_CAR_POS_SPEEDE6, speed));
		params.add(new BasicNameValuePair(DatabaseHelper.DB_COLUMN_CAR_POS_BEARINGE6, bearing));
		params.add(new BasicNameValuePair(DatabaseHelper.DB_COLUMN_CAR_POS_ACCURACY, accuracy));
		
		postApi(strUrl,params, CALLER_ID_REPORT_POSITION);
		
	}
	


	
	/*
	 * 设备登录
	 */
	
	public void loginDevice(){
		
		
		String strUrl = HTTP_PROTOCOL + PrefProxy.getHost(context) + ":" + PrefProxy.getPort(context) + API_ROOT + API_CAR_NUMBER_QUERY;
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		
		String sim_imsi,brand,model,os,sim_number,sim_carrier;
		
		TelephonyManager mgrTelephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
	        
	    sim_imsi = mgrTelephony.getSubscriberId();
	    sim_number = mgrTelephony.getLine1Number();
	    sim_carrier = mgrTelephony.getSimOperator();  
	    
	    brand = Build.BRAND;
	    model = Build.MODEL;
	    os = "Android "+Build.VERSION.RELEASE;
	    
	    WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		
		params.add(new BasicNameValuePair(DatabaseHelper.DB_COLUMN_DEVICE_SIM_IMSI, sim_imsi));
		params.add(new BasicNameValuePair(DatabaseHelper.DB_COLUMN_DEVICE_BRAND, brand));
		params.add(new BasicNameValuePair(DatabaseHelper.DB_COLUMN_DEVICE_MODEL, model));
		params.add(new BasicNameValuePair(DatabaseHelper.DB_COLUMN_DEVICE_OS, os));
//		params.add(new BasicNameValuePair(DatabaseHelper.DB_COLUMN_DEVICE_SIM_NUMBER, sim_number));
//		params.add(new BasicNameValuePair(DatabaseHelper.DB_COLUMN_DEVICE_SIM_CARRIER, sim_carrier));
		
		postApi(strUrl,params, CALLER_ID_LOGIN_DEVICE );

	}
	

	public void registerDevice(){
		
		String strUrl = HTTP_PROTOCOL + PrefProxy.getHost(context) + ":" + PrefProxy.getPort(context) + API_ROOT+API_DEVICE_REGISTER;
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		
		String sim_imsi,brand,model,os,sim_number,sim_carrier;
		
		TelephonyManager mgrTelephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
	        
	    sim_imsi = mgrTelephony.getSubscriberId();
	    sim_number = mgrTelephony.getLine1Number();
	    sim_carrier = mgrTelephony.getSimOperator();  
	    
	    brand = Build.BRAND;
	    model = Build.MODEL;
	    os = "Android "+Build.VERSION.RELEASE;
	    
		params.add(new BasicNameValuePair(DatabaseHelper.DB_COLUMN_DEVICE_SIM_IMSI, sim_imsi));
		params.add(new BasicNameValuePair(DatabaseHelper.DB_COLUMN_DEVICE_BRAND, brand));
		params.add(new BasicNameValuePair(DatabaseHelper.DB_COLUMN_DEVICE_MODEL, model));
		params.add(new BasicNameValuePair(DatabaseHelper.DB_COLUMN_DEVICE_OS, os));
		params.add(new BasicNameValuePair(DatabaseHelper.DB_COLUMN_DEVICE_SIM_NUMBER, sim_number));
		params.add(new BasicNameValuePair(DatabaseHelper.DB_COLUMN_DEVICE_SIM_CARRIER, sim_carrier));
		
		postApi(strUrl, params, CALLER_ID_REGISTER_DEVICE);

	}

	/*
	 * 提取api返回信息中的车次/乘客JSONArray
	 */

	public static JSONArray infoToJsonArray(String info) {
		
		JSONArray array = null;
		
		try {
			
			JSONObject obj = new JSONObject(info);
		
			if(obj.getBoolean(WebApi.API_RESP_SUCCESS)){
			
				obj = obj.getJSONObject(WebApi.API_JSON_SHUTTLE_ARRAY);
				
				array = obj.getJSONArray(WebApi.API_JSON_SHUTTLE_ARRAY);
			
			}
				
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

				
		return array;

	}
	
	
	/*
	 * 提取车次/乘客 JSONArray中制定instacnce信息
	 */

	public static JSONObject getInstance(int instance_serial, JSONArray arrayServiceInfo){

		JSONObject result = null;
		
		int length = arrayServiceInfo.length();
		
		for (int i = 0; i < length; i++){
			
			try {
				JSONObject obj = arrayServiceInfo.getJSONObject(i);
				
				int serial = obj.getInt(DatabaseHelper.DB_COLUMN_INSTANCE_SERIAL);
				
				if (serial == instance_serial){
					return obj;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
		return result;
		
	}
	
	/****************************************************************************
	 * private 方法
	 */
	
	
	/*
	 * 检查位置报告响应
	 */
	
	private boolean checkReportPosition(String resp){
		
		boolean result = false;
		
		if(isRespSuccess(resp)){
			
			operator.deletePosRecord(uri, rowid);

			result = true;
						
		}
		
		return result;
		
	}
	
	/*
	 * 检查设备登陆响应
	 */
	
	private boolean checkLoginDevice(String resp){
		
		boolean result = false;

		if(resp!=null){
			JSONObject jsonResp;
			try {
				
				jsonResp = new JSONObject(resp);
				
				if(jsonResp.getBoolean(API_RESP_SUCCESS)){
					
					
					String car_number = jsonResp.getString(DatabaseHelper.DB_COLUMN_CAR_NUMBER);
					
					if(car_number != null){

						PrefProxy.setCarNumber(context, car_number);
					
						result = true;
						
					}
					
					
					String driver_name = jsonResp.getString(DatabaseHelper.DB_COLUMN_DRIVER_NAME);
					
					if(driver_name != null){
						PrefProxy.setDriverName(context, driver_name);
					}
					
					else{
						PrefProxy.setDriverName(context, context.getResources().getString(R.string.driver_null));
					}
					
					
					int device_serial = jsonResp.getInt(DatabaseHelper.DB_COLUMN_DEVICE_SERIAL);
					
					if(device_serial !=0 ){
						
						PrefProxy.setDeviceSerial(context, device_serial);
					
					}
					
					
				
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		return result;
	
	}
	
	
	/*
	 * 检查是否返回有效 车次/乘客信息
	 */
	private static boolean isServiceInfo(String resp) {
		
		boolean result = false;
		
		JSONObject obj = null;
		
		JSONArray array = null;
		
		try {
			
			obj = new JSONObject(resp);
		
			if(obj.getBoolean(WebApi.API_RESP_SUCCESS)){
			
				obj = obj.getJSONObject(WebApi.API_JSON_SHUTTLE_ARRAY);
				
				array = obj.getJSONArray(WebApi.API_JSON_SHUTTLE_ARRAY);
				
				if(array!=null){
				
					Log.i("array",array.toString());
					result = true;
				}
			
			}
				
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			Log.i("array",array.toString());
			
		}

		return result;

	}

	
	/*
	 * 检查WebApi响应是否成功 
	 */
	private boolean isRespSuccess(String response){
		
		boolean flag = false;
		
		try {
			JSONObject obj = new JSONObject(response);
			if(obj.getBoolean(API_RESP_SUCCESS)){
				flag = true;
			}
			
		}
		catch (Exception e){
			e.printStackTrace();
		}
		finally{
			return flag;
		}
	}

	/*
	 * 创建http线程，post API
	 */
	
	private void postApi(String strUrl, ArrayList<NameValuePair> params, int caller_id){
		
		TelephonyManager mgrTelephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

		String imei = mgrTelephony.getDeviceId();
		
		params.add(new BasicNameValuePair(DatabaseHelper.DB_COLUMN_DEVICE_IMEI, imei));
		
		ThreadHttpPost thread = new ThreadHttpPost(strUrl, params, this, caller_id);
		
		thread.start();
	
	}
	

}
