package com.mycompany.myapp7;
import android.database.*;
import android.view.*;
import android.os.*;
import android.widget.*;
import android.widget.AdapterView.*;

public class TrackListViewFragment extends ListViewFragment implements OnItemClickListener, OnItemLongClickListener
{
	ListViewItemClickedListener listener;
	
	public static TrackListViewFragment newInstance(Cursor cursor, ListViewItemClickedListener listener){
		TrackListViewFragment f = new TrackListViewFragment();
		f.setCursor(cursor);
		f.setListener(listener);
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
	public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4){
		
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> p1, View p2, int p3, long p4){
		
		return false;
	}
	
	
	
}
