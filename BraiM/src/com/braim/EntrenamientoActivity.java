package com.braim;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.braim.HttpActivity.InterfazEntrenamiento;
import com.example.pruebasherlock.R;

public class EntrenamientoActivity extends Activity implements InterfazEntrenamiento{
	
	protected static boolean conexionExitosa;
	private MiTareaAsincrona tarea;
	private AlertDialog mensaje;
	public static EntrenamientoActivity a;
	


	CheckBox cajon1;
	private CheckBox cajon2;
	private CheckBox cajon3;
	private CheckBox cajon4;
	private TextView txt_emo1;
	private TextView txt_emo2;
	private TextView txt_emo3;
	private TextView txt_emo4;
	
	private MediaPlayer iads1;
	private MediaPlayer iads2;
	private MediaPlayer iads3;
	private MediaPlayer iads4;
	private static  Button boton_iniciar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_activity_entrenamiento);
		

		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("Espere mientras se configura el entrenamiento");
    	 mensaje = builder.create();
    	 mensaje.setCancelable(false);
    	 
		cajon1 = (CheckBox) findViewById(R.id.checkBox1);
		cajon2 = (CheckBox) findViewById(R.id.checkBox2);
		cajon3 = (CheckBox) findViewById(R.id.checkBox3);
		cajon4 = (CheckBox) findViewById(R.id.checkBox4);
		
		cajon1.setClickable(false);
		cajon2.setClickable(false);
		cajon3.setClickable(false);
		cajon4.setClickable(false);
		
		txt_emo1 = (TextView) findViewById(R.id.txt_emo1);
		txt_emo2 = (TextView) findViewById(R.id.txt_emo2);
		txt_emo3 = (TextView) findViewById(R.id.txt_emo3);
		txt_emo4 = (TextView) findViewById(R.id.txt_emo4);
		
		txt_emo1.setVisibility(View.GONE);
		txt_emo2.setVisibility(View.GONE);
		txt_emo3.setVisibility(View.GONE);
		txt_emo4.setVisibility(View.GONE);
		
		boton_iniciar = (Button) findViewById(R.id.button1);
		a = this;
		
		cargarIADS();
	}


	private void cargarIADS() {
		// TODO Auto-generated method stub
		
		iads1 = MediaPlayer.create(this, R.raw.traingin);
		
	}
	
	
	public void onClick_conexion(View v){
		conexionExitosa = false;
		
	
		if (MainActivity.conexion){

			 AlertDialog.Builder builder = new AlertDialog.Builder(this);
		      builder.setMessage("Va a iniciar el entrenamiento: duracion 2 min");
		      builder.setCancelable(false);
		      builder.setPositiveButton("Aceptar",
		              new DialogInterface.OnClickListener() {
		                  public void onClick(DialogInterface dialog, int id) {
		                    

		          	    	mensaje.show();
		          			tarea = new MiTareaAsincrona();
		          			tarea.execute();
		          			boton_iniciar.setEnabled(false);
		                     
		                  }
		              });
		      builder.setNegativeButton("Cancelar",
		              new DialogInterface.OnClickListener() {
		                  public void onClick(DialogInterface dialog, int id) {
		                      dialog.cancel();
		                  }
		              });
		      AlertDialog alertDialog = builder.create();
		      alertDialog.show();
			
			
			
		}else{
			Toast.makeText(this, "no puede entrenar sin estar conectado con el servidor de procesamiento", Toast.LENGTH_LONG).show();
	
		} 
		
	}
	

	private class MiTareaAsincrona extends AsyncTask<Void, Integer, Boolean> {
		 
		
		
		 
	    @Override
	    protected Boolean doInBackground(Void... params) {
	    	HttpActivity.actualizar_interfaz(); 
	    	try {
				Thread.sleep(15000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	if (conexionExitosa){
	    		
	        return true;}else{
	        
	        	return false;
	        }
	    }
	 
	
	 
	    @Override
	    protected void onPreExecute() {
	     	
	        HttpActivity.s.Snd_txt_Msg("Train");
	      	cajon1.setChecked(false);
        	cajon2.setChecked(false);
        	cajon3.setChecked(false);
        	cajon4.setChecked(false);
	    }
	 
	    @Override
	    protected void onPostExecute(Boolean result) {
	    //	mensaje.dismiss();
	        if(result){
	        //	Toast.makeText(Main.this, "conexion exitosa",Toast.LENGTH_SHORT).show();
	        //	Intent i = new Intent(getApplicationContext(), EntrenamientoActivity.class);
	        //	startActivity(i);
	  
	        	
	        }else{
	        	boton_iniciar.setEnabled(true);
	        	mensaje.dismiss();
	        	Toast.makeText(EntrenamientoActivity.this, "No se pudo realizar el entrenamiento",
	                    Toast.LENGTH_SHORT).show();
	        HttpActivity.s.Snd_txt_Msg("cancel");  
	        
	        Handler handler = new Handler();
	        handler.postDelayed(new Runnable() {
	            public void run() {
	                // acciones que se ejecutan tras los milisegundos
	          //  	HttpActivity.s.Disconnect();
	    	   //     HttpActivity.s.conectStatus = false;
	    	    //    conexion= false;
	            }
	        }, 3000);
	        
	        
	                    }
	        
	    }
	 
	}


	@Override
	public void emocionTerminada(String e) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				if (e.contains("PV")){
					
				//	iads1.reset();
					try {
				//		iads1.prepare();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
					iads1.start();
					mensaje.dismiss();
					txt_emo1.setVisibility(View.VISIBLE);
				}else if (e.contains("NV")){
				//	iads1.stop();
					txt_emo2.setVisibility(View.VISIBLE);
					txt_emo1.setVisibility(View.GONE);
					
					cajon1.setChecked(true);
				}else if (e.contains("PA")){
					txt_emo3.setVisibility(View.VISIBLE);
					txt_emo2.setVisibility(View.GONE);
					
					cajon2.setChecked(true);
				}else if (e.contains("NA")){
					
					txt_emo4.setVisibility(View.VISIBLE);
					txt_emo3.setVisibility(View.GONE);
					
					cajon3.setChecked(true);
				}
	}


	@Override
	public void finEntrenamiento() {
		// TODO Auto-generated method stub
		cajon4.setChecked(true);
		txt_emo4.setVisibility(View.GONE);
		Toast.makeText(this, "Entrenamiento Terminado Satisfactoriamente", Toast.LENGTH_SHORT).show();
		boton_iniciar.setEnabled(true);
	}

}
