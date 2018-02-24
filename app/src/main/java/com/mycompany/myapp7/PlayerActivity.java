package com.mycompany.myapp7;
import android.support.v7.app.*;
import android.widget.*;
import android.os.*;
import android.content.*;
import java.util.*;
import android.database.*;
import android.provider.*;
import java.io.*;
import com.squareup.picasso.*;
import android.net.*;
import android.graphics.*;
import com.squareup.picasso.Picasso.*;
import android.graphics.drawable.*;
import android.view.*;

public class PlayerActivity extends AppCompatActivity
{
	PlayerService playerService;
	Intent playIntent;
	boolean isBound = false;
	boolean play;
	
	List<Track> trackList = new ArrayList<Track>();
	int trackIndex;
	Track curTrack;

	TextView tvArtist, tvTitle, tvAlbum, tvSeekTime, tvSeekDuration;
	ImageView ivAlbumArt;
	SeekBar seekBar;
	ImageButton btnShuffle, btnPrev, btnNext, btnRepeat;
	ToggleButton btnPlayPause;
	LinearLayout layoutMain;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);
		
		if(savedInstanceState != null) {
			isBound = savedInstanceState.getBoolean(Constants.EXTRA_IS_BOUND, false);
			long[] ids = savedInstanceState.getLongArray(Constants.EXTRA_PLAYER_TRACK_LIST);
			if(ids != null){
				trackList = getTracksFromIds(ids);
			}
			trackIndex = savedInstanceState.getInt(Constants.EXTRA_PLAYER_TRACK_INDEX, 0);
		}
		else {
			long[] ids = getIntent().getExtras().getLongArray(Constants.EXTRA_PLAYER_TRACK_LIST);
			if(ids != null){
				trackList = getTracksFromIds(ids);
			}
			trackIndex = getIntent().getIntExtra(Constants.EXTRA_PLAYER_TRACK_INDEX, 0);
		}
		initViews();
	}
	
	@Override
	protected void onStart(){
		super.onStart();
		Utils.Instance().ToastShort("onStart() - PlayerActivity");
		if(isBound == false) {
			playIntent = new Intent(this, PlayerService.class);
			playIntent.setAction(getIntent().getAction());
			playIntent.putExtra(Constants.EXTRA_PLAYER_TRACK_LIST, getIntent().getLongArrayExtra(Constants.EXTRA_PLAYER_TRACK_LIST));
			playIntent.putExtra(Constants.EXTRA_PLAYER_TRACK_INDEX, getIntent().getIntExtra(Constants.EXTRA_PLAYER_TRACK_INDEX, 0));
			bindService(playIntent, playerConnection, BIND_AUTO_CREATE);
			startService(playIntent);
		}
	}

	@Override
	protected void onStop(){
		super.onStop();
		if(isBound) {
			unbindService(playerConnection);
			isBound = false;
		}
		Utils.Instance().ToastShort("onStop() - PlayerActivity");
		Utils.Instance().ToastShort("Is Bound?\n" + (isBound == true) +"");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState){
		outState.putBoolean(Constants.EXTRA_IS_BOUND, isBound);
		long[] trackListArray = new long[trackList.size()]; 
		Long[] list = trackList.toArray(new Long[trackList.size()]);
		int idx = 0;
		for(long t : list) {
			trackListArray[idx] = t;
			idx++;
		}
		outState.putLongArray(Constants.EXTRA_PLAYER_TRACK_LIST, trackListArray);
		outState.putInt(Constants.EXTRA_PLAYER_TRACK_INDEX, trackIndex);
		super.onSaveInstanceState(outState);
	}
	
	ServiceConnection playerConnection = new ServiceConnection(){

		@Override
		public void onServiceConnected(ComponentName name, IBinder service){
			PlayerService.PlayerBinder binder = (PlayerService.PlayerBinder)service;
			playerService = binder.getService();
			isBound = true;
			Utils.Instance().ToastShort("Connected\n" + "Is Bound = " + (isBound == true));
		}

		@Override
		public void onServiceDisconnected(ComponentName name){
			playerService = null;
			isBound = false;
			Utils.Instance().ToastShort("Disconnected\n" + "Is Bound = " + (isBound == true));
		}
	};
	
	void setPlayerList(){
		if(isBound){
			long[] ids = new long[trackList.size()];
			for(int i=0; i<trackList.size(); i++) {
				ids[i] = trackList.get(i).getId();
			}
			playerService.setTrackIdList(ids);
			playerService.setIndex(trackIndex);
		}
	}

	private List<Track> getTracksFromIds(long[] ids){
		List<Track> list = new ArrayList<Track>();
		try{
			Cursor cTracks = Utils.Instance().getTrackCursor(MediaStore.Audio.Media.IS_MUSIC + " =1 ");
			for(long id : ids){
				if(cTracks.moveToFirst()){
					do{
						long cId = cTracks.getLong(cTracks.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
						if(id == cId){
							Track t = new Track(id);
							t.setTitle(cTracks.getString(cTracks.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
							t.setArtist(cTracks.getString(cTracks.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
							t.setAlbum(cTracks.getString(cTracks.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)));
							t.setPath(cTracks.getString(cTracks.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
							t.setArtistId(cTracks.getLong(cTracks.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID)));
							t.setAlbumId(cTracks.getLong(cTracks.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)));
							t.setTrack(cTracks.getLong(cTracks.getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK)));
							t.setDuration(cTracks.getLong(cTracks.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));

							Cursor cDisc = Utils.Instance().getDiscCursor(MediaStore.Audio.Albums._ID + " = " + t.getAlbumId());
							if(cDisc.moveToFirst()){
								do{
									String art = cDisc.getString(cDisc.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ART));
									if(art != null){
										t.albumImgMain = art;
									}
								}while(cDisc.moveToNext());
							}
							cDisc.close();
							list.add(t);
						}
					}while(cTracks.moveToNext());
				}
			}
			cTracks.close();
			Utils.Instance().ToastShort("Tracks count: " + list.size());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	private void initViews(){
		curTrack = trackList.get(trackIndex);
		
		tvArtist = (TextView) findViewById(R.id.activityplayer_textView_artist);
		tvTitle = (TextView) findViewById(R.id.activityplayer_textView_title);
		tvAlbum = (TextView) findViewById(R.id.activityplayer_textView_album);
		tvSeekTime = (TextView) findViewById(R.id.activityplayer_textView_seek_time);
		tvSeekDuration = (TextView) findViewById(R.id.activityplayer_textView_seek_duration);
		ivAlbumArt = (ImageView) findViewById(R.id.activityplayer_imageView_main);
		seekBar = (SeekBar)findViewById(R.id.activityplayer_seekBar);
		btnShuffle = (ImageButton)findViewById(R.id.activityplayer_imageButton_shuffle);
		btnPrev = (ImageButton)findViewById(R.id.activityplayer_imageButton_prev);
		btnPlayPause = (ToggleButton)findViewById(R.id.activityplayer_imageButton_play_pause);
		btnNext = (ImageButton)findViewById(R.id.activityplayer_imageButton_next);
		btnRepeat = (ImageButton)findViewById(R.id.activityplayer_imageButton_repeat);
		layoutMain = (LinearLayout)findViewById(R.id.activityplayer_linearLayout_main);
		
		tvTitle.setText(curTrack.title);
		tvArtist.setText(curTrack.artist);
		tvAlbum.setText(curTrack.album);
		tvSeekDuration.setText(Utils.Instance().getTimeFormatted(curTrack.duration));
		
		btnShuffle.setOnClickListener(mediaButtonListener);
		btnPrev.setOnClickListener(mediaButtonListener);
		btnPlayPause.setOnClickListener(mediaButtonListener);
		btnNext.setOnClickListener(mediaButtonListener);
		btnRepeat.setOnClickListener(mediaButtonListener);
		
		File f = new File(curTrack.albumImgMain);
		Picasso.with(this).load(f).into(ivAlbumArt);
		Picasso.with(this).load(f).transform(blurTransformation).into(layoutBgTarget);
		
		
	}

	ImageButton.OnClickListener mediaButtonListener = new ImageButton.OnClickListener(){

		@Override
		public void onClick(View view){
			if(!isBound) { return; }
			int index = trackIndex;
			switch(view.getId()){
				case R.id.activityplayer_imageButton_shuffle:
				
					break;
					
				case R.id.activityplayer_imageButton_prev:
					
					if(playerService.player.getCurrentPosition() < 5000){
						index -= 1;
						if(index < 0){
							index = trackList.size() - 1;
						}
					}
					playerService.stop();
					playerService.reset();
					playerService.setIndex(index);
					playerService.prepare();
					break;
					
				case R.id.activityplayer_imageButton_play_pause:
					if(playerService.player.isPlaying()){
						playerService.pause();
					}
					else {
						playerService.play();
					}
					break;
					
				case R.id.activityplayer_imageButton_next:
					index += 1;
					if(index >= trackList.size()){
						index = 0;
					}
					playerService.stop();
					playerService.reset();
					playerService.setIndex(index);
					playerService.prepare();
					break;
					
				case R.id.activityplayer_imageButton_repeat:

					break;
			}
			trackIndex = index;
			initViews();
			Utils.Instance().ToastShort("activity index = " + index);
		}
		
		
	};
	
	Transformation blurTransformation = new Transformation(){

		@Override
		public Bitmap transform(Bitmap input){
			GaussianBlur gaussian = new GaussianBlur(PlayerActivity.this);
			gaussian.setMaxImageSize(300);
			gaussian.setRadius(7); //max

			Bitmap output = gaussian.render(input, true);
			input.recycle();
			return output;
		}

		@Override
		public String key(){
			return "blur";
		}
	};
	
	Target layoutBgTarget = new Target(){

		@Override
		public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom p2){
			BitmapDrawable bg = new BitmapDrawable(bitmap);
			layoutMain.setBackgroundDrawable(bg);
		}

		@Override
		public void onBitmapFailed(Drawable p1){
			// TODO: Implement this method
		}

		@Override
		public void onPrepareLoad(Drawable p1){
			// TODO: Implement this method
		}
	};

}
