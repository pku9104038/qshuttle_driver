/**
 * 
 */
package com.qshuttle.car;


import com.amap.mapapi.map.MapView;
import com.amap.mapapi.map.Overlay;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;


/**
 * @author wangpeifeng
 *
 */
public class MapGestureDetectorOverlay extends Overlay implements
		OnGestureListener {

	private GestureDetector gestureDetector;
	private OnGestureListener onGestureListener;

	 
	public MapGestureDetectorOverlay() {
		gestureDetector = new GestureDetector(this);
	}

	 
	public MapGestureDetectorOverlay(OnGestureListener onGestureListener) {
		this();
		setOnGestureListener(onGestureListener);
	}


	public boolean onTouchEvent(MotionEvent event, MapView mapView) {
		if (gestureDetector.onTouchEvent(event)) {
			return true;
		}
		return false;
	}

	public boolean onDown(MotionEvent e) {
		if (onGestureListener != null) {
			return onGestureListener.onDown(e);
		}
		return false;
	}


	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		if (onGestureListener != null) {
			return onGestureListener.onFling(e1, e2, velocityX, velocityY);
		}
		return false;
	}

	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		if (onGestureListener != null) {
			onGestureListener.onLongPress(e);
		}
		
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		if (onGestureListener != null) {
			onGestureListener.onScroll(e1, e2, distanceX, distanceY);
		}
		return false;

	}

	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		if (onGestureListener != null) {
			onGestureListener.onShowPress(e);
		}
	
	}

	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		if (onGestureListener != null) {
			onGestureListener.onSingleTapUp(e);
		}
		return false;

	}

	public boolean isLongpressEnabled() {
		return gestureDetector.isLongpressEnabled();
	}

	
	public void setIsLongpressEnabled(boolean isLongpressEnabled) {
		gestureDetector.setIsLongpressEnabled(isLongpressEnabled);
	}

	
	public OnGestureListener getOnGestureListener() {
		return onGestureListener;
	}

		 
	public void setOnGestureListener(OnGestureListener onGestureListener) {
		this.onGestureListener = onGestureListener;
	}


}
