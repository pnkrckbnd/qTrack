package com.mycompany.myapp7;
import android.content.*;
import android.database.*;
import android.provider.*;
import android.widget.*;

public class Utils
{
	private static Utils instance;
	
	public static Utils Instance() throws Exception{
		if(instance == null) {
			instance = new Utils();
		}
		
			
		return instance;
	}
	
	private Utils(){
	}
	
	private Context context;
	private boolean isInit = false;
	
	public void init(Context c){
		context = c;
		isInit = true;
	}
	
	public Cursor getTrackCursor(String selection) throws Exception{
		Cursor c = context.getContentResolver().query(
			MediaStore.Audio.Media.INTERNAL_CONTENT_URI,
			null,
			selection,
			null,
			null
		);
		return c;
	}
	
	public Cursor getDiscCursor(String selection){
		Cursor c = context.getContentResolver().query(
			MediaStore.Audio.Albums.INTERNAL_CONTENT_URI,
			null,
			selection,
			null,
			null
		);
		return c;
	}
	
	public Cursor getArtistCursor(String selection){
		Cursor c = context.getContentResolver().query(
			MediaStore.Audio.Artists.INTERNAL_CONTENT_URI,
			null,
			selection,
			null,
			null
		);
		return c;
	}
	
	public void ToastLong(String s) {
		if(!instance.isInit) {return;}
		Toast.makeText(context, s, Toast.LENGTH_LONG).show();
	}
	
	public void ToastShort(String s) {
		if(!instance.isInit) {return;}
		Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
	}
}
