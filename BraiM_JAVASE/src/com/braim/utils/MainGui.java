package com.braim.utils;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;

import javax.print.DocFlavor.READER;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;


import vrpn.ButtonRemote;

import java.awt.BorderLayout;
import javax.swing.BoxLayout;

public class MainGui extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	static Process p;
	private static JTextField txtNull;
	private static JLabel lblCl;
	private JTextField txtS;
	private JTextField textField_1;
	private JLabel lblOpenvibePath;
	private static JTextField txt_entrenamiento_path;
	private static JTextField txt_open_path;
	private static Process acquisition;
	private static ButtonRemote button;
	public static HDVrpn test = null;
	private JLabel label_1;
	private static JLabel lblConexion;
	private static JTextField txt_classificacion;
	private static Process p_class;
	private static ButtonRemote button_class;
	private static HDVrpn_class test_class;

	/**
	 * Launch the application.
	 */
	class WindowEventHAndler extends WindowAdapter{

		@Override
		public void windowClosing(WindowEvent e) {
			
			// TODO Auto-generated method stub
				if (Server.ooc != null){
				Mensaje_data mensaje_Data = new Mensaje_data();
				// action -1 no es mensaje de accion
				mensaje_Data.Action = -1;
				// // no es el ultimo msg
				mensaje_Data.last_msg = false;
				mensaje_Data.texto = "descon";
				
			
			
			try {
				
				if (test !=null){
					button.removeButtonChangeListener(test);
			//		test.butt.finalize();
			//		test.butt.stopRunning();
									}
				Server.ooc.writeObject(mensaje_Data);
				 Server.ooc.flush();
				 
					
			//	button.finalize();
			//	Server.ooc.writeObject();
			} catch (Throwable e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		//	button.stopRunning();
			
			//super.windowClosing(e);
				}
				try {
					p = Runtime.getRuntime().exec("killall openvibe-acquisition-server");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		System.exit(0);
		}
		
	}
	public static void main(String[] args) throws IOException {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGui frame = new MainGui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});
		try {
			// p =
			// Runtime.getRuntime().exec("bash "+openvibe_path+" --no-gui --no-session-management --play "+classification_path);
			acquisition = Runtime.getRuntime().exec("bash /home/jesus/openvibe/dist/openvibe-acquisition-server.sh");
		
	}catch(Exception e){
		}
		Server s = new Server();
		
	}




	/**
	 * Create the frame.
	 */
	public MainGui() {

	//	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Configuracion BraiM");
		addWindowListener(new WindowEventHAndler());
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		

		JLabel lblBramFeel = new JLabel("BraiM .. Feel the music");
		lblBramFeel.setBounds(12, 12, 171, 15);
		contentPane.add(lblBramFeel);

		lblCl = new JLabel("Path Entrenamiento");
		lblCl.setBounds(35, 110, 183, 15);
		contentPane.add(lblCl);

		lblOpenvibePath = new JLabel("Openvibe path");
		lblOpenvibePath.setBounds(34, 52, 161, 15);
		contentPane.add(lblOpenvibePath);

		txt_entrenamiento_path = new JTextField();
		txt_entrenamiento_path.setBounds(25, 140, 377, 19);
		contentPane.add(txt_entrenamiento_path);

		txt_entrenamiento_path.setText("/home/jesus/Dropbox/Hernan-Chu/scripts_matlab/prueba_entreno_jesus.xml");
//		txt_entrenamiento_path.setText("/home/jesus/Dropbox/Hernan-Chu/scripts_matlab/emotions_server_jesus.xml");
		
		// txt_class_path.setText("/home/jesus/Escritorio/prueba_entreno3.xml");
		txt_entrenamiento_path.setColumns(10);

		txt_open_path = new JTextField();
		txt_open_path.setBounds(25, 79, 377, 19);
		contentPane.add(txt_open_path);
		txt_open_path
				.setText("/home/jesus/openvibe/dist/openvibe-designer.sh");
		// txt_open_path.setText("/home/jesus/openvibe/dist/openvibe-designer.sh");
		txt_open_path.setColumns(10);
		
		label_1 = new JLabel("Path clasificacion");
		label_1.setBounds(35, 171, 183, 15);
		contentPane.add(label_1);
		
		txt_classificacion = new JTextField();
	//	txt_classificacion.setText("/home/jesus/Dropbox/Hernan-Chu/scripts_matlab/prueba_entreno_jesus.xml");
		txt_classificacion.setText("/home/jesus/Dropbox/Hernan-Chu/scripts_matlab/emotions_server_jesus.xml");
		txt_classificacion.setColumns(10);
		txt_classificacion.setBounds(25, 198, 377, 19);
		contentPane.add(txt_classificacion);
		
		lblConexion = new JLabel("Sin Conexion ...");
		lblConexion.setBounds(253, 229, 139, 15);
		contentPane.add(lblConexion);
		lblConexion.setForeground(Color.RED);

		
	
	}

	public static void ponerCancion(String track) {
	//	lblSong.setText(track);
	}

	public static void ponerPlayLbl() {
	//	lblReproducir.setVisible(true);
	}

	public static void quitarPlayLbl() {
	//	lblReproducir.setVisible(false);
	}

	public static void ponerUsuarioLbl(String user) {
	//	lblUser.setText(user);
	}

	public static void ponerEstado(int funcion) {
		if (funcion == 0){
			lblConexion.setText("Conectado");
		
	//	lblTraining.setText("Conectado");
		lblConexion.setForeground(Color.GREEN);
		}else{
			lblConexion.setText("Sin conexion");
			
	//		lblClasificacion.setForeground(Color.GREEN);
		}
		
	}

	public static void desconexionbl() {
	//	lblSong.setText("");
	//	lblUser.setText("");
	//	lblTraining.setText("Sin Conexion");
	//	lblTraining.setForeground(Color.RED);
	//	lblClasificacion.setForeground(Color.GREEN);
		
		if (test != null){
			try {
				button.removeButtonChangeListener(test);
				//	test.butt.finalize();
				if (HDVrpn.butt !=null){
				HDVrpn.butt.finalize();}
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	//		test.butt.stopRunning();
			test = null;
		}	
		System.exit(0);
	}
	
	

	public static void entrenar() {
		String classification_path = txt_entrenamiento_path.getText().toString().trim();
		String openvibe_path = txt_open_path.getText().toString().trim();

		if (classification_path.equals("") || classification_path == null
				|| openvibe_path.equals("") || openvibe_path == null) {
			JOptionPane.showMessageDialog(null, "Error paths");
		} else {
		//	button.removeButtonChangeListener(test);
//			if (HDVrpn.butt !=null){
//				button.removeButtonChangeListener(test);
//				//	test.butt.finalize();
//					if (HDVrpn.butt !=null){
//					try {
//						HDVrpn.butt.finalize();
//					} catch (Throwable e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}}
//			}
			

			try {
				// p =
				// Runtime.getRuntime().exec("bash "+openvibe_path+" --no-gui --no-session-management --play "+classification_path);
				p = Runtime.getRuntime().exec(
						"bash " + openvibe_path
								+ " --no-session-management --no-gui --play "
								+ classification_path);
		
				
				String buttonName = "openvibe-vrpn@localhost";
				
				 button = null;
				
				try {
			
				
					button = new ButtonRemote(buttonName, null, null, null,
							null);
					
				} catch (InstantiationException e) {
					// do something b/c you couldn't create the button
					System.out.println("We couldn't connect to button "
							+ buttonName + ".");
					System.out.println(e.getMessage());
					return;
				}
				
				if (test== null){
				test = new HDVrpn();
				button.addButtonChangeListener(test);
				}
				
				
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.out.println(e1+"ERRRORRRORORO");
				//JOptionPane.showMessageDialog(null, "Error paths");
			}
		}
	}
	
	public static void clasificar() {
		String classification_path = txt_classificacion.getText().toString().trim();
		String openvibe_path = txt_open_path.getText().toString().trim();

		if (classification_path.equals("") || classification_path == null
				|| openvibe_path.equals("") || openvibe_path == null) {
			JOptionPane.showMessageDialog(null, "Error paths");
		} else {
		//	button.removeButtonChangeListener(test);
//			if (HDVrpn.butt !=null){
//				button.removeButtonChangeListener(test);
//				//	test.butt.finalize();
//					if (HDVrpn.butt !=null){
//					try {
//						HDVrpn.butt.finalize();
//					} catch (Throwable e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}}
//			}
			

			try {
				// p =
				// Runtime.getRuntime().exec("bash "+openvibe_path+" --no-gui --no-session-management --play "+classification_path);
				p_class = Runtime.getRuntime().exec(
						"bash " + openvibe_path
								+ " --no-session-management --no-gui --play "
								+ classification_path);
		
				
				String buttonName = "openvibe-vrpn-live@localhost";
				
				 button_class = null;
				
				try {
			
				
					button_class = new ButtonRemote(buttonName, null, null, null,
							null);
					
				} catch (InstantiationException e) {
					// do something b/c you couldn't create the button
					System.out.println("We couldn't connect to button "
							+ buttonName + ".");
					System.out.println(e.getMessage());
					return;
				}
				
				if (test_class== null){
				test_class = new HDVrpn_class();
				button_class.addButtonChangeListener(test_class);
				}
				
				
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.out.println(e1+"ERRRORRRORORO");
				//JOptionPane.showMessageDialog(null, "Error paths");
			}
		}
	}

	public static void clasificador_cancelado() {
		// TODO Auto-generated method stub
	//	button.stopRunning();
	//	button.stoppedRunning();
		if (test_class != null){
			try {
				button_class.removeButtonChangeListener(test_class);
			//	test.butt.finalize();
				if (HDVrpn_class.butt !=null){
				HDVrpn_class.butt.finalize();}
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//	HDVrpn.butt.stopRunning();
			test_class = null;
		}	
//		
//		try {
//			button.finalize();
//			button.removeButtonChangeListener(test);
//		
//		} catch (Throwable e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		try {
			
			p = Runtime.getRuntime().exec("killall openvibe-designer");
	//		System.exit(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void entreno_cancelado() {
		// TODO Auto-generated method stub
	//	button.stopRunning();
	//	button.stoppedRunning();
		if (test != null){
			try {
				button.removeButtonChangeListener(test);
			//	test.butt.finalize();
				if (HDVrpn.butt !=null){
				HDVrpn.butt.finalize();}
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//	HDVrpn.butt.stopRunning();
			test = null;
		}	
//		
//		try {
//			button.finalize();
//			button.removeButtonChangeListener(test);
//		
//		} catch (Throwable e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		try {
			
			p = Runtime.getRuntime().exec("killall openvibe-designer");
	//		System.exit(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
