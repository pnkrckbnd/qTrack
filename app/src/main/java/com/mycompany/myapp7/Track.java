package com.mycompany.myapp7;

public class Track
{
	long id, duration;
	long artistId, albumId, track;
	String title, artist, album, albumImgThumb, albumImgMain, path;

	public Track(long id){
		this.id = id;
	}

	public void setArtistId(long artistId){
		this.artistId = artistId;
	}

	public long getArtistId(){
		return artistId;
	}

	public void setAlbumId(long albumId){
		this.albumId = albumId;
	}

	public long getAlbumId(){
		return albumId;
	}

	public void setTrack(long track){
		this.track = track;
	}

	public long getTrack(){
		return track;
	}

	public long getId(){
		return id;
	}

	public void setDuration(long duration){
		this.duration = duration;
	}

	public long getDuration(){
		return duration;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setArtist(String artist){
		this.artist = artist;
	}

	public String getArtist(){
		return artist;
	}

	public void setAlbum(String album){
		this.album = album;
	}

	public String getAlbum(){
		return album;
	}

	public void setAlbumImgThumb(String albumImgThumb){
		this.albumImgThumb = albumImgThumb;
	}

	public String getAlbumImgThumb(){
		return albumImgThumb;
	}

	public void setAlbumImgMain(String albumImgMain){
		this.albumImgMain = albumImgMain;
	}

	public String getAlbumImgMain(){
		return albumImgMain;
	}

	public void setPath(String path){
		this.path = path;
	}

	public String getPath(){
		return path;
	}
	
	
}
