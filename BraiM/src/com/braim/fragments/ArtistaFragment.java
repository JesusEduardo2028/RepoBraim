package com.braim.fragments;


import com.braim.MainActivity;
import com.braim.deezer.data.Artist;
import com.example.pruebasherlock.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ArtistaFragment extends Fragment{

	private Activity a;
	private View view;
	private ListView listaArtistas;
	private InterfazBusquedaListas interfaz;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		a = activity;
		interfaz = (InterfazBusquedaListas) a;
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
			 view = inflater.inflate(R.layout.layout_fragment_artista, container,
					false);
		
			 
			 
		listaArtistas = (ListView) view.findViewById(R.id.listView1);
		listaArtistas.setAdapter(MainActivity.arrayAdapterArtist);
		listaArtistas.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
			interfaz.buscarlistas(MainActivity.listArtist.get(arg2));
			}
	
		});
		
		
		
		return view;
	}

	
	public interface InterfazBusquedaListas{
		public void buscarlistas(Artist artista);
	}
	
	
}
