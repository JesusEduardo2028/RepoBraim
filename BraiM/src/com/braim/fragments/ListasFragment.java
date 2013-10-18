package com.braim.fragments;


import com.braim.adapters.ListaAdapter;
import com.example.pruebasherlock.R;


import android.app.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class ListasFragment extends Fragment {
	
	private Activity actividad;
//	private ListaAdapter adaptador;
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
	if (view != null){
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null)
		parent.removeView(view);
	}
		 view = inflater.inflate(R.layout.layout_fragment_playlist_user, container,
				false);
		
//		ListView listView = (ListView) view.findViewById(R.id.listView1);
//		adaptador = new ListaAdapter(actividad);
//		listView.setAdapter(adaptador);
//
//		listView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				// TODO Auto-generated method stub
//				
//				Toast.makeText(actividad, "Lista emocion " + arg2 + " presionada", Toast.LENGTH_SHORT).show();
//				
//			}
//		});

		
		return view;
		
		}
	
	

}
