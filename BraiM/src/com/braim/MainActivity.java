package com.braim;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.braim.deezer.data.Album;
import com.braim.deezer.data.Artist;
import com.braim.deezer.data.Thumbnailable;
import com.braim.deezer.data.Track;
import com.braim.deezer.data.User;
import com.braim.deezer.io.DeezerDataReader;
import com.braim.deezer.io.ListDeezerDataReader;
import com.braim.deezer.ui.ThumbFetcher;
import com.braim.deezer.ui.event.ThumbFetcherListener;
import com.braim.fragments.AlbumFragment;
import com.braim.fragments.AlbumFragment.InterfazBusquedaCanciones;
import com.braim.fragments.ArtistaFragment;
import com.braim.fragments.ArtistaFragment.InterfazBusquedaListas;
import com.braim.fragments.MenuSlideFragment;
import com.braim.fragments.ProfileFragment;
import com.braim.fragments.TrackAlbumFragment;
import com.braim.fragments.SearchFragment.InterfazBusquedaArtsita;
import com.braim.fragments.TrackFragment;
import com.braim.utils.ImageLoader;
import com.deezer.sdk.AsyncDeezerTask;
import com.deezer.sdk.DeezerError;
import com.deezer.sdk.DeezerRequest;
import com.deezer.sdk.OAuthException;
import com.deezer.sdk.RequestListener;
import com.deezer.sdk.SessionStore;
import com.example.pruebasherlock.R;

public class MainActivity extends BaseActivity  implements InterfazBusquedaArtsita, InterfazBusquedaListas, InterfazBusquedaCanciones{
	
	private Fragment contenido;
	

	//declaracion callback consultar usuario
	private RequestListener userRequestListenerHandler = new UserRequestHandler();
	//Objeto para gestionar las miniatiras de las fotos del usuario
	private ThumbFetcher thumbFetcher;
	public static  String nombre_user;
	public static String apellido_user;
	public static Drawable image_user;
	public static String user_id;
	

	
	//declaracion callback consultar playlist
		private  RequestListener playlistRequestListenerHAndler = new PlayListRequestHandler();
	//Objeto para gestionar las miniatiras de las fotos de las Playlist	
	
		public static List<Album> listPlaylist =  new ArrayList<Album>();
		public static  ArrayAdapter<Album> arrayAdapterPlayList = null; 
		
		
		//declaracion callback consultar amigos 
		private RequestListener userlistRequestListenerHandler = new UserListRequestHandler();
		public static List<User> listUsers = new  ArrayList<User>();
		//Objeto para gestionar las miniaturas de las fotos de los amigos en Deezer
	
		public static ArrayAdapter<User> arrayAdapterUsers = null;
		
		//declaracion callback consultar lista canciones
		
		private RequestListener trackRequesListenertHandler_recommendation = new trackRequestHandler();
		private List<Track> listTrack_recommendation = new ArrayList<Track>();


		private long  playlistId = 4341978;


		public static ArrayAdapter<Artist> arrayAdapterArtist;
		public static  List<Artist> listArtist = new ArrayList<Artist>();
		private  RequestListener artistSearchRequestHandler = new ArtistSearchRequestHandler();


		private AlertDialog mensaje;

		private static MainActivity a;


		public static boolean conexion =false;
		protected static boolean conexionExitosa;
		public static ArrayAdapter<Track>  arrayAdapterTrack_recommendation = null;
		
		
		private RequestListener trackLookupRequestHandler = new TrackLookupRequestHandler();


		private Album new_album;
		/** The list of albums of displayed by this activity. */
		public static List<Track> listTrack = new ArrayList<Track>();
		/** The adapter of the main list view. */
		public static ArrayAdapter<Track> arrayAdapterTrack = null;
	
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setSlidingActionBarEnabled(true);
		
		a = this;
		
