package com.mycompany.myapp7;
import android.support.v4.widget.CursorAdapter;
import android.database.*;
import android.view.*;
import android.content.*;
import android.widget.TextView;
import android.widget.*;
import android.provider.*;

public class TracksListCursorAdapter extends CursorAdapter
{
	public TracksListCursorAdapter(Context ctx, Cursor cursor){
		super(ctx, cursor);
		
	}
	
	@Override
	public View newView(Context p1, Cursor p2, ViewGroup p3){
		View v = LayoutInflater.from(p1).inflate(R.layout.track_list_item, p3, false);
		ViewHolder vh = new ViewHolder(
			(TextView)v.findViewById(R.id.tracklistitem_textView_track),
			(TextView)v.findViewById(R.id.tracklistitem_textView_artist_album), 
			(TextView)v.findViewById(R.id.tracklistitem_textView_duration),
			(ImageButton)v.findViewById(R.id.tracklistitem_imageButton_menu)
		);
		v.setTag(vh);
		return v;
	}

	@Override
	public void bindView(View v, Context p2, Cursor p3){
		ViewHolder vh = (ViewHolder)v.getTag();
		if(vh == null) {
			vh = new ViewHolder(
				(TextView)v.findViewById(R.id.tracklistitem_textView_track),
				(TextView)v.findViewById(R.id.tracklistitem_textView_artist_album), 
				(TextView)v.findViewById(R.id.tracklistitem_textView_duration),
				(ImageButton)v.findViewById(R.id.tracklistitem_imageButton_menu)
			);
			v.setTag(vh);
		}
		vh.track.setText(getCursor().getString(getCursor().getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
		vh.artist_album.setText(getCursor().getString(getCursor().getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
													  + " - " + getCursor().getString(getCursor().getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)));
		vh.duration.setText(getCursor().getLong(getCursor().getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)) + "");
	}
	
	class ViewHolder{
		TextView track, artist_album, duration;
		ImageButton menuBtn;

		public ViewHolder(TextView track, TextView artist_album, TextView duration, ImageButton menuBtn){
			this.track = track;
			this.artist_album = artist_album;
			this.duration = duration;
			this.menuBtn = menuBtn;
		}
	}
}
