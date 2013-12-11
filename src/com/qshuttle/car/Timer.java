/**
 * 
 */
package com.qshuttle.car;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public class Timer {
	
	private long timer_start;
	
	private long miliseconds;
	
	private boolean recycle;
	
	private TimerCallBack callback;
	
	private Message msg , msg_buffer;
	
	private Handler handler;
	
	
	public Timer(long miliseconds, boolean recycle, TimerCallBack callback, Handler handler, Message msg){
		
		timer_start = System.currentTimeMillis();
		
		this.miliseconds = miliseconds;
		
		this.recycle = recycle;
		
		this.callback = callback;
		
		this.handler = handler;
		
		this.msg = msg;
		
		this.bufferMsg();
		
		
	}
	
	public Timer(long miliseconds, boolean recycle, TimerCallBack callback, Handler handler, int msg_what){
		
		timer_start = System.currentTimeMillis();
		
		this.miliseconds = miliseconds;
		
		this.recycle = recycle;
		
		this.callback = callback;
		
		this.handler = handler;
		
		this.msg = new Message();
		
		this.msg.what = msg_what;
		
		this.bufferMsg();
		
		
	}
	
	public Timer(long miliseconds, boolean recycle, TimerCallBack callback){
		
		timer_start = System.currentTimeMillis();
		
		this.miliseconds = miliseconds;
		
		this.recycle = recycle;
		
		this.callback = callback;

		this.handler = null;
		
		this.msg = null;
		
		this.bufferMsg();
		
	}

	public Timer(long miliseconds, boolean recycle, Handler handler, Message msg){
		
		timer_start = System.currentTimeMillis();
		
		this.miliseconds = miliseconds;
		
		this.recycle = recycle;
		
		this.callback = null;
		
		this.handler = handler;
		
		this.msg = msg;
		
		this.bufferMsg();
		
	}

	public Timer(long miliseconds, boolean recycle, Handler handler, int msg_what){
		
		timer_start = System.currentTimeMillis();
		
		this.miliseconds = miliseconds;
		
		this.recycle = recycle;
		
		this.callback = null;
		
		this.handler = handler;
		
		this.msg = new Message();
		
		this.msg.what = msg_what;
		
		this.bufferMsg();
		
	}
	
	public void registerCallBack(TimerCallBack callback){
		
		this.callback = callback;
	
	}

	public void removeCallBcak(){
		
		this.callback = null;
		
	}
	
	public void registerHandlerMessage(Handler handler, Message msg){
		
		this.handler = handler;
		
		this.msg = msg;
		
	}
	
	public void removeHandlerMessage(){
		
		this.handler = null;
		
		this.msg = null;
		
	}
	
	public void setTimer(long miliseconds){
		
		this.miliseconds = miliseconds;
		
	}
	
	public boolean isRecycle(){
		
		return recycle;
		
	}
	
	public void reset(){
		
		this.timer_start = System.currentTimeMillis();
		
		this.restoreMsg();
		
	}
	
	public void reset(long miliseconds){
		
		this.timer_start = System.currentTimeMillis();
		
		this.miliseconds = miliseconds;
		
		this.restoreMsg();
	}
	
	
	public boolean isTimerOut(){
		
		if(System.currentTimeMillis() - timer_start > miliseconds){
			
			return true;
		
		}
		else {
			
			return false;
		}
	}
	
	public boolean isHandlerMessage(){
		
		if(handler != null && msg != null){

//			Log.i("timer", "hadler msg true");
			
			return true;
			
		}
		else{
			
//			Log.i("timer", "hadler msg true");
			
			return false;
			
		}
		
	}
	
	public void sendMessage(){
		
		try{

//			Log.i("timer", "msg:" + msg.what);

			handler.sendMessage(msg);
		
		}
		catch(Exception e){
			
			e.printStackTrace();
			
		}
	}
	
	public boolean isCallBack(){
		
		if(callback != null){
			
			return true;
			
		}
		else{
			
			return false;
			
		}
	}
	
	public void doCallBack(){
		
		try{
		
			callback.onTimerOut();
		
		}
		catch(Exception e){
			
			e.printStackTrace();
			
		}
	}
	
	public long getStart(){
		
		return timer_start;
		
	}
	
	public int getTimer(){
		
		return (int)(miliseconds/1000);
		
	}
	
	
	private void bufferMsg(){
		
		if(msg == null){
			
			this.msg_buffer = msg;
			
		}
		else{
			
			this.msg_buffer = new Message();
			
			this.msg_buffer.copyFrom(msg);
		}
	}
	
	
	private void restoreMsg(){
		
		if(this.msg_buffer == null){
			
			msg =null;
		
		}
		else{
			
			msg = new Message();
			
			msg.copyFrom(msg_buffer);
		
		}
	}
	
}
