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
import com.braim.deezer.data.Album;
import com.braim.deezer.data.Playlist;
import com.braim.deezer.data.Thumbnailable;
import com.braim.deezer.data.Track;
import com.braim.deezer.io.ListDeezerDataReader;
import com.braim.deezer.ui.PlayerView;
import com.braim.deezer.ui.ThumbFetcher;
import com.braim.deezer.ui.event.ThumbFetcherListener;
import com.braim.fragments.ListasFragment;
import com.braim.fragments.MenuSlideFragment;
import com.braim.fragments.TrackFragment;
import com.braim.fragments.TrackFragment.InterfazCancion;
import com.braim.utils.Emocion;
import com.braim.utils.ImageLoader;

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
import android.widget.ListAdapter;
import android.widget.SlidingDrawer.OnDrawerCloseListener;

import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.SlidingDrawer;

import android.widget.TextView;
import android.widget.Toast;

public class ReproductorActivity extends FragmentActivity implements OnItemClickListener, OnItemLongClickListener,InterfazClasificacion,InterfazCancion
{
//,BlInterface{
	
	public static final String PLAYLIST_ID = "PLAYLIST_ID";
	public static final String PLAYLIST_TITLE = "PLAYLIST_TITLE";
	public static final String PLAYLIST_COVER = "PLAYLIST_COVER";
	public static List<Emocion> listaEmociones = null;
	public static int back = 0;
	
	private RequestListener trackRequesListenertHandler = new trackRequestHandler();
	private static List<Track> listTrack = new ArrayList<Track>();
	
	
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
	public static Album album;
	private Playlist playlist;
	private Track track;
	private int contador;
	
	private int arousal_p;
	private int arousal_n;
	private int valence_p;
	private int valence_n;
	
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
		
		listaEmociones = new ArrayList<Emocion>();
		contador = 1;
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
		ViewGroup layout = (ViewGroup) findViewById(R.id.container_tracks);
		playerView = (PlayerView) findViewById(R.id.playerView1);
		portada_imagen_playlist = (ImageView) findViewById(R.id.imagen_playlist);
		portada_imagen_playlist.setVisibility(View.GONE);
		nombre_imagen_playlist = (TextView) findViewById(R.id.nombre_playlist);
		txtConexion = (TextView) findViewById(R.id.txtConexion);
		usuario  = getIntent().getStringExtra("user");
		epocInfo_title = (TextView) findViewById(R.id.epoc_title);
		
		
		

		if (getIntent().getLongExtra(ReproductorActivity.PLAYLIST_ID, -1) ==-1){
			
			album = new Album();
			album.setId(getIntent().getLongExtra("albumID", 0));
			album.setTitle(getIntent().getStringExtra("albumTittle"));
			album.setCoverUrl(getIntent().getStringExtra("albumUrl"));
			
		
			nombre_imagen_playlist.setText(album.getTitle());
			back =0;
			buscarCanciones(album);
			
		}else{
			album = null;
	
		playlist = new Playlist();
		playlist.setId(getIntent().getLongExtra(ReproductorActivity.PLAYLIST_ID, -1));
		playlist.setTitle(getIntent().getStringExtra(ReproductorActivity.PLAYLIST_TITLE));
		playlist.setPicture(getIntent().getStringExtra(ReproductorActivity.PLAYLIST_COVER));
	
		
		nombre_imagen_playlist.setText(playlist.getTitle());
		back =0;
		buscarCanciones(playlist);
		}
		MenuSlideFragment.colocarVista();
		animatorSet.setTarget(imgAnimacion);
		
		vp = new ViewPager(this);
		vp.setId("VP".hashCode());
		vp.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		layout.addView(vp);
		crearReproductor();

		
		if (comprobarConexion()){
       	cambiarInterfazBL();
        }else{
        cambiarInterfazBL_Denied();
    	}
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
	private void buscarCanciones (Album album){
		long albumId = album.getId();
		AsyncDeezerTask trackLookupTask = new AsyncDeezerTaskWithDialog(this, BaseActivity.deezerConnect, trackRequesListenertHandler );
		DeezerRequest request = new DeezerRequest( "album/"+albumId+"/tracks");
		request.setId( String.valueOf( albumId ) );
		trackLookupTask.execute( request );
	}
	
	public void busquedaCancionesTerminada(List<Track> listTracks) {
	
		listTrack.clear();
		try{
	
			listTrack.addAll(listTracks);
			if(listTrack.isEmpty()){
				Toast.makeText(this, "no hay canciones", Toast.LENGTH_SHORT).show();
				
			}
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
		
			
			}
		 if (track.hasStream()){
			 player.init(track.getId(), track.getStream());
			 
		 }else{
			 player.init(track.getId(), track.getPreview());
	
			sendPause();
	
		 }
		
		 playerView.setTrackName(track.getId()+"");
		 if (track.getArtist() != null){

			 
		 }else{

			 
		 }
		 player.setPlayerProgressInterval(1000);
	
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
				arousal_p++;
				contador++;
			}else if (e.equals("emo_na")){
				txt_arousal.setText("Arousal : -");
				arousal_n++;
				contador++;
			}else if ((e.equals("emo_pv"))){
				txt_valence.setText("Valence : +");
				valence_p++;
			}else if ((e.equals("emo_nv"))){
				txt_valence.setText("Valence : -");
				valence_n++;
			}
			
			if (contador==5){
				contador=0;
				int a = 0;
				int v = 0;
				if (arousal_p>arousal_n){
					a=1;
				}
				if (valence_p>valence_n){
					v=1;
				}
							
				Emocion emocion = new Emocion(a, v,track);
				listaEmociones.add(emocion);
			Toast.makeText(this, "Emocion publicada", Toast.LENGTH_LONG).show();	
			}
		}


		@Override
		public void actualizarCancion(Track t) {
			// TODO Auto-generated method stub
			track = t;
		}

	
	
	}

    
    
	


