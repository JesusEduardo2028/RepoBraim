package com.braim.fragments;

import com.example.pruebasherlock.R;

import android.app.Activity;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;




public class ContenedorTabla extends Fragment implements OnTabChangeListener {

	
	private static final String TAG = "FragmentTabs";
	private static final String TAG_Recomendacion = "recomendador";
	private static final String TAG_listas = "lista";
	private static final String TAG_amigos = "amigos";
	private TabHost tabHost;
	private int curretTab;
	private View v;
	private Activity actividad;
	public static ContenedorTabla contenedorTabla;

	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		actividad = activity;
		contenedorTabla = this;
		
		
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (v != null){
			ViewGroup parent = (ViewGroup) v.getParent();
			if (parent != null)
				parent.removeView(v);
		
		}
	
				v = inflater.inflate(R.layout.tabs_fragment,container,false);
			
				
			
		
		 
		
		tabHost = (TabHost)  v.findViewById(R.id.tabhost);
		
		ImageButton boton1 =  (ImageButton) v.findViewById(R.id.imageButton1);
		boton1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			//	Toast.makeText(getActivity(), "botton1", Toast.LENGTH_SHORT).show();
				 FragmentManager fm = getFragmentManager();
			       
		            fm.beginTransaction()
		                    .replace(R.id.contenedorListas, new ListasFragment()).commit();
				
				
			}
		});
		
		ImageButton boton2 =  (ImageButton) v.findViewById(R.id.imageButton2);
		
		boton2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			//	Toast.makeText(getActivity(), "botton2", Toast.LENGTH_SHORT).show();
				 FragmentManager fm = getFragmentManager();
			       
		            fm.beginTransaction()
		                    .replace(R.id.contenedorListas, new ListasFragmentDeezer()).commit();
				
			}
		});
		configurarBotones();
		configurarTabla();
		boton1.performClick();
		return v;
	}





	private void configurarBotones() {
		// TODO Auto-generated method stub
		ImageButton button1 =(ImageButton) v.findViewById(R.id.imageButton1);
		ImageButton button2 = (ImageButton) v.findViewById(R.id.imageButton2);
		
		Drawable b1 = button1.getBackground();
		Drawable b2 = button2.getBackground();

		PorterDuffColorFilter filter = new PorterDuffColorFilter(getResources()
				.getColor(R.color.Amarillo_t), Mode.SRC_ATOP);
		
		b1.setColorFilter(filter);
		b2.setColorFilter(filter);
	}



	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
		if(getArguments().getString("tag_tab").equals("amigos")){
			tabHost.setCurrentTab(1);
		}else{
	//	tabHost.setOnTabChangedListener(this);
		tabHost.setCurrentTab(0);
		}
	
	}
	public void cambiar_amigosVista (){
		tabHost.setCurrentTab(1);
	}




	private void configurarTabla() {
		// TODO Auto-generated method stub
		tabHost.setup();
		
		tabHost.addTab(newtab(this.TAG_listas,"Listas de Reproduccion",R.id.tab2Layout));
		tabHost.addTab(newtab(this.TAG_amigos,"Amigos",R.id.tab3Layout));
		tabHost.setOnTabChangedListener(this);
	}

 

	private TabSpec newtab(String tag, String titulo,
			int idTab) {
		// TODO Auto-generated method stub
		TabSpec  tabSpec = tabHost.newTabSpec(tag);
		tabSpec.setIndicator(titulo,null);
		tabSpec.setContent(idTab);
		
		
		
		return tabSpec;
	}



	@Override
	public void onTabChanged(String tabID) {
		// TODO Auto-generated method stub
	//	Toast.makeText(getActivity(), tabID, Toast.LENGTH_SHORT).show();
		if (tabID.equals("lista")){
			
			 FragmentManager fm = getFragmentManager();
		       
		            fm.beginTransaction()
		                    .replace(R.id.contenedorListas, new ListasFragment()).commit();
		            
		       
		}else {
			FragmentManager fm = getFragmentManager();
			fm.beginTransaction().replace(R.id.contenedorAmigos, new AmigosFragment()).commit();
		}
		
		
	}



	public interface myinterfazTabla { // Interfaz creada para comunicacion
		// entre fragment y la actividad
		// principal
public void onTAbPresionado(String tabId); // Metodo para llevar el
			
}
	







	
	

}
