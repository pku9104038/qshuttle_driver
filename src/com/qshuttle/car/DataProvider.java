/**
 * 
 */
package com.qshuttle.car;



import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.util.Log;

/**
 * @author wangpeifeng
 *
 */
public class DataProvider extends ContentProvider {

	private final static String TAG = "DataProvider";
	
	private DatabaseHelper dbHelper = null;
	private final  static byte[] _dblock = new byte[0]; 


	/* (non-Javadoc)
	 * @see android.content.ContentProvider#delete(android.net.Uri, java.lang.String, java.lang.String[])
	 */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		int rows = -1;
		
		synchronized (_dblock){

			SQLiteDatabase db = dbHelper.getWritableDatabase();
			
			try{
			
				rows = db.delete(uri.getLastPathSegment(), selection, selectionArgs);
				
			}
			
			catch(SQLiteException e){
			
				e.printStackTrace();
				
			}
			finally{
				
				db.close();
			
			}
				
			
		}
		
		Log.i(TAG, "delete: " + uri.getPath() + "=>" + rows);
			
		return rows;
		
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#getType(android.net.Uri)
	 */
	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#insert(android.net.Uri, android.content.ContentValues)
	 */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		long index = -1;
		synchronized (_dblock){
				
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				try{
					index = db.insert(uri.getLastPathSegment(), null, values);
				}
				catch(SQLiteException e){
					e.printStackTrace();
				}
				finally{
					db.close();
				}
				
			
		}
		Log.i(TAG, "insert: " + uri.getPath() + "=>" +index);
		
		if(index==-1)
			
			return null;
		
		else{

			return uri.buildUpon().appendEncodedPath("/"+index).build();
			
		}
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#onCreate()
	 */
	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		
		String dbName = DatabaseHelper.DB_DATABASE;
		int dbVersion = DatabaseHelper.DB_VERSION;
		CursorFactory factory = null;
		dbHelper = new DatabaseHelper(getContext(),dbName,factory,dbVersion);
		
		return true;
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#query(android.net.Uri, java.lang.String[], java.lang.String, java.lang.String[], java.lang.String)
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub

		String limit = uri.getQuery();
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String groupBy = null, having = null;
		Cursor cursor = null;
		try{

			//Log.i(TAG, "query: "+uri.getPath());
			cursor = db.query(uri.getLastPathSegment(), projection, selection, selectionArgs, groupBy, having, sortOrder,limit);
			
		}
		catch(SQLiteException e){
			e.printStackTrace();
		}

		return cursor;
		
	}

	/* (non-Javadoc)
	 * @see android.content.ContentProvider#update(android.net.Uri, android.content.ContentValues, java.lang.String, java.lang.String[])
	 */
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		int rows = -1;
		synchronized (_dblock){
			
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			try{
				rows = db.update(uri.getLastPathSegment(), values, selection, selectionArgs);
			}
			catch(SQLiteException e){
				e.printStackTrace();
			}
			finally{
				db.close();
			}
			
		}
		Log.i(TAG, "update: " + uri.getPath() + " => "+rows);
		
		return rows;
	}

}