		if (savedInstanceState != null){
			contenido = getSupportFragmentManager().getFragment(savedInstanceState, "contenido");
			
		}
		if (contenido == null){
			contenido = new ProfileFragment();
		}
		setContentView(R.layout.layout_activity_main);
		getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, contenido).commit();
		
		
		
		thumbFetcher = new ThumbFetcher(this);
		new SessionStore().restore(deezerConnect, this);
		

		arrayAdapterPlayList = new ArrayAdapter<Album>(this, R.layout.thumbnailable_list_item_view, listPlaylist) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				//provide a compund view associated to an artist
				Album playlist= getItem(position);
				if (convertView == null ) {
					LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					convertView = vi.inflate(R.layout.thumbnailable_list_item_view, null);
				}//if

				Drawable thumbnail = thumbFetcher.getThumbnail( playlist ); 
				
				String url = playlist.getThumbnailUrl();
				int loader = R.drawable.abs__spinner_48_inner_holo;
				ImageView image = (ImageView) convertView.findViewById(R.id.imageView1);
				ImageLoader imgloader= new ImageLoader(getApplicationContext());
				imgloader.DisplayImage(url, loader, image);
				
				TextView nameHolder = (TextView) convertView.findViewById( R.id.t_text_view);
				nameHolder.setText( playlist.getTitle() );
				
				return convertView;
			}//met

		};
		
		arrayAdapterUsers = new ArrayAdapter<User>(this, R.layout.thumbnailable_user_item_view, listUsers) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				//provide a compund view associated to an artist
				User Amigo= getItem(position);
				if (convertView == null ) {
					LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					convertView = vi.inflate(R.layout.thumbnailable_list_item_view, null);
				}//if

				Drawable thumbnail = thumbFetcher.getThumbnail( Amigo ); 

				TextView nameHolder = (TextView) convertView.findViewById( R.id.t_text_view);
				String nombreAmigo = Amigo.getFirstname()+"";
				if (nombreAmigo.equals("null")){
					nombreAmigo = "Amigo "+position;
				}
				
				
				String url = Amigo.getThumbnailUrl();
				int loader = R.drawable.abs__spinner_48_inner_holo;
				ImageView image = (ImageView) convertView.findViewById(R.id.imageView1);
				ImageLoader imgloader= new ImageLoader(getApplicationContext());
				imgloader.DisplayImage(url, loader, image);
				
				nameHolder.setText( nombreAmigo);
				
				return convertView;
			}//met

		};
		arrayAdapterTrack_recommendation = new ArrayAdapter<Track>(this, R.layout.thumbnailable_user_track_view,listTrack_recommendation){
			
			

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				//provide a compund view associated to an artist
				
				Track track = getItem(position);
				if (convertView == null ) {
					LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					convertView = vi.inflate(R.layout.thumbnailable_user_track_view, null);
				}//if
			
				Drawable thumbnail =  thumbFetcher.getThumbnail(track.getArtist());
				
				TextView nameHolder = (TextView) convertView.findViewById( R.id.t_text_view);
				TextView autorHolder = (TextView) convertView.findViewById( R.id.t_text_view_autor);
				
				nameHolder.setText( track.getTitle());
				View emocion = new ImageView(getApplicationContext());
				emocion.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
				
				if (position == 0 || position ==2){
					emocion.setBackgroundDrawable(getApplication().getResources().getDrawable(R.drawable.mad));
				}else{
				emocion.setBackgroundDrawable(getApplication().getResources().getDrawable(R.drawable.happy));}
				
				ViewGroup v = (ViewGroup) convertView.findViewById(R.id.contenedor);
				v.addView(emocion);
				autorHolder.setText(track.getArtist().getName());
				String url = track.getArtist().getThumbnailUrl();
				
				int loader = R.drawable.abs__spinner_48_inner_holo;
				ImageView image = (ImageView) convertView.findViewById(R.id.imageView1);
				ImageLoader imgloader= new ImageLoader(getApplicationContext());
				imgloader.DisplayImage(url, loader, image);
				return convertView;
			}//met

		};
		
		
		arrayAdapterArtist = new ArrayAdapter<Artist>(this,
				R.layout.thumbnailable_list_item_view, listArtist) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				//provide a compund view associated to an artist
				Artist artist= getItem(position);

				if (convertView == null ) {
					LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					convertView = vi.inflate(R.layout.thumbnailable_list_item_view, null);
				}//if

				String url = artist.getThumbnailUrl();
				int loader = R.drawable.abs__spinner_48_inner_holo;
				ImageView image = (ImageView) convertView.findViewById(R.id.imageView1);
				ImageLoader imgloader= new ImageLoader(getApplicationContext());
				imgloader.DisplayImage(url, loader, image);
				
				Drawable thumbnail = thumbFetcher.getThumbnail( artist ); 
				TextView nameHolder = (TextView) convertView.findViewById( R.id.t_text_view);
				nameHolder.setText( artist.getName() );


				return convertView;
			}//met
			
			

		};
		
		arrayAdapterTrack = new ArrayAdapter<Track>(this,
				android.R.layout.simple_list_item_1, listTrack);

		
	 buscarUsuario();
	 buscarAmigos();
	 buscarCanciones();
	}
	


	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", contenido);
	}
	
	
	private void buscarCanciones() {
		// TODO Auto-generated method stub
	//	long playlistId = playlist.getId();
		
		AsyncDeezerTask buscarTracks = new AsyncDeezerTaskWithDialog(this, BaseActivity.deezerConnect,trackRequesListenertHandler_recommendation );
		
		DeezerRequest request = new DeezerRequest("playlist/"+ playlistId+"/tracks");
		request.setId(String.valueOf(playlistId));
		buscarTracks.execute(request);
		
	}
	
	public void busquedaCancionesTerminada(List<Track> listTracks) {
		// TODO Auto-generated method stub
		
		listTrack_recommendation.clear();
		try{
			listTrack_recommendation.addAll(listTracks);
			if(listTrack_recommendation.isEmpty()){
				Toast.makeText(this, "no hay canciones", Toast.LENGTH_SHORT).show();
				
			}arrayAdapterTrack_recommendation.notifyDataSetChanged();
		
		//	ponerFragments();
		}
			
			catch (Exception e) {
				// TODO: handle exception
			}
	
	}

	private void buscarUsuario() {
		// TODO Auto-generated method stub
		
		AsyncDeezerTask busquedaAsincronaUser = new AsyncDeezerTaskWithDialog(this, deezerConnect, userRequestListenerHandler);
		DeezerRequest request = new DeezerRequest("user/me");
		busquedaAsincronaUser.execute(request);
		
	}

	

	public void busquedaDeUsuarioTerminada(User user) {
		// TODO Auto-generated method stub
		nombre_user = user.getFirstname();
		apellido_user = user.getLastname();
		
		
		
		
	}	
	

	private void buscarAmigos() {
		// TODO Auto-generated method stub
		AsyncDeezerTask busquedaAmigosAsyncDeezerTask = new AsyncDeezerTaskWithDialog(this, deezerConnect, userlistRequestListenerHandler);
		DeezerRequest request = new DeezerRequest("user/me/followings");
		busquedaAmigosAsyncDeezerTask.execute(request);
		
	}
	public void busquedaAmigosTerminada(List<User> listaRecibida ) {
		// TODO Auto-generated method stub
		listUsers.clear();
		try{
			listUsers.addAll(listaRecibida);
			if(listUsers.isEmpty()){
				Toast.makeText(this, "no tiene amigos xD", Toast.LENGTH_SHORT).show();
				
			}
			arrayAdapterUsers.notifyDataSetChanged();
		}catch (Exception e){
			handleError(e);
		}
		
	}

