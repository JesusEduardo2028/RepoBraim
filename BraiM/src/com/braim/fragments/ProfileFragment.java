package com.braim.fragments;


import com.braim.HttpActivity;
import com.braim.MainActivity;
import com.example.pruebasherlock.R;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileFragment extends Fragment {

	private static TextView name;
	private static String nombre;
	private static String apell;
	private static Drawable img;
	private static TextView apellido;
	private static ImageView imagen;
	private static ImageView estado;
	private Activity a;
	private static TextView estado_txt;
	private static View view;
	public static ProfileFragment profileFragment;
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		profileFragment = this;
		 a = activity;
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
		 view = inflater.inflate(R.layout.layout_fragment_profile, container,
				false);
		 
		 if (nombre!=null){
				revisarUser();
			}
		
		 
		 estado = (ImageView) view.findViewById(R.id.imageView2);
		 estado_txt = (TextView) view.findViewById(R.id.textView6);
		 
			if (MainActivity.conexion){
				cambiarEstado(true);
			}else{
			cambiarEstado(false);
			}
		return view;
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
		 name  = (TextView) view.findViewById(R.id.textView1);
		 apellido =(TextView) view.findViewById(R.id.textView2);
		 imagen = (ImageView) view.findViewById(R.id.imageView1);
		 
		 if (nombre != null){
				name.setText(nombre);
				apellido.setText(apell);
				imagen.setImageDrawable(img);
				}
	
	}


	
	public static void cambiarEstado(Boolean state) {
		// TODO Auto-generated method stub
		if (state){
			estado.setImageResource(android.R.drawable.presence_online);
			estado_txt.setText("Conectado");
		}else{
			estado.setImageResource(android.R.drawable.presence_offline);
			estado_txt.setText("Sin conexion");
		}
	}
	

}
