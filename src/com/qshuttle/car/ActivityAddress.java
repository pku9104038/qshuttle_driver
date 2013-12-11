package com.qshuttle.car;

import java.util.ArrayList;

import com.qshuttle.car.PrefProxy.Address;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class ActivityAddress extends Activity {
	
	
	private ListView listView;
	
	private ListAdapterAddress listAdapter;
	
	
	private ArrayList<Address> listAddress;
	
	int resultCode ;
	
	Intent intentResult;
	
	
	

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Context context = getApplicationContext();
		
		listAddress = PrefProxy.getRecentAddressList(context);
		
		if(listAddress != null && listAddress.size() > 0){
		
			setContentView(R.layout.addresslist);
		
			listView = (ListView)findViewById(R.id.listView);
		
			listAdapter = new ListAdapterAddress(listAddress, context);
		
			listView.setAdapter(listAdapter);
		
			listView.setOnItemClickListener(listener);
		
			((ImageButton)findViewById(R.id.imageButtonBack)).setOnClickListener(lsrButton);
		
		
			resultCode = Activity.RESULT_CANCELED;
		
			intentResult = new Intent();
				
		}
		else{
			
			Toast.makeText(context, R.string.address_list_null, Toast.LENGTH_LONG).show();
			
			finish();
			
		}

	}
	
	public OnClickListener lsrButton = new OnClickListener(){

		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			finish();
			
		}
		
	};
	
	
	public  OnItemClickListener listener = new OnItemClickListener(){

		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub

			
			Address address = listAddress.get(position);
			
			int late6 = address.late6;
			int longe6 = address.longe6;
			
			intentResult.putExtra(ActivityMain.INTENT_EXTRA_KEY_LATE6, late6);
			intentResult.putExtra(ActivityMain.INTENT_EXTRA_KEY_LONGE6, longe6);
			
			resultCode = Activity.RESULT_OK;
			
			Log.i("address", address.address + ":"+address.late6 + "," +address.longe6);
			
			setResult(resultCode, intentResult);

			finish();

			
		}
		
		
		
		
		
	};

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		super.onDestroy();


	}
	
	

}
