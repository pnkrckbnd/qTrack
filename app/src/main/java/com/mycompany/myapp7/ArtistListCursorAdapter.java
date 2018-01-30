package com.mycompany.myapp7;
import android.support.v4.widget.CursorAdapter;
import android.database.*;
import android.view.*;
import android.content.*;
import android.widget.TextView;
import android.widget.*;
import android.provider.*;

public class ArtistListCursorAdapter extends CursorAdapter
{
	public ArtistListCursorAdapter(Context ctx, Cursor cursor){
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
			vh = new ArtistListCursorAdapter.ViewHolder(
				(TextView)v.findViewById(R.id.artistlistitem_textView_title),
				(TextView)v.findViewById(R.id.artistlistitem_textView_discs_songs), 
				(ImageButton)v.findViewById(R.id.artistlistitem_imageButton_menu),
				(ImageView)v.findViewById(R.id.artistlistitem_imageView_thumb)
			);
			v.setTag(vh);
		}
		vh.title.setText(getCursor().getString(getCursor().getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST)));
		vh.songs_albums.setText(getCursor().getString(getCursor().getColumnIndexOrThrow(MediaStore.Audio.Artists.NUMBER_OF_TRACKS))
								+ " songs - " + getCursor().getString(getCursor().getColumnIndexOrThrow(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS))
								+ " albums");
		
		
	}

	class ViewHolder{
		TextView title, songs_albums;
		ImageButton menuBtn;
		ImageView thumb;

		public ViewHolder(TextView track, TextView artist_album, ImageButton menuBtn, ImageView thumb){
			this.title = track;
			this.songs_albums = artist_album;
			this.thumb = thumb;
			this.menuBtn = menuBtn;
		}
	}
}
