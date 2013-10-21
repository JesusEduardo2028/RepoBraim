package com.braim.fragments;

import com.braim.MainActivity;
import com.braim.deezer.data.Album;
import com.braim.deezer.data.Track;
import com.example.pruebasherlock.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class AlbumFragment extends Fragment {

	private Activity a;
	private View view;
	private ListView listaAlbum;
	private InterfazBusquedaCanciones interfaz;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		a = activity;
		
		interfaz = (InterfazBusquedaCanciones) a;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (view != null){
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null)
				parent.removeView(view);
			}
			 view = inflater.inflate(R.layout.layout_fragment_album, container,
					false);
		listaAlbum = (ListView) view.findViewById(R.id.listView1);
		listaAlbum.setAdapter(MainActivity.arrayAdapterPlayList);
		
		listaAlbum.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				interfaz.buscarCanciones(MainActivity.listPlaylist.get(arg2));
			}
		});
		
		
		
		
		return view;
	}
	
	public interface InterfazBusquedaCanciones{
		public void buscarCanciones(Album album);
	}

}
