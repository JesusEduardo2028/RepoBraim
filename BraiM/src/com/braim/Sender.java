package com.braim;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import android.app.Activity;

import com.braim.utils.Mensaje_data;
import com.braim.utils.Server;

public class Sender {

		private static Socket miCliente;
		private static ObjectOutputStream oos;
		public static boolean conectStatus;
		
		String IP;
		int PORT;
		private Activity a;
		
		

		public Sender(String iP, int pORT, Activity aa) {
			super();
			IP = iP;
			PORT = pORT;
			a= aa;
		}


//		public void conectar(){
//			boolean conectstatus = Connect();
//			//si nos pudimos conectar
//			if (conectstatus) {//mostramos mensaje 
//				System.out.println("conexion --OK");
//
//
//			} else {//error al conectarse 
//				System.out.println("conexion --Error --");
//			}
//		}

		
		public boolean Connect() {
			//Obtengo datos ingresados en campos
			

			try {//creamos sockets con los valores anteriores
				miCliente = new Socket(IP, PORT);
				
				
				//si nos conectamos
				if (miCliente.isConnected() == true) {
					new Server(miCliente, a);
					conectStatus = true;
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				System.out.println(e+"");
				return false;
			}
		}

		//Metodo de desconexion
		public boolean Disconnect() {
			try {
				//Prepramos mensaje de desconexion
				Mensaje_data msgact = new Mensaje_data();
				msgact.texto = "";
				msgact.Action = -1;
				msgact.last_msg = true;
				//avisamos al server que cierre el canal
				boolean val_acc = Snd_Msg(msgact);

				if (!val_acc) {//hubo un error
					System.out.println("Desconexion -- error");
					return false;
				} else {//ok nos desconectamos
					conectStatus = false;
					miCliente.close();
					return true;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(e+"");
				return false;
			}
		
		}
		
		public boolean Snd_txt_Msg(String txt) {

			Mensaje_data mensaje = new Mensaje_data();
			//seteo en texto el parametro  recibido por txt
			mensaje.texto = txt;
			//action -1 no es mensaje de accion
			mensaje.Action = -1;
			//no es el ultimo msg
			mensaje.last_msg = false;
			//mando msg
			boolean val_acc = Snd_Msg(mensaje);
			//error al enviar
			if (!val_acc) {
			return false;
			}else{
				return true;
			}
			
		}

		/*Metodo para enviar mensaje por socket
		 *recibe como parmetro un objeto Mensaje_data
		 *retorna boolean segun si se pudo establecer o no la conexion
		 */
		public boolean Snd_Msg(Mensaje_data msg) {

			try {
				//Accedo a flujo de salida
				oos = new ObjectOutputStream(miCliente.getOutputStream());
				//creo objeto mensaje
				Mensaje_data mensaje = new Mensaje_data();

				if (miCliente.isConnected())// si la conexion continua
				{
					//lo asocio al mensaje recibido
					mensaje = msg;
					//Envio mensaje por flujo
					oos.writeObject(mensaje);
					oos.flush();
					//envio ok
					return true;

				} else {//en caso de que no halla conexion al enviar el msg
					
					return false;
				}

			} catch (IOException e) {// hubo algun error
				System.out.println(e+"");
				return false;
			}
		}
}
