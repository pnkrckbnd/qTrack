package com.mycompany.myapp7;
import android.database.*;
import android.view.*;
import android.os.*;
import android.widget.*;
import android.widget.AdapterView.*;
import android.provider.*;

public class ArtistListViewFragment extends ListViewFragment implements OnItemClickListener, OnItemLongClickListener
{
	ListViewItemClickedListener listener;

	public static ArtistListViewFragment newInstance(Cursor cursor, ListViewItemClickedListener listener){
		ArtistListViewFragment f = new ArtistListViewFragment();
		f.setCursor(cursor);
		f.setListener(listener);
		return f;
	}

	public ArtistListViewFragment(){

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View v = super.onCreateView(inflater, container, savedInstanceState);
		ArtistListCursorAdapter adapter = new ArtistListCursorAdapter(getContext(), getCursor());
		getListView().setAdapter(adapter);
		getListView().setOnItemClickListener(this);
		getListView().setOnItemLongClickListener(this);
		return v;
	}

	@Override
	public void onItemClick(AdapterView<?> p1, View p2, int pos, long p4){
		Cursor c = ((TracksListCursorAdapter) getListView().getAdapter()).getCursor();
		c.moveToPosition(pos);
		listener.onClicked(c.getLong(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID)));
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> p1, View p2, int p3, long p4){

		return false;
	}



}
