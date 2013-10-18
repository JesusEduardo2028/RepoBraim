package com.braim;


import java.io.ObjectOutputStream;
import java.net.Socket;

import com.braim.fragments.ContenedorTabla;
import com.braim.fragments.MenuSlideFragment;
import com.braim.fragments.ProfileFragment;
import com.braim.utils.Mensaje_data;
import com.braim.utils.Server;
import com.braim.utils.Server.respuesta_procesamiento;
import com.example.pruebasherlock.R;
import com.example.pruebasherlock.R.layout;
import com.example.pruebasherlock.R.menu;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class HttpActivity extends Activity implements respuesta_procesamiento{

	private EditText ip_input;
	private EditText puerto_input;
	private String IP;
	private int PUERTO;
	private Button btn_conectar;
	public static InterfazEntrenamiento interfaz;
	public static InterfazClasificacion interfaz_class;

	public static Sender s = null;
	public static Server server = null;

	
	public static void  actualizar_interfaz(){
		interfaz = (InterfazEntrenamiento) EntrenamientoActivity.a  ;
	}
	
	public static void  actualizar_interfaz_class(){
		interfaz_class = (InterfazClasificacion) ReproductorActivity.a  ;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_activity_http);

		ip_input = (EditText) findViewById(R.id.editText1);
		puerto_input = (EditText) findViewById(R.id.editText2);
		btn_conectar = (Button) findViewById(R.id.button1);

		if (s != null && s.conectStatus) {
			interfazConconexion();
		}
		
		// interfaz = (InterfazEntrenamiento) this;
		interfaz = (InterfazEntrenamiento) EntrenamientoActivity.a  ;
		interfaz_class = (InterfazClasificacion) ReproductorActivity.a  ;
//		interfazEstado1 = (InterfazEstado) ContenedorTabla.contenedorTabla;
//		interfazEstado2 = (InterfazEstado) ProfileFragment.profileFragment;
	//	interfaz = (InterfazEntrenamiento) new EntrenamientoActivity();
	}

	private void interfazSinconexion() {
		// TODO Auto-generated method stub
		ip_input.setEnabled(true);
		puerto_input.setEnabled(true);
		btn_conectar.setText("Conectar");

	}

	private void interfazConconexion() {
		// TODO Auto-generated method stub
		ip_input.setEnabled(false);
		puerto_input.setEnabled(false);
		btn_conectar.setText("Desconectar");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.http, menu);
		return true;
	}

	boolean estado = false;

	public void conectarse(View v) {
		if (s != null && s.conectStatus) {
			
			
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					estado = s.Disconnect();
					
				}
			}).start();

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (estado) {
				
				Toast.makeText(this, "Desconectado Satisfactoriamente", Toast.LENGTH_SHORT)
						.show();
				ProfileFragment.cambiarEstado(false);
				MenuSlideFragment.cambiarEstado(false);
				interfazSinconexion();
				setResult(0);
				finish();

			} else {
				Toast.makeText(this, "Error en la desconexion", Toast.LENGTH_SHORT)
						.show();
				
				s = null;
			}
		} else {

			IP = ip_input.getText().toString().trim();
			ip_input.setHint(IP);
			PUERTO = Integer.valueOf(puerto_input.getText().toString().trim());

			s = new Sender(IP, PUERTO,this);

			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					estado = s.Connect();
				}
			}).start();

			try {
				Thread.sleep(2500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (estado) {
			//	Toast.makeText(this, "Conexion establecida", Toast.LENGTH_SHORT).show();
				ProfileFragment.cambiarEstado(true);
				MenuSlideFragment.cambiarEstado(true);
				interfazConconexion();
				setResult(1);
				finish();

			} else {
				Toast.makeText(this, "Error en la conexion", Toast.LENGTH_SHORT)
						.show();
				s = null;
				setResult(0);
			}

		}

	}

	@Override
	public void publicar(String mensaje) {
		// TODO Auto-generated method stub
	//	Toast.makeText(getApplicationContext(), mensaje,  Toast.LENGTH_SHORT).show();
		Message msg = new Message();
		msg.obj = mensaje;
		handler.sendMessage(msg);
	}
	
	

	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
	//		interfaz = (InterfazEntrenamiento) Main.a;
			String mensaje = msg.obj.toString();
			// TODO Auto-generated method stub
	//		Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
			if (mensaje.contains("descon")){
				//else if (mensaje.contains("descon")){
					HttpActivity.s.Disconnect();
					MainActivity.conexion = false;
					Toast.makeText(getApplicationContext(), "Se ha cerrado el servidor de procesamiento", Toast.LENGTH_SHORT).show();
					ProfileFragment.cambiarEstado(false);
					MenuSlideFragment.cambiarEstado(false);
					//	Main.conexionExitosa = false;
			}else if (mensaje.contains("fin")){
				
				interfaz.finEntrenamiento();
				
			}else if (mensaje.contains("emo")){
				
				interfaz_class.emocionTerminada(mensaje);
				
			}else{
				
				interfaz.emocionTerminada(mensaje);
				
			}
			EntrenamientoActivity.conexionExitosa = true;

			super.handleMessage(msg);
		
		}
		
	};
	
	
	public static interface InterfazEntrenamiento {
		public void emocionTerminada(String e);

		public void finEntrenamiento();
	}

	public static interface InterfazClasificacion {
		public void emocionTerminada(String e);

	}
	

}
