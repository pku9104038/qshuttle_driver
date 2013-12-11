/**
 * 
 */
package com.qshuttle.car;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;

/**
 * @author wangpeifeng
 *
 */
public class DataOperator {
	
	private static final String TAG = "DataOperator";
	
	private Context context;
	private ContentResolver resolver;

	public DataOperator(Context context){
		this.context = context;
		resolver = context.getContentResolver();
		
	}

	/********************************************************************************
	 * public 方法
	 */
	
	/*
	 * 关闭数据库
	 */
	
	public void close(){
		
		
	}
	
	/*
	 * 记录位置信息
	 */
	public Uri recordPosition(String number, Location location){

		//Uri.Builder builder = new Uri.Builder();
		Uri uri = new Uri.Builder().build().parse(DatabaseHelper.URI_HEADER
			+DatabaseHelper.URI_CAR_TRACKER);

		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.DB_COLUMN_CAR_NUMBER, number);
		values.put(DatabaseHelper.DB_COLUMN_CAR_POS_LATE6,""+(int)(location.getLatitude()*1E6));
		values.put(DatabaseHelper.DB_COLUMN_CAR_POS_LONGE6, ""+(int)(location.getLongitude()*1E6));
		values.put(DatabaseHelper.DB_COLUMN_CAR_POS_STAMP, Utilities.StampToString(location.getTime()));
		values.put(DatabaseHelper.DB_COLUMN_CAR_POS_PROVIDER, location.getProvider());
		values.put(DatabaseHelper.DB_COLUMN_CAR_POS_SPEEDE6, ""+(int)(location.getSpeed()*1E6));
		values.put(DatabaseHelper.DB_COLUMN_CAR_POS_BEARINGE6, ""+(int)(location.getBearing()*1E6));
		values.put(DatabaseHelper.DB_COLUMN_CAR_POS_ACCURACY, ""+(int)(location.getAccuracy()));
		
		return resolver.insert(uri, values);

	}

	/*
	 * 报告位置信息
	 */
	
	public void reportPosition(){

		
		Uri uri = new Uri.Builder().build().parse(DatabaseHelper.URI_HEADER
				+DatabaseHelper.URI_CAR_TRACKER
				+DatabaseHelper.URI_LIMIT_1);
		
		
		String[] table = DatabaseHelper.Db_Table_Car_Tracker;
		int l = table.length;
		
		String[] projection = new String[l+1];
		int i =0;
		while (i<l){
			projection[i] = table[i];
			i++;
		}
		projection[i] = DatabaseHelper.DB_COLUMN_ROWID;
		
		String selection = null;
		String sortOrder = null;
		String[] selectionArgs = null;
		
		Cursor cursor = null;
		
		cursor = resolver.query(uri,projection, selection, selectionArgs, sortOrder);
	
		int rows = 0, columns = 0;
		if(cursor!=null){
			rows = cursor.getCount();
			columns = cursor.getColumnCount();
		}
		if(rows>0 && columns>0){
		
			cursor.moveToFirst();
		
			String number = cursor.getString(getProjectionIndex(projection,DatabaseHelper.DB_COLUMN_CAR_NUMBER));
			String late6 = cursor.getString(getProjectionIndex(projection,DatabaseHelper.DB_COLUMN_CAR_POS_LATE6));
			String longe6 = cursor.getString(getProjectionIndex(projection,DatabaseHelper.DB_COLUMN_CAR_POS_LONGE6));
			String stamp = cursor.getString(getProjectionIndex(projection,DatabaseHelper.DB_COLUMN_CAR_POS_STAMP));
			String type = cursor.getString(getProjectionIndex(projection,DatabaseHelper.DB_COLUMN_CAR_POS_PROVIDER));
			String speed = cursor.getString(getProjectionIndex(projection,DatabaseHelper.DB_COLUMN_CAR_POS_SPEEDE6));
			String bearing = cursor.getString(getProjectionIndex(projection,DatabaseHelper.DB_COLUMN_CAR_POS_BEARINGE6));
			String accuracy = cursor.getString(getProjectionIndex(projection,DatabaseHelper.DB_COLUMN_CAR_POS_ACCURACY));
			int rowid = cursor.getInt(getProjectionIndex(projection,DatabaseHelper.DB_COLUMN_ROWID));
			
			WebApi webApi = new WebApi(context);
			webApi.reportPosition(late6, longe6, number, type, stamp,speed,bearing,accuracy, uri, rowid, this);
		}	
		if(cursor!=null)
			cursor.close();

	}
	
	/*
	 * 删除已经报告的位置记录
	 */
	
	public int deletePosRecord(Uri uri, int rowid){
		
		int result = 0;

		String where = DatabaseHelper.DB_COLUMN_ROWID +"=?";
		String[] selectionArgs = new String[1];
		selectionArgs[0] =""+rowid;
		result = resolver.delete(uri,where, selectionArgs);
		
		return result;
		
	}
	
	/*
	 * 检查WebApi响应是否成功
	 */
	
	public boolean checkResponse(String response){
		boolean flag = false;
		
		try {
			JSONObject obj = new JSONObject(response);
			if(obj.getBoolean(WebApi.API_RESP_SUCCESS)){
				flag = true;
			}
			
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
		return flag;
	}
	

	/*
	 * private 方法
	 */
	
	/*
	 * 取得数据表字段位置影射关系
	 */
	private int getProjectionIndex(String[] projection,String column){
		int index = 0;

		int i = 0;
		int l = projection.length;
		while(i<l){
			if(projection[i].equals(column)){
				index = i;
				return index;
			}
			i++;
		}

		
		return index;
	}
	
	
}
