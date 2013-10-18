package com.braim.deezer.ui.event;

import com.braim.deezer.data.Thumbnailable;

/**
 * Defines the behavior of a listener that will be notified of
 * successfull downloading of thumbnails.
 * @author Deezer
 */
public interface ThumbFetcherListener {

	/**
	 * Called to notify the listener that the thumbnail image of an artist has been downloaded 
	 * successfuly.
	 * @param thumbnailable the artist whose thumbnail is now available.
	 */
	public void thumbLoaded( Thumbnailable thumbnailable );

}//class
