/**
 * 
 */
package com.qshuttle.car;

import java.util.ArrayList;
import java.util.Iterator;

import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public class ThreadTimer extends Thread{
	
	
	private static final int TIMER_QUICK_START = 1;
	
	private ArrayList<Timer> listTimers;
	
	private int timer_sleep_seconds;
	
	private boolean running;
	
	public ThreadTimer(int seconds){
		
		listTimers = new ArrayList<Timer>();
		
		timer_sleep_seconds = seconds;
		
		running = false;
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		//super.run();
		running = true;
		
		do{
			
//			Log.i("timer", "timer:"+ listTimers.size());

			int i = 0;
			int size = listTimers.size();
			Timer timer;
			
			for( i = size-1; i >= 0; i-- ){
					
				timer = (Timer)(listTimers.get(i));
				
//				Log.i("timer", "timer:"+ Utilities.StampToString(timer.getStart()) + "@" + timer.getTimer());
				
				if(timer.isTimerOut()){
					
					if(timer.isCallBack()){
						
						timer.doCallBack();
					
					}
					
					if(timer.isHandlerMessage()){
						
						timer.sendMessage();
				
					}
					
					if(timer.isRecycle()){
						
						timer.reset();
					
					}
					else{
						
						listTimers.remove(timer);
					
					}
					
				}
				
				
			}
			
			
			
			try{
				
				sleep(timer_sleep_seconds*1000);
				
			}
			catch(Exception e){
				
				e.printStackTrace();
				
			}
			
			
		}while(running);
		
	}
	
	
	public boolean isTimerAdded(Timer timer){
		
		if(listTimers.contains(timer)){
			
			return true;
			
		}
		else{
			
			return false;
		}
		
	}
	
	public void addTimer(Timer timer){
		
		listTimers.add(timer);
		
	}
	
	public void removeTimer(Timer timer){
		
		listTimers.remove(timer);
		
		
	}
	
	public void stopRunning(){
		
		running= false;
		
		this.interrupt();
		
	}
	
    /*
     * 
     * 
     */
    
    public void timerReset(Timer timer){
   	
    	timer.reset();
    	
    	if(!this.isTimerAdded(timer)){
    		
    		this.addTimer(timer);
    		
    	}
 
    }
    
    
    public void timerReset(Timer timer, long miliseconds){
    	
    	timer.reset(miliseconds);
    	
    	if(!this.isTimerAdded(timer)){
    		
    		this.addTimer(timer);
    		
    	}

    }
    
    public void timerQuickStart(Timer timer){
    	
    	timer.reset(TIMER_QUICK_START);
    	
    	if(!this.isTimerAdded(timer)){
    		
    		this.addTimer(timer);
    		
    	}
    	
    	if(this.getState().equals(Thread.State.NEW)){
    	
    		this.start();
    	
    	}
    	else if(this.getState().equals(Thread.State.TIMED_WAITING)){
    		
    		this.interrupt();
    	
    	}
    
    }
    

}
