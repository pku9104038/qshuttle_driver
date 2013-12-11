/**
 * 
 */
package com.qshuttle.car;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.core.OverlayItem;
import com.amap.mapapi.map.ItemizedOverlay;
import com.amap.mapapi.map.MapView;



/**
 * @author wangpeifeng
 *
 */
public class GPSLocationOverlay extends ItemizedOverlay {
	
	private ArrayList<OverlayItem> listItems;
	
	private OverlayItem item;
	
	private Context context;

	private Drawable marker;
	
	private Location location;
	
	private GeoPoint point;
	
	public GPSLocationOverlay(Context context, Drawable marker, Location location) {
		super(marker);
		// TODO Auto-generated constructor stub
		
		this.context = context;
		
		this.marker = marker;
		
		this.location = location;
		
		point = new GeoPoint((int)(location.getLatitude()*1E6), (int)(location.getLongitude()*1E6));
		
		item = new OverlayItem(point, location.getProvider(), location.getAccuracy() + context.getResources().getString(R.string.meter));
		
		listItems = new ArrayList<OverlayItem>();
		
		listItems.add(item);
		
		populate();
		
		
	}

	/* (non-Javadoc)
	 * @see com.amap.mapapi.map.ItemizedOverlay#createItem(int)
	 */
	@Override
	protected OverlayItem createItem(int index) {
		// TODO Auto-generated method stub
		return listItems.get(index);
	}

	/* (non-Javadoc)
	 * @see com.amap.mapapi.map.ItemizedOverlay#size()
	 */
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return listItems.size();
	}
	
	
	public void onLocationChanged(Location location){
		
		this.location = location;
		
		point = new GeoPoint((int)(location.getLatitude()*1E6), (int)(location.getLongitude()*1E6));
		
		item = new OverlayItem(point, location.getProvider(), location.getAccuracy() + context.getResources().getString(R.string.meter) );
		
		listItems.remove(0);
		
		listItems.add(item);
		
	}

	/* (non-Javadoc)
	 * @see com.amap.mapapi.map.ItemizedOverlay#onTap(int)
	 */
	@Override
	protected boolean onTap(int arg0) {
		// TODO Auto-generated method stub
		
		Toast.makeText(context, listItems.get(arg0).getTitle() + ":"+ listItems.get(arg0).getSnippet(), Toast.LENGTH_LONG).show();
		
		return super.onTap(arg0);
		
	}

	/* (non-Javadoc)
	 * @see com.amap.mapapi.map.ItemizedOverlay#draw(android.graphics.Canvas, com.amap.mapapi.map.MapView, boolean)
	 */
	@Override
	public void draw(Canvas canvas, MapView mapview, boolean shadow) {
		// TODO Auto-generated method stub
//		super.draw(canvas, mapview, shadow);
		
		Point out = new Point();
		
		Bitmap bitmap = null;
		
		Paint paint = new Paint();
		
		Matrix matrix; 
		
		float scale;
		
		mapview.getProjection().toPixels(point, out);
		
		bitmap = ((BitmapDrawable)marker).getBitmap() ;
				
		matrix = new Matrix();
		
		scale = (float)1;
		
		matrix.postTranslate(-bitmap.getWidth()*scale/2, -bitmap.getHeight()*scale/2);
		
//		scale = (float) 0.5;
		
		matrix.postScale(scale, scale);
					
		matrix.postRotate(location.getBearing());
	
		matrix.postTranslate(out.x, out.y);
					
		canvas.drawBitmap(bitmap, matrix, paint);
		
		
		//int mapscale = mapview.getScale(mapview.getZoomLevel());
		
		double mapscale = mapview.getMetersPerPixel(mapview.getZoomLevel());
		
		float radius = (float)(location.getAccuracy()/mapscale);
		
		if(radius > bitmap.getWidth()/2){
		
			paint = new Paint();
		
			int color = context.getResources().getColor(R.color.transparent_accuracy);
		
			paint.setColor(color);
		
			paint.setStyle(Style.FILL);
		
			paint.setStrokeCap(Cap.ROUND);
		
			canvas.drawCircle(out.x, out.y, radius, paint);

			color = context.getResources().getColor(R.color.circle_accuracy);
			
			paint.setColor(color);
		
			paint.setStyle(Style.STROKE);
		
//			paint.setStrokeWidth(2);
			
			paint.setStrokeCap(Cap.ROUND);
		
			canvas.drawCircle(out.x, out.y, radius, paint);
		
		}
	}

}
