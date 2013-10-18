package com.braim.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


import vrpn.ButtonRemote;

public class HDVrpn_class implements vrpn.ButtonRemote.ButtonChangeListener {



	private Process p;
	public static ButtonRemote butt;

	public void buttonUpdate(ButtonRemote.ButtonUpdate u, ButtonRemote button) {
	
		Mensaje_data mensaje_Data = new Mensaje_data();
		
		// action -1 no es mensaje de accion
		mensaje_Data.Action = -1;
		// // no es el ultimo msg
		mensaje_Data.last_msg = false;
	//	System.out	.println("  emotion:  " + u.button);
		try{
		switch (u.button) {
		
		case 0:
	
			System.out
					.println("  emotion:  " + u.button + "= Positive Valence");
			
			mensaje_Data.texto = "emo_pv";
			Server.ooc.writeObject(mensaje_Data);
			Server.ooc.flush();
			
			// BluetoothSender_JavaSE.envio("stim");
			break;
		case 1:
		
			System.out
					.println("  emotion:  " + u.button + "= Negative Valence");
		
			mensaje_Data.texto = "emo_nv";
			Server.ooc.writeObject(mensaje_Data);
			Server.ooc.flush();
			
			break;
		case 2:
			
			System.out
					.println("  emotion:  " + u.button + "= Positive Arousal");
			
			mensaje_Data.texto = "emo_pa";
			Server.ooc.writeObject(mensaje_Data);
			Server.ooc.flush();
			
			
			break;
		case 3:

			System.out
					.println("  emotion:  " + u.button + "= Negative Arousal");
		
			mensaje_Data.texto = "emo_na";
			Server.ooc.writeObject(mensaje_Data);
			Server.ooc.flush();
			break;

		default:
			System.out.println("No emotion detected!");
			break;
			
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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