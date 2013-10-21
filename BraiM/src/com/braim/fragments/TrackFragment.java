package com.braim.fragments;

import org.w3c.dom.Text;

import com.braim.ReproductorActivity;
import com.braim.deezer.data.Track;
import com.braim.utils.ImageLoader;
import com.example.pruebasherlock.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class TrackFragment extends Fragment {
	
	private Track track = new Track();
	private View view;
	private Activity a;
	private InterfazCancion interfaz;
	
public TrackFragment(Track cancion){
	track = cancion;
	setRetainInstance(true);
	
}


@Override
public void onAttach(Activity activity) {
	// TODO Auto-generated method stub
	a= activity;
	interfaz = (InterfazCancion) a;
	super.onAttach(activity);
}


@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	if (savedInstanceState != null){
		track = savedInstanceState.getParcelable("track");
		view = container;		
	}else{
		view = inflater.inflate(R.layout.layout_fragment_track, container,false);
		
	}
	
	interfaz.actualizarCancion(track);
	
	ImageView image = (ImageView)view.findViewById(R.id.imageView1);
	int loader = R.drawable.ic_launcher;
	if (ReproductorActivity.album==null){
	
	
	
	String url = track.getArtist().getThumbnailUrl();
	ImageLoader imgloader= new ImageLoader(getActivity());
	imgloader.DisplayImage(url, loader, image);
	}else{
		String url = ReproductorActivity.album.getThumbnailUrl();
		ImageLoader imgloader= new ImageLoader(getActivity());
		imgloader.DisplayImage(url, loader, image);
	}
	TextView nombre_cancion = (TextView) view.findViewById(R.id.txt_cancion);
	TextView nombre_artista = (TextView) view.findViewById(R.id.txt_artista);
	

	nombre_artista.setText(track.getArtist().getName());
	nombre_cancion.setText(track.getTitle());
	//Poer configuracion cancion
	
	return view;
	
		
		
	
	
}

@Override
public void onSaveInstanceState(Bundle outState) {
	// TODO Auto-generated method stub
	super.onSaveInstanceState(outState);
	outState.putParcelable("track", track);
}

public interface InterfazCancion{
	public void actualizarCancion(Track track);
}

}
