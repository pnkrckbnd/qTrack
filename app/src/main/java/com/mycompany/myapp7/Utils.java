package com.mycompany.myapp7;
import android.content.*;
import android.database.*;
import android.provider.*;
import android.widget.*;
import java.util.concurrent.*;

public class Utils
{
	private static Utils instance;
	
	public static Utils Instance() {
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
			MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
			null,
			selection,
			null,
			null
		);
		return c;
	}
	
	public Cursor getDiscCursor(String selection){
		Cursor c = context.getContentResolver().query(
			MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
			null,
			selection,
			null,
			null
		);
		return c;
	}
	
	public Cursor getArtistCursor(String selection){
		Cursor c = context.getContentResolver().query(
			MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
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
	
	public String getTimeFormatted(long timeInMs){
		
		long hour = TimeUnit.MILLISECONDS.toHours(timeInMs);
		long min = TimeUnit.MILLISECONDS.toMinutes(timeInMs);
		long sec = TimeUnit.MILLISECONDS.toSeconds(timeInMs);
		
		StringBuilder sb = new StringBuilder();
		String h = String.valueOf(hour);
		if(hour > 0)
			sb.append(h).append(":");
		
		String m = String.valueOf(min);
		if(min < 10 && hour > 0)
			sb.append("0");
		sb.append(m).append(":");
		
		String s = String.valueOf(sec);
		if(sec < 10){
			sb.append("0");
		}
		sb.append(s.substring(0,2));
		return new String(sb.toString());
	}
}
