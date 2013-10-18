package com.braim.deezer.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A track in an Album.
 * <pre>
 * {
 *   "id":"6816834",
 *   "name":"Cold Wind Blows", 
 *   "url":"http:\/\/www.deezer.com\/listen-6816834",
 *   "duration":"303",
 *   "rank":"0",
 *   "preview":"http:\/\/preview-6.deezer.com\/stream\/679128e7e326cdd3168366eb68dd85b7-0.mp3",
 *   "stream": "8d33e25de8e2c6ba041862c14a4cbcf408b5eae8eeccbfeb361404574fb991f94f330f084261fc9a03e99ec83f1d07f979a1302220f1981c658e115989131109f6affe15f644533b78fc0e9b0af4df72de69e4d09e9c5738ce383957e5d30c83fef085a2b6a6a76ddf46381b77f1013912778d503f97828f543e2344dbbab6038166a16bde6f1272aef812ce1b02a8ecc36e7792d22f7e9e151f2d342a2579bb7f3e969bf3ddd1e6660723f86461e46e835226da7304b05eb9ca70e1c790f8db7bf7cbe6f5a0122c7a0b1b669d7a4903"
 * }
 * </pre>
 * @author Deezer
 */
public class Track implements Thumbnailable, Parcelable {

	/** The id of the track. */
	private long id;
	/** The title of the track. */
	private String title;
	/** The url of the web page on Deezer for the track. */
	private String link;
	/** The duration of the track (in seconds). */
	private int duration;
	/** Url of an exerpt from the track, can be passed to the player. If user is using a premium deezer account, this has to be passed to the player. */
	private String stream;
	/** Url of preview, can be passed to the player. If user is using a free deezer account, this has to be passed to the player. */
	private String preview;
	/** The author of the track. */
	private Artist artist;
	private String picture;
	
	public long getId() {
		return id;
	}//met
	
	public void setId(long id) {
		this.id = id;
	}//met
	
	public String getTitle() {
		return title;
	}//met
	
	public void setTitle(String title) {
		this.title = title;
	}//met
	
	public String getLink() {
		return link;
	}//met
	
	public void setLink(String link) {
		this.link = link;
	}//met
	
	public int getDuration() {
		return duration;
	}//met
	
	public void setDuration(int duration) {
		this.duration = duration;
	}//met
	
	public String getStream() {
		return stream;
	}//met
	
	public void setStream(String stream) {
		this.stream = stream;
	}//met

	@Override
	public String toString() {
		return title;
	}//met

	public boolean hasStream() {
		return stream != "false";
	}//met

	public String getPreview() {
		return preview;
	}//met
	
	public void setPreview( String preview ) {
		this.preview = preview;
	}//met

	public Artist getArtist() {
		return artist;
	}//met

	public void setArtist(Artist artist) {
		this.artist = artist;
	}//met

	@Override
	public String getThumbnailUrl() {
		// TODO Auto-generated method stub
		return picture;
	}
	public String getPicture() {
		return picture;
	}//met
	
	public void setPicture(String picture) {
		this.picture = picture;
	}//met

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	
}//class 
