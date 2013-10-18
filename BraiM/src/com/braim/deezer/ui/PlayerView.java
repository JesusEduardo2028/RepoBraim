package com.braim.deezer.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.braim.ReproductorActivity;
import com.deezer.sdk.player.Player;
import com.deezer.sdk.player.event.OnBufferErrorListener;
import com.deezer.sdk.player.event.OnBufferProgressListener;
import com.deezer.sdk.player.event.OnBufferStateChangeListener;
import com.deezer.sdk.player.event.OnPlayerErrorListener;
import com.deezer.sdk.player.event.OnPlayerProgressListener;
import com.deezer.sdk.player.event.OnPlayerStateChangeListener;
import com.deezer.sdk.player.event.PlayerState;
import com.deezer.sdk.player.event.BufferState;
//import com.deezer.sdk.sample.R;
import com.example.pruebasherlock.R;



/**
 * A UI component for the Deezer Player
 * @author Deezer
 */
public class PlayerView extends LinearLayout {
	/** Log tag for logcat.*/
	private static final String LOG_TAG = "PlayerView";

	// msg for handlers
	private final static int MSG_SHOW_PLAYER_PROGRESS = 0;
	private final static int MSG_SHOW_BUFFER_PROGRESS = 1;
	private final static int MSG_SHOW_PLAYER_STATE = 2;
	private final static int MSG_SHOW_BUFFER_STATE = 3;
	private final static int MSG_SHOW_ERROR = 4;

	private Player player = null;
	private PlayerHandler playerHandler = new PlayerHandler();

	private ImageButton mButtonPlaylistSkipBackward;
	private ImageButton mButtonPlayerSeekBackward;
	private ImageButton mButtonPlayerStop;
	private ImageButton mButtonPlayerPause;
	private ImageButton mButtonPlayerSeekForward;
	private ImageButton mButtonPlaylistSkipForward;

	private SeekBar mSeekBar;
	private boolean mIsUserSeeking = false;
	private TextView mTextTime;
	private TextView mTextLength;
	
//	private TextView mTextArtist;
//	private TextView mTextTrack;

	private OnClickHandler onClickHandler = new OnClickHandler();
	private OnSeekBarChangeHandler onSeekBarChangeHandler = new OnSeekBarChangeHandler();

	/**
	 * Create an instance using given context and player.
	 * @param context the context in which to show the widget.
	 * @param player the player that will be controlled by the widget.
	 */
	public PlayerView(Context context, Player player ) {
		this(context, null, player );
	}//cons

	/**
	 * Create an instance of the widget. This constructor is used 
	 * for easily inflating xml layouts.
	 * @param context the context in which to show the widget.
	 * @param attrs the attributes defined in xml.
	 */
	public PlayerView(Context context, AttributeSet attrs) {
		this( context, attrs, null );
	}//if

	/**
	 * Create an instance of the widget. This constructor is used 
	 * internally, but is still accessible, just in case.
	 * @param context the context in which to show the widget.
	 * @param attrs the attributes defined in xml.
	 * @param player the player that will be controlled by the widget.
	 */
	public PlayerView(Context context, AttributeSet attrs, Player player) {
		super(context, attrs);

		if( player != null ) {
			setPlayer(player);			
		}//if

		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		inflater.inflate(R.layout.audio_playback_common, this );
		mButtonPlayerPause = (ImageButton) findViewById( R.id.button_pause);
		mButtonPlayerStop = (ImageButton) findViewById( R.id.button_stop);
		mButtonPlaylistSkipForward = (ImageButton) findViewById( R.id.button_skip_forward);
		mButtonPlaylistSkipBackward = (ImageButton) findViewById( R.id.button_skip_backward);
		mButtonPlayerSeekForward = (ImageButton) findViewById( R.id.button_seek_forward);
		mButtonPlayerSeekBackward = (ImageButton) findViewById( R.id.button_seek_backward);

		mSeekBar = (SeekBar) findViewById(R.id.seek_progress);
		mTextTime = (TextView) findViewById(R.id.text_time);
		mTextLength = (TextView) findViewById(R.id.text_length);
//		
//		mTextArtist = (TextView) findViewById(R.id.text_artist);
//		mTextTrack = (TextView) findViewById(R.id.text_track);

		mSeekBar.setOnSeekBarChangeListener( onSeekBarChangeHandler );
		mButtonPlayerPause.setOnClickListener( onClickHandler );
		mButtonPlayerStop.setOnClickListener( onClickHandler );
		mButtonPlayerSeekForward.setOnClickListener( onClickHandler );
		mButtonPlayerSeekBackward.setOnClickListener( onClickHandler );

		setPlaylistButtonsVisible( false );
	//	setArtistName( null );
		setTrackName( null );
	}//cons

