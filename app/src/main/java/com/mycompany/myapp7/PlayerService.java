package com.mycompany.myapp7;
import android.app.*;
import android.content.*;
import android.media.*;
import android.os.*;
import java.util.*;
import android.net.*;
import android.provider.*;
import java.io.*;

public class PlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener
{
	private final IBinder playerBinder = new PlayerBinder();
	
	MediaPlayer player;
	long[] trackList;
	int index = 0;
	boolean isStopped = false;
	boolean isStarted = false;
	
	
	public void setTrackIdList(long[] trackIdList){
		this.trackList = trackIdList;
	}
	
	public void setIndex(int index){
		this.index = index;
	}
	
	
	@Override
	public void onCreate(){
		initPlayer();
		super.onCreate();
	}

	private void initPlayer(){
		player = new MediaPlayer();
		player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
		player.setAudioStreamType(AudioManager.STREAM_MUSIC);
		player.setOnPreparedListener(this);
		player.setOnErrorListener(this);
		player.setOnCompletionListener(this);
	}

	@Override
	public void onStart(Intent intent, int startId){
		isStarted = true;
		super.onStart(intent, startId);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		if(intent.getAction().equals(Constants.ACTION_PLAYER_BIND)){
			return super.onStartCommand(intent, flags, startId);
		}
		if(intent.getAction().equals(Constants.ACTION_PLAYER_NEW_INDEX)){
			index = intent.getIntExtra(Constants.EXTRA_PLAYER_TRACK_INDEX, 0);
		}
		if(intent.getAction().equals(Constants.ACTION_PLAYER_NEW_LIST)){
			trackList = intent.getLongArrayExtra(Constants.EXTRA_PLAYER_TRACK_LIST);
			index = intent.getIntExtra(Constants.EXTRA_PLAYER_TRACK_INDEX, 0);
		}
		stop();
		reset();
		prepare();
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	Timer t;
	void prepare() {
		long trackId = trackList[index];
		//if(isStopped || !isInitialized){
			//isStopped = false;
		//}
		Uri uri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, String.valueOf(trackId));
		try{
			player.setDataSource(getApplicationContext(), uri);
			player.prepareAsync();
			//isInitialized = true;
		}catch(Exception e){e.printStackTrace();}
	}
	
	
	public void stop() {
		//if(!isStopped && isInitialized){
			player.stop();
			//isStopped = true;
		//}
	}

	public void play() {
		if(!player.isPlaying()){ //&& !isStopped && isInitialized){
			player.start();
		}
	}

	public void pause() {
		if(player.isPlaying()){ //&& !isStopped && isInitialized) {
			player.pause();
		}
	}
	
	public void reset() {
		player.reset();
		//isInitialized = false;
	}
	
	@Override
	public void onDestroy(){
		if(player != null){
			player.release();
			player = null;
		}
		super.onDestroy();
	}
	
	@Override
	public void onCompletion(MediaPlayer p1){
		index++;
		if(index >= trackList.length){
			// add repeat enum and hold reference to
			// current repeat type
			
			//switch(repeatType) {
			//	case RepeatType.ALL:
			//	...
			//}
			index = 0;
		}
		prepare();
	}

	@Override
	public boolean onError(MediaPlayer mp, int p2, int p3){
		initPlayer();
		return true;
	}

	@Override
	public void onPrepared(MediaPlayer mp){
		mp.start();
	}
	
	@Override
	public IBinder onBind(Intent p1){
		return playerBinder;
	}
	
	
	public class PlayerBinder extends Binder{
		PlayerService getService() {
			return PlayerService.this;
		}
	}
}
