/**
 * 
 */
package com.qshuttle.car;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public class ThreadHttpPost extends Thread {
	
	private static final String TAG = "ThreadHttpPost";
	
	private boolean running;
	private String url;
	private ArrayList<NameValuePair> params;
	
	private WebApi webApi;
	
	private String response;

	private int caller_id;
	
	public ThreadHttpPost(String url,
			ArrayList<NameValuePair> params, WebApi webApi, int caller_id) {
		super();
		this.running = running;
		this.url = url;
		this.params = params;
		this.webApi = webApi;
		this.caller_id = caller_id;
		
		response = null;
	}

	public boolean isRunning(){
		return this.running;
	}
	
	public void stopRunning(){
		running = false;
	}
	
	public String getResponse(){
		return this.response;
	}
	

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		
		running = true;
		
		DefaultHttpClient httpClient = new DefaultHttpClient();
	    HttpPost httpPost = new HttpPost(this.url);
	          		
	    try {
	    
	    	httpPost.setEntity(new UrlEncodedFormEntity(this.params,HTTP.UTF_8));
	    	
	    	Log.i(TAG, "post = "+this.url);
	    	
	    	HttpResponse httpResp = httpClient.execute(httpPost);
	    	
			HttpEntity httpEntity = httpResp.getEntity();

		    if(httpEntity != null){
				BufferedReader br = new BufferedReader(new InputStreamReader(httpEntity.getContent()));
				String line = null;
				response = new String();
				while((line = br.readLine()) != null){
					response += line;
				}
		    	Log.i(TAG, "resp = "+response);
			}
		    
	    }catch (Exception e) {
	    	// TODO Auto-generated catch block
	    	e.printStackTrace();
	    	Log.i(TAG, "cause: "+ e.getCause()+",msg:"+e.getMessage()+",locMsg:"+e.getLocalizedMessage());
			
		}
		
		running = false;
		
		try{
			
			webApi.checkResponse(response, caller_id);
		
		}
		catch(NullPointerException e1){
			
			e1.printStackTrace();
		
		}
		
	}
	
	

}
