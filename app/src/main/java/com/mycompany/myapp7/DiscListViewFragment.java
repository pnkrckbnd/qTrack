package com.mycompany.myapp7;
import android.database.*;
import android.view.*;
import android.os.*;
import android.widget.*;
import android.widget.AdapterView.*;
import android.provider.*;

public class DiscListViewFragment extends ListViewFragment implements OnItemClickListener, OnItemLongClickListener
{
	ListViewItemClickedListener listener;
	DiscListCursorAdapter adapter;
	
	public static DiscListViewFragment newInstance(Cursor cursor, ListViewItemClickedListener listener){
		DiscListViewFragment f = new DiscListViewFragment();
		f.setCursor(cursor);
		f.listener = listener;
		return f;
	}

	public DiscListViewFragment(){

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View v = super.onCreateView(inflater, container, savedInstanceState);
		adapter = new DiscListCursorAdapter(getContext(), getCursor());
		getListView().setAdapter(adapter);
		getListView().setOnItemClickListener(this);
		getListView().setOnItemLongClickListener(this);
		return v;
	}

	@Override
	public void onItemClick(AdapterView<?> p1, View p2, int pos, long p4){
		Cursor c = adapter.getCursor();
		c.moveToPosition(pos);
		long id = c.getLong(c.getColumnIndexOrThrow(MediaStore.Audio.Albums._ID));
		listener.onClicked(id);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> p1, View p2, int p3, long p4){

		return false;
	}

}
