package com.mycompany.myapp7;
import android.database.*;
import android.view.*;
import android.os.*;
import android.widget.*;
import android.widget.AdapterView.*;
import android.provider.*;

public class TrackListViewFragment extends ListViewFragment implements OnItemClickListener, OnItemLongClickListener
{
	ListViewItemClickedListener listener;
	
	public static TrackListViewFragment newInstance(Cursor cursor, ListViewItemClickedListener listener){
		TrackListViewFragment f = new TrackListViewFragment();
		f.setCursor(cursor);
		f.listener = listener;
		return f;
	}
	
	public TrackListViewFragment(){
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View v = super.onCreateView(inflater, container, savedInstanceState);
		TracksListCursorAdapter adapter = new TracksListCursorAdapter(getContext(), getCursor());
		getListView().setAdapter(adapter);
		getListView().setOnItemClickListener(this);
		getListView().setOnItemLongClickListener(this);
		return v;
	}

	@Override
	public void onItemClick(AdapterView<?> p1, View p2, int pos, long p4){
		Cursor c = getCursor();
		if(c.getCount() > 0) {
			long[] ids = new long[c.getCount()];
			for(int i=0; i<ids.length; i++) {
				c.moveToPosition(i);
				ids[i] = c.getLong(c.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
			}
			listener.onClickSetupPlayer(ids, pos);
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> p1, View p2, int p3, long p4){
		
		return false;
	}
}
