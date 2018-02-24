package com.mycompany.myapp7;
import android.content.*;

public class Constants
{
	public static final String EXTRA_PLAYER_PLAY = "play";
	public static final String EXTRA_PLAYER_TRACK_LIST = "track_list";
	public static final String EXTRA_PLAYER_TRACK_INDEX = "track_index";
	public static final String ACTION_PLAYER_NEW_INDEX = "new_index";
	public static final String ACTION_PLAYER_NEW_LIST = "new_list";
	public static final String ACTION_PLAYER_BIND = "bind";
	public static final String EXTRA_IS_BOUND = "is_bound";
	
	public static String getPageTitle(Context context, int pos){
		String[] pageTitles = context.getResources().getStringArray(R.array.navigation_titles);
		return pageTitles[pos];
	}
}

enum DataType
{
	TRACK_ALL,
	ARTIST_ALL,
	DISC_ALL,
	ARTIST_DISCS,
	ARTIST_TRACKS,
	DISC_TRACKS,
	GENRE_TRACKS,
}

enum AdapterType
{
	LIST,
	GRID
}


