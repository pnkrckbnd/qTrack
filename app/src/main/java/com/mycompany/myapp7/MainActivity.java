package com.mycompany.myapp7;


import android.os.*;
import android.support.v7.app.*;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView.*;
import android.view.*;
import java.util.*;
import android.support.v7.widget.*;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.view.*;
import android.widget.FrameLayout;
import android.support.v4.app.*;
import android.database.*;
import android.provider.*;
import android.content.*;

public class MainActivity extends AppCompatActivity
{
	String[] pageTitles;
	DrawerLayout drawerLayout;
	ListView drawerListView;
	ActionBar actionBar;
	FrameLayout container;
	
	ListViewItemClickedListener trackClickListener, artistClickListener, discClickListener;

	long currentId = 0;
	int fragmentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		try{
			Utils.Instance().init(this);
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}

		String title = getResources().getString(R.string.app_name);
		pageTitles = getResources().getStringArray(R.array.navigation_titles);
		
		drawerLayout = (DrawerLayout)findViewById(R.id.main_drawerLayout);
	
		setSupportActionBar((Toolbar) findViewById(R.id.toolbar_layout));
		actionBar = getSupportActionBar();
		actionBar.setTitle(title);
		actionBar.setDefaultDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
			this, 
			R.layout.navbarlist, 
			R.id.navBar_textView, 
			pageTitles
		);
		
		drawerListView = (ListView)findViewById(R.id.main_left_drawer);
		drawerListView.setAdapter(adapter);
		drawerListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id){
				drawerLayout.closeDrawer(Gravity.LEFT);
				if(pos == fragmentIndex) 
					return;
						
				try{
					handleMenuClick(pos);
				}
				catch(Exception e){}
			}
		});

		if(savedInstanceState != null && savedInstanceState.containsKey("FRAGMENT_INDEX")) {
			fragmentIndex = savedInstanceState.getInt("FRAGMENT_INDEX");
			setTitleByMenuIndex(fragmentIndex);
		} 
		try{
			setupListeners();
			handleMenuClick(fragmentIndex);
		}
		catch(Exception e){}
	}

	private void handleMenuClick(int pos) throws Exception {
		setTitleByMenuIndex(pos);
		fragmentIndex = pos;
		
		Fragment frag = null;
		String tag = pageTitles[pos];
		
		switch (tag)
		{
				case "Artists":
				frag = getFragmentOfType(
					DataType.ARTIST_ALL, 
					Utils.Instance().getArtistCursor(null));
				break;
				case "Albums":
				frag = getFragmentOfType(
					DataType.DISC_ALL, 
					Utils.Instance().getDiscCursor(null));
				break;
				case "Tracks":
				frag = getFragmentOfType(
					DataType.TRACK_ALL, 
					Utils.Instance().getTrackCursor(MediaStore.Audio.Media.IS_MUSIC + " = 1 "));
				break;
				case "Genre":
				frag = getFragmentOfType(
					DataType.TRACK_ALL, 
					Utils.Instance().getTrackCursor(null));
				break;
				case "Playlist":
				frag = getFragmentOfType(
					DataType.TRACK_ALL, 
					Utils.Instance().getTrackCursor(null));
				break;
				case "Top Ten":
				frag = getFragmentOfType(
					DataType.TRACK_ALL, 
					Utils.Instance().getTrackCursor(null));
				break;
				default:

				break;
		}
		replaceFragment(frag, tag);
	}
	
	
	private Fragment getFragmentOfType(DataType type, Cursor cursor){
		if(type.equals(DataType.TRACK_ALL)){
			return TrackListViewFragment.newInstance(cursor, trackClickListener);
		}
		if(type.equals(DataType.ARTIST_ALL)){
			return ArtistListViewFragment.newInstance(cursor, artistClickListener);
		}
		if(type.equals(DataType.DISC_ALL)){
			return DiscListViewFragment.newInstance(cursor, discClickListener);
		}
		if(type.equals(DataType.ARTIST_DISCS)){
			return DiscListViewFragment.newInstance(cursor, discClickListener);
		}
		if(type.equals(DataType.ARTIST_TRACKS)){
			return TrackListViewFragment.newInstance(cursor, trackClickListener);
		}
		if(type.equals(DataType.DISC_TRACKS)){
			return TrackListViewFragment.newInstance(cursor, trackClickListener);
		}
		if(type.equals(DataType.GENRE_TRACKS)){
			return TrackListViewFragment.newInstance(cursor, trackClickListener);
		}
		throw new UnsupportedOperationException();
	}

	private void replaceFragment(Fragment frag, String tag){
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction()
			.replace(R.id.main_fragContainer, frag, tag);
		if(fm.findFragmentById(R.id.main_fragContainer) != null) {
			ft.addToBackStack(null);
		}
		ft.commit();
	}

//	@Override
//	public void onBackPressed(){
//		super.onBackPressed();
//		Fragment frag = getSupportFragmentManager().findFragmentById(R.id.main_fragContainer);
//		if(frag != null){
//			int pos = Integer.parseInt(frag.getTag());
//			setTitleByMenuIndex(pos);
//		}
//	}

	private void setTitleByMenuIndex(int pos) throws NumberFormatException{
		String title = Constants.getPageTitle(getApplicationContext(), pos);
		actionBar.setTitle(title);
	}
	
	private void setTitleByTag(Fragment f) {
		if(f != null){
			String title = f.getTag();
			actionBar.setTitle(title);
		}
	}
	
	
	
	void setupListeners(){
		trackClickListener = new ListViewItemClickedListener(){

			@Override
			public void onClickSetupPlayer(long[] ids, int index){
				Intent i = new Intent(MainActivity.this, PlayerActivity.class);
				i.setAction(Constants.ACTION_PLAYER_NEW_LIST);
				i.putExtra(Constants.EXTRA_PLAYER_TRACK_LIST, ids);
				i.putExtra(Constants.EXTRA_PLAYER_TRACK_INDEX, index);
				startActivity(i, null);
			}
			
			@Override
			public void onClicked(long id) {
				try{
					Cursor trackCursor = Utils.Instance().getTrackCursor(
						MediaStore.Audio.Media._ID + " = " + id
					);
					trackCursor.moveToFirst();
					String trackTitle = trackCursor.getString(
						trackCursor.getColumnIndexOrThrow(
							MediaStore.Audio.Media.TITLE
						)
					);
					Toast.makeText(MainActivity.this, "Now Playing " + trackTitle, Toast.LENGTH_SHORT).show();
					
					
				}
				catch(Exception e){}
				Track[] trackList = new Track[1];

				Intent i = new Intent(MainActivity.this, PlayerActivity.class);
				i.setAction(Constants.ACTION_PLAYER_NEW_LIST);
				i.putExtra(Constants.EXTRA_PLAYER_TRACK_LIST, trackList);
				i.putExtra(Constants.EXTRA_PLAYER_TRACK_INDEX, 0);
				startActivity(i, null);
			}

			@Override
			public void onLongClicked(long id){
				// TODO: Implement this method
				currentId = id;
			}
		};
		artistClickListener = new ListViewItemClickedListener(){

			@Override
			public void onClickSetupPlayer(long[] ids, int index){
				// TODO: Implement this method
			}
			
			@Override
			public void onClicked(long id){
				try{
					Cursor artistCursor = Utils.Instance().getArtistCursor(
						MediaStore.Audio.Artists._ID + " = " + id);
				
					artistCursor.moveToFirst();
					String artistName = artistCursor.getString(
						artistCursor.getColumnIndexOrThrow(
							MediaStore.Audio.Artists.ARTIST
						)
					);
					artistCursor.close();
					if(artistName.equals("<unknown>")){
						artistName = "";
					}
					Cursor discCursor = Utils.Instance().getDiscCursor(MediaStore.Audio.Artists.Albums.ARTIST + " LIKE '" + artistName + "'");
				
					Fragment f = getFragmentOfType(DataType.ARTIST_DISCS, discCursor);
					replaceFragment(f, artistName);
				}
				catch(Exception e){e.printStackTrace();}
			}

			@Override
			public void onLongClicked(long id){
				
				try{
					Fragment f = getFragmentOfType(
						DataType.TRACK_ALL, 
						Utils.Instance().getTrackCursor(MediaStore.Audio.Media.ARTIST_ID + " = " + id)
					);
					replaceFragment(f, "Artist");
				} catch(Exception e){}
			}
		};
		discClickListener = new ListViewItemClickedListener(){

			@Override
			public void onClickSetupPlayer(long[] ids, int index){
				// TODO: Implement this method
			}
			
		
			
			public void onClicked(long id){
				try{
					Fragment f = getFragmentOfType(
						DataType.DISC_TRACKS, 
						Utils.Instance().getTrackCursor(MediaStore.Audio.Media.ALBUM_ID + " = " + id));
						replaceFragment(f, "Album Tracks");
				}catch(Exception e){}
			}

			@Override
			public void onLongClicked(long id){
				// TODO: Implement this method
				currentId = id;
			}
		};
	}
	
	
	@Override
	protected void onSaveInstanceState(Bundle outState){
		outState.putInt("FRAGMENT_INDEX", fragmentIndex);
		super.onSaveInstanceState(outState);
	}
}
