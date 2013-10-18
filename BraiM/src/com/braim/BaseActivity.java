package com.braim;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.braim.fragments.MenuSlideFragment;
import com.deezer.sdk.DeezerConnect;
import com.deezer.sdk.DeezerConnectImpl;
import com.deezer.sdk.DeezerError;
import com.deezer.sdk.DialogError;
import com.deezer.sdk.DialogListener;
import com.deezer.sdk.OAuthException;
import com.deezer.sdk.SessionStore;
import com.example.pruebasherlock.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class BaseActivity extends SlidingFragmentActivity{
	

	protected Fragment mFragment;
	
private static final int MENU_ITEM_LOGOUT= 0;
	
	
	//Id de nuestra aplicacion
	public static final String APP_ID= "119413";
	//permisos que requiere Deezer
	protected static final String[] PERMISOS = new String[]{"basic_access","offline_access" };
	
	protected static DeezerConnect deezerConnect = null;

	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//Fijamos el titulo de la actividad
	
		//Colocamos lo que va atras del slide
		setBehindContentView(R.layout.menu_frame);
		
		if (savedInstanceState == null){
			FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
			mFragment = new MenuSlideFragment();
			t.replace(R.id.menu_frame,mFragment);
			t.commit();
		}else{
			mFragment =(Fragment)this.getSupportFragmentManager().findFragmentById(R.id.menu_frame);
		}
		
		//personalizamos nuestro SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidth(5);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffset(70);
		sm.setFadeDegree(0.35f);
		
		
		//Se selecciona el modo de deslizar el menu
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		//
		deezerConnect = new DeezerConnectImpl(APP_ID);
		

	}
//
//
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// TODO Auto-generated method stub
//		switch(item.getItemId()){
//		case android.R.id.home:
//
//			
//			
//			toggle();
//			return true;
//		case  MENU_ITEM_LOGOUT:
//			desconectarseDeezer();
//		Intent intent = new Intent(this, LoginActivity.class);
//		startActivity(intent);
//		return true;
//		
//		}
//		return super.onOptionsItemSelected(item);
//	}
//	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// TODO Auto-generated method stub
//		MenuItem menuItem = menu.add(0,MENU_ITEM_LOGOUT,0,"Cerrar Sesion");
//		menuItem.setIcon(android.R.drawable.ic_lock_idle_lock);
//		
//		
//		return super.onCreateOptionsMenu(menu);
//		
//	}


	
	
	private void desconectarseDeezer() {
		// TODO Auto-generated method stub
		if (deezerConnect != null){
			deezerConnect.logout(this);

		}
		new SessionStore().clear(this);
	}

	protected void reconectarseDeezer(){
		deezerConnect.authorize(this, PERMISOS, new ReconnectDialogHAndler());
		
			}
	protected void handleError(Exception e){
		Toast.makeText(this, "Error de conexion" + e, Toast.LENGTH_SHORT).show();
		
	}
	
	//Bloquear Rotacion por codigo
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
	}
	
	
	
	//************************************************************************
	//CLase Callbak
	
	protected class ReconnectDialogHAndler implements DialogListener{

		@Override
		public void onCancel() {
			// TODO Auto-generated method stub
			Toast.makeText(BaseActivity.this, "Login Cancelado", Toast.LENGTH_SHORT).show();
			
		}

		@Override
		public void onComplete(Bundle arg0) {
			// TODO Auto-generated method stub
			SessionStore sessionStore = new SessionStore();
			sessionStore.save(deezerConnect, BaseActivity.this);
			
			Toast.makeText(BaseActivity.this, "Usuario Identificado", Toast.LENGTH_SHORT).show();
					
			
		}

		@Override
		public void onDeezerError(DeezerError error) {
			// TODO Auto-generated method stub
			Toast.makeText(BaseActivity.this, "Error durante el Login: "+ error, Toast.LENGTH_SHORT).show();
			
		}

		@Override
		public void onError(DialogError error) {
			// TODO Auto-generated method stub
			Toast.makeText(BaseActivity.this, "Error durante el Login: "+ error, Toast.LENGTH_SHORT).show();
			
		}

		@Override
		public void onOAuthException(OAuthException error) {
			// TODO Auto-generated method stub
			Toast.makeText(BaseActivity.this, "Error durante el Login: "+ error, Toast.LENGTH_SHORT).show();
			
		}
		
		
	}
	
	
	

}
	
	
	


