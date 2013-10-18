package com.braim.fragments;


import com.braim.MainActivity;
import com.braim.ReproductorActivity;
import com.braim.adapters.ListaAdapter;
import com.braim.adapters.ListaAdapterDeezer;
import com.braim.deezer.data.Playlist;
import com.example.pruebasherlock.R;


import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class ListasFragmentDeezer extends Fragment {
	
	private Activity actividad;
	private ListaAdapterDeezer adaptador;
	private ArrayAdapter<Playlist> arrayAdapter;
	private View view;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		actividad = activity;
	
		try {
		//	mCallback = (myinterfaz) activity;
	
		} catch (ClassCastException e) {
			// TODO: handle exception
			throw new ClassCastException(activity.toString()
					+ " ha de implementar onGridViewListener");
		}
	
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		try {
		arrayAdapter = MainActivity.arrayAdapterPlayList;
	//	Toast.makeText(getActivity(), " ADAPTER", Toast.LENGTH_SHORT).show();
		}catch(Exception e){
			Toast.makeText(getActivity(), "ERROR ADAPTER", Toast.LENGTH_SHORT).show();
		}
		if (view != null){
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null)
			parent.removeView(view);
		}
		 view = inflater.inflate(R.layout.layout_fragment_playlist_deezer, container,
				false);
		
		ListView listView = (ListView) view.findViewById(R.id.listView1);
		adaptador = new ListaAdapterDeezer(actividad);
	//	listView.setAdapter(adaptador);
listView.setAdapter(arrayAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int listId,
					long arg3) {
				// TODO Auto-generated method stub
				Playlist a  = MainActivity.listPlaylist.get(listId);
				Intent intent  = new Intent(getActivity(), ReproductorActivity.class);
				intent.putExtra(ReproductorActivity.PLAYLIST_ID, a.getId());
				intent.putExtra(ReproductorActivity.PLAYLIST_TITLE, a.getTitle());
				intent.putExtra(ReproductorActivity.PLAYLIST_COVER, a.getThumbnailUrl());
				intent.putExtra("nombre_user", MainActivity.nombre_user+" "+MainActivity.apellido_user);
			
				startActivity(intent);
				
			//	Toast.makeText(getActivity(),a.getTitle() + " presionada", Toast.LENGTH_SHORT).show();
				
			}
		});

		
		return view;
		
		}
	
	

}
