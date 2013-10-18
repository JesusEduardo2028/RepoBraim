package com.braim;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

import com.deezer.sdk.AsyncDeezerTask;
import com.deezer.sdk.DeezerConnect;
import com.deezer.sdk.RequestListener;

/**
 * A simple subclass of {@link com.deezer.sdk.AsyncDeezerTask} that displays a dialog 
 * while contacting deezer. You should use directly AsyncDeezerTask and not this class.
 * @author Deezer
 */
public class AsyncDeezerTaskWithDialog extends AsyncDeezerTask {
	/** Progress dialog to show user that the request is beeing processed. */
	private ProgressDialog progressDialog;

	/**
	 * Simply creates an Deezer task with a dialog.
	 * @param context the context used to create the dialog into.
	 * @param deezerConnect the DeezerConnect object used to connect to Deezer web services.
	 * @param listener the request listener.
	 */
	public AsyncDeezerTaskWithDialog(Context context, DeezerConnect deezerConnect,
			RequestListener listener ) {
		super(deezerConnect, listener );
		progressDialog = new ProgressDialog( context );
		progressDialog.setCancelable( true );
		progressDialog.setOnCancelListener( new OnCancelHandler() );
	}//met

	@Override
	protected void onPreExecute() {
		progressDialog.setMessage( "Contacting Deezer..." );
		progressDialog.show();
		super.onPreExecute();
	}//met

	@Override
	public void onPostExecute( String s ) {
		//http://stackoverflow.com/questions/2745061/java-lang-illegalargumentexception-view-not-attached-to-window-manager
		try {
			if( progressDialog.isShowing() ) {
				progressDialog.dismiss();
			}//if
		} catch (IllegalArgumentException e) {
			//can happen sometimes, and nothing to get against it
		}//catch
		super.onPostExecute( s );
	}//met

	@Override
	protected void onCancelled() {
		//http://stackoverflow.com/questions/2745061/java-lang-illegalargumentexception-view-not-attached-to-window-manager
		try {
			if ( progressDialog.isShowing() ) {
				progressDialog.dismiss();
			}//if
		} catch (IllegalArgumentException e) {
			//can happen sometimes, and nothing to get against it
		}//catch
		super.onCancelled();
	}//met
	
	private class OnCancelHandler implements OnCancelListener {
		@Override
		public void onCancel(DialogInterface dialog) {
			cancel( true );
		}//met
	}//inner class
}//class
