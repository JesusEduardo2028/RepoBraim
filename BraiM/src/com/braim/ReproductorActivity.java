package com.braim;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;



import com.braim.HttpActivity.InterfazClasificacion;
import com.braim.deezer.data.Playlist;
import com.braim.deezer.data.Thumbnailable;
import com.braim.deezer.data.Track;
import com.braim.deezer.io.ListDeezerDataReader;
import com.braim.deezer.ui.PlayerView;
import com.braim.deezer.ui.ThumbFetcher;
import com.braim.deezer.ui.event.ThumbFetcherListener;
import com.braim.fragments.MenuSlideFragment;
import com.braim.fragments.TrackFragment;

import com.deezer.sdk.AsyncDeezerTask;
import com.deezer.sdk.DeezerError;
import com.deezer.sdk.DeezerRequest;
import com.deezer.sdk.OAuthException;
import com.deezer.sdk.RequestListener;
import com.deezer.sdk.SessionStore;
import com.deezer.sdk.player.Player;
import com.deezer.sdk.player.TooManyPlayersExceptions;
import com.deezer.sdk.player.event.PlayerState;
import com.deezer.sdk.player.impl.DefaultPlayerFactory;
import com.deezer.sdk.player.networkcheck.WifiAndMobileNetworkStateChecker;

import com.example.pruebasherlock.R;



import android.R.anim;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.AnimatorSet.Builder;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources.Theme;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.GetChars;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SlidingDrawer.OnDrawerCloseListener;

import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.SlidingDrawer;

import android.widget.TextView;
import android.widget.Toast;

public class ReproductorActivity extends FragmentActivity implements OnItemClickListener, OnItemLongClickListener,InterfazClasificacion
{
//,BlInterface{
	
	public static final String PLAYLIST_ID = "PLAYLIST_ID";
	public static final String PLAYLIST_TITLE = "PLAYLIST_TITLE";
	public static final String PLAYLIST_COVER = "PLAYLIST_COVER";
	public static int back = 0;
	
	private RequestListener trackRequesListenertHandler = new trackRequestHandler();
	private static List<Track> listTrack = new ArrayList<Track>();
	public static ArrayAdapter<Track>  arrayAdapterTrack = null;
	
	private ThumbFetcher thumbFetcher;
	//Deezer  Player 
	protected Player player;
	protected static PlayerView playerView;
	private ImageView portada_imagen_playlist;
	private TextView nombre_imagen_playlist;
	private TrackFragmentAdapter track_adapter_fragment;
	private static ViewPager vp;
	private  AnimatorSet animatorSet;
	private TextView numero_canciones;

	//Bluethooth items
	private TextView epocInfo_title;

	private int posicion;
	private TextView txtConexion;
	private TextView txt_arousal;
	private TextView txt_valence;
	public static Activity a;
	
	private static String usuario = "null";
	
	public  Drawable TrackImage(String url){
		try{
			InputStream is =(InputStream) new URL(url).getContent();
			Drawable d = Drawable.createFromStream(is, null);
			return d;
		}catch(Exception e){
			
			return null;
		}
	}
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		a = this;
		HttpActivity.actualizar_interfaz_class();
		setContentView(R.layout.layout_activity_reproductor);
		
	
		posicion= getIntent().getIntExtra("posicion", 0);
		TextView textVacio = new TextView(this);
		textVacio.setText("Lista Vacia...");
		thumbFetcher  = new ThumbFetcher(this);
		new SessionStore().restore( BaseActivity.deezerConnect, this);
	
		numero_canciones = (TextView) findViewById(R.id.numero_playlist);
		
		txt_arousal = (TextView) findViewById(R.id.textView2);
		txt_valence = (TextView) findViewById(R.id.textView3);
		
