package com.braim.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


import vrpn.ButtonRemote;

public class HDVrpn implements vrpn.ButtonRemote.ButtonChangeListener {



	private Process p;
	public static ButtonRemote butt;

	public void buttonUpdate(ButtonRemote.ButtonUpdate u, ButtonRemote button) {
	
		
		butt = button; 
		Mensaje_data mensaje_Data = new Mensaje_data();
		
		// action -1 no es mensaje de accion
		mensaje_Data.Action = -1;
		// // no es el ultimo msg
		mensaje_Data.last_msg = false;
		
		
Mensaje_data mensaje_Data_fin = new Mensaje_data();
		
		// action -1 no es mensaje de accion
mensaje_Data_fin.Action = -1;
		// // no es el ultimo msg
mensaje_Data_fin.last_msg = false;
mensaje_Data_fin.texto = "fin";
	//	System.out	.println("  emotion:  " + u.button);
		switch (u.button) {
		
		case 0:
			mensaje_Data.texto = "PV";
			System.out
					.println("  emotion:  " + u.button + "= Positive Valence");
			
			
			try {
				Server.ooc.writeObject(mensaje_Data);
				 Server.ooc.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// BluetoothSender_JavaSE.envio("stim");
			break;
		case 1:
			mensaje_Data.texto = "NV";
			System.out
					.println("  emotion:  " + u.button + "= Negative Valence");
		
			try {
				Server.ooc.writeObject(mensaje_Data);
				 Server.ooc.flush();
				// Server.ooc.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// BluetoothSender_JavaSE.envio("stim");
			break;
		case 2:
			mensaje_Data.texto = "PA";
			System.out
					.println("  emotion:  " + u.button + "= Positive Arousal");
			
			try {
				Server.ooc.writeObject(mensaje_Data);
				 Server.ooc.flush();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// BluetoothSender_JavaSE.envio("stim");
			break;
		case 3:
			mensaje_Data.texto = "NA";
			System.out
					.println("  emotion:  " + u.button + "= Negative Arousal");
			
			try {
				Server.ooc.writeObject(mensaje_Data);
				 Server.ooc.flush();
				Thread.sleep(35000);
				System.out.println("TErmino entrenamiento ");
				
		//		mensaje_Data.texto = "fin";
				Server.ooc.writeObject(mensaje_Data_fin);
				 Server.ooc.flush();
				 
				 p = Runtime.getRuntime().exec("killall openvibe-designer");
			//	this.finalize();
		
		//	button.finalize();
		//	MainGui.test = null;
		//	button.stopRunning();
		//	button.removeButtonChangeListener(this);
					
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// BluetoothSender_JavaSE.envio("fin");
			// t.stop();
			// t.destroy();

			break;
		case 4:
			System.out.println("oreden de desconezxion??");
	
			
						break;
		default:
			System.out.println("No emotion detected!");
			break;
		}
		// System.out.println( "Button message from vrpn: \n" +
		// "\ttime:  " + u.msg_time.getTime( ) + "  button:  " + u.button + "\n"
		// +
		// "\tstate:  " + u.state +"\n feliz");
	}

//	public void main() {
//		String buttonName = "openvibe-vrpn@localhost";
//		ButtonRemote button = null;
//		try {
//			button = new ButtonRemote(buttonName, null, null, null, null);
//		} catch (InstantiationException e) {
//			// do something b/c you couldn't create the button
//			System.out.println("We couldn't connect to button " + buttonName
//					+ ".");
//			System.out.println(e.getMessage());
//			return;
//		}
//		HDVrpn test = new HDVrpn();
//		button.addButtonChangeListener(test);
//	}
}