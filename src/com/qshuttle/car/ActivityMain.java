/* ********************************************************************************
 *  Qshuttle Main Activity
 * 
 * 
 ***********************************************************************************/


package com.qshuttle.car;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.amap.mapapi.core.AMapException;
import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.core.OverlayItem;
import com.amap.mapapi.geocoder.Geocoder;
import com.amap.mapapi.location.LocationManagerProxy;
import com.amap.mapapi.map.MapActivity;
import com.amap.mapapi.map.MapController;
import com.amap.mapapi.map.MapView;
import com.amap.mapapi.map.MyLocationOverlay;
import com.amap.mapapi.map.RouteMessageHandler;
import com.amap.mapapi.map.RouteOverlay;
import com.amap.mapapi.map.ZoomButtonsController.OnZoomListener;
import com.amap.mapapi.route.Route;
import com.amap.mapapi.route.Route.FromAndTo;
import com.amap.mapapi.route.Segment;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;
import android.support.v4.app.NavUtils;
import android.provider.Settings.Secure;

public class ActivityMain extends MapActivity implements RouteMessageHandler{
	
	/*--------------------------------------------------------------------------
	 * public static final 
	 * -------------------------------------------------------------------------
	 */
	
	public static final String TAG = "ActivityMain";
	
	//map zoom level map to scale meter
	public static final int ZOOM_5M = 20;
	public static final int ZOOM_10M = 19;
	public static final int ZOOM_25M = 18;
	public static final int ZOOM_50M = 17;
	public static final int ZOOM_100M = 16;
	public static final int ZOOM_200M = 15;
	public static final int ZOOM_400M = 14;
	public static final int ZOOM_800M = 13;
	public static final int ZOOM_1500M = 12;
	public static final int ZOOM_3KM = 11;
	public static final int ZOOM_6KM = 10;
	public static final int ZOOM_12KM = 9;
	public static final int ZOOM_25KM = 8;
	public static final int ZOOM_50KM = 7;
	public static final int ZOOM_100KM = 6;
	public static final int ZOOM_200KM = 5;
	public static final int ZOOM_400KM = 4;
	public static final int ZOOM_800KM = 3;
	
	public static final int ZOOM_COUNTRY = ZOOM_400KM;
	public static final int ZOOM_PROVINCE = ZOOM_50KM;
	public static final int ZOOM_CITY = ZOOM_1500M;
	public static final int ZOOM_TOWN = ZOOM_800M;
	public static final int ZOOM_STREET = ZOOM_50M;
	public static final int ZOOM_NAVI = ZOOM_50M;
	
	public static final int ZOOM_MAX_BITMAP = 18;
	
	
	
	public static final int ZOOM_DEFAULT = ZOOM_TOWN;
	
	//------------------
	// handler msg.what
	//--------------------
	public static final int HANDLER_MSG_WHAT_LONG_PRESS = 1;
	
	public static final int HANDLER_MSG_WHAT_START_ROUTE = 1 + HANDLER_MSG_WHAT_LONG_PRESS;
	
	public static final int HANDLER_MSG_WHAT_UPDATE_ROUTE = 1 + HANDLER_MSG_WHAT_START_ROUTE;
	
	public static final int HANDLER_MSG_WHAT_CLEAR_INFO = 1 + HANDLER_MSG_WHAT_UPDATE_ROUTE;
	
	public static final int HANDLER_MSG_WHAT_CLEAR_BUTTON = 1 + HANDLER_MSG_WHAT_CLEAR_INFO;
	
	public static final int HANDLER_MSG_WHAT_CLEAR_ADDRESS = 1 + HANDLER_MSG_WHAT_CLEAR_BUTTON;
	
	public static final int HANDLER_MSG_WHAT_DEVICE_LOGINED = 1 + HANDLER_MSG_WHAT_CLEAR_ADDRESS;
	
	public static final int HANDLER_MSG_WHAT_POSITION_REPORTED = 1 + HANDLER_MSG_WHAT_DEVICE_LOGINED;
	
	public static final int HANDLER_MSG_WHAT_DEVICE_REGISTER = 1 + HANDLER_MSG_WHAT_POSITION_REPORTED;
	
	public static final int HANDLER_MSG_WHAT_DEVICE_REGISTER_FAILED = 1 + HANDLER_MSG_WHAT_DEVICE_REGISTER;
	
	public static final int HANDLER_MSG_WHAT_LOCATION_CHANGED = 1 + HANDLER_MSG_WHAT_DEVICE_REGISTER_FAILED;
	
	public static final int HANDLER_MSG_WHAT_SERVICE_INFO_READY = 1 + HANDLER_MSG_WHAT_LOCATION_CHANGED;
	
	public static final int HANDLER_MSG_WHAT_SERVICE_INFO_NULL = 1 + HANDLER_MSG_WHAT_SERVICE_INFO_READY;
	
	public static final int HANDLER_MSG_WHAT_SERVICE_INFO_QUERY = 1 + HANDLER_MSG_WHAT_SERVICE_INFO_NULL;
	
	public static final int HANDLER_MSG_WHAT_SERVICE_INFO_UPDATE = 1 + HANDLER_MSG_WHAT_SERVICE_INFO_QUERY;

	public static final int HANDLER_MSG_WHAT_PASSENGER_PICKUP = 1 + HANDLER_MSG_WHAT_SERVICE_INFO_UPDATE;
	
	public static final int HANDLER_MSG_WHAT_PASSENGER_ROUTE = 1 + HANDLER_MSG_WHAT_PASSENGER_PICKUP;

	public static final int HANDLER_MSG_WHAT_PASSENGER_CALL = 1 + HANDLER_MSG_WHAT_PASSENGER_ROUTE;

	public static final int HANDLER_MSG_WHAT_PASSENGER_SCHEDULE = 1 + HANDLER_MSG_WHAT_PASSENGER_CALL;
	
	public static final int HANDLER_MSG_WHAT_PASSENGER_PICKUP_REPORT = 1 + HANDLER_MSG_WHAT_PASSENGER_SCHEDULE;
	
	public static final int HANDLER_MSG_WHAT_SHOW_SCHEDULE = 1 + HANDLER_MSG_WHAT_PASSENGER_PICKUP_REPORT;

	public static final int HANDLER_MSG_WHAT_TAP_PASSENGER = 1 + HANDLER_MSG_WHAT_SHOW_SCHEDULE;

	public static final int HANDLER_MSG_WHAT_SHOW_ADDRESS = 1 + HANDLER_MSG_WHAT_TAP_PASSENGER;

	public static final int HANDLER_MSG_WHAT_SHOW_PASSENGER = 1 + HANDLER_MSG_WHAT_SHOW_ADDRESS;

	//---------------------
	// Bundle key
	public static final String BUNDLE_KEY_LATE6 = "bundle_key_late6";
	
	public static final String BUNDLE_KEY_LONGE6 = "bundle_key_longe6";
	
	public static final String BUNDLE_KEY_NUMBER = "bundle_key_number";
	
	public static final String BUNDLE_KEY_PASSENGER_NUMBER = "bundle_key_passenger_number";
	public static final String BUNDLE_KEY_PASSENGER_NAME = "bundle_key_passenger_name";
	
	
	//---------------------------------------------------------------------------------------------------
	// Activity request
	//----------------------------------------------------------------------------------------------------
	
	public static final int ACTIVITY_REQUEST_WAITING = 0;
	
	public static final int ACTIVITY_REQUEST_SCHEDULE = 1;
	
	public static final int ACTIVITY_REQUEST_PICKUP = 1 + ACTIVITY_REQUEST_SCHEDULE;
	
	public static final int ACTIVITY_REQUEST_ROUTE = 1 + ACTIVITY_REQUEST_PICKUP;
	
	public static final int ACTIVITY_REQUEST_CALL = 1 + ACTIVITY_REQUEST_ROUTE;
	
	public static final int ACTIVITY_RECENT_ADDRESS = 1 + ACTIVITY_REQUEST_CALL;
	
	
	//-----------------------
	// intent extra key
	//------------------------
	public static final String INTENT_EXTRA_KEY_ACTIVITY = "activity";
	
	public static final String INTENT_EXTRA_KEY_ACTIVITY_ROUTE = "activity_route";
	
	public static final String INTENT_EXTRA_KEY_ACTIVITY_CALL = "activity_caLL";
	
	public static final String INTENT_EXTRA_KEY_ACTIVITY_SCHEDULE = "activity_schedule";
	
	public static final String INTENT_EXTRA_KEY_ACTIVITY_PICKUP = "activity_pickup";
	
	public static final String INTENT_EXTRA_KEY_LATE6 = "late6";
	
	public static final String INTENT_EXTRA_KEY_LONGE6 = "longe6";

	public static final String INTENT_EXTRA_KEY_NUMBER = "number";
	
	public static final String INTENT_EXTRA_KEY_INSTANCE = "instance";