		ImageView imgAnimacion = (ImageView) findViewById(R.id.animacion_img);
		animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.animacion3d);
		
		animatorSet.setTarget(imgAnimacion);
	
		MenuSlideFragment.colocarVista();
		
	//	final ListView listaCanciones = (ListView) findViewById(R.id.listView1);
	//	listaCanciones.setEmptyView(textVacio);
		 vp = new ViewPager(this);
		vp.setId("VP".hashCode());
		
		ViewGroup layout = (ViewGroup) findViewById(R.id.container_tracks);
		vp.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		layout.addView(vp);
		
		arrayAdapterTrack = new ArrayAdapter<Track>(this, R.layout.thumbnailable_user_track_view,listTrack){
		
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
				nameHolder.setText( track.getArtist().getThumbnailUrl());
				nameHolder.setCompoundDrawablesWithIntrinsicBounds( thumbnail, null, null, null );
				return convertView;
			}//met

		};
	//	listaCanciones.setAdapter(arrayAdapterTrack);
		
	//	listaCanciones.setOnItemClickListener(this);
	//	listaCanciones.setOnItemLongClickListener(this);
		
		playerView = (PlayerView) findViewById(R.id.playerView1);
	
		crearReproductor();
		Drawable imagen_playlist = null;
		
		//******busqueda  de canciones para mis listas de reproduccion**********///////
		Playlist playlist = new Playlist();
		playlist.setId(getIntent().getLongExtra(ReproductorActivity.PLAYLIST_ID, -1));
		playlist.setTitle(getIntent().getStringExtra(ReproductorActivity.PLAYLIST_TITLE));
		playlist.setPicture(getIntent().getStringExtra(ReproductorActivity.PLAYLIST_COVER));
		
		imagen_playlist = thumbFetcher.getThumbnail(playlist);
	

		portada_imagen_playlist = (ImageView) findViewById(R.id.imagen_playlist);
		nombre_imagen_playlist = (TextView) findViewById(R.id.nombre_playlist);
		
		portada_imagen_playlist.setImageDrawable(imagen_playlist);
		nombre_imagen_playlist.setText(playlist.getTitle());
		
		//Bl configuration
		epocInfo_title = (TextView) findViewById(R.id.epoc_title);


		usuario  = getIntent().getStringExtra("user");
		txtConexion = (TextView) findViewById(R.id.txtConexion);

        if (comprobarConexion()){
	//	configurarBlConection();
        	cambiarInterfazBL();
	//	animatorSet.cancel();
        }else{
        	;
        	cambiarInterfazBL_Denied();
    		
    		 }
        
		//	declarar_salida();
	//	declarar_salida_decryp();
