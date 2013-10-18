package com.braim.utils;

/* SERVIDOR   
 * Creado por Sebastian Cipolat
 * */

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;



public class Server {
	Socket skCliente;
	ServerSocket skServidor;
	String datareceived, substring1, substring2;
	public static ObjectOutputStream ooc;
	final int PUERTO = 5555;// Puerto que utilizara el servidor utilizar este
							// mismo en el cliente
	String IP_client;
	Mensaje_data mdata = null;
	ObjectOutputStream oos = null;
	String TimeStamp;

	private Mensaje_data mensaje_Data;

	public Server() {

		try {
			System.out.println("************ SERVER ****************");
			// creamos server socket
			skServidor = new ServerSocket(PUERTO);
			System.out.println("Escuchando el puerto "
					+ skServidor.getInetAddress() + " : " + PUERTO);
			System.out.println("En Espera....");

			TimeStamp = new java.util.Date().toString();

			try {
			
				// Creamos socket para manejar conexion con cliente
				skCliente = skServidor.accept(); // esperamos al cliente
				// una vez q se conecto obtenemos la ip
				
				MainGui.ponerEstado(0);
				IP_client = skCliente.getInetAddress().toString();

				System.out.println("[" + TimeStamp + "] Conectado al cliente "
						+ "IP:" + IP_client);

				ooc = new ObjectOutputStream(skCliente.getOutputStream());
				// s = new Sender(IP_client, 5550);
				// MainGui.crearCliente(sk);
				while (true) {
					
					// Manejamos flujo de Entrada
					ObjectInputStream ois = new ObjectInputStream(
							skCliente.getInputStream());
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
								System.out.println("[" + TimeStamp + "] "
										+ "Ejecutar Accion " + mdata.Action
										+ " [" + IP_client + "]");
							} else {
								// No es un mensaje de accion entonces es de
								// texto
								System.out.println("[" + TimeStamp + "] "
										+ "Mensaje de [" + IP_client + "]--> "
										+ mdata.texto);

								String mensaje = mdata.texto;

								if (mensaje.startsWith("Train")) {
									MainGui.entrenar();
								} else if (mensaje.startsWith("T: ")) {
									String track = mensaje.replace("T: ", "");
									MainGui.ponerCancion(track);
								} else if (mensaje.startsWith("Play")) {
									MainGui.ponerPlayLbl();
								} else if (mensaje.startsWith("Pause")) {
									MainGui.quitarPlayLbl();
								} else if (mensaje.startsWith("Train")) {
									MainGui.entrenar();
								} else if (mensaje.startsWith("Class")) {
									MainGui.clasificar();
								} else if (mensaje.startsWith("cancel")) {
									MainGui.entreno_cancelado();
								}else if (mensaje.equals("cancel_c")) {
									MainGui.entreno_cancelado();
								}else if (mensaje.equals("Con_train")) {
									MainGui.ponerEstado(0);
									// oos = new
									// ObjectOutputStream(skCliente.getOutputStream());
									// mensaje_Data = new Mensaje_data();
									// mensaje_Data.texto = "CONFIRMACION";
									// //action -1 no es mensaje de accion
									// mensaje_Data.Action = -1;
									// //no es el ultimo msg
									// mensaje_Data.last_msg = false;
									// ooc.writeObject(mensaje_Data);
									// s.Snd_txt_Msg("Con");
								}else if (mensaje.equals("Con_class")) {
									MainGui.ponerEstado(1);
									
								} else if (mensaje.startsWith("U: ")) {
									String user = mensaje.replace("U: ", "");
									MainGui.ponerUsuarioLbl(user);
								}
								// else if (s.startsWith("End")){ conn.close();
								// System.out.println("salir �����");host.close();din.close();MainGui.desconexionbl();break;}
							}
						} else {// cerramos socket
							skCliente.close();
							ois.close();
							System.out
									.println("["
											+ TimeStamp
											+ "] Last_msg detected Conexion cerrada, gracias vuelva pronto");
							MainGui.desconexionbl();
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
				ACTNUM = ACT4;
				break;
			}
			default: {
				System.out.println("EXEC Error invalid parameter:" + ACTNUM);
				ACTNUM = null;

				break;
			}
			}
			// Realizamos la accion
			Process p = Runtime.getRuntime().exec(ACTNUM);
		} catch (Exception e) {
			/*
			 * Se lanza una excepción si no se encuentra en ejecutable o el
			 * fichero no es ejecutable.
			 */
			System.out.println("EXEC Error " + e);
		}
	}
	// public static void main(String[] args) {
	// new Server();
	// }
}