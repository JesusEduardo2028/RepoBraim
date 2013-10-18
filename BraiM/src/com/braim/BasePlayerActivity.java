package com.braim;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.jar.Pack200.Packer;

import android.animation.AnimatorSet;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbRequest;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import android.widget.TextView;
import android.widget.Toast;

public class BasePlayerActivity extends FragmentActivity{

	public UsbDevice dispositivoActual;
	private static final String ACTION_USB_PERMISSION = "com.acces.device.USB_PERMISSION";
	private UsbDeviceConnection connectionRead;
	private UsbEndpoint usbEndpointRead;
	private UsbManager usbManager;
	private PendingIntent permissionIntent;
	private TextView epoc_data;

	private boolean epoc_conection = false;
	private TextView epoc_data_decrypt;
	
//	static {
//		System.loadLibrary("decrypt");
//	}
//	
//	public native String dameDatos();
	
	public void declarar_salida_decryp(){
	//	epoc_data_decrypt = (TextView) this.findViewById(R.id.decrypt_epoc_data);
	//	epoc_data_decrypt.setText("aun no hay desencriptado");
		
	}
	
	public void declarar_salida(){
		
	//	epoc_data = (TextView) this.findViewById(R.id.epoc_data);
		epoc_data.setText("no data");
		
		
	}
	
	public boolean comprobarEpoc(){
		
		if (epoc_conection == true){
			return true;
		}else{ 
			return false;
		}
	}
	
