/**
 * 
 */
package com.qshuttle.car;



//import com.qk_systems.jiesongche.libs.Constants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public class DatabaseHelper extends SQLiteOpenHelper {
	
	/*
	 * public 常量
	 */

	//database
	public static final String DB_DATABASE = "com.qshuttle.car";
	public static final String DB_AUTHORITY ="com.qshuttle.car";
	public static final int DB_VERSION = 1;
	public static final String DB_COLUMN_ROWID = "ROWID";	
	
	//database data type
	public static final String DB_DATA_TYPE_TEXT = "TEXT";
	public static final String DB_DATA_TYPE_INT = "INTEGER";
	public static final String DB_DATA_TEXT_VALUE_NULL = "NULL";
	public static final String DB_DATA_INT_VALUE_ZERO = "0";
	// use TEXT for all data to simplify database and http operation
	
	//database tables
	public static final String DB_TABLE_CAR_TRACKER = "car_tracker";
	public static final String DB_TABLE_DEVICE_REGISTER = "device_register";
	public static final String DB_TABLE_SHUTTLE_INSTANCE = "shuttle_instance";
	
	//uri for DataProvider interface
	public static final String URI_HEADER = "content://"+DB_AUTHORITY;
	public static final String URI_LIMIT_1 = "?"	+ "1";
	public static final String URI_CAR_TRACKER = 			"/"	+	DB_TABLE_CAR_TRACKER;
	

	//table constructor

	//car_tracker
	//table columns
	public static final String DB_COLUMN_CAR_NUMBER = "car_number";
	public static final String DB_COLUMN_CAR_POS_LATE6 = "car_pos_late6";
	public static final String DB_COLUMN_CAR_POS_LONGE6 = "car_pos_longe6";
	public static final String DB_COLUMN_CAR_POS_STAMP = "car_pos_stamp";
	public static final String DB_COLUMN_CAR_POS_PROVIDER = "car_pos_provider";
	public static final String DB_COLUMN_CAR_POS_SPEEDE6 = "car_pos_speede6";
	public static final String DB_COLUMN_CAR_POS_BEARINGE6 = "car_pos_bearinge6";
	public static final String DB_COLUMN_CAR_POS_ACCURACY = "car_pos_accuracy";

	//columns value
	public static final String DB_VALUE_CAR_POS_TYPE_MAPABC = "MAPABC";
	public static final String DB_VALUE_CAR_POS_TYPE_RAWGPS = "RAWGPS";
	public static final String DB_VALUE_CAR_POS_TYPE_GOOGLE = "GOOGLE";
	public static final String DB_VALUE_CAR_POS_TYPE_INERTIAL = "INERTIAL";

	
	//table structure
	public static final String[] Db_Table_Car_Tracker = {
		DB_COLUMN_CAR_NUMBER
		,DB_COLUMN_CAR_POS_LATE6
		,DB_COLUMN_CAR_POS_LONGE6
		,DB_COLUMN_CAR_POS_STAMP
		,DB_COLUMN_CAR_POS_PROVIDER
		,DB_COLUMN_CAR_POS_SPEEDE6
		,DB_COLUMN_CAR_POS_BEARINGE6
		,DB_COLUMN_CAR_POS_ACCURACY
	};
	

	//device_register
	//table columns
	public static final String DB_COLUMN_DEVICE_SERIAL = "device_serial";
	public static final String DB_COLUMN_DEVICE_IMEI = "device_imei";
	public static final String DB_COLUMN_DEVICE_WIFI_MAC = "device_wifi_mac";
	public static final String DB_COLUMN_DEVICE_BRAND = "device_brand";
	public static final String DB_COLUMN_DEVICE_MODEL = "device_model";
	public static final String DB_COLUMN_DEVICE_OS = "device_os";
	public static final String DB_COLUMN_DEVICE_SIM_IMSI = "device_sim_imsi";
	public static final String DB_COLUMN_DEVICE_SIM_NUMBER = "device_sim_number";
	public static final String DB_COLUMN_DEVICE_SIM_CARRIER = "device_sim_carrier";
	public static final String DB_COLUMN_DEVICE_STATE = "device_state";
	public static final String DB_COLUMN_DEVICE_UPDATE_STAMP = "device_update_stamp";
	//table structure
	public static final String[] Db_Table_Device_Register = {
		DB_COLUMN_DEVICE_SERIAL
		,DB_COLUMN_DEVICE_IMEI
		,DB_COLUMN_DEVICE_WIFI_MAC
		,DB_COLUMN_DEVICE_BRAND
		,DB_COLUMN_DEVICE_MODEL
		,DB_COLUMN_DEVICE_OS
		,DB_COLUMN_DEVICE_SIM_IMSI
		,DB_COLUMN_DEVICE_SIM_NUMBER
		,DB_COLUMN_DEVICE_SIM_CARRIER
		,DB_COLUMN_DEVICE_STATE
		,DB_COLUMN_DEVICE_UPDATE_STAMP
	};
	
	//shuttle_instance
	//table columns
	public static final String DB_COLUMN_INSTANCE_SERIAL = "instance_serial";
	//public static final String DB_COLUMN_CAR_NUMBER = "car_number";
	public static final String DB_COLUMN_DRIVER_NAME = "driver_name";
	public static final String DB_COLUMN_INSTANCE_STATE = "instance_state";
	public static final String DB_COLUMN_LINE_DESCRIPTION = "line_description";
	public static final String DB_COLUMN_LINE_OPERATION_TYPE = "line_operation_type";
	public static final String DB_COLUMN_LINE_START_ADDRESS = "line_start_address";
	public static final String DB_COLUMN_LINE_START_LATE6 = "line_start_late6";
	public static final String DB_COLUMN_LINE_START_LONGE6 = "line_start_longe6";
	public static final String DB_COLUMN_LINE_TERMINAL_ADDRESS = "line_terminal_address";
	public static final String DB_COLUMN_LINE_TERMINAL_LATE6 = "line_terminal_late6";
	public static final String DB_COLUMN_LINE_TERMINAL_LONGE6 = "line_terminal_longe6";
	public static final String DB_COLUMN_SCHEDULE_DEPARTURE_TIME = "schedule_departure_time";
	public static final String DB_COLUMN_SCHEDULE_ARRIVE_TIME = "schedule_arrive_time";
	public static final String DB_COLUMN_INSTANCE_UPDATE_STAMP = "instance_update_stamp";
	
	//value
	public static final String DB_VALUE_LINE_OPERATION_TYPE_TO = "送站";
	
	public static final String DB_VALUE_LINE_OPERATION_TYPE_FROM = "接站";
	
	//table structure
	public static final String[] Db_Table_Shuttle_Instance = {
		DB_COLUMN_INSTANCE_SERIAL
		,DB_COLUMN_CAR_NUMBER
		,DB_COLUMN_DRIVER_NAME
		,DB_COLUMN_LINE_DESCRIPTION
		,DB_COLUMN_SCHEDULE_DEPARTURE_TIME
		,DB_COLUMN_SCHEDULE_ARRIVE_TIME
		,DB_COLUMN_INSTANCE_STATE
		,DB_COLUMN_INSTANCE_UPDATE_STAMP
	};
	

	//shuttle_instance
	//table columns
	public static final String DB_COLUMN_TICKET_PASSENGER_NAME = "ticket_passenger_name";
	public static final String DB_COLUMN_TICKET_PASSENGER_PHONE = "ticket_passenger_phone";
	public static final String DB_COLUMN_TICKET_PASSENGER_ADDRESS = "ticket_passenger_address";
	public static final String DB_COLUMN_TICKET_PASSENGER_LATE6 = "ticket_passenger_late6";
	public static final String DB_COLUMN_TICKET_PASSENGER_LONGE6 = "ticket_passenger_longe6";
	public static final String DB_COLUMN_TICKET_SERVE_CONFIRM = "ticket_serve_confirm";
	public static final String DB_COLUMN_TICKET_SERIAL = "ticket_serial";
	
	public static final String DB_VALUE_TICKET_SERVE_YES = "yes";
	public static final String DB_VALUE_TICKET_SERVE_NO = "no";
	public static final String DB_VALUE_TICKET_SERVE_ONBOARD = "onboard";
	public static final String DB_VALUE_TICKET_SERVE_WAITING = "waiting";
	
	//table structure
	public static final String[] Db_Table_Ticket_List = {
		DB_COLUMN_TICKET_PASSENGER_NAME
		,DB_COLUMN_TICKET_PASSENGER_PHONE
		,DB_COLUMN_TICKET_PASSENGER_ADDRESS
		,DB_COLUMN_TICKET_PASSENGER_LATE6
		,DB_COLUMN_TICKET_PASSENGER_LONGE6
	};
		
	
	//table index via tables name string
	public static final String[] Db_Table_Name = {
		DB_TABLE_CAR_TRACKER
		,DB_TABLE_DEVICE_REGISTER
		,DB_TABLE_SHUTTLE_INSTANCE
	};
	
	//table object list
	public static final Object[] Db_Table_Object = {
		Db_Table_Car_Tracker
		,Db_Table_Device_Register
		,Db_Table_Shuttle_Instance
		
	};
	

	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
		CreateTables(db);
		
		InitDeviceRegister(db);
		
		InitDummyShuttleDispatch(db);
		
	}
	
	private void CreateTables(SQLiteDatabase db){
		
		String sql = null ;
		String tablecolumns = null;
		String[] table = null;
	
		int length = Db_Table_Object.length;
		int i = 0, j = 0, l =0 ;
		while(i<length){
			
			table = (String[])Db_Table_Object[i];
			
			tablecolumns = "(";
			
			j = 0;
			l = table.length;
			while (j<l){
				tablecolumns += table[j];
				tablecolumns += " TEXT";
				j++;
				if(j<l)
					tablecolumns += ",";
			}

			tablecolumns += ");";
		
			sql = "CREATE TABLE " + Db_Table_Name[i] + tablecolumns;
			
			try{
				db.execSQL(sql);
			}
			catch(SQLiteException e){
				Log.e("DB_ERROR", e.toString());
			}
			i++;
		
		}

		
	}

	private void InitDeviceRegister(SQLiteDatabase db){
		
	}
	
	private void InitDummyShuttleDispatch(SQLiteDatabase db){
		
	}
	
	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	
}