	//-------------------------------
	// timer
	//-----------------------------------
	public static final long TIMER_SECOND = 1000;
	
	public static final long TIMER_MINUTE = TIMER_SECOND * 60;
	
	public static final long TIMER_HOUR = TIMER_MINUTE * 60;
	
	public static final long TIMER_DAY = TIMER_HOUR * 24;
	
	public static final long TIMER_AUTO_CLEAR = TIMER_SECOND * 10;
	
	public static final long TIMER_SUSPEND = TIMER_DAY;
	
	public static final long TIMER_ROUTE_UPDATE = TIMER_SECOND * 10;
	
	public static final long TIMER_POSITION_REPORT = TIMER_MINUTE;
	
	public static final long TIMER_POSITION_RECORD = TIMER_MINUTE;
	
	public static final long TIMER_SHUTTLE_TICKET_QUERY = TIMER_MINUTE * 30;
	
	public static final long TIMER_QUICK_START = 1;
	
	public static final long TIMER_GPS_LOST = 10000;
	
	
	//
	public static final int LANE_SHIFT = 0;//5;
	
	
	
	/*-----------------------------------------------------------------------------
	 * public static 
	 * 
	 * ----------------------------------------------------------------------------
	 */
	
	public static Handler handlerRef;
	
	/*----------------------------------------------------------------------------
	 * private 
	 * ------------------------------------------------------------------------------
	 */
	
	private Context context;
	
	private Resources resources;
	
	private WakeLock wakeLock;
	
	private static boolean mapAvailable;;
	
	private boolean updatingRoute = false; //
	
	private boolean showAddress = false;
	
	private boolean newRoute = false; //	
	
	private boolean deviceLogined = false; //
	
	private boolean deviceLocated = false; //
	
	public  enum InfoRequest{
	
		QUERY,
		
		UPDATE,
		
		SCHEDULE,
		
		PICKUP,
		
		ROUTE,
		
		CALL;
	};
	
	private InfoRequest infoRequest = InfoRequest.QUERY;
	
	private boolean infoReqBussy = false;
	
	private int instance_serial = 0;
	
	private WebApi webApi = null;
	
	private DataOperator dataOperator;
	
	private String carNumber = null;
	
	private String driverName = null;
	
	
	//-----------------------------------------------------------------------
	// private for map operation 
	//--------------------------------------------------------------------------
	private MapView mapView = null;
	
	private MapController mapController = null;
	
	
	private MapGestureDetectorOverlay overlayMapGestureDetector;//	
	
	
	private LongPressPopupOverlay overlayLongPress;
	
	

	// fields for location
	private OverlayMapLocation overlayMapLocation;
	
	private GPSLocationOverlay gpsOverlay;
	
	private LocationManager locManager;
	
	private boolean myLocationLock; //
	
	private GeoPoint myPoint = null;//
	
	private Location myLocation = null;
	
	private Location gpsLocation = null;
	
	private int fixedAccuracy = 10000;
	
	private boolean gpsFixed = false;
	
	private double gpsDeltaLat = 0;
	
	private double gpsDeltaLong = 0;
	
	private int gpsDeltaLatE6 = 0;
	
	private int gpsDeltaLongE6 = 0;
	
	private double speedLat = 0;
	
	private double speedLong = 0;

	// fields for GeoCoder
	private GeoPoint addressPoint = null;

	private String pointAddress;
	
	// fields for passenger
	private String passengerName;
	private String passengerNumber;
	private boolean showPassenger = false;
	
	// fields for route 
	private GeoPoint myRoutePointStart = null;
	
	private GeoPoint myRoutePointEnd = null;
	private ArrayList<GeoPoint> listPassby = null;
	
	private Route routeCal = null;
	
    private RouteOverlay routeOverlay = null;
    
    
    // fields for scheduel
    private PassengerScheduleOverlay scheduleOverlay = null, scheduleCalOverlay = null;
    
    //fields for info request GUI
	private ExpandableListView listView;
	
	private ExpandableListAdapter listAdapter;	
	
	private ImageButton ibtnBack;
	
	private JSONArray arrayServiceInfo;
	
	private int infoReqActivity;


	//------------------------------------------------------
	// private of controller
	//------------------------------------------------------
	private ImageButton ibtnLocation, 
						ibtnPickup, 
						ibtnCall,
						ibtnSchedule, 
						ibtnClear, 
						ibtnRoute,
						ibtnTraffic,
						ibtnVector;
	
	private TextView 	tvMeters,
						tvMinutes,
						tvArriveTime,
						tvCarNumber,
						tvDriver,
						tvInfo;
	
	
	private RelativeLayout layoutButton,
						layoutText,
						layoutInfo,
						layoutPassenger,
						layoutProgress,
						layoutFilter;
	
	
	private ThreadTimer threadTimer;
	
	private Timer timerButtons,
					timerInfo,
					timerAddress,
					timerRoute,
					timerService,
					timerReportPos,
					timerRecordPos;

	
	/*----------------------------------------------------------------------------
	 * public method
	 * 
	 * -----------------------------------------------------------------------------
	 */
	
	
	/*
	 * (non-Javadoc)
	 * @see com.amap.mapapi.map.MapActivity#onCreate(android.os.Bundle)
	 */

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
        context = this;
        
        resources = this.getResources();
        
        handlerRef = handler;
        
        webApi = new WebApi(context);
        