	private final BroadcastReceiver usbReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if (ACTION_USB_PERMISSION.equals(action)){
				synchronized(this){
					UsbDevice dispositivoConectado = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
					if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)){
						if (dispositivoConectado != null){
							dispositivoActual = dispositivoConectado;
							if(!conectarInterfaz(dispositivoActual)){
								Toast.makeText(getApplicationContext(), "Error al configurar el dispositivo", Toast.LENGTH_SHORT).show();
							}else{ Toast.makeText(getApplicationContext(), "Dispositivo conectado y funcionando", Toast.LENGTH_SHORT).show();
							empezarLecturaDatos();
							epoc_conection = true;
							}
							
						}else{ 
							dispositivoActual = null;
							Toast.makeText(getApplicationContext(), "Error al configurar el dispositivo", Toast.LENGTH_SHORT).show();}
						
						
					}
 				}
			}
			if (UsbManager.ACTION_USB_ACCESSORY_DETACHED.equals(action)){
				synchronized(this){
					UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
					if ((device != null)&&(dispositivoActual != null)){
						if (device.equals(dispositivoActual)){
						limpiar_Cerrar();
						}
					}
				}
			}else if (UsbManager.ACTION_USB_ACCESSORY_ATTACHED.equals(action)){
				synchronized(this){
					UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
					if ((device!=null)&&(dispositivoActual ==null)){
						validarDispositivo(device);
					}
				}
			}
		}

		private boolean conectarInterfaz(UsbDevice device) {
			// TODO Auto-generated method stub
			
			UsbInterface usbInterfaceRead = null;
			
			UsbEndpoint ep1 = null;
			
			usbInterfaceRead = device.getInterface(0x01);
			
			 usbEndpointRead = null;
			if (usbInterfaceRead.getEndpointCount()==1){
				ep1  = usbInterfaceRead.getEndpoint(0);
				
			}
			if (ep1 == null){
				return false;
			}
			//Se comprueba si el endpoint es para leer o escribir datos
			
			if (ep1.getType() == UsbConstants.USB_ENDPOINT_XFER_INT){
				if (ep1.getDirection() == UsbConstants.USB_DIR_IN){
					  usbEndpointRead = ep1;
				}else{
					Toast.makeText(getApplicationContext(), "Endpoint de salida", Toast.LENGTH_SHORT).show();
					return false;
				}
			}
			if (usbEndpointRead == null){
				Toast.makeText(getApplicationContext(), "Endpoint vacio", Toast.LENGTH_SHORT).show();
				return false;
			}
			
			connectionRead = 	usbManager.openDevice(device);
			connectionRead.claimInterface(usbInterfaceRead, true);
			
		//	Toast.makeText(getApplicationContext(), "Endpoint configurado", Toast.LENGTH_SHORT).show();
				return true;
			}
		

		private void limpiar_Cerrar() {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "Dispositivo desconectado", Toast.LENGTH_SHORT).show();
			dispositivoActual = null;
		}

		private void validarDispositivo(UsbDevice device) {
			// TODO Auto-generated method stub
			int productID =	device.getProductId();
			int vendorID =	device.getVendorId();
			
			if (productID == 60674 && vendorID == 4660){
				Toast.makeText(getApplicationContext(), "Emotiv Epoc conectado", Toast.LENGTH_SHORT).show();
				dispositivoActual = device;
			}else{
				dispositivoActual = null;
				Toast.makeText(getApplicationContext(), "Dispositivo no reconocido", Toast.LENGTH_SHORT).show();
			}
				
		}
		
		
	};
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//Registrar intent de comprobacion
				usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
				permissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
				IntentFilter permissionFilter = new IntentFilter(ACTION_USB_PERMISSION);
				registerReceiver(usbReceiver, permissionFilter);
				//Registrar intent para gestionar la conexion o desconexion del dispositivo
				IntentFilter deviceAtachedFilter = new IntentFilter();
				deviceAtachedFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
				deviceAtachedFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
				registerReceiver(usbReceiver, deviceAtachedFilter);
				
				
			
			//Inicio la comprobacion del dispositivo conectado
				HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
				
				if (deviceList.isEmpty()){
					Toast.makeText(this, "ningun dispositivo conectado", Toast.LENGTH_SHORT).show();
				
				}else{
					Iterator<UsbDevice> iteratorDevices  = deviceList.values().iterator();
					UsbDevice device = iteratorDevices.next();
					usbManager.requestPermission(device, permissionIntent);
					
					
				}
				
			//	epoc_data_decrypt.setText(desencriptador.desencriptarValor());
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
	//	unregisterReceiver(usbReceiver);
		
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
	//	unregisterReceiver(usbReceiver);
		super.onPause();
	}
	
	
	private void empezarLecturaDatos() {
		// TODO Auto-generated method stub
		MyAsyncTask asyncTask = new MyAsyncTask();
		Long d = null;
	

		asyncTask.execute(d);
	}
	
	private class MyAsyncTask  extends AsyncTask<Long, String, Boolean>{
		
		@Override
		protected Boolean doInBackground(Long... arg0) {
			// TODO Auto-generated method stub
		    int bufferDataLength = usbEndpointRead.getMaxPacketSize();
		    String longitud = bufferDataLength +"";
            ByteBuffer buffer =ByteBuffer.allocate(bufferDataLength +1);
            UsbRequest requestQueued = null;
            UsbRequest request = new UsbRequest();
            request.initialize(connectionRead, usbEndpointRead);
            
            try {
				DatagramSocket socket = new DatagramSocket();
				final InetAddress destination = InetAddress.getByName("192.168.0.10");
				DatagramPacket packet;
			
           
            try{
          	  while(comprobar()){
          	
          		  request.queue(buffer, bufferDataLength);
          	//	 publishProgress("hey");
          	//	Thread.sleep(500);
          		  requestQueued = connectionRead.requestWait();
          		  if (request.equals(requestQueued)){
          //		if (true){
          			  byte[] byteBuffer = new byte[bufferDataLength+1];
          			  buffer.get(byteBuffer,0,bufferDataLength);
          			  packet = new DatagramPacket(byteBuffer,byteBuffer.length,destination,50005);
          			  socket.send(packet);
          		  	  publishProgress(byteBuffer.toString());
          			 
          			  buffer.clear();
          			
          		
          		  }else{
          			  publishProgress("else");
           			 
          			  Thread.sleep(20);
          		  }
          		
          		  }
          	  
            }catch(Exception e){
            //	return false;
            	  publishProgress(e.toString());
       			 
          	  }} catch (SocketException e1) {
  				// TODO Auto-generated catch block
  				e1.printStackTrace();
  			} catch (UnknownHostException e) {
  				// TODO Auto-generated catch block
  				e.printStackTrace();
  			}
  
            try{
            request.cancel();
            request.close();
            }catch(Exception e){
            	publishProgress(e.toString());
          //	  Toast.makeText(getApplicationContext(), "Error al cerrar el hilo" + e, Toast.LENGTH_SHORT).show();
            }
			
			return true;
		}
		
		

		@Override
		protected void onProgressUpdate(String... values) {
			// TODO Auto-generated method stub
			String valor  = values[0];
			
			
			 epoc_data.setText(valor);
	
		}



		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "Captura de datos Terminada...", Toast.LENGTH_SHORT).show();
		}



		private boolean comprobar() {
			// TODO Auto-generated method stub
			if (dispositivoActual ==null){
			return false;}else{
				return true;
			}
		}
		
		
	}
	
	

}