//        menuDeslizable = (SlidingDrawer) findViewById(R.id.slidingDrawer1);
//        menuDeslizable.setOnDrawerCloseListener(new OnDrawerCloseListener() {
//			
//			@Override
//			public void onDrawerClosed() {
//				// TODO Auto-generated method stub
//				if (btConn!=null){
//					enviarUsuario();
//				}
//			}
//		});
		buscarCanciones(playlist);
		back =0;
	
	}
	
	public void cambiarInterfazBL(){
		
		epocInfo_title.setText("Conectado con el servidor");
		txtConexion.setVisibility(View.GONE);
		animatorSet.start();
		HttpActivity.s.Snd_txt_Msg("Class");
		
	}
	
	public void cambiarInterfazBL_Denied(){
		
		epocInfo_title.setText("Sin conexion con servidor, Presione Menu acceder a configuracion");
  		txtConexion.setVisibility(View.VISIBLE);
//		Intent i = new Intent(this, ConfigurationActivity.class);
//		startActivityForResult(i, 1);
		
	}

	    
	public void iniciarAimacion(View v){
		animatorSet.start();
	}
	
	
	private void buscarCanciones(Playlist playlist) {
		// TODO Auto-generated method stub
		long playlistId = playlist.getId();
		
		AsyncDeezerTask buscarTracks = new AsyncDeezerTaskWithDialog(this, BaseActivity.deezerConnect,trackRequesListenertHandler );
		
		DeezerRequest request = new DeezerRequest("playlist/"+playlistId+"/tracks");
		request.setId(String.valueOf(playlistId));
		buscarTracks.execute(request);
		
	}
	
	public void busquedaCancionesTerminada(List<Track> listTracks) {
		// TODO Auto-generated method stub
		
//		if (comprobarEpoc()){
//			animatorSet.start();
//			epocInfo.setText("Datos de llegada Emotiv Epoc");
//		}else{
//			epocInfo.setText("No hay ningun Dispositivo Epoc conectado, por favor conectelo y vuelva al menu de Reproduccion");
//		}
		listTrack.clear();
		try{
			listTrack.addAll(listTracks);
			if(listTrack.isEmpty()){
				Toast.makeText(this, "no hay canciones", Toast.LENGTH_SHORT).show();
				
			}else{
			
			thumbFetcher.startFetchingThumbnails(listTrack, new ThumbNailFletcherHAndler_Tracks());
			}arrayAdapterTrack.notifyDataSetChanged();
			numero_canciones.setText(listTrack.size()+ " canciones");
			ponerFragments();
			vp.setCurrentItem(posicion);
		}
			
			catch (Exception e) {
				// TODO: handle exception
			}
	
	}
	
	private void ponerFragments() {
		// TODO Auto-generated method stub
		track_adapter_fragment = new TrackFragmentAdapter(getSupportFragmentManager());
		Track track = listTrack.get(0);
		playTrack(track);
		vp.setAdapter(track_adapter_fragment);
		vp.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				Track track = listTrack.get(arg0);
				playTrack(track);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		vp.setCurrentItem(0);
	}

	private void crearReproductor() {
		// TODO Auto-generated method stub
		try {
			try {
				player = new DefaultPlayerFactory(getApplication(), 
						BaseActivity.deezerConnect, new WifiAndMobileNetworkStateChecker()).createPlayer();
			} catch (TooManyPlayersExceptions e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				handleError(e);
			}
			playerView.setPlayer(player);
			
		}catch(OAuthException e){
			handleError(e);
		
		}catch(DeezerError e){
			handleError(e);
		}
		
	}

	private void handleError(Exception e) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), "Error: "+e, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		Track track = listTrack.get(position);
		playTrack(track);
	}

	private void playTrack(Track track) {
		// TODO Auto-generated method stub

		 if (player.getPlayerState() != PlayerState.STARTED){
			player.stop();
		//	animatorSet.end();
			
			}
		 if (track.hasStream()){
			 player.init(track.getId(), track.getStream());
			 
		 }else{
			 player.init(track.getId(), track.getPreview());
		//	 Toast.makeText(this, "Solo puede escuchar 30 segundos", Toast.LENGTH_SHORT).show();
			sendPause();
		//		animatorSet.start();
		 }
		// playerView.setTrackName(track.getTitle());
		 playerView.setTrackName(track.getId()+"");
		 if (track.getArtist() != null){
		// 	 playerView.setArtistName(track.getArtist().getName());
			 
		 }else{
		//	 playerView.setArtistName(null);
			 
		 }
		 player.setPlayerProgressInterval(1000);
	//	 player.play();
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (comprobarConexion()){
		HttpActivity.s.Snd_txt_Msg("cancel_class");
		}
		destruirPlayer();
		super.onDestroy();
	}

	protected void destruirPlayer() {
		// TODO Auto-generated method stub
		if (player != null && player.getPlayerState() != PlayerState.STOPPED){
			player.stop();
			
		}
		player.release();
	//	Toast.makeText(this, "player destruido", Toast.LENGTH_SHORT).show();
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

	private class ThumbNailFletcherHAndler_Tracks implements ThumbFetcherListener{

		@Override
		public void thumbLoaded(Thumbnailable thumbnailable) {
			// TODO Auto-generated method stub
			arrayAdapterTrack.notifyDataSetChanged();
			
		}
	
	}
	
	public class TrackFragmentAdapter extends FragmentPagerAdapter{

			
		private ArrayList<Fragment> listaFragments;
		
		public TrackFragmentAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
			listaFragments= new ArrayList<Fragment>();
			for (Track track: listTrack){
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

	public static void sendPlay() {
        try {
        	if (comprobarConexion()){
          enviar("Play");}
        } catch (Exception e) { }
      }
    
 	public static void sendPause() {
        try {
        	if (comprobarConexion()){
        		enviar("Pause");}
        } catch (Exception e) { }
      }

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		super.onBackPressed();
		
	}
	
	public static void sendTrack(String track){
		
		
		try{
			if (comprobarConexion()){
				enviar("T: "+track);
			
		//	btConn.send("U: "+usuario);
		//	btConn.send("User: "+usuario);		
			
			}
		}catch(Exception e){}
		
		}
	
	public void desconexionBl(View v){
//		
//	btConn.send("End");
//		
//	btConn.close();
//	btConn = null;
//	ConfigurationActivity.btConn =null;
	Intent i = getIntent();
	finish();
	startActivity(i);
	}
	
	public void entrenar(View v){
		
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      builder.setMessage("Va a iniciar el entrenamiento: duracion 2 min");
      builder.setCancelable(false);
      builder.setPositiveButton("Yes",
              new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int id) {
                     Intent i = new Intent(getApplicationContext(),EntrenamientoActivity.class);
                     startActivity(i);
                     try {
                     	if (comprobarConexion()){
                     		enviar("Train");}
                     } catch (Exception e) { }
                     
                  }
              });
      builder.setNegativeButton("No",
              new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int id) {
                      dialog.cancel();
                  }
              });
      AlertDialog alertDialog = builder.create();
      alertDialog.show();
      
        
		
	}
	
	public static void enviarUsuario(){
		

		try{
			if (comprobarConexion()){
				enviar("U: "+usuario);
			
	
			}
		}catch(Exception e){}
		
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
			MainActivity.conexion = false;
			back=1;
			onBackPressed();
			break;

		default:
			break;
		}
		
		return true;
		// TODO Auto-generated method stub
		
	}

	   private static void enviar(String string) {
			// TODO Auto-generated method stub
		   HttpActivity.s.Snd_txt_Msg(string);
		}

		private static boolean comprobarConexion() {
			if (HttpActivity.s.conectStatus){
				return true;
			}else{
			return false;
		}
		}


		@Override
		public void emocionTerminada(String e) {
			// TODO Auto-generated method stub
			if (e.equals("emo_pa")){
				txt_arousal.setText("Arousal : +");
			}else if (e.equals("emo_na")){
				txt_arousal.setText("Arousal : -");
			}else if ((e.equals("emo_pv"))){
				txt_valence.setText("Valence : +");
			}else if ((e.equals("emo_nv"))){
				txt_valence.setText("Valence : -");
			}
		}

	
	
	}

    
    
	