//
//	private void buscarPlayLists() {
//		// TODO Auto-generated method stub
//		
//		AsyncDeezerTask busquedaPlayListAsyncDeezerTask = new AsyncDeezerTaskWithDialog(this, deezerConnect, playlistRequestListenerHAndler);
//		DeezerRequest request = new DeezerRequest("user/me/playlists");
//		busquedaPlayListAsyncDeezerTask.execute(request);
//		
//		
//		
//	}

	
	
//**********************************CLASE de peticiones callback para los usuarios *******************************	
	private class UserRequestHandler implements RequestListener{

		@Override
		public void onComplete(String respuesta, Object arg1) {
			// TODO Auto-generated method stub
			
			try{
				
				//Obtengo la respuesta en Json y saco el usuario mediante la clase de ayuda
				User user = new DeezerDataReader<User>(User.class).read(respuesta);
				
				busquedaDeUsuarioTerminada(user);
				thumbFetcher.startFetchingThumbnails(user, new ThumbNailFletcherHAndler());
			}catch (IllegalStateException e){
				handleError(e);
				e.printStackTrace();
			}
			
		}

		@Override
		public void onDeezerError(DeezerError e, Object arg1) {
			// TODO Auto-generated method stub
			handleError(e);
		}

		@Override
		public void onIOException(IOException e, Object arg1) {
			// TODO Auto-generated method stub
			handleError(e);
		}

		@Override
		public void onMalformedURLException(MalformedURLException e,
				Object arg1) {
			// TODO Auto-generated method stub
			handleError(e);
			
		}

		@Override
		public void onOAuthException(OAuthException e, Object arg1) {
			// TODO Auto-generated method stub
			handleError(e);
		}
		
	}


	//*************************************Clase de ayuda Callback para las playlist*************************
	
	private class PlayListRequestHandler implements RequestListener{

		@Override
		public void onComplete(String respuesta, Object arg1) {
			// TODO Auto-generated method stub
			
			List<Album> lisPlaylists = null;
			
			try {
				lisPlaylists = new ListDeezerDataReader<Album>(Album.class).readList(respuesta);
				buscarPlayListTerminado(lisPlaylists);
				
			}catch(IllegalStateException exception){
				handleError(exception);
			}
			
		}

		@Override
		public void onDeezerError(DeezerError e, Object arg1) {
			// TODO Auto-generated method stub
			handleError(e);
			
		}

		@Override
		public void onIOException(IOException e, Object arg1) {
			// TODO Auto-generated method stub
			handleError(e);
		}

		@Override
		public void onMalformedURLException(MalformedURLException e,
				Object arg1) {
			// TODO Auto-generated method stub
			handleError(e);
		}

		@Override
		public void onOAuthException(OAuthException e, Object arg1) {
			// TODO Auto-generated method stub
			handleError(e);
		}
		
		
	}
	
	
	//*************************************************************************************+
	
	//***************************Clase de ayuda para los callbak de la lista de amigos*+****
	
	
	private class UserListRequestHandler implements RequestListener{

		@Override
		public void onComplete(String respuesta, Object arg1) {
			// TODO Auto-generated method stub
			List<User> listUsuarios = null;
			try{
				listUsuarios = new ListDeezerDataReader<User>(User.class).readList(respuesta);
				busquedaAmigosTerminada(listUsuarios);
				
			}catch (IllegalStateException e){
				handleError(e);
			}
		}

		@Override
		public void onDeezerError(DeezerError e, Object arg1) {
			// TODO Auto-generated method stub
			handleError(e);
		}

		@Override
		public void onIOException(IOException e, Object arg1) {
			// TODO Auto-generated method stub
			handleError(e);
			
		}

		@Override
		public void onMalformedURLException(MalformedURLException e,
				Object arg1) {
			// TODO Auto-generated method stub
			handleError(e);
		}

		@Override
		public void onOAuthException(OAuthException e, Object arg1) {
			// TODO Auto-generated method stub
			handleError(e);
			reconectarseDeezer();
		}
		
	}

