/**
 * 
 */
package com.qshuttle.car;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * @author wangpeifeng
 *
 */
public class HostSettingActivity extends Activity {
	
    /////////////////////////////////////////////////
    // PROPERTIES, PUBLIC
    /////////////////////////////////////////////////
	
	/////////////////////////////////////////////////
    // PROPERTIES, PROTECTED
    /////////////////////////////////////////////////

    /////////////////////////////////////////////////
    // PROPERTIES, PRIVATE
    /////////////////////////////////////////////////
    
	private Context context;
	
	private EditText 		etHost,
							etPort,
							etSocketPort;
	
	
	private Button			btnSubmit;
	
    /////////////////////////////////////////////////
    // CONSTANTS
    /////////////////////////////////////////////////
	
    /////////////////////////////////////////////////
    // METHODS
    /////////////////////////////////////////////////
	

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		context = this;
		
		setContentView(R.layout.network_setting);
		
		etHost = (EditText)findViewById(R.id.editTextHost);
		etHost.setText(PrefProxy.getHost(context));
		
		etPort = (EditText)findViewById(R.id.editTextPort);
		etPort.setText(PrefProxy.getPort(context));
		
		etSocketPort = (EditText)findViewById(R.id.editTextSocketPort);
//		etSocketPort.setText(""+PrefProxy.getSocketPort(context));
		
		btnSubmit = (Button)findViewById(R.id.buttonSubmit);
		btnSubmit.setOnClickListener(lsrButton);
	}
	
	private OnClickListener lsrButton = new OnClickListener(){

		public void onClick(View v) {
			// TODO Auto-generated method stub
			PrefProxy.setHost(context, etHost.getText().toString());
			PrefProxy.setPort(context, etPort.getText().toString());
//			PrefProxy.setSocketPort(context, Integer.parseInt(etSocketPort.getText().toString()));
			
			finish();
			
		}
		
	};
	
	

}
