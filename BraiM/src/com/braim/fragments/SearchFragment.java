package com.braim.fragments;

import com.braim.MainActivity;
import com.example.pruebasherlock.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



public class SearchFragment extends Fragment{

	
	private Activity a;
	private View view;
	private EditText busqueda_artista;
	private Button btn_busqueda;
	private InterfazBusquedaArtsita interfaz;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		a = activity;
		interfaz = (InterfazBusquedaArtsita) a;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		if (view != null){
			ViewGroup parent = (ViewGroup)view.getParent();
			if (parent != null){
				parent.removeView(view);
			}
		}
		 view = inflater.inflate(R.layout.layout_fragment_search, container,
				false);
		 busqueda_artista = (EditText) view.findViewById(R.id.editText1);
		 
		 btn_busqueda = (Button) view.findViewById(R.id.button1);
		 
		 btn_busqueda.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			String artista = busqueda_artista.getText().toString();
				if (artista== null || artista.equals("")){
					Toast.makeText(a.getApplicationContext(), "Escriba un artista", Toast.LENGTH_SHORT).show();
				}else{
				 interfaz.buscarArtista(artista);
				}
			}
		});
		 
	
		 
		return view;
	}
	
	public interface InterfazBusquedaArtsita{
		public void buscarArtista(String artista);
	}
	

}
