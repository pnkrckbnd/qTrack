package com.mycompany.myapp7;
import android.support.v4.app.*;
import android.database.*;
import android.view.*;
import android.os.*;
import android.widget.*;
import java.util.*;

public class ListViewFragment extends Fragment
{
	Cursor cursor;
	ListView listView;
	ListViewItemClickedListener listener;

	public ListViewFragment(){}

	public void setListener(ListViewItemClickedListener listener){
		this.listener = listener;
	}

	public ListViewItemClickedListener getListener(){
		return listener;
	}

	public void setCursor(Cursor cursor){
		this.cursor = cursor;
	}

	public Cursor getCursor(){
		return cursor;
	}

	public void setListView(ListView listView){
		this.listView = listView;
	}

	public ListView getListView(){
		return listView;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.fragment_list_view, container, false);
		listView = (ListView) v.findViewById(R.id.fragmentlistview_listView);
		return v;
	}
}

interface ListViewItemClickedListener{
	public void onClickSetupPlayer(long[] ids, int index);
	public void onClicked(long id);

	public void onLongClicked(long id)
}
