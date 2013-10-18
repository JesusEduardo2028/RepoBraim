package com.braim.utils;

/* SERVIDOR   
 * Creado por Sebastian Cipolat
 * */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.logging.LogRecord;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;



public class Server {
	
//	handler handler = new handler(){
//
//		@override
//		public void handlemessage(message msg) {
//			// todo auto-generated method stub
//			interfaz.publicar(msg.tostring());
//			super.handlemessage(msg);
//		}
//		
//	};
	
	String datareceived, substring1, substring2;
	
	Mensaje_data mdata = null;
	ObjectOutputStream oos = null;
	String TimeStamp;

	private respuesta_procesamiento interfaz;

	public Server(final Socket miCliente,Activity a) {
		 interfaz = (respuesta_procesamiento) a;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					System.out.println("************ SERVER android****************");
					// creamos server socket
			
					TimeStamp = new java.util.Date().toString();

					try {
						// Creamos socket para manejar conexion con cliente
						
						ObjectInputStream ois = new ObjectInputStream(
								miCliente.getInputStream());
						while (true) {
							// Manejamos flujo de Entrada
							
							// Cremos un Objeto con lo recibido del cliente
							Object aux = ois.readObject();// leemos objeto

							// si el objeto es una instancia de Mensaje_data
							if (aux instanceof Mensaje_data) {
								// casteamos el objeto
								mdata = (Mensaje_data) aux;

								// Analizamos el mensaje recibido
								// si no es el mensaje FINAL
								if (!mdata.last_msg) {

									// Es un mensaje de Accion
									if (mdata.Action != -1) {
										// exec accion
										Exec(mdata.Action);
//										System.out.println("[" + TimeStamp + "] "
//												+ "Ejecutar Accion " + mdata.Action
//												+ " [" + IP_client + "]");
									} else {
										// No es un mensaje de accion entonces es de
//										// texto
//										System.out.println("[" + TimeStamp + "] "
//												+ "Mensaje de [" + IP_client + "]--> "
//												+ mdata.texto);
										
										String s = mdata.texto;	
								//		Message msg = new Message();
								//		msg.obj=s;
								//		Looper.prepare();
								//		handler.sendMessage(msg);
									//	msg.setTarget(handler);
									//	msg.sendToTarget();
										
									//	Server.this.handler.sendMessage(new Message("s"));
										interfaz.publicar(s);
										System.out.println("*******LLEGOOOOOOOO*************"+s);
							//		ois.close();
									
//									       if (s.startsWith("Train")){MainGui.entrenar();}
//									       else if (s.startsWith("T: ")) {
//										    	  String track = s.replace("T: ", ""); MainGui.ponerCancion(track);}
//									       else if (s.startsWith("Play")){MainGui.ponerPlayLbl();}
//									       else if (s.startsWith("Pause")){MainGui.quitarPlayLbl();}
//									       else if (s.startsWith("Train")){MainGui.entrenar();}
//									       else if (s.startsWith("Con")){MainGui.ponerEstado();}
//									       else if (s.startsWith("U: ")) {
//									    	  String user = s.replace("U: ", ""); MainGui.ponerUsuarioLbl(user);}
									      
									 //      else  if (s.startsWith("End")){  conn.close(); System.out.println("salir �����");host.close();din.close();MainGui.desconexionbl();break;}
									}
								} else {// cerramos socket
									miCliente.close();
									ois.close();
									System.out
											.println("["
													+ TimeStamp
													+ "] Last_msg detected Conexion cerrada, gracias vuelva pronto");
									break;
								}
							} else {
								// Si no es del tipo esperado, se marca error
								System.err.println("Mensaje no esperado ");
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("[" + TimeStamp + "] Error ");
					}
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("[" + TimeStamp + "] Error ");
				}
			}
		}).start();
		
	}

	// en base al codigo de accion recibido realizaremos una accion
	public void Exec(int action_num) {
		String ACTNUM = null;
		String ACT1 = "vlc";// abrir VLC
		String ACT2 = "/opt/google/chrome/google-chrome %U";// Chrome
		String ACT3 = "gnome-terminal";// terminal
		String ACT4 = "";

		try {
			switch (action_num) {
			case 1: {
				ACTNUM = ACT1;
				break;
			}
			case 2: {
				ACTNUM = ACT2;
				break;
			}
			case 3: {
				ACTNUM = ACT3;
				break;
			}
			case 4: {
				  ACTNUM =ACT4;
				  break;
			  }
			  default:{
				  System.out.println("EXEC Error invalid parameter:"+ACTNUM);
				  ACTNUM=null;

				  break;
			  }
			  }
			  //Realizamos la accion
		     Process p = Runtime.getRuntime().exec (ACTNUM); 
		  } 
		  catch (Exception e) 
		  { 
			  /* Se lanza una excepción si no se encuentra en ejecutable o el fichero no es ejecutable. */
			  System.out.println("EXEC Error "+e);			   
		  }		  
	  }
	public interface respuesta_procesamiento{
		public void publicar(String mensaje);
	}
//	public static void main(String[] args) {
//		new Server();
//	}
}	