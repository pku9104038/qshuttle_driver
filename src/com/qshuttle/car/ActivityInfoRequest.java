/**
 * 
 */
package com.qshuttle.car;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;

/**
 * @author wangpeifeng
 *
 */
public class ActivityInfoRequest extends Activity {
	
	
	private static final String TAG = "ActivityInfoRequest";
	
	private static final int MSG_WHAT_UPDATE_PICKUP = 1;
	
	private Context context;
	
	private WakeLock wakeLock;
	
	private ExpandableListView listView;
	
	private ExpandableListAdapter listAdapter;	
	
	private ImageButton ibtnBack;
	
	private ImageView ivTitle;
	
	private static JSONArray arrayServiceInfo;

	private String info; 
	
	private int activity;
	
	private Intent intentResult;
	
	private int resultCode = Activity.RESULT_CANCELED;
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		context = this;
		
		Intent intent = getIntent();
		
		info = intent.getStringExtra(WebApi.API_JSON_SHUTTLE_ARRAY);

		activity = intent.getIntExtra(ActivityMain.INTENT_EXTRA_KEY_ACTIVITY, ActivityMain.ACTIVITY_REQUEST_PICKUP);
		
		intentResult = new Intent();
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		
		setTheme(R.style.Transparent);

		setContentView(R.layout.activity_inforequest);
		
		if(infoToJsonArray(info)){
	
			Resources resources = getResources();
			
			ivTitle = (ImageView)findViewById(R.id.imageViewTitle);
			
			String title = resources.getString(R.string.app_name) + resources.getString(R.string.from_to);
					
			switch(activity){
			
			case ActivityMain.ACTIVITY_REQUEST_CALL:
				
				title += resources.getString(R.string.passenger_call);
				
				ivTitle.setImageResource(R.drawable.btn_call);
				
				break;
				
			case ActivityMain.ACTIVITY_REQUEST_PICKUP:
				
				title += resources.getString(R.string.passenger_pickup);
				
				ivTitle.setImageResource(R.drawable.btn_pickup);
				
				break;
				
			case ActivityMain.ACTIVITY_REQUEST_ROUTE:
				
				title += resources.getString(R.string.passenger_route);
				
				ivTitle.setImageResource(R.drawable.btn_route);
				
				break;
				
			case ActivityMain.ACTIVITY_REQUEST_SCHEDULE:
				
				title += resources.getString(R.string.passenger_schedule);
				
				ivTitle.setImageResource(R.drawable.btn_schedule);
				
				break;
			}
		
			((TextView)findViewById(R.id.textViewTitle)).setText(title);
			
			listAdapter = new ExpandableListAdapter(context, arrayServiceInfo, activity);
		
			listView = (ExpandableListView)findViewById(R.id.expandableListView);
			
			listView.setAdapter(listAdapter);
		
			listView.setOnChildClickListener(onChildClickListener);
			
			ibtnBack = (ImageButton)findViewById(R.id.imageButtonBack);
			ibtnBack.setOnClickListener(lsrImageButton);
			
			
		}
		else{
			
			Toast.makeText(context, R.string.shuttle_ticket_null , Toast.LENGTH_LONG).show();
			
			finish();
			
		}
		