        dataOperator = new DataOperator(context);
        
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.activity_main);
        
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0 , lsrGPSLocation);
        
        mapView = (MapView)findViewById(R.id.mapView);
       
        mapView.setVectorMap(PrefProxy.getMapVector(context));
        
        mapView.setTraffic(PrefProxy.getMapTraffic(context));
        
        mapView.setBuiltInZoomControls(true);
        
        mapController = mapView.getController();
        
        myPoint = new GeoPoint(PrefProxy.getMyLate6(context),PrefProxy.getMyLonge6(context));
        
        mapController.setCenter(myPoint);
        
        mapController.setZoom(PrefProxy.getZoomLevel(context));
        
        mapView.getZoomButtonsController().setOnZoomListener(lsrZoom);
        
        
        overlayMapGestureDetector = new MapGestureDetectorOverlay(onGestureListener);
		
        mapView.getOverlays().clear();
        
        mapView.getOverlays().add(overlayMapGestureDetector);
        
        overlayMapLocation = new OverlayMapLocation(context, mapView);
        
        overlayMapLocation.registerLocationListener(lsrMapLocation);
        
        overlayMapLocation.enableMyLocation();
        
        mapView.getOverlays().add(overlayMapLocation);
        
        
        myLocation = new Location(LocationManagerProxy.GPS_PROVIDER);
        
        myLocation.setLatitude(myPoint.getLatitudeE6()/1E6);
        
        myLocation.setLongitude(myPoint.getLongitudeE6()/1E6);
        
        myLocation.setAccuracy(-1);
        
        myLocation.setBearing(0);
        
        gpsOverlay = new GPSLocationOverlay(context, resources.getDrawable(R.drawable.marker_gps_location), myLocation);
        
        mapView.getOverlays().add(gpsOverlay);
        
        pointAddress = "";
		
        addressPoint = myPoint;
	       
        //---------------------
        //initial controller        
		//-----------------------
        ibtnLocation = (ImageButton)findViewById(R.id.imageButtonLocation);
        ibtnLocation.setOnClickListener(listenerImageButton);
        
        myLocationLock = true;
        //ibtnLocation.setImageResource(R.drawable.btn_location_locked);
        ibtnLocation.setImageResource(PrefProxy.getScreenFilter(context) ? R.drawable.btn_filter_on : R.drawable.btn_filter_off);
        
        ibtnPickup = (ImageButton)findViewById(R.id.imageButtonPickup);
        ibtnPickup.setOnClickListener(listenerImageButton);
        
        ibtnCall = (ImageButton)findViewById(R.id.imageButtonCall);
        ibtnCall.setOnClickListener(listenerImageButton);
        
        ibtnSchedule = (ImageButton)findViewById(R.id.imageButtonSchedule);
        ibtnSchedule.setOnClickListener(listenerImageButton);
        
        ibtnClear = (ImageButton)findViewById(R.id.imageButtonClear);
        ibtnClear.setOnClickListener(listenerImageButton);
        
        ibtnRoute = (ImageButton)findViewById(R.id.imageButtonRoute);
        ibtnRoute.setOnClickListener(listenerImageButton);
        
        ibtnTraffic = (ImageButton)findViewById(R.id.imageButtonTraffic);
        ibtnTraffic.setOnClickListener(listenerImageButton);
        ibtnTraffic.setImageResource(PrefProxy.getMapTraffic(context) ? R.drawable.btn_traffic_on : R.drawable.btn_traffic_off);
        
        ibtnVector = (ImageButton)findViewById(R.id.imageButtonVector);
        ibtnVector.setOnClickListener(listenerImageButton);
        ibtnVector.setImageResource(PrefProxy.getMapVector(context) ? R.drawable.btn_map_vector : R.drawable.btn_map_bitmap);
        
        //-------------
        //initial ui text        
        //--------------

        tvMeters = (TextView)findViewById(R.id.textViewMeters);
        
        tvMinutes = (TextView)findViewById(R.id.textViewMinutes);
        
        tvArriveTime = (TextView)findViewById(R.id.textViewArriveTime);
        
        clearRouteText();
        
        tvCarNumber = (TextView)findViewById(R.id.textViewCarNumber);
        
        tvDriver = (TextView)findViewById(R.id.textViewDriver);
        
        tvInfo = (TextView)findViewById(R.id.textViewInfo);
       

        
 
        
        //---------------------------------------------
        //initial layout layers
        //----------------------------------
       
        layoutButton = (RelativeLayout)findViewById(R.id.relativeLayoutButtons);
        
        layoutText = (RelativeLayout)findViewById(R.id.relativeLayoutText);
        
        layoutInfo = (RelativeLayout)findViewById(R.id.relativeLayoutInfo);
        
        layoutPassenger = (RelativeLayout)findViewById(R.id.relativeLayoutPassenger);
        
        layoutProgress = (RelativeLayout)findViewById(R.id.relativeLayoutProgress);
       
        layoutFilter = (RelativeLayout)findViewById(R.id.relativeLayoutFilter);
        layoutFilter.setVisibility(PrefProxy.getScreenFilter(context) ? View.VISIBLE : View.GONE);
        
        //--------------------------------
        //initial threads    
        //---------------------------------
        
        threadTimer = new ThreadTimer(1);// timer per 1 seconds
        
        threadTimer.start();
        
        timerButtons = new Timer(TIMER_AUTO_CLEAR, false, handler, HANDLER_MSG_WHAT_CLEAR_BUTTON);
               
        timerInfo = new Timer(TIMER_AUTO_CLEAR, false, handler, HANDLER_MSG_WHAT_CLEAR_INFO);
        
        timerAddress = new Timer(TIMER_AUTO_CLEAR, false, handler, HANDLER_MSG_WHAT_CLEAR_ADDRESS);
        
        timerRoute = new Timer(TIMER_ROUTE_UPDATE, true, callbackRoute, handler, HANDLER_MSG_WHAT_UPDATE_ROUTE);
        
        
        timerService = new Timer(TIMER_SHUTTLE_TICKET_QUERY, true, callbackServiceInfo);
        
        timerReportPos = new Timer(TIMER_POSITION_REPORT, true, callbackReportPosition);
        
        timerRecordPos = new Timer(TIMER_POSITION_RECORD, true, callbackRecordPosition);

        threadTimer.timerReset(timerReportPos);
        
        threadTimer.timerReset(timerRecordPos);
        
        threadTimer.timerReset(timerService);
 
        Timer timer = new Timer(1, false, callbackReportPosition);
    	
    	threadTimer.timerQuickStart(timer);
   	
	//	checkGPSSetting();

    //    checkDataConnection();
        
        screenWakeLock();
        
   }
    
    
    
    

    /* (non-Javadoc)
	 * @see com.amap.mapapi.map.MapActivity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		mapAvailable = true;

		clearInfo();

		showButtons();
        
	}



	/* (non-Javadoc)
	 * @see com.amap.mapapi.map.MapActivity#onStop()
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
        mapAvailable = false;

		super.onStop();
		
	}

	/* (non-Javadoc)
	 * @see com.amap.mapapi.map.MapActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		
		overlayMapLocation.disableMyLocation();
			
		locManager.removeUpdates(lsrGPSLocation);
			
		threadTimer.stopRunning();
			
		threadTimer = null;

//		closeGPSSuggesttion();

		screenWakeRelease();
			
		super.onDestroy();
			
	}



	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		Log.i(TAG, "requestCode:"+requestCode+ ",resultCode:" + resultCode);
		
		infoReqBussy = false;
		
		infoRequest = InfoRequest.UPDATE;
		
		clearProgress();
			
		switch(requestCode){
		
		case ACTIVITY_REQUEST_PICKUP:
			
			//infoRequest = InfoRequest.UPDATE;
			
			handler.sendEmptyMessage(HANDLER_MSG_WHAT_SERVICE_INFO_UPDATE);
		
		case ACTIVITY_REQUEST_CALL:
			
//			if(data.getBooleanExtra(INTENT_EXTRA_KEY_ACTIVITY_CALL, false)){
			if(Activity.RESULT_OK == resultCode){
				
				String number = data.getStringExtra(INTENT_EXTRA_KEY_NUMBER);
			
				Bundle bundle = new Bundle();
			
				bundle.putString(BUNDLE_KEY_NUMBER, number);
			
				Message msg = new Message();
			
				msg.setData(bundle);
			
				msg.what = HANDLER_MSG_WHAT_PASSENGER_CALL;
			
				handler.sendMessage(msg);
			
			}
			
			break;
			
		case ACTIVITY_REQUEST_ROUTE:

			if(Activity.RESULT_OK == resultCode){
				
				this.listPassby = null;
				int late6 = data.getIntExtra(INTENT_EXTRA_KEY_LATE6, myPoint.getLatitudeE6());
				
				int longe6 = data.getIntExtra(INTENT_EXTRA_KEY_LONGE6, myPoint.getLongitudeE6());
				
				Bundle bundle = new Bundle();
			
				bundle.putInt(BUNDLE_KEY_LATE6, late6);
				
				bundle.putInt(BUNDLE_KEY_LONGE6, longe6);
			
				Message msg = new Message();
			
				msg.setData(bundle);
			
				msg.what = HANDLER_MSG_WHAT_START_ROUTE;
			
				handler.sendMessage(msg);
			
			}
			
			break;

		case ACTIVITY_REQUEST_SCHEDULE:
			
			if(Activity.RESULT_OK == resultCode){
				
				String json = data.getCharSequenceExtra(WebApi.API_JSON_SHUTTLE_ARRAY).toString();
				
				int instance_serial = data.getIntExtra(DatabaseHelper.DB_COLUMN_INSTANCE_SERIAL, 0);
				
				Message msg = new Message();
				
				msg.what = HANDLER_MSG_WHAT_PASSENGER_SCHEDULE;
				
				Bundle bundle = new Bundle();
				
				bundle.putString(WebApi.API_JSON_SHUTTLE_ARRAY, json);
				
				bundle.putInt(DatabaseHelper.DB_COLUMN_INSTANCE_SERIAL, instance_serial);
				
				msg.setData(bundle);
				
				handler.sendMessage(msg);
			
			}
			else{
				;
			}
	
			break;
			
		case ACTIVITY_RECENT_ADDRESS:
			
			if(resultCode == Activity.RESULT_OK){

				int late6 = data.getIntExtra(INTENT_EXTRA_KEY_LATE6, myPoint.getLatitudeE6());
				
				int longe6 = data.getIntExtra(INTENT_EXTRA_KEY_LONGE6, myPoint.getLongitudeE6());
				
	 			Message msg = new Message();
	 			msg.what = HANDLER_MSG_WHAT_LONG_PRESS;
	 			
	 			Bundle bundle = new Bundle();
	 			bundle.putInt(BUNDLE_KEY_LATE6, late6);
	 			bundle.putInt(BUNDLE_KEY_LONGE6, longe6);
	 			msg.setData(bundle);
	 			
	 		    handler.sendMessage(msg);
				
				Log.i(TAG, "lat:"+late6+",long:"+longe6);
			}
			
			break;
			
			
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}

	
	
	

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
		getMenuInflater().inflate(R.menu.activity_main, menu);
        
		return true;
		
    }
    
    
 

    /* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		switch(item.getItemId()){
		
		case R.id.menu_info:
			startActivityForResult(new Intent(context, HostSettingActivity.class),ACTIVITY_RECENT_ADDRESS);
			
			//showInfo(Utilities.getDeviceInfo(context));
			
			break;
			
		case R.id.menu_register:
			
			registerDevice();
			showInfo(Utilities.getDeviceInfo(context));
			break;
			
		case R.id.menu_address:
			
			//startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS),0);
			startActivityForResult(new Intent(context, ActivityAddress.class),ACTIVITY_RECENT_ADDRESS);
			
			break;
			
		case R.id.menu_exit:
			
			finish();
		
			break;
			
		case R.id.menu_offlline_map:
			
			startActivityForResult(new Intent(context, ActivityOfflineMap.class),0);
			
			break;
			
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	
	

	/* (non-Javadoc)
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch(keyCode){

		case KeyEvent.KEYCODE_BACK:
			
			return true;
		    
		}

		return super.onKeyDown(keyCode, event);
	}

    /*******************************************************************************
     * RouteMessageHandler Interface
     */
    
    
    /*
     * (non-Javadoc)
     * @see com.amap.mapapi.map.RouteMessageHandler#onDrag(com.amap.mapapi.map.MapView, com.amap.mapapi.map.RouteOverlay, int, com.amap.mapapi.core.GeoPoint)
     */


	public void onDrag(MapView arg0, RouteOverlay arg1, int arg2, GeoPoint arg3) {
		// TODO Auto-generated method stub
		
	}




	/*
	 * (non-Javadoc)
	 * @see com.amap.mapapi.map.RouteMessageHandler#onDragBegin(com.amap.mapapi.map.MapView, com.amap.mapapi.map.RouteOverlay, int, com.amap.mapapi.core.GeoPoint)
	 */

	public void onDragBegin(MapView mapView, RouteOverlay overlayRoute, int arg2,
			GeoPoint point) {
		// TODO Auto-generated method stub
		
		
	}




	/*
	 * (non-Javadoc)
	 * @see com.amap.mapapi.map.RouteMessageHandler#onDragEnd(com.amap.mapapi.map.MapView, com.amap.mapapi.map.RouteOverlay, int, com.amap.mapapi.core.GeoPoint)
	 */

	public void onDragEnd(MapView mapView, RouteOverlay overlayRoute, int arg2,
			GeoPoint point) {
		// TODO Auto-generated method stub


		Message msg = new Message();
		msg.what = ActivityMain.HANDLER_MSG_WHAT_START_ROUTE;
		
		Bundle bundle = new Bundle();
		bundle.putInt(BUNDLE_KEY_LATE6, point.getLatitudeE6());
		bundle.putInt(BUNDLE_KEY_LONGE6, point.getLongitudeE6());
		msg.setData(bundle);
		
		handler.sendMessage(msg);

	}


	


	/*
	 * (non-Javadoc)
	 * @see com.amap.mapapi.map.RouteMessageHandler#onRouteEvent(com.amap.mapapi.map.MapView, com.amap.mapapi.map.RouteOverlay, int, int)
	 */

	public boolean onRouteEvent(MapView arg0, RouteOverlay arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
		return false;
	}



	
	
	/********************************************************************************
	 * public method
	 * 
	 ****************************************************************************/
    
	/*
	 * 
	 */
	
	private OnZoomListener lsrZoom = new OnZoomListener(){

		public void onVisibilityChanged(boolean arg0) {
			// TODO Auto-generated method stub
			
		}

		public void onZoom(boolean arg0) {
			// TODO Auto-generated method stub
			PrefProxy.setZoomLevel(context, mapView.getZoomLevel());
			
		}
		
	};
	
    /*
     * listener for Map location
     */
    
    private LocationListener lsrMapLocation = new LocationListener(){

		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub

			if(!deviceLocated){
				
				deviceLocated = true;
			
			}
			
			int late6 = (int)(location.getLatitude()*1E6);
			int longe6 = (int)(location.getLongitude()*1E6);
			
			myPoint = new GeoPoint(late6,longe6);
			
			if(myLocationLock){

				try{
				
					mapController.animateTo(myPoint);
					
				}
				
				catch(NullPointerException e){
				
					e.printStackTrace();
					
				}
			
			}
			
			if(updatingRoute){
				
				myRoutePointStart = new GeoPoint(myPoint.getLatitudeE6(), myPoint.getLongitudeE6());
				
			}
		
			PrefProxy.setMyLate6(context, late6);
			PrefProxy.setMyLonge6(context, longe6);
			
			myLocation = new Location(location);
			
			myLocation.setTime(System.currentTimeMillis());
			
			if(gpsLocation == null){
				
				gpsLocation = new Location(LocationManager.GPS_PROVIDER);
			
				gpsLocation.setTime(location.getTime() - TIMER_GPS_LOST - 1);
				
			}

			long timeDelta = myLocation.getTime() - gpsLocation.getTime();

			if( (timeDelta < 1000 && (location.getAccuracy() < fixedAccuracy) || location.getAccuracy() < 10)){

				if(gpsLocation.getSpeed() < 1.5){
					
					gpsDeltaLat = myLocation.getLatitude() - gpsLocation.getLatitude();
					
					gpsDeltaLong = myLocation.getLongitude()  - gpsLocation.getLongitude();
					
				}
				else{
					
					double circleLong = 6371.004*1000*2*Math.PI;
				
					double circleLat = circleLong*Math.cos(Math.toRadians(location.getLatitude()));
				
					double speedNS  = gpsLocation.getSpeed()*Math.cos(Math.toRadians(gpsLocation.getBearing()));
						
					double speedEW = gpsLocation.getSpeed()*Math.sin(Math.toRadians(gpsLocation.getBearing()));
				
					double speedLat = speedNS/1000/circleLong*360;
				
					double speedLong = speedEW/1000/circleLat*360;
				
					double latDelta = speedLat * timeDelta;
					
					double longDelta = speedLong * timeDelta;

					gpsDeltaLat = myLocation.getLatitude() - (gpsLocation.getLatitude()+latDelta);
					
					gpsDeltaLong = myLocation.getLongitude()  - (gpsLocation.getLongitude()+longDelta);
				
				}
				
				gpsDeltaLatE6 = (int)(gpsDeltaLat*1E6);
				
				gpsDeltaLongE6 = (int)(gpsDeltaLong*1E6);
				
				gpsFixed = true;
				
				fixedAccuracy = (int) location.getAccuracy();
				
			}
			
/*			
			Log.i(TAG, "MAPAPI:"+myLocation.getProvider()
					+",time:"+ Utilities.StampToString(myLocation.getTime())
					+",speed:" + myLocation.getSpeed()
					+",bearing:"+ myLocation.getBearing()
					+",accuracy:"+myLocation.getAccuracy()
					+",latitude:"+myLocation.getLatitude()
					+",longitude:"+myLocation.getLongitude());
*/
			
			// draw map location while lost gps more than 10 second
			if(System.currentTimeMillis() - gpsLocation.getTime() > TIMER_GPS_LOST ){
			
				gpsOverlay.onLocationChanged(location);
			
				mapView.invalidate();
			
			}
			
		}

		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
    	
    };
    
    /*
     * listener for LocationManager, GPS provider
     */
    
    private LocationListener lsrGPSLocation = new LocationListener(){

		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			
			gpsLocation = new Location(location);
			
			gpsLocation.setTime(System.currentTimeMillis());
			
			
			if(gpsFixed){
			
				int late6 = (int)((location.getLatitude()+gpsDeltaLat) * 1E6);
				
				int longe6 = (int)((location.getLongitude()+gpsDeltaLong) * 1E6);
				
				myPoint = new GeoPoint(late6,longe6);

				PrefProxy.setMyLate6(context, late6);
				PrefProxy.setMyLonge6(context, longe6);
				
				myLocation = new Location(location);
				
				myLocation.setLatitude(myPoint.getLatitudeE6()/1E6);
				
				myLocation.setLongitude(myPoint.getLongitudeE6()/1E6);
				
				if(myLocationLock){

					try{
					
						mapController.animateTo(myPoint);
						
					}
					
					catch(NullPointerException e){
					
						e.printStackTrace();
						
					}
				
				}
				
				if(updatingRoute){

					double circleLong = 6371.004*1000*2*Math.PI;
					
					double circleLat = circleLong*Math.cos(Math.toRadians(location.getLatitude()));

					double shiftRight = LANE_SHIFT;
					
					double shiftNS = shiftRight * Math.cos(Math.toRadians(location.getBearing()+90));
					
					double shiftEW = shiftRight * Math.sin(Math.toRadians(location.getBearing()+90));
					
					int shiftLat = (int)(shiftNS/circleLong*360*1E6);
					
					int shiftLong = (int)(shiftEW/circleLat*360*1E6);

					myRoutePointStart = new GeoPoint(myPoint.getLatitudeE6() + shiftLat, myPoint.getLongitudeE6() + shiftLong);
					
				}
			
				
				gpsOverlay.onLocationChanged(myLocation);
				
				mapView.invalidate();

			}
/*
			Log.i(TAG, "GPS:"+gpsLocation.getProvider()
					+",time:"+ Utilities.StampToString(gpsLocation.getTime())
					+",speed:" + gpsLocation.getSpeed()
					+",bearing:"+ gpsLocation.getBearing()
					+",accuracy:"+gpsLocation.getAccuracy()
					+",latitude:"+gpsLocation.getLatitude()
					+",longitude:"+gpsLocation.getLongitude()
					+ "lat:" + (gpsFixed ? myPoint.getLatitudeE6()/1E6 : 0 )
					+ "long:" + (gpsFixed ? myPoint.getLongitudeE6()/1E6 : 0)
					);
*/			
		}

		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
    	
    };
    
    
   	
	/*
     * listener for image buttons
     */
    private OnClickListener listenerImageButton = new OnClickListener(){

		public void onClick(View v) {
			
			// TODO Auto-generated method stub
			
			switch(v.getId()){
			
			case R.id.imageButtonLocation:
				
				if(myLocationLock==true){
					
					toggleFilter();
				
				}
				else{

					lockMyLocation();
					
				}
				
				break;
				
			case R.id.imageButtonTraffic:
				
				toggleTraffic();
				
				break;
				
			case R.id.imageButtonVector:
				
				toggleVector();
				
				break;
			
			case R.id.imageButtonPickup:
				
				infoRequest = InfoRequest.PICKUP;

				handleServiceInfo();
				
				break;

				
			case R.id.imageButtonCall:

				if(showPassenger){
					
					Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:" + passengerNumber));
					
					startActivityForResult(intent, ACTIVITY_REQUEST_WAITING);
					
				}
				else{
					infoRequest = InfoRequest.CALL;
					
					handleServiceInfo();
				}				
				break;
				
			case R.id.imageButtonRoute:

				if(showAddress){
					
					routeAddress();
					
					clearAddress();
					
				}
				else if(showPassenger){
					routePassengerAddress();
					clearAddress();
					
				}
				else{
				
					infoRequest = InfoRequest.ROUTE;
				
					handleServiceInfo();
				
				}
				
				break;


			case R.id.imageButtonSchedule:

				infoRequest = InfoRequest.SCHEDULE;
				
				handleServiceInfo();
				
				break;
				
			case R.id.imageButtonClear:
				
				stopRoute();
				
				clearPassengerSchedule();

				clearInfo();
				
				clearAddress();
				
				break;
			
			}
			
		}
    	
    };
    


    /*
     * listener for gesture
     */
    
    private OnGestureListener onGestureListener = new OnGestureListener(){

 		public boolean onDown(MotionEvent e) {
 			// TODO Auto-generated method stub
 			
 			showButtons();
 			
 			return false;
 		}

 		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
 				float velocityY) {
 			// TODO Auto-generated method stub

 			showButtons();
 			
 			unlockMyLocation();
 			
 			return true;
 		}

 		
 		public void onLongPress(MotionEvent e) {
 			// TODO Auto-generated method stub

 			GeoPoint point = mapView.getProjection().fromPixels((int)(e.getX()), (int)(e.getY()));
 			
 			Message msg = new Message();
 			msg.what = HANDLER_MSG_WHAT_LONG_PRESS;
 			
 			Bundle bundle = new Bundle();
 			bundle.putInt(BUNDLE_KEY_LATE6, point.getLatitudeE6());
 			bundle.putInt(BUNDLE_KEY_LONGE6, point.getLongitudeE6());
 			msg.setData(bundle);
 			
 		    handler.sendMessage(msg);

 		}

		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// TODO Auto-generated method stub
		
			showButtons();
			
			unlockMyLocation();
 			
 			return true;
		}

		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub

 			showButtons();
 			
		}

		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub

			showButtons();

			return false;
		}
    };
    
    


	/*
	 * handler for main activity
	 */
    private Handler handler = new Handler(){

		/* (non-Javadoc)
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
//		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
			switch(msg.what){
			
			case HANDLER_MSG_WHAT_LONG_PRESS:
				
				showProgress();
				
				getPointAddress(msg);

				break;
				
			case HANDLER_MSG_WHAT_SHOW_ADDRESS:
				
				showAddress();
				
				clearProgress();
				
				break;
			
			case HANDLER_MSG_WHAT_SHOW_PASSENGER:
				
				showPassenger();
				
				clearProgress();
				
				break;
				
			case HANDLER_MSG_WHAT_START_ROUTE:
				
				myRoutePointStart = new GeoPoint(myPoint.getLatitudeE6(), myPoint.getLongitudeE6());

				startRoute(msg);
				
				newRoute = true;
				
				showProgress();
				
				break;
				
			case HANDLER_MSG_WHAT_UPDATE_ROUTE : 
				
				if(mapAvailable){
					try{
				
						updateRoute();
				
						if(newRoute){
					
							newRoute = false;
					
							clearProgress();
				
						}
				
					}
					catch(Exception e){
					
						e.printStackTrace();
				
					}
				}
				
				break;
				
				
			case HANDLER_MSG_WHAT_CLEAR_INFO:
				
				clearInfo();
				
				break;
				
			case HANDLER_MSG_WHAT_CLEAR_BUTTON:
				
				clearButtons();
				
				break;
				

			case HANDLER_MSG_WHAT_CLEAR_ADDRESS:
				
				clearAddress();
				
				break;
				
			case HANDLER_MSG_WHAT_DEVICE_LOGINED:
				
				deviceLogined = true;
				
				carNumber = PrefProxy.getCarNumber(context);
				
				showLoginInfo();
				
				break;
				
			case HANDLER_MSG_WHAT_POSITION_REPORTED:
				
				reportPosition();
				
				break;
				
				
			case HANDLER_MSG_WHAT_DEVICE_REGISTER:
				
				Toast.makeText(context, R.string.device_register, Toast.LENGTH_LONG).show();
				
				loginDevice();
				
				break;
				
				
			case HANDLER_MSG_WHAT_DEVICE_REGISTER_FAILED:
				
				Toast.makeText(context, R.string.device_register_failed, Toast.LENGTH_LONG).show();
				
				break;
				
				
			case HANDLER_MSG_WHAT_SERVICE_INFO_QUERY:
				
				Toast.makeText(context, R.string.shuttle_ticket_query, Toast.LENGTH_LONG).show();
				showProgress();
				queryServiceInfo();

				break;
				
			case HANDLER_MSG_WHAT_SERVICE_INFO_UPDATE:
				
				infoRequest = InfoRequest.UPDATE;
				
				queryServiceInfo();

				break;
				
			case HANDLER_MSG_WHAT_SERVICE_INFO_NULL:
				
				Toast.makeText(context, R.string.shuttle_ticket_null, Toast.LENGTH_LONG).show();
				clearProgress();
				break;
				
			case HANDLER_MSG_WHAT_SERVICE_INFO_READY:
				clearProgress();
				switch(infoRequest){
				
				case QUERY:
					
					break;
					
				case UPDATE:
					
					if(isPassengerScheduled()){
						
						Toast.makeText(context, R.string.shuttle_ticket_update, Toast.LENGTH_LONG).show();//澶氱偣鎺掑簭鏃惰淇℃伅鏃犳硶鏄剧ずi
						
						showProgress();
						
						optimizeScheduel();

					}
					
					break;
					
				case SCHEDULE:
					
					startActivityInfoRequest(ACTIVITY_REQUEST_SCHEDULE);
					
					break;
					
				case PICKUP:
					
					startActivityInfoRequest(ACTIVITY_REQUEST_PICKUP);
					
					break;
					
				case ROUTE:

					startActivityInfoRequest(ACTIVITY_REQUEST_ROUTE);
					
					break;
					
				case CALL:

					startActivityInfoRequest(ACTIVITY_REQUEST_CALL);
					
					break;
				}
				
				break;
				
			case HANDLER_MSG_WHAT_PASSENGER_PICKUP:
				
				break;
				
			case HANDLER_MSG_WHAT_PASSENGER_ROUTE:
				
				break;

			case HANDLER_MSG_WHAT_PASSENGER_CALL:

				String number = msg.getData().getString(BUNDLE_KEY_NUMBER);
				
				Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:" + number));
				
				startActivityForResult(intent, ACTIVITY_REQUEST_WAITING);

				//startActivity(intent);

				break;

			case HANDLER_MSG_WHAT_PASSENGER_SCHEDULE:
				
				Toast.makeText(context, R.string.passenger_schedule_calculating, Toast.LENGTH_LONG).show();
				
				instance_serial = msg.getData().getInt(DatabaseHelper.DB_COLUMN_INSTANCE_SERIAL);
				
				showProgress();
				
				optimizeScheduel();
				
				break;

			case HANDLER_MSG_WHAT_SHOW_SCHEDULE:
				
				showPassengerSchedule(instance_serial);
				
				clearProgress();
				
				break;
			
			case HANDLER_MSG_WHAT_PASSENGER_PICKUP_REPORT:
				
				int ticket_serial = msg.getData().getInt(DatabaseHelper.DB_COLUMN_TICKET_SERIAL);
				
				String pickup = msg.getData().getString(DatabaseHelper.DB_COLUMN_TICKET_SERVE_CONFIRM);

				webApi.reportPickup(ticket_serial, pickup);

				break;
				
				
			case HANDLER_MSG_WHAT_TAP_PASSENGER:
				
				getPassengerAddress(msg);
				
	 			break;
				
			}
		}
    	
    	
    	
    };
    
    

    /******************************************************************
     * private 
     ***************************************************************/
    

    
    /*--------------------------------------------------------------------------------------
     * private
     * 
     * ----------------------------------------------------------------------------------
     */
    
    
    /*
     * toggle screen filter
     */
    
    private void toggleFilter(){
  
    	boolean filter = PrefProxy.getScreenFilter(context);
    	
    	filter = !filter;
    	
    	PrefProxy.setScreenFilter(context, filter);
    	
    	layoutFilter.setVisibility(filter ? View.VISIBLE : View.GONE);
    	
    	this.ibtnLocation.setImageResource( filter ? R.drawable.btn_filter_on : R.drawable.btn_filter_off);
    	
    }
    

    /*
     * toggle map vector mode
     */
    private void toggleVector(){
    	
    	boolean vector = PrefProxy.getMapVector(context);
    	
    	vector = !vector;
    	
    	PrefProxy.setMapVector(context, vector);
    	
    	int zoom = mapView.getZoomLevel();
    	
    	if((vector == false) && (zoom > ZOOM_MAX_BITMAP)){
    		
    		mapController.setZoom(ZOOM_MAX_BITMAP);
    		
    	}
    	
    	mapView.setVectorMap(vector);
    	
    	ibtnVector.setImageResource(vector ? R.drawable.btn_map_vector : R.drawable.btn_map_bitmap);
    	
    }
    
    
    /*
     * toggle traffic layer
     */
    private void toggleTraffic(){
    	
    	boolean traffic = PrefProxy.getMapTraffic(context);
    	
    	traffic  = !traffic;
    	
    	PrefProxy.setMapTraffic(context, traffic);
    	
    	mapView.setTraffic(traffic);
    	
    	//mapView.setVectorMap(showTraffic);
    	
    	ibtnTraffic.setImageResource(traffic ? R.drawable.btn_traffic_on : R.drawable.btn_traffic_off);
    	
    }
    
    /*
     * clear progress bar
     */
    private void clearProgress(){
    	
    	layoutProgress.setVisibility(View.GONE);
    }
    
    /*
     * show progress bar
     */
    private void showProgress(){
    
    	layoutProgress.setVisibility(View.VISIBLE);
    	
    }
    
    
    /*
     * 
     */
    
    private TimerCallBack callbackRecordPosition = new TimerCallBack(){

		public void onTimerOut() {
			// TODO Auto-generated method stub
			threadTimer.timerReset(timerRecordPos);

			if(deviceLogined && deviceLocated){
				
				dataOperator.recordPosition(carNumber,myLocation); 
		    	
			}
			
		}
    	
    	
    };
    
    /*
     * 
     */
    
    private TimerCallBack callbackReportPosition = new TimerCallBack(){

		public void onTimerOut() {
			// TODO Auto-generated method stub

			threadTimer.timerReset(timerReportPos, TIMER_POSITION_REPORT);

			if(isDataConnected()){
				
				if(!deviceLogined){
			
					webApi.loginDevice();
			
				}
		
				dataOperator.reportPosition();
			
			}
			
		}
    	
    	
    };
    
    private void reportPosition(){
    	
    	Timer timer = new Timer(1, false, callbackReportPosition);
    	
    	threadTimer.timerReset(timerReportPos);
    	
    	threadTimer.timerQuickStart(timer);
    	
    }
    
    private TimerCallBack callbackServiceInfo = new TimerCallBack(){

		public void onTimerOut() {
			// TODO Auto-generated method stub
			
			webApi.queryServiceInfo();
			
		}
    	
    };
    
    private void queryServiceInfo(){
    	
    	Timer timer = new Timer(1, false, callbackServiceInfo);
    	
    	threadTimer.timerReset(timerService);
    	
    	threadTimer.timerQuickStart(timer);
    	
    }
    
    /*
     * start service info activity
     */
    private void startActivityInfoRequest(int request){
    	
    	if(WebApi.isServiceInfoReady(context) && !infoReqBussy){
    		
    		showProgress();
    	
    		Intent intent = new Intent(context, ActivityInfoRequest.class); 
    	
    		intent.putExtra(WebApi.API_JSON_SHUTTLE_ARRAY, PrefProxy.getServiceInfo(context));
		
    		intent.putExtra(INTENT_EXTRA_KEY_ACTIVITY, request);
		
    		infoReqBussy = true;
    		
    		((Activity) context).startActivityForResult(intent, request);
    	
    	}
    	else{
    		
    		if(!WebApi.isServiceInfoReady(context)){
    		
    			handler.sendEmptyMessage(HANDLER_MSG_WHAT_SERVICE_INFO_NULL);
    		
    		}
    	}
    }
    
    /*
     * check passenger schedule status
     */
    private boolean isPassengerScheduled(){

    	return (scheduleOverlay!=null ? true : false);
    	
    }
    
    /*
     * clear passenger schedule overlay
     */
    private void clearPassengerSchedule(){

    	if(isPassengerScheduled()){
			
			mapView.getOverlays().remove(scheduleOverlay);
			
			scheduleOverlay = null;
			
		}
    	
    	
    }
    
    /*
     * 
     */
    
    private TimerCallBack callbackSchedule = new TimerCallBack(){

		public void onTimerOut() {
			// TODO Auto-generated method stub
			
			try {
				
				String info = PrefProxy.getServiceInfo(context);
				
				JSONArray array = WebApi.infoToJsonArray(info);
		
				JSONObject obj = WebApi.getInstance(instance_serial, array);
				 
				Drawable marker = context.getResources().getDrawable(R.drawable.marker_passenger_waiting);
				    
			    scheduleCalOverlay = new PassengerScheduleOverlay(marker, obj, context);
			
			}
			catch(Exception e){
				
				e.printStackTrace();
			
			}
			
		}
    	
    	
    };
    
    /*
     * 
     */
    
    private void optimizeScheduel(){
    	
    	Timer timer = new Timer(1, false, callbackSchedule, handler, HANDLER_MSG_WHAT_SHOW_SCHEDULE );
    	
    	threadTimer.timerQuickStart(timer);
    	
    }
    
    /*
     * show passenger schedule overlay
     */
    private void showPassengerSchedule(int instance_serial){
		
		if(instance_serial > 0){
		
			try {
				
				clearPassengerSchedule();
				
				scheduleOverlay = scheduleCalOverlay;
				
				scheduleOverlay.setDrawFocusedItem(true);
				
		        mapView.getOverlays().add(scheduleOverlay);
				
				unlockMyLocation();
				
				mapController.setZoom(ZOOM_CITY);

				mapController.animateTo(scheduleOverlay.getCenter());
				/*
				Toast.makeText(context, Utilities.MetersToString(scheduleOverlay.getScheduleMeters(),context)+
						","+
						Utilities.MinutesToString(scheduleOverlay.getScheduleMinutes(),context), 
						Toast.LENGTH_LONG).show();
				*/
				
				this.listPassby = (ArrayList<GeoPoint>) scheduleCalOverlay.getPassby();
				Log.i(TAG,"passby:"+listPassby.size());
				Message msg = new Message();
				msg.what = ActivityMain.HANDLER_MSG_WHAT_START_ROUTE;
				
				Bundle bundle = new Bundle();
				GeoPoint endpoint = scheduleCalOverlay.getRouteEnd();
				bundle.putInt(ActivityMain.BUNDLE_KEY_LATE6, endpoint.getLatitudeE6());
				bundle.putInt(ActivityMain.BUNDLE_KEY_LONGE6, endpoint.getLongitudeE6());
				msg.setData(bundle);
				
				handler.sendMessage(msg);

				
				
			}
			catch(Exception e){
			
				e.printStackTrace();
			
			}
		
		}
    }

    private void startPassengerScheduleRoute(int instance_serial){
		
		if(instance_serial > 0){
		
			try {
				
				clearPassengerSchedule();
				
				scheduleOverlay = scheduleCalOverlay;
				
				scheduleOverlay.setDrawFocusedItem(true);
				
		        mapView.getOverlays().add(scheduleOverlay);
				
				unlockMyLocation();
				
				mapController.setZoom(ZOOM_CITY);

				mapController.animateTo(scheduleOverlay.getCenter());
				
				Toast.makeText(context, Utilities.MetersToString(scheduleOverlay.getScheduleMeters(),context)+
						","+
						Utilities.MinutesToString(scheduleOverlay.getScheduleMinutes(),context), 
						Toast.LENGTH_LONG).show();
				
				
			}
			catch(Exception e){
			
				e.printStackTrace();
			
			}
		
		}
    }

    
    /*
     * show login info of car number and driver name
     */
    
    private void showLoginInfo(){
    	
    	tvCarNumber.setText(PrefProxy.getCarNumber(context));
    	
    	tvDriver.setText(PrefProxy.getDriverName(context));
    	
    }
    
    /*
     * get address from point
     */
    
    
    private String parseAddress(){
    	
    	String addressname = "";
		
    	try{
		
    		Geocoder geoCoder = new Geocoder((Activity) context);
			
			List<Address> listAddress = geoCoder.getFromLocation(addressPoint.getLatitudeE6()/1E6,
					 addressPoint.getLongitudeE6()/1E6, 1);
			
			int length = listAddress.size();
			
			if(listAddress.size()!=0){
			
				Address address;
			 	
				int i = 0;
			 	
				length = 1;
			 	 
				while(i<length){
			
					address = listAddress.get(i);
				
					addressname="";
					 
					if(address.getAdminArea()!=null)
						addressname += address.getAdminArea();
					
					if(address.getSubAdminArea()!=null)
						addressname += address.getSubAdminArea();
					 
					if(address.getSubLocality()!=null)
						addressname += address.getSubLocality();
					 
					if(address.getFeatureName()!=null)
						addressname += address.getFeatureName();
					 
					i++;
				}
			}
				
			
		}
		
		catch(AMapException e){
		
		//	 Log.i(TAG, "err msg:"+e.getErrorMessage());
			e.printStackTrace();
			
		}
		
		return addressname;
    	
    }
    
    /*
     * clear address popup overlay
     */
    private void clearAddress(){
    	
    	try{
    		
    		
    	
    		if(overlayLongPress != null){
    		
    			overlayLongPress.getPopView().setVisibility(View.GONE);
    					
    			mapView.getOverlays().remove(overlayLongPress);
    		
    			if(overlayLongPress.size() > 0){  

    				overlayLongPress.removeItem(0);  
            
    			}
    		}
    	
    		showAddress = false;
    		showPassenger = false;
    		
    		ibtnRoute.setImageResource(R.drawable.btn_route);
    		
    	}
    	catch(Exception e){
    		
    		e.printStackTrace();
    		
    	}
    }
    
    private TimerCallBack calbackAddress = new TimerCallBack(){

		public void onTimerOut() {
			// TODO Auto-generated method stub

			threadTimer.timerReset(timerAddress);
			
			pointAddress = parseAddress();
			
		}
    	
    	
    };

    /*
     * 
     */
    
    private void getPointAddress(Message msg){

    	Bundle bundle = msg.getData();
		
		int late6 = bundle.getInt(BUNDLE_KEY_LATE6);
		
		int longe6 = bundle.getInt(BUNDLE_KEY_LONGE6);
		
		addressPoint = new GeoPoint(late6, longe6);
		
		Message message = new Message();
		message.what = HANDLER_MSG_WHAT_SHOW_ADDRESS;
    	
		Timer timer = new Timer(1, false, calbackAddress, handler, message);
		
		threadTimer.timerQuickStart(timer);
		
    	
    }
    
    /*
     * show address popup overlay
     */
    private void showAddress(){
 

    	try {
    		
    		unlockMyLocation();

    		clearAddress();
    		
    		Drawable marker = context.getResources().getDrawable(R.drawable.bullet);
		
    		overlayLongPress = new LongPressPopupOverlay(context, mapView, marker);

    		OverlayItem item = new OverlayItem(addressPoint
        		, pointAddress
        		, "N:"+addressPoint.getLatitudeE6()+"  E:"+addressPoint.getLongitudeE6()
        		);  
        
    		overlayLongPress.getPopView().setVisibility(View.GONE);  
        
    		overlayLongPress.addItem(item);  
        
    		overlayLongPress.setFocus(item);  
        
    		mapView.getOverlays().add(overlayLongPress);  
        
    		mapView.invalidate();  
    		
    		showAddress =true;
    		
    		ibtnRoute.setImageResource(R.drawable.btn_route_address);
        
    		threadTimer.timerReset(timerAddress);
    		
		
    	}
    	catch(Exception e){
    		
    		e.printStackTrace();
    		
    	}
    }
    
    /*
     * 
     */
    
    private void getPassengerAddress(Message msg){

    	Bundle bundle = msg.getData();
		
		int late6 = bundle.getInt(BUNDLE_KEY_LATE6);
		
		int longe6 = bundle.getInt(BUNDLE_KEY_LONGE6);
		
		addressPoint = new GeoPoint(late6, longe6);
		
		passengerName = bundle.getString(BUNDLE_KEY_PASSENGER_NAME);
		passengerNumber = bundle.getString(BUNDLE_KEY_PASSENGER_NUMBER);
		
		
		Message message = new Message();
		message.what = HANDLER_MSG_WHAT_SHOW_PASSENGER;
    	
		Timer timer = new Timer(1, false, calbackAddress, handler, message);
		
		threadTimer.timerQuickStart(timer);
		
    	
    }
    
    /*
     * show address popup overlay
     */
    private void showPassenger(){
 

    	try {
    		
    		unlockMyLocation();

    		clearAddress();
    		
    		Drawable marker = context.getResources().getDrawable(R.drawable.bullet);
		
    		overlayLongPress = new LongPressPopupOverlay(context, mapView, marker);

    		OverlayItem item = new OverlayItem(addressPoint
        		, passengerName
        		, "TEL:"+passengerNumber
        		);  
        
    		overlayLongPress.getPopView().setVisibility(View.GONE);  
        
    		overlayLongPress.addItem(item);  
        
    		overlayLongPress.setFocus(item);  
        
    		mapView.getOverlays().add(overlayLongPress);  
        
    		mapView.invalidate();  
    		
    		showPassenger =true;
    		
    		ibtnRoute.setImageResource(R.drawable.btn_route_address);
        
    		threadTimer.timerReset(timerAddress);
    		
		
    	}
    	catch(Exception e){
    		
    		e.printStackTrace();
    		
    	}
    }
    /*
     * 
     */
    
    
    private void routePassengerAddress(){
    	
    	this.listPassby = null;
		Message msg = new Message();
		msg.what = ActivityMain.HANDLER_MSG_WHAT_START_ROUTE;
		
		Bundle bundle = new Bundle();
		
		bundle.putInt(ActivityMain.BUNDLE_KEY_LATE6, addressPoint.getLatitudeE6());
		bundle.putInt(ActivityMain.BUNDLE_KEY_LONGE6, addressPoint.getLongitudeE6());
		msg.setData(bundle);
		
		handler.sendMessage(msg);
		
    }
    
    
    
    /*
     * 
     */
    
    
    private void routeAddress(){
    	this.listPassby = null;
		Message msg = new Message();
		msg.what = ActivityMain.HANDLER_MSG_WHAT_START_ROUTE;
		
		OverlayItem item = overlayLongPress.getFocus();
		
		Bundle bundle = new Bundle();
		
		bundle.putInt(ActivityMain.BUNDLE_KEY_LATE6, item.getPoint().getLatitudeE6());
		bundle.putInt(ActivityMain.BUNDLE_KEY_LONGE6, item.getPoint().getLongitudeE6());
		msg.setData(bundle);
		
		handler.sendMessage(msg);

		PrefProxy.Address address = (new PrefProxy()).new Address();
		
		address.address = item.getTitle();
		
		address.late6 = item.getPoint().getLatitudeE6();
		
		address.longe6 = item.getPoint().getLongitudeE6();
		
		PrefProxy.updateRecentAddress(context, address);

    }
    
    
    /*
     * stop route update
     */
    
    private void stopRoute(){
    	
		updatingRoute = false;
		
    	if(routeOverlay!=null){
			
			routeOverlay.removeFromMap(mapView);
			
			mapView.invalidate();
			
			routeOverlay = null;
			
			
		}

    	clearRouteText();

		tvMeters.setText(resources.getString(R.string.distance_left));
		tvMinutes.setText(resources.getString(R.string.time_left));
		
		threadTimer.removeTimer(timerRoute);

    }
    
    /*
     *  get a route 
     */
    
    private void calculateRoute(){
    	
    	Log.i(TAG, "calculateRoute");

    	FromAndTo fromAndTo = new FromAndTo(myRoutePointStart, myRoutePointEnd,Route.DrivingDefault);
		
		List<Route> listRoute = null;
		routeCal = null;
		
		try {
			
			try{
			
				if(this.listPassby == null){
					Log.i(TAG,"no passby");
					listRoute = Route.calculateRoute((MapActivity) context, fromAndTo, Route.DrivingDefault);
				}
				else{
					Log.i(TAG,"cal passby:"+listPassby.size());
					Log.i(TAG,"p1:"+listPassby.get(0).getLatitudeE6()+","+listPassby.get(0).getLongitudeE6());
					listRoute = Route.calculateRoute((MapActivity) context, fromAndTo, Route.DrivingDefault, this.listPassby);
				}
			}
			catch(AMapException emap){
				
				Log.i(TAG,"cause:"+emap.getCause()+",err msg:" + emap.getErrorMessage());
				
				
			}
		
			if(listRoute.size()>0){
				
				routeCal = listRoute.get(0);
				
			}

		}
		catch(Exception e){
			
			e.printStackTrace();
			
		}

    }
    
    /*
     * 
     */
    
    private TimerCallBack callbackRoute = new TimerCallBack(){

		public void onTimerOut() {
			// TODO Auto-generated method stub
			
			calculateRoute();
			
		}
    	
    };
    
    
    /*
     *start route update
     */
    
    private void startRoute(Message msg){
    	
    	stopRoute();

		Bundle bundle = msg.getData();
		
		int late6 = bundle.getInt(BUNDLE_KEY_LATE6);
		
		int longe6 = bundle.getInt(BUNDLE_KEY_LONGE6);
		
		myRoutePointEnd = new GeoPoint(late6,longe6);

		//lockMyLocation();
 
		updatingRoute = true;
		
		threadTimer.timerQuickStart(timerRoute);
		
    }
    
    
    /*
     * update the route info
     */
    
    private void updateRoute(){

    	
    	try {
			
    	   	threadTimer.timerReset(timerRoute, TIMER_ROUTE_UPDATE);
    	    
    	   	if(routeCal != null){
				
				Route route = routeCal;
							
				if(routeOverlay!=null){
		
					routeOverlay.removeFromMap(mapView);
				
				}
				
				routeOverlay = new RouteOverlay((MapActivity) context,route);
			
				routeOverlay.registerRouteMessage((RouteMessageHandler) context);
			
				routeOverlay.addToMap(mapView);

				/*
				int km = (int)Math.floor(route.getLength()/1000);
				
				int m = route.getLength()%1000;
				
				int km_left = (int)Math.floor(m/100);
				
				String strMeters;
				
				
				if(km > 0){
					
					//strMeters = km + resources.getString(R.string.kilometers)
					//		+ m + resources.getString(R.string.meter);
					
					strMeters = km + "." + km_left + resources.getString(R.string.kilometers);
					
				
				}
				else{
					
					//strMeters = route.getLength() + resources.getString(R.string.meter);
					
					strMeters = km + "." + km_left + resources.getString(R.string.kilometers);
					
						
				}
				tvMeters.setText(strMeters);
				*/
				
				String strMeters = Utilities.MetersToString(route.getLength(), context);
				
				tvMeters.setText(strMeters);
				
				
				int steps = route.getStepCount();
				
				int i = 0;
				
				int time = 0;
				
				String HOUR = resources.getString(R.string.HOUR);
				
				String MINUTE = resources.getString(R.string.MINUTE);
				
				do{
				
					Segment segment = route.getStep(i);
					
					String strTime = segment.getConsumeTime();
					
					String strHour, strMinute;
					
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
/*				
				int hours = (int)Math.floor(time/60); 
				
				int minutes = time%60;
				
				String strMinutes;
				
				if(hours > 0){
					
					strMinutes = hours + resources.getString(R.string.hours)
							+ minutes + resources.getString(R.string.minutes);
					
					
				}
				else{
				
					 strMinutes = minutes + resources.getString(R.string.minutes);
				
				}

				tvMinutes.setText(strMinutes);
*/				

				String strMinutes = Utilities.MinutesToString(time, context);
				tvMinutes.setText(strMinutes);
				
				tvArriveTime.setText(//resources.getString(R.string.predict)+
						Utilities.StampToHHMM12(System.currentTimeMillis()+time*60*1000)
						+ resources.getString(R.string.arrive)
						);
				
				if(newRoute){
					
					Toast.makeText(context, 
							resources.getString(R.string.distance_left) 
							+ strMeters
							+ "\n"
							+ resources.getString(R.string.time_left)
							+ strMinutes, 
							Toast.LENGTH_LONG).show();
					
					showRouteText();
					
				}
				
			}

		} catch (Exception e) {
		
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
    
    }
    
    /*
     * clear route info layer
     */
    private void clearRouteText(){

    	tvMeters.setVisibility(View.INVISIBLE);
    	
    	tvMinutes.setVisibility(View.INVISIBLE);
    	
    	tvArriveTime.setVisibility(View.INVISIBLE);
    	
    }
    
    /*
     * show route info layer
     */
    
    private void showRouteText(){
    	
    	tvMeters.setVisibility(View.VISIBLE);
    	
    	tvMinutes.setVisibility(View.VISIBLE);
    	
    	tvArriveTime.setVisibility(View.VISIBLE);
    	
    }
    
    /*
     * clear buttons layer
     */
    
    private void clearButtons(){

    	layoutButton.setVisibility(View.GONE);
    	
    	layoutText.setVisibility(View.VISIBLE);
    	
    }
    
    /*
     *show buttons layer
     */
    
    private void showButtons(){
    	
    	layoutButton.setVisibility(View.VISIBLE);
    	
    	layoutText.setVisibility(View.GONE);

    	threadTimer.timerReset(timerButtons);		
    }
    

    
    /*
     * clear info layer
     */
    private void clearInfo(){
    	
    	layoutInfo.setVisibility(View.GONE);
    	
    }
    
    /*
     * show info layer
     */
    
    private void showInfo(String info){

    	
		tvInfo.setText(info);
    	
    	layoutInfo.setVisibility(View.VISIBLE);

    	threadTimer.timerReset(timerInfo);
    	
    }
    

    /*
     * screen wake lock
     */
    
    private void screenWakeLock(){

    	PowerManager powerManager = (PowerManager) this
                .getSystemService(Service.POWER_SERVICE);
        
    	wakeLock = powerManager.newWakeLock(
                PowerManager.SCREEN_DIM_WAKE_LOCK, TAG);
       	
    	wakeLock.setReferenceCounted(false);
        
    	wakeLock.acquire();
    	
    }
    
    
    /*
     * sreen wake unlock 
     */
    private void screenWakeRelease(){
    	
    	wakeLock.release();

    }
    
    /*
     * location lock
     */
    private void lockMyLocation(){
		
    	myLocationLock = true;
		
    	ibtnLocation.setImageResource(R.drawable.btn_location_locked);
 	
    	boolean filter = PrefProxy.getScreenFilter(context);
    	
    	this.ibtnLocation.setImageResource( filter ? R.drawable.btn_filter_on : R.drawable.btn_filter_off);
   
		mapController.animateTo(myPoint);

    }
  
    /*
     * location unlock
     */
    private void unlockMyLocation(){
		
    	myLocationLock = false;
		
    	ibtnLocation.setImageResource(R.drawable.btn_my_location);
    	
    }
      
    /*
     * check data connected
     * */

    private boolean isDataConnected(){
    	
    	boolean result = false;

    	ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

    	NetworkInfo activeNetworkInfo = connManager.getActiveNetworkInfo();
    	
    	if(activeNetworkInfo != null){
    		
    		result = true;
    		
    	}
    	
    	return result;
    }
    
    
    /*
     * check data connection
     */
    private void checkDataConnection(){
    	
    	ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

    	NetworkInfo activeNetworkInfo = connManager.getActiveNetworkInfo();

    	if(activeNetworkInfo == null){
			
    		showInfo(resources.getString(R.string.data_connection_lost));

		}
    	
		else{
			
			Toast.makeText(this, resources.getString(R.string.data_connection_type)
					+ activeNetworkInfo.getTypeName() + 
					" " +activeNetworkInfo.getSubtypeName(),
					Toast.LENGTH_LONG).show();
		
		}
    	
    }
    
 
    /*
     * check GPS enable
     */
    private boolean isGPSEnabled(){
    	
    	String str = Settings.Secure.getString(getContentResolver(),
    					Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
    	
    	if (str != null) {
		
    		return str.contains("gps");
	
    	}
	
    	else{
		
    		return false;
	
    	}
    }
    
    /*
     * closeGPSSuggestion()
     */
    private void closeGPSSuggesttion(){
    	
    	Toast.makeText(context, R.string.gps_close_suggestion, Toast.LENGTH_LONG).show();

    	Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS );

        startActivity(intent); 
    	
    }
    
    /*
     * switch gps setting suggetion
     */
    private void switchGPSSetting(){
    	
/*
		Intent gpsIntent = new Intent();
		
		gpsIntent.setClassName("com.android.settings",
				"com.android.settings.widget.SettingsAppWidgetProvider");
		
		gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
		
		gpsIntent.setData(Uri.parse("custom:3"));
		
		try {
		
			PendingIntent.getBroadcast(context, 0, gpsIntent, 0).send();
			
		
		}
		
		catch (CanceledException e) {
		
			e.printStackTrace();
		
		}    		
*/		

    	
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS );

        startActivityForResult(intent,0); 

    }
    
    /*
     *check gps setting
     */
    private void checkGPSSetting(){

    	if(!isGPSEnabled()){
    		
    		switchGPSSetting();
    		
    	}
    	
    }

    /*
     * register device to server
     */
    
    private void registerDevice(){
    	
    	webApi.registerDevice();
    	
    }
    
    /*
     * loging device to server
     */
    private void loginDevice(){

    	deviceLogined = false;
    	
    	reportPosition();
    	
    }
    

    /*
     * handler service info according to the service info status
     */
    
    private void handleServiceInfo(){
// query service info every time
//    	if(!WebApi.isServiceInfoReady(context)){
			
			handler.sendEmptyMessage(HANDLER_MSG_WHAT_SERVICE_INFO_QUERY);

//		}
//		else{

//			handler.sendEmptyMessage(HANDLER_MSG_WHAT_SERVICE_INFO_READY);
			
//		}
    	
    }
    
}
