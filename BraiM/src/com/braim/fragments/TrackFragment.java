package com.braim.fragments;

import org.w3c.dom.Text;

import com.braim.deezer.data.Track;
import com.braim.utils.ImageLoader;
import com.example.pruebasherlock.R;

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
	
public TrackFragment(Track cancion){
	track = cancion;
	setRetainInstance(true);
	
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
	int loader = R.drawable.ic_launcher;
	ImageView image = (ImageView)view.findViewById(R.id.imageView1);
	String url = track.getArtist().getThumbnailUrl();
	ImageLoader imgloader= new ImageLoader(getActivity());
	imgloader.DisplayImage(url, loader, image);
	
	TextView nombre_cancion = (TextView) view.findViewById(R.id.txt_cancion);
	TextView nombre_artista = (TextView) view.findViewById(R.id.txt_artista);
//	nombre_cancion.setTextColor(getActivity().getResources().getColor(R.color.Amarillo));
//	nombre_artista.setTextColor(getActivity().getResources().getColor(R.color.Amarillo_t));
//	nombre_artista.setBackgroundColor(getActivity().getResources().getColor(R.color.Amarillo_t));
//	nombre_cancion.setBackgroundColor(getActivity().getResources().getColor(R.color.Amarillo_t));
	
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



}