		screenWakeLock();
	}
	
	
 
	
	
    	
	/* (non-Javadoc)
	 * @see android.app.Activity#finish()
	 */
	@Override
	public void finish() {
		// TODO Auto-generated method stub

		screenWakeRelease();
		
		setResult(resultCode, intentResult);

		super.finish();
	}






	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		
		
		super.onDestroy();
	}







	/*
	 * 监听/响应 点击子列表项动作
	 */
	public OnChildClickListener onChildClickListener = new OnChildClickListener(){

		
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			// TODO Auto-generated method stub


			switch(activity){
			
			case ActivityMain.ACTIVITY_REQUEST_PICKUP:

				listAdapter.toggleChildPickup(groupPosition, childPosition);
				
				listAdapter.notifyDataSetChanged();

				listView.invalidate();

				int ticket_serial = listAdapter.getChildTicketSerial(groupPosition, childPosition);
				
				String pickup = listAdapter.getChildPickup(groupPosition, childPosition);
				
				Message msg = new Message();
				
				msg.what = ActivityMain.HANDLER_MSG_WHAT_PASSENGER_PICKUP_REPORT;
				
				Bundle bundle = new Bundle();
				
				bundle.putInt(DatabaseHelper.DB_COLUMN_TICKET_SERIAL, ticket_serial);
				
				bundle.putString(DatabaseHelper.DB_COLUMN_TICKET_SERVE_CONFIRM, pickup);
				
				msg.setData(bundle);
				
				ActivityMain.handlerRef.sendMessage(msg);
				
				break;
				
			case ActivityMain.ACTIVITY_REQUEST_ROUTE:
			
				intentResult.putExtra(ActivityMain.INTENT_EXTRA_KEY_ACTIVITY_ROUTE, true);
				
				int late6 = listAdapter.getChildLate6(groupPosition, childPosition);
				int longe6 = listAdapter.getChildLonge6(groupPosition, childPosition);
				
				intentResult.putExtra(ActivityMain.INTENT_EXTRA_KEY_LATE6, late6);
				intentResult.putExtra(ActivityMain.INTENT_EXTRA_KEY_LONGE6, longe6);
				
				resultCode = Activity.RESULT_OK;

				finish();

				break;
				
			case ActivityMain.ACTIVITY_REQUEST_CALL:

				intentResult.putExtra(ActivityMain.INTENT_EXTRA_KEY_ACTIVITY_CALL, true);
				
				String number = listAdapter.getChildNumber(groupPosition, childPosition);
				
				intentResult.putExtra(ActivityMain.INTENT_EXTRA_KEY_NUMBER, number);
				
				resultCode = Activity.RESULT_OK;

				Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:" + number));
				
				startActivityForResult(intent,1);
				
//				finish();
				
				break;
			
			case ActivityMain.ACTIVITY_REQUEST_SCHEDULE:

				
				try {

					JSONObject obj = (JSONObject)listAdapter.getGroup(groupPosition);
					
					int instance_serial = obj.getInt(DatabaseHelper.DB_COLUMN_INSTANCE_SERIAL);
					
					intentResult.putExtra(WebApi.API_JSON_SHUTTLE_ARRAY, obj.toString());		
					
					intentResult.putExtra(DatabaseHelper.DB_COLUMN_INSTANCE_SERIAL, instance_serial);		
					
					resultCode = Activity.RESULT_OK;
				
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
					resultCode = Activity.RESULT_CANCELED;
				}
				
				
				finish();
				
				break;
			
			
			
			}
			
			return false;
		}
		
	};
	
	

	private OnClickListener lsrImageButton = new OnClickListener(){

		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			
			case R.id.imageButtonBack:

				resultCode = Activity.RESULT_CANCELED;
				
				finish();
				
				break;
			}
			
		}
		
		
	};
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		//return super.onKeyDown(keyCode, event);
		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:

			// avoid miss operation after call out
//			resultCode = Activity.RESULT_CANCELED;
			
//			finish();
			
			return true;
			
		}
		return super.onKeyDown(keyCode, event);
	}
	

	/***************************************************************
	 * private 方法
	 * 
	 ***************************************************************/

	/*
	 * 
	 */
	private Handler handler = new Handler(){

		/* (non-Javadoc)
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			//super.handleMessage(msg);
			switch(msg.what){
			
			case MSG_WHAT_UPDATE_PICKUP:

				listAdapter.notifyDataSetChanged();
				listView.invalidate();

				Bundle bundle = msg.getData();
				int ticket_serial = bundle.getInt(DatabaseHelper.DB_COLUMN_TICKET_SERIAL);
				String pickup = bundle.getString(DatabaseHelper.DB_COLUMN_TICKET_SERVE_CONFIRM);
				
				WebApi webApi = new WebApi(context);
				
				webApi.reportPickup(ticket_serial, pickup);
				
				break;
			}
		}
		
	};
	
	
	/*
	 * 屏幕长亮锁定
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
     * 屏幕长亮释放
     */
    private void screenWakeRelease(){
    	
    	wakeLock.release();

    }
	

	public static boolean infoToJsonArray(String info) {
		
		boolean result = false;
		
		try {
			
			JSONObject obj = new JSONObject(info);
		
			if(obj.getBoolean(WebApi.API_RESP_SUCCESS)){
			
				obj = obj.getJSONObject(WebApi.API_JSON_SHUTTLE_ARRAY);
				
				JSONArray array = obj.getJSONArray(WebApi.API_JSON_SHUTTLE_ARRAY);
				
				if(array!=null){
				
					arrayServiceInfo  = array;
					
					result = true;
				}
			
			}
				
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

				
		return result;

	}
	
	
	
	


}
