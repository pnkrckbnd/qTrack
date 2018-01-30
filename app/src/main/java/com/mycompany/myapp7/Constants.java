package com.mycompany.myapp7;
import android.content.*;

public class Constants
{
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