	/**
	 * Show/hide playlist management buttons on view.
	 * @param visible wether to show (true) or hide (false) the playlist management buttons on view. 
	 */
	public void setPlaylistButtonsVisible(boolean visible) {
		if( visible ) {
			mButtonPlaylistSkipForward.setOnClickListener( onClickHandler );
			mButtonPlaylistSkipBackward.setOnClickListener( onClickHandler );
			mButtonPlaylistSkipForward.setVisibility( View.VISIBLE );
			mButtonPlaylistSkipBackward.setVisibility( View.VISIBLE  );
			mButtonPlayerSeekForward.setVisibility( View.VISIBLE  );
			mButtonPlayerSeekBackward.setVisibility( View.VISIBLE  );
		} else {
			mButtonPlaylistSkipForward.setOnClickListener( null );
			mButtonPlaylistSkipBackward.setOnClickListener( null );
			mButtonPlaylistSkipForward.setVisibility( View.GONE );
			mButtonPlaylistSkipBackward.setVisibility( View.GONE  );
			mButtonPlayerSeekForward.setVisibility( View.GONE  );
			mButtonPlayerSeekBackward.setVisibility( View.GONE  );
			mSeekBar.setEnabled(false);
		}//else
	}//met


	@Override
	protected synchronized void onDetachedFromWindow() {
		if( player != null ) {
			player.removeOnBufferErrorListener( playerHandler );
			player.removeOnBufferStateChangeListener( playerHandler );
			player.removeOnBufferProgressListener( playerHandler );

			player.removeOnPlayerErrorListener( playerHandler );
			player.removeOnPlayerStateChangeListener( playerHandler );
			player.removeOnPlayerProgressListener( playerHandler );
			this.player = null;
		}//if

		super.onDetachedFromWindow();
	}//met

	/**
	 * Sets the player to be controlled/rendered by the PlayerView.
	 * @param player the player to be controlled/rendered by the PlayerView.
	 */
	public void setPlayer(Player player) {
		if( player == null ) {
			throw new IllegalArgumentException( "PlayerController doesn't support a null player." );
		}//if
		this.player = player;
		player.addOnBufferErrorListener( playerHandler );
		player.addOnBufferStateChangeListener( playerHandler );
		player.addOnBufferProgressListener( playerHandler );

		player.addOnPlayerErrorListener( playerHandler );
		player.addOnPlayerStateChangeListener( playerHandler );
		player.addOnPlayerProgressListener( playerHandler );
	}//if

	/**
	 * @param artistName the name of the artist to be displayed in the widget. If null, text field will be hidden.
	 */
	public void setArtistName( String artistName ) {
		if( artistName == null ) {
		//	mTextArtist.setVisibility( GONE );
		} else {
		//	mTextArtist.setVisibility( VISIBLE );
		//	mTextArtist.setText( artistName );
			
		}//else
	}//met