//	***************************Clase para busqueda de artista*************
	private class ArtistSearchRequestHandler implements RequestListener {
		@Override
		public void onComplete(String response, Object state) {
			List<Artist> listArtist = null;
			try {
		
				listArtist = new ListDeezerDataReader<Artist>( Artist.class ).readList( response );
			
				artistSearchComplete( listArtist );
			} catch (IllegalStateException e) {
				handleError( e );
				e.printStackTrace();
			}//catch
		}//met

		@Override
		public void onIOException(IOException e, Object state) {
			handleError( e );			
		}//met

		@Override
		public void onMalformedURLException(MalformedURLException e,Object state) {
			handleError( e );
		}//met

		@Override
		public void onOAuthException(OAuthException e, Object state) {
			handleError( e );

		}//met
		
		@Override
		public void onDeezerError(DeezerError e, Object state) {
			handleError( e );
		}//met
	}//inner class
	
	
	
	//**********************************************************************************
	//Clase de busqueda para la lista del album****************************************
	
	private class TrackLookupRequestHandler implements RequestListener {
		@Override
		public void onComplete(String response, Object state) {
			List<Track> listTrack = null;
			try {
			
				listTrack = new ListDeezerDataReader<Track>( Track.class ).readList( response );
				
				trackSearchComplete(listTrack);
			} catch (IllegalStateException e) {
				handleError( e );
			}//catch
		}//met

		@Override
		public void onIOException(IOException e, Object state) {
			handleError( e );			
		}//met

		@Override
		public void onMalformedURLException(MalformedURLException e, Object state) {
			handleError( e );
		}//met

		@Override
		public void onOAuthException(OAuthException e, Object state) {
			handleError( e );
	
		}//met

		@Override
		public void onDeezerError(DeezerError e, Object state) {
			handleError( e );
		}//met
	}//inner class
	
	//Escuchador de la foto miniatura
	private class ThumbNailFletcherHAndler implements ThumbFetcherListener{

		

		@Override
		public void thumbLoaded(Thumbnailable thumbnailable) {
			
			// TODO Auto-generated method stub
			 image_user = thumbFetcher.getThumbnail(thumbnailable);
			 MenuSlideFragment.revisarUser();
			 ProfileFragment.revisarUser();
		}
		
	}

		
		public void switchContent(Fragment nuevoContenido) {
			// TODO Auto-generated method stub
			
			contenido = nuevoContenido;
			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, nuevoContenido).commit();
			getSlidingMenu().showContent();
			
		}

	

		public void switchContentAmigos(Fragment nuevoContenido) {
			// TODO Auto-generated method stub
			contenido = nuevoContenido;
			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, nuevoContenido).commit();
			getSlidingMenu().showContent();
		}
		
		
		private class trackRequestHandler implements RequestListener{

			@Override
			public void onComplete(String respuesta_json, Object arg1) {
				// TODO Auto-generated method stub
				List<Track> listTracks = null;
				try{
					listTracks = new ListDeezerDataReader<Track>(Track.class).readList(respuesta_json);
					busquedaCancionesTerminada(listTracks);
				}catch (IllegalStateException e){
					handleError(e);
				}
				
			}

			@Override
			public void onDeezerError(DeezerError e, Object arg1) {
				// TODO Auto-generated method stub
				handleError(e);
				
			}

			@Override
			public void onIOException(IOException e, Object arg1) {
				// TODO Auto-generated method stub
				handleError(e);
				
			}

			@Override
			public void onMalformedURLException(MalformedURLException e,
					Object arg1) {
				// TODO Auto-generated method stub
				handleError(e);
			}

			@Override
			public void onOAuthException(OAuthException e, Object arg1) {
				// TODO Auto-generated method stub
				handleError(e);
			}
			
			
			
		}


		
		public class TrackFragmentAdapter extends FragmentPagerAdapter{

				
			private ArrayList<Fragment> listaFragments;
			
			public TrackFragmentAdapter(FragmentManager fm) {
				super(fm);
				// TODO Auto-generated constructor stub
				listaFragments= new ArrayList<Fragment>();
				for (Track track: listTrack_recommendation){
					listaFragments.add(new TrackFragment(track));
				}
			}

			@Override
			public Fragment getItem(int arg0) {
				// TODO Auto-generated method stub
				
				
				return listaFragments.get(arg0);
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return listaFragments.size();
			}
			
		}

		

		public void onClick_entrenar(View v){
			conexionExitosa = false;
			
				
			if (conexion){
				
				HttpActivity.s.Snd_txt_Msg("Con_train");
				Intent i = new Intent(this, EntrenamientoActivity.class);
				startActivity(i);
				
			}else{
				
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
			      builder.setMessage("Debes seleccionar un dispositivo para el procesamiento de señales");
			      builder.setCancelable(false);
			      builder.setPositiveButton("Configurar",
			              new DialogInterface.OnClickListener() {
			                  public void onClick(DialogInterface dialog, int id) {
			                   
			                		conectionJavaProcesor();
			                  }
			              });
			      builder.setNegativeButton("Cancelar",
			              new DialogInterface.OnClickListener() {
			                  public void onClick(DialogInterface dialog, int id) {
			                      dialog.cancel();
			                  }
			              });
			      AlertDialog alertDialog = builder.create();
			      alertDialog.show();
		        
		                    }
			//	Toast.makeText(this, "no puede entrenar sin estar conectado con el servidor de procesamiento", Toast.LENGTH_LONG).show();
		
			} 
			
	


		@Override
		protected void onActivityResult(int arg0, int arg1, Intent arg2) {
			// TODO Auto-generated method stub
			
			
			if (arg1 ==0){
				conexion = false;
			//	Toast.makeText(this, "Sin conexion", Toast.LENGTH_SHORT).show();
				ProfileFragment.cambiarEstado(false);
				MenuSlideFragment.cambiarEstado(false);
				
				
			}else{
				
				conexion = true;
			//	Toast.makeText(this, "Conectado con servidor", Toast.LENGTH_SHORT).show();
				ProfileFragment.cambiarEstado(true);
				MenuSlideFragment.cambiarEstado(true);
		
			}
			
		}
		
		
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// TODO Auto-generated method stub
			
			
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.menu_reproductor, menu);
			return true;
		}


		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case R.id.conexion:
			//	MainActivity.conexion = false;
			//	Intent i= new Intent(this, MainActivity.class);
			//	startActivity(i);
				
			
				conectionJavaProcesor();
				
			break;

			default:
				break;
			}
			
			return true;
			// TODO Auto-generated method stub
			
		}
		
		



		private void conectionJavaProcesor() {
			// TODO Auto-generated method stub
			Intent i = new Intent(this, HttpActivity.class);
			startActivityForResult(i, 0);
		}



	
		@Override
		public void buscarArtista(String artista) {
			// TODO Auto-generated method stub
			
			
		
				AsyncDeezerTask artistSearchTask = new AsyncDeezerTaskWithDialog(this, deezerConnect, artistSearchRequestHandler  );
				Bundle bundle = new Bundle();
				bundle.putString( "q", artista );
				bundle.putString( "nb_items", "30");
				DeezerRequest request = new DeezerRequest( "search/artist", bundle );
				request.setId( artista );
				artistSearchTask.execute( request );
				
			
		}


		public void artistSearchComplete(List<Artist> listArtistReceived) {
			// TODO Auto-generated method stub
			listArtist.clear();

			try {
				listArtist.addAll( listArtistReceived );
				if( listArtist.isEmpty() ) {
					Toast.makeText( MainActivity.this, getResources().getString( R.string.no_result ), Toast.LENGTH_LONG );
				} else {
					//async pick thumb images of artists, will launch event for each downloaded thumb
		//			thumbFetcher.startFetchingThumbnails(listArtist, new ThumbnailFetcherHandler_artist() );
					contenido = new ArtistaFragment();
					getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, contenido).addToBackStack("tag").commit();
				}//else
				arrayAdapterArtist.notifyDataSetChanged();
			} catch (Exception exception) {
				handleError(exception);
			}//catch
		}



		@Override
		public void buscarlistas(Artist artista) {
			// TODO Auto-generated method stub
			long artistId = artista.getId();
			AsyncDeezerTask albumLookupTask = new AsyncDeezerTaskWithDialog( this, deezerConnect, playlistRequestListenerHAndler );
			DeezerRequest request = new DeezerRequest( "artist/"+artistId+"/albums" );
			request.setId( String.valueOf( artistId ) );
			albumLookupTask.execute( request );
		}
		
		
		public void buscarPlayListTerminado(List<Album> lisPlaylists_recibidas) {
			// TODO Auto-generated method stub
			listPlaylist.clear();
			
			try{
				listPlaylist.addAll(lisPlaylists_recibidas);
				if(listPlaylist.isEmpty()){
					Toast.makeText(MainActivity.this,"Sin resultados" , Toast.LENGTH_SHORT).show();
					
				}else{
		//			thumbFetcher.startFetchingThumbnails(listPlaylist, new ThumbNailFletcherHAndler_PLayList());
					contenido = new AlbumFragment();
					getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, contenido).addToBackStack("tag").commit();
				
				}arrayAdapterPlayList.notifyDataSetChanged();
			}catch(Exception e){
					handleError(e);
				}
				
			
		}



		@Override
		public void buscarCanciones(Album album) {
			// TODO Auto-generated method stub
			new_album = album;
			long albumId = album.getId();
			AsyncDeezerTask trackLookupTask = new AsyncDeezerTaskWithDialog(this, deezerConnect, trackLookupRequestHandler );
			DeezerRequest request = new DeezerRequest( "album/"+albumId+"/tracks");
			request.setId( String.valueOf( albumId ) );
			trackLookupTask.execute( request );
		}
		
			
			private void trackSearchComplete( List<Track> listTrackReceived ) {
				listTrack.clear();
				try {
					listTrack.addAll( listTrackReceived );
					if( listTrack.isEmpty() ) {
						Toast.makeText( this, getResources().getString( R.string.no_result ), Toast.LENGTH_LONG );
					}else{
						contenido = new TrackAlbumFragment();
						Bundle bundle = new Bundle();
						bundle.putLong("albumID", new_album.getId());
						bundle.putString("albumTittle", new_album.getTitle());
						bundle.putString("albumUrl", new_album.getCover());
						contenido.setArguments(bundle);
						getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, contenido).addToBackStack("tag").commit();
					}
					arrayAdapterTrack.notifyDataSetChanged();
				} catch (Exception e) {
					Toast.makeText( this, getResources().getString( R.string.download_error ), Toast.LENGTH_LONG ).show();
					e.printStackTrace();
				}//catch
			}//me

		
}