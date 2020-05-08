package Chuquivilca.sockets;

import javax.swing.*;

import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor  {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		MarcoServidor mimarco=new MarcoServidor();
		
		mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
	}	
}

class MarcoServidor extends JFrame implements Runnable {
	
	public MarcoServidor(){
		
		setBounds(1200,300,280,350);				
			
		JPanel milamina= new JPanel();//hemos creado la lamina  dentro del constructor del marco
		
		milamina.setLayout(new BorderLayout());//
		
		areatexto=new JTextArea();
		
		milamina.add(areatexto,BorderLayout.CENTER);
		
		add(milamina);
		
		setVisible(true);
		
		Thread mihilo=new Thread(this);
		mihilo.start();
		
		}
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		//System.out.print("estoy en ahi");
		
		//serversocket pondra ala escucha el servidor en un puerto que le indiquemos
		
		try {
			ServerSocket servidor =new ServerSocket(9999);//con eso ya lo hemos puesto en escucho con el peurto 9999
			
			String nick, ip, mensaje;
			
			PaqueteEnvio paquete_recibido;
			
			while(true) {
				
			
			Socket misocket=servidor.accept(); 
			
			ObjectInputStream paquete_datos=new ObjectInputStream(misocket.getInputStream());
			
			paquete_recibido=(PaqueteEnvio) paquete_datos.readObject();
			
			nick=paquete_recibido.getNick();
			
			ip=paquete_recibido.getIp();
			
			mensaje = paquete_recibido.getMensaje();
			
			//crear flujo de entrada
			/*DataInputStream flujo_entrada=new DataInputStream(misocket.getInputStream());
			
			String mensaje_texto=flujo_entrada.readUTF();
			
			areatexto.append("\n" + mensaje_texto);*/
			
			areatexto.append("\n" + nick + ":" + mensaje + "para" + ip);// un servidor y un cliente aqui crearemos un ouente
			
			Socket enviaDestinatario= new Socket(ip,9090);//tender un puente y el puerto de entrada va ser el 9090
			
			ObjectOutputStream paqueteReenvio= new ObjectOutputStream(enviaDestinatario.getOutputStream());
			
			paqueteReenvio.writeObject(paquete_recibido);
			
	        paqueteReenvio.close();
			
			enviaDestinatario.close();
			
			misocket.close();
			}
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private	JTextArea areatexto;
}