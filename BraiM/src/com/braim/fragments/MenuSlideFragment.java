package com.braim.fragments;


import com.braim.MainActivity;
import com.braim.fragments.ContenedorTabla.myinterfazTabla;
import com.example.pruebasherlock.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;



public class MenuSlideFragment extends ListFragment{
	private static View user;
	private static TextView name;
	private static TextView apellido;
	private static ImageView imagen;
	private static String nombre;
	private static String apell;
	private static Drawable img;
	private static ImageView estado;




	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.list, null);
		}

	
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		Fragment nuevoContenido = null;
		
		switch(position){
		case 0:
			nuevoContenido = new ProfileFragment(); 
			switchFragment(nuevoContenido);break;
			
		case 1:
			nuevoContenido = new ContenedorTabla();
			switchFragment(nuevoContenido);break;
		case 2:
			nuevoContenido = new RecomendadorFragment();
			switchFragment(nuevoContenido);break;
		case 3:
			nuevoContenido = new ContenedorTabla();
			switchFragment_amigos(nuevoContenido);break;
		}
	
		
	}

	private void switchFragment_amigos(Fragment nuevoContenido) {
		// TODO Auto-generated method stub
		if (getActivity() == null){
			return;
		}
		if (getActivity()  instanceof MainActivity){
			MainActivity actividad = (MainActivity) getActivity();
			actividad.switchContentAmigos(nuevoContenido);
		}
	}



	private void switchFragment(Fragment nuevoContenido) {
		// TODO Auto-generated method stub
		if (getActivity() == null){
			return;
		}
		if (getActivity()  instanceof MainActivity){
			MainActivity actividad = (MainActivity) getActivity();
			actividad.switchContent(nuevoContenido);
		}
	}

	private class SampleItem {
		public String tag;
		public int iconRes;
		public SampleItem(String tag, int iconRes) {
			this.tag = tag; 
			this.iconRes = iconRes;
		}
	}


	public class SampleAdapter extends ArrayAdapter<SampleItem> {

	

		public SampleAdapter(Context context) {
			super(context, 0);
		}
	
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {

			
				
				if (getItem(position).tag.equals("perfil")){
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.menu_slide_layout, null);
				}else {convertView = LayoutInflater.from(getContext()).inflate(R.layout.row,null);}
			}
			
			if (getItem(position).tag.equals("perfil")){
				 user = convertView;
			}else if (getItem(position).tag.equals("recomendacion")){
				TextView texto =  (TextView) convertView.findViewById(R.id.row_title);
				texto.setText("Recomendaciones");
				
				ImageView imagen = (ImageView) convertView.findViewById(R.id.row_icon);
				imagen.setImageDrawable(getContext().getResources().getDrawable(android.R.drawable.ic_menu_agenda));
			}else if (getItem(position).tag.equals("configuracion")){
				TextView texto =  (TextView) convertView.findViewById(R.id.row_title);
				texto.setText("Amigos");
				
				ImageView imagen = (ImageView) convertView.findViewById(R.id.row_icon);
				imagen.setImageDrawable(getContext().getResources().getDrawable(android.R.drawable.ic_menu_agenda));
			}else if (getItem(position).tag.equals("listas")){
				TextView texto =  (TextView) convertView.findViewById(R.id.row_title);
				texto.setText("Mis Listas");
				
				ImageView imagen = (ImageView) convertView.findViewById(R.id.row_icon);
				imagen.setImageDrawable(getContext().getResources().getDrawable(android.R.drawable.ic_menu_agenda));
			}
			
			return convertView;
		}
	}
	
public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onActivityCreated(savedInstanceState);
		SampleAdapter adapter =new SampleAdapter(getActivity());
		
		adapter.add(new SampleItem("perfil", android.R.drawable.ic_menu_add));
		adapter.add(new SampleItem("listas", android.R.drawable.ic_menu_add));
		adapter.add(new SampleItem("recomendacion",android.R.drawable.ic_input_get));
		adapter.add(new SampleItem("configuracion", android.R.drawable.ic_menu_preferences));
		
		setListAdapter(adapter);
	}



public static void revisarUser(){
	
	
	  nombre =	MainActivity.nombre_user;
	  if (nombre != null){
	  apell = MainActivity.apellido_user;
	  img = MainActivity.image_user;

	  // colocar en vista
	  colocarVista();}
	
	
}



public static void colocarVista() {
	// TODO Auto-generated method stub
	 name  = (TextView) user.findViewById(R.id.user_nombre);
	 apellido =(TextView) user.findViewById(R.id.user_apellido);
	 imagen = (ImageView) user.findViewById(R.id.user_foto);
	 
	 if (nombre != null){
			name.setText(nombre);
			apellido.setText(apell);
			imagen.setImageDrawable(img);
			}
	
}







public static void cambiarEstado(Boolean state) {
	estado = (ImageView) user.findViewById(R.id.imageView1);
	// TODO Auto-generated method stub
	if (state){
		estado.setImageResource(android.R.drawable.presence_online);
	}else{
		estado.setImageResource(android.R.drawable.presence_offline);
	}
	
	
}
	
	
	
	
}