	/**
	 * @param artistName the name of the artist to be displayed in the widget. If null, text field will be hidden.
	 */
	public void setTrackName( String trackName ) {
		if( trackName == null ) {
		//	mTextTrack.setVisibility( GONE );
			ReproductorActivity.sendTrack("null");
		} else {
		//	mTextTrack.setVisibility( VISIBLE );
		//	mTextTrack.setText( trackName );
			ReproductorActivity.sendTrack(trackName);
		}//else
	}//met

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch( msg.what ) {
			case MSG_SHOW_PLAYER_PROGRESS :
				showPlayerProgress( msg.arg1 );
				break;
			case MSG_SHOW_PLAYER_STATE :
				showPlayerState( (PlayerState) msg.obj );
				break;
			case MSG_SHOW_BUFFER_PROGRESS :
				showBufferProgress( msg.arg1 );
				break;
			case MSG_SHOW_BUFFER_STATE :
				showBufferState( (BufferState) msg.obj );
				break;
			case MSG_SHOW_ERROR :
				Toast.makeText( getContext() , msg.obj.toString(), Toast.LENGTH_SHORT ).show() ;
				break;
			}//switch
		}//met
	};

	public void showPlayerProgress( long timePosition ) {
		Log.d( LOG_TAG, "Updating player progress" + timePosition / 1000 );
		
		if (!mIsUserSeeking) {
			mSeekBar.setProgress( (int) timePosition / 1000 );
			String text = formatTime( timePosition );
			mTextTime.setText(text);
		}
		
		synchronized ( this ) {
			if( player != null ) {
				mSeekBar.setEnabled( player.isAllowedToSeek() );
				mButtonPlaylistSkipForward.setVisibility( player.isAllowedToSeek() ? VISIBLE : GONE );
				mButtonPlayerSeekBackward.setEnabled( timePosition != 0 || player.getPlayerState() == PlayerState.STOPPED );
				mButtonPlayerSeekForward.setEnabled( timePosition != player.getTrackDuration() || player.getPlayerState() == PlayerState.STOPPED );
			}//if
		}//sync
	}//met

	public void showBufferProgress( int position ) {
		synchronized ( this ) {
			if( player != null ) {
				if( position > 0 ) {
					showTrackDuration( player.getTrackDuration() );
				
				}//if
				Log.d( LOG_TAG, "Updating buffer position" + position );
				long progress = position * player.getTrackDuration() / 100 ;
				Log.d( LOG_TAG, "Updating buffer progress" + progress );
				mSeekBar.setSecondaryProgress( (int)progress / 1000 );
			}//if
		}//sync
	}//met

	private void showBufferState(BufferState state ) {
		switch( state ) {
		case PAUSED :
			mSeekBar.setBackgroundColor( Color.GRAY );
			break;
		case STOPPED :
		case STARTED :
			//mSeekBar.setBackgroundColor( Color.BLUE );
			break;
		}//switch
	}//met

	public void showTrackDuration( long trackLength ) {
		Log.d( LOG_TAG, "Track duration is " + trackLength );
		String text = formatTime( trackLength );
		mTextLength.setText(text);
		mSeekBar.setMax( (int) trackLength / 1000 );
	}//met

	public void showPlayerState( PlayerState state ) {
		mSeekBar.setEnabled( true );
		mButtonPlayerPause.setEnabled( true );
		mButtonPlayerSeekBackward.setEnabled( true );
		mButtonPlaylistSkipBackward.setEnabled( true );
		mButtonPlayerStop.setEnabled( true );
		mButtonPlayerSeekForward.setEnabled( true );
		mButtonPlaylistSkipForward.setEnabled( true );

		switch( state ) {
		case STARTED :
			mButtonPlayerPause.setEnabled( true );
			mButtonPlayerPause.setImageResource( R.drawable.ic_media_playback_start );
			break;
		case INITIALIZING:
			mButtonPlayerPause.setEnabled( true );
			mButtonPlayerPause.setImageResource( R.drawable.ic_media_playback_start );
			break;
		case READY:
			mButtonPlayerPause.setEnabled( true );
			mButtonPlayerPause.setImageResource( R.drawable.ic_media_playback_start );
			showPlayerProgress( 0 );
			break;
		case PLAYING:
			mButtonPlayerPause.setEnabled( true );
			mButtonPlayerPause.setImageResource( R.drawable.ic_media_playback_pause );
			break;
		case PAUSED:
		case PLAYBACK_COMPLETED:
			mButtonPlayerPause.setEnabled( true /*player.getPosition() != player.getTrackDuration()*/ );
			mButtonPlayerPause.setImageResource( R.drawable.ic_media_playback_start );
			break;
		
		case WAITING_FOR_DATA:
			mButtonPlayerPause.setEnabled( false );
			break;

		case STOPPED :
			mSeekBar.setEnabled( false );
			mButtonPlayerPause.setEnabled( false );
			mButtonPlayerSeekBackward.setEnabled( false );
			mButtonPlaylistSkipBackward.setEnabled( false );
			mButtonPlayerStop.setEnabled( false );
			mButtonPlayerSeekForward.setEnabled( false );
			mButtonPlaylistSkipForward.setEnabled( false );
			break;
		}//switch
	}//met

	/**
	 * Ensure double decimal representation of numbers.
	 * @param builder a builder where a number is gonna be inserted at beginning.
	 * @param value the number value. If below 10 then a leading 0 is inserted.
	 */
	private static void doubleDigit(StringBuilder builder, long value) {
		builder.insert(0, value);
		if (value < 10) {
			builder.insert(0, '0');
		}//if
	}//met

	/**
	 * Formats a time.
	 * @param time the time (in seconds)
	 * @return the formatted time.
	 */
	private static String formatTime(long time) {
		time /= 1000;
		long seconds = time % 60;
		time /= 60;
		long minutes = time % 60;
		time /= 60;
		long hours = time;
		StringBuilder builder = new StringBuilder(8);
		doubleDigit(builder, seconds);
		builder.insert(0, ':');
		if (hours == 0) {
			builder.insert(0, minutes);
		} else {
			doubleDigit(builder, minutes);
			builder.insert(0, ':');
			builder.insert(0, hours);
		}//else
		return builder.toString();
	}//met

	private void sendMessageShowPlayerProgress( long timePosition ) {
		Message msg = new Message();
		msg.what = MSG_SHOW_PLAYER_PROGRESS;
		msg.arg1 = (int) timePosition;
		mHandler.sendMessage(msg);	
		
	}//met
	

	private void sendMessageShowPlayerState( PlayerState state ) {
		Message msg = new Message();
		msg.what = MSG_SHOW_PLAYER_STATE;
		msg.obj = state;
		mHandler.sendMessage(msg);	
	}//met

	private void sendMessageShowBufferProgress( double percent ) {
		Message msg = new Message();
		msg.what = MSG_SHOW_BUFFER_PROGRESS;
		msg.arg1 = (int)Math.round( percent );
		mHandler.sendMessage(msg);	
	}//met

	private void sendMessageShowBufferState( BufferState state ) {
		Message msg = new Message();
		msg.what = MSG_SHOW_BUFFER_STATE;
		msg.obj = state;
		mHandler.sendMessage(msg);	
	}//met

	private void sendMessageShowError( String error ) {
		Message msg = new Message();
		msg.what = MSG_SHOW_ERROR;
		msg.obj = error;
		mHandler.sendMessage(msg);	
	}//met

	private class PlayerHandler implements OnPlayerProgressListener, OnBufferProgressListener, OnPlayerStateChangeListener, OnPlayerErrorListener, OnBufferStateChangeListener, OnBufferErrorListener {

		@Override
		public void onBufferError(Exception ex, double percent) {
			if( ex.getMessage() != null ) {
				sendMessageShowError( ex.getMessage() );
			} else {
				sendMessageShowError( ex.getClass().getName() );
			}//else
			Log.e(LOG_TAG, "Buffer error:", ex );
		}//met

		@Override
		public void onBufferStateChange( BufferState state, double percent) {
			sendMessageShowBufferProgress( percent );
			sendMessageShowBufferState( state );
		}//met

		@Override
		public void onPlayerError(Exception ex, long timePosition) {
			sendMessageShowError( ex.getMessage() );
			Log.e(LOG_TAG, "Buffer error:", ex );
		}//met

		@Override
		public void onPlayerStateChange(
				PlayerState state, long timePosition) {
			sendMessageShowPlayerState( state );
			sendMessageShowPlayerProgress( timePosition );
		}//met

		@Override
		public void onBufferProgress(double percent) {
			sendMessageShowBufferProgress( percent );
		}//met

		@Override
		public void onPlayerProgress(long timePosition) {
			sendMessageShowPlayerProgress( timePosition );
		}//met
	}//inner class

	private class OnSeekBarChangeHandler implements OnSeekBarChangeListener {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			if (fromUser && mIsUserSeeking) {
				long timePosition = (long)seekBar.getProgress() * player.getTrackDuration() / seekBar.getMax();
				String text = formatTime( timePosition );
				mTextTime.setText(text);
			}
		}//met

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			mIsUserSeeking = true;
			//do nothing
		}//met

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			player.seek( (long)seekBar.getProgress() * player.getTrackDuration() / seekBar.getMax() );
			mIsUserSeeking = false;
		}//met

	}//inner class

	private class OnClickHandler implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			if (v == mButtonPlayerPause) {
				if( player.getPlayerState() == PlayerState.PLAYING ) {
					player.pause();
					ReproductorActivity.sendPause();
				} else {
					player.play();
					ReproductorActivity.sendPlay();
				}
			} else if (v == mButtonPlayerStop) {
				player.stop();
			} else if (v == mButtonPlaylistSkipBackward) {
				//nothing to do, sample app doesn't support playlists.
			} else if (v == mButtonPlaylistSkipForward) {
				//nothing to do, sample app doesn't support playlists.
			} else if (v == mButtonPlayerSeekBackward) {
				try {
					player.seek( player.getPosition() - 10*1000 );
				} catch (Exception e) {
					sendMessageShowError( e.getMessage() );
					Log.e( LOG_TAG, "Seek not allowed.", e);
				}//catch
			} else if (v == mButtonPlayerSeekForward) {
				try {
					player.seek( player.getPosition() + 10*1000 );
				} catch (Exception e) {
					sendMessageShowError( e.getMessage() );
					Log.e( LOG_TAG, "Seek not allowed.", e);
				}//catch
			}//else
		}//met
	}//inner class
}//class
