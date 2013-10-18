package com.braim;

import com.deezer.sdk.DeezerConnect;
import com.deezer.sdk.DeezerConnectImpl;
import com.deezer.sdk.DeezerError;
import com.deezer.sdk.DialogError;
import com.deezer.sdk.DialogListener;
import com.deezer.sdk.OAuthException;
import com.deezer.sdk.SessionStore;
import com.example.pruebasherlock.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SlidingDrawer;
import android.widget.Toast;

public class LoginActivity extends Activity{

	private DialogHandler dialogHAndler = new DialogHandler();
	
	//Ojeto Deezer conect posiblemente sea el que haga los respectivos llamados al los callbacks
	private DeezerConnect deezerConect = new DeezerConnectImpl(BaseActivity.APP_ID);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_activity_login);
		
		
		
	SessionStore sessionStore = new SessionStore();
	if (sessionStore.restore(deezerConect, this)){
		Toast.makeText(this, "Ya estas logeado", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(LoginActivity.this, MainActivity.class);
		startActivity(intent);
	}
		
		
	}
	
	public void loginDeezer(View v){
		conectarseDeezer(dialogHAndler);
	}
	
	
	
	private void conectarseDeezer(DialogHandler dialogHAndler) {
		// TODO Auto-generated method stub
		deezerConect.authorize(this, BaseActivity.PERMISOS, dialogHAndler);
	}



	//***********************************************************************//
	
	// Clase encargada de las llamadas asincronas a Deezer,
	//implementa los metodos callback necesarios
	
	private class DialogHandler implements DialogListener{

	

		@Override
		public void onCancel() {
			// TODO Auto-generated method stub
			Toast.makeText(LoginActivity.this, "Login Cancelado ", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onComplete(Bundle values) {
			// TODO Auto-generated method stub
			
			SessionStore sessionStore = new SessionStore();
			sessionStore.save(deezerConect, LoginActivity.this);
			
		
			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			startActivity(intent);
			
		}

		@Override
		public void onDeezerError(DeezerError error) {
			// TODO Auto-generated method stub
			Toast.makeText(LoginActivity.this, "Error al logearse en Deezer ! "+ error, Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onError(DialogError error) {
			// TODO Auto-generated method stub
			Toast.makeText(LoginActivity.this, "Error al logearse en Deezer ! "+ error, Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onOAuthException(OAuthException error) {
			// TODO Auto-generated method stub
			Toast.makeText(LoginActivity.this, "Datos errados "+ error, Toast.LENGTH_SHORT).show();
			
		}
		
		
	}
	
	

}
