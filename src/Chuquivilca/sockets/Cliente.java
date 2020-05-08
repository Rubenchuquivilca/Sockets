package Chuquivilca.sockets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.swing.*;

import java.net.*;

//socket enviara de cleinte al servidor al eviar un mensaje mediante los puertos de//
//cliente a servidor//
public class Cliente {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		MarcoCliente mimarco=new MarcoCliente();
		
		mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}


class MarcoCliente extends JFrame{
	
	public MarcoCliente(){
		
		setBounds(600,300,280,350);
				
		LaminaMarcoCliente milamina=new LaminaMarcoCliente();
		
		add(milamina);
		
		setVisible(true);
		}	
	
}

class LaminaMarcoCliente extends JPanel implements Runnable{
	
	public LaminaMarcoCliente(){
		
		nick= new JTextField(5);
		
		add(nick);
	
		JLabel texto=new JLabel("*>>>CHAT<<<*");
		
		add(texto);
		
		ip= new JTextField(8);
		
		add(ip);
		
		campochat=new JTextArea(12,20);
		
		add(campochat);
	
		campo1=new JTextField(20);
	
		add(campo1);		
	
		miboton=new JButton("Agregar");
		
		//instancia de clase envia texto
		EnviaTexto mievento= new EnviaTexto();
		
		//ponemos el boton a la escucha
		miboton.addActionListener(mievento);
		
		add(miboton);	
		
		Thread mihilo= new Thread(this);
		
		mihilo.start();
		
	}
	//hemos creado clase que va gestionar los eventos//
	private class EnviaTexto implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
			//System.out.println(campo1.getText());
			
			campochat.append("\n" + campo1.getText());
			
			try {
				Socket misocket= new Socket("192.168.0.103",9999);//vamos a introducir el mensaje mediante ese puerto
				
				PaqueteEnvio datos= new PaqueteEnvio();
				
				datos.setNick(nick.getText());
				
				datos.setIp(ip.getText());
				
				datos.setMensaje(campo1.getText());
				
				ObjectOutputStream paquete_datos= new ObjectOutputStream(misocket.getOutputStream());
				
				paquete_datos.writeObject(datos);
				
				misocket.close();
				
				
				//DataOutputStream flujo_salida=new DataOutputStream(misocket.getOutputStream());//vamos a dirigir por donde va circular el socket
				//hemos creaod un flujo de salida  que va acircular por el socket (minisocket)
				
				
				//flujo_salida.writeUTF(campo1.getText());
				//ahora vamos a decirle que es lo que va circular el el flujo de salida
				//QUe escriba este writeuf 
				//lo q dice es que vamos a mandar un mensaje de salida va  viajr el etsto del cmapo1 este flujo cicrculara el servidor con este ip abierto
			//flujo_salida.close();
			//cerramps el flujo de salida
			
			
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace(); //hace el seguimiento de errores
				System.out.print(e1.getMessage());// si ocurre un erro el socket y no puedo comunicar y nos mande un mensaje cual fue el error
			}//comunicar y nos mande un mensaje cual fue el error
		}
		
		//esto es sun servidor local
		
		//si fera un servidor remota fuera diferente
		
		
	}
	
	
	
		
		
		
	private JTextField campo1, nick, ip;
	
	private JTextArea campochat;
	
	private JButton miboton;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		 try {
			 
			 ServerSocket servidor_cliente=new ServerSocket(9090);
			 
			 Socket cliente;
			 
			 PaqueteEnvio paqueteRecibido;
			 
			 while(true) {
				  
				 cliente=servidor_cliente.accept();
				 
				  ObjectInputStream flujoentrada=new ObjectInputStream(cliente.getInputStream());
				  
				  paqueteRecibido= (PaqueteEnvio) flujoentrada.readObject();
				  
				  campochat.append("\n" +paqueteRecibido.getNick() + ": " + paqueteRecibido.getMensaje());
				  
				  
				 
			 }
			 
			 
		 }catch(Exception e) {
			 
			 System.out.print(e.getMessage());
		 }
		
	}
	
}


class PaqueteEnvio implements Serializable{
	private  String nick, ip, mensaje;

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
}