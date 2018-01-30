package com.mycompany.myapp7;
import android.support.v4.widget.CursorAdapter;
import android.database.*;
import android.view.*;
import android.content.*;
import android.widget.TextView;
import android.widget.*;
import android.provider.*;

public class DiscListCursorAdapter extends CursorAdapter
{
	public DiscListCursorAdapter(Context ctx, Cursor cursor){
		super(ctx, cursor);

	}

	@Override
	public View newView(Context p1, Cursor p2, ViewGroup p3){
		View v = LayoutInflater.from(p1).inflate(R.layout.artist_list_item, p3, false);
		ViewHolder vh = new ViewHolder(
			(TextView)v.findViewById(R.id.artistlistitem_textView_title),
			(TextView)v.findViewById(R.id.artistlistitem_textView_discs_songs), 
			(ImageButton)v.findViewById(R.id.artistlistitem_imageButton_menu),
			(ImageView)v.findViewById(R.id.artistlistitem_imageView_thumb)
		);
		v.setTag(vh);
		return v;
	}

	@Override
	public void bindView(View v, Context p2, Cursor p3){
		ViewHolder vh = (ViewHolder)v.getTag();
		if(vh == null) {
			vh = new DiscListCursorAdapter.ViewHolder(
				(TextView)v.findViewById(R.id.artistlistitem_textView_title),
				(TextView)v.findViewById(R.id.artistlistitem_textView_discs_songs), 
				(ImageButton)v.findViewById(R.id.artistlistitem_imageButton_menu),
				(ImageView)v.findViewById(R.id.artistlistitem_imageView_thumb)
			);
			v.setTag(vh);
		}
		vh.title.setText(getCursor().getString(getCursor().getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM)));
		vh.artist_tracks.setText(getCursor().getString(getCursor().getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST))
								+ " - " + getCursor().getString(getCursor().getColumnIndexOrThrow(MediaStore.Audio.Albums.NUMBER_OF_SONGS))
								+ " tracks");


	}

	class ViewHolder{
		TextView title, artist_tracks;
		ImageButton menuBtn;
		ImageView thumb;

		public ViewHolder(TextView track, TextView artist_tracks, ImageButton menuBtn, ImageView thumb){
			this.title = track;
			this.artist_tracks = artist_tracks;
			this.thumb = thumb;
			this.menuBtn = menuBtn;
		}
	}
}
