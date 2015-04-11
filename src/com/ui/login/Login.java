/**
 * 
 */
package com.ui.login;

import java.awt.EventQueue;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.socket.Message;
import com.socket.SocketData;
import com.ui.ChatFrame;

/**
 * @author Kawsar Ahmed
 * @author Abu Raihan
 *
 */
public class Login implements Runnable {

	

    private String serverAddr;
    private int port;
    private Socket socket;
    private ObjectInputStream In;
    private ObjectOutputStream Out;
    private boolean isConnected = false ;
	private Message tempMessage;
	private LoginPanel loginUI;
	private String userName;
	private String password;
    
    
    /**
	 * @param serverAddr
	 * @param port
     * @param password password of the client
     * @param userName username of the client
     * @param loginUI the login panel interface
	 */
	public Login(String serverAddr, int port, LoginPanel loginUI, String userName, String password) {
		this.serverAddr = serverAddr;
		this.port = port;
		this.loginUI = loginUI;
		this.userName = userName;
		this.password = password;
	}
	
	
	/**
	 * @return the isConnected
	 */
	public boolean isConnected() {
		return isConnected;
	}


	/**
	 * @param msg
	 * @return
	 */
	private Message send(Message msg){
		String msgContent = null;
		String msgType = "success";
        try {
            Out.writeObject(msg);
            Out.flush();
        } 
        catch (IOException ex) {
            try {
				Out.writeObject(msg);
	            Out.flush();
	            System.out.println("Outgoing : "+msg.toString());
			} catch (IOException e) {
				msgContent = "Error communicating with server";
				msgType = "error";
			}
        }
        return new Message(msgType, null, msgContent, null);
    }
	
	
	/**
	 * @param type
	 * @return
	 */
	private Message receive (String type/*, String sender, String content, String recipient*/) {
		boolean keepRunning = true;
		Message message = null;
        while(keepRunning){
			try {
				message = (Message) In.readObject();
				System.out.println("Incoming : "+message.toString());
				if(message.type.equals(type))
					return message;
			} catch (ClassNotFoundException e) {
				keepRunning = false;
				e.printStackTrace();
			} catch (IOException e) {
				keepRunning = false;
				e.printStackTrace();
			}
        }
		return null;
	}
	
	
	/**
	 * @return
	 */
	public Message connectToServer() {
		String msgContent = null;
		String msgType = "error";
		try {
			socket = new Socket(InetAddress.getByName(serverAddr), port);
			Out = new ObjectOutputStream(socket.getOutputStream());
		    Out.flush();
		    In = new ObjectInputStream(socket.getInputStream());
		    					//r/		type		sender		content		recipient
		    tempMessage = send( new Message("test", "testUser", "testContent", "SERVER") );
		    if(tempMessage.type.equals("error"))
		    	return tempMessage;
		    	
		    tempMessage = receive("test");
		    if(tempMessage != null){
		    	isConnected = true;
		    	msgType = "success";	
		    } else {
		    	msgContent = "Server is not responding.  Please check server setting from menu and Please try again.";
		    }
		} catch (UnknownHostException e) {
//			JOptionPane.showMessageDialog(JFrame.getFrames()[0], "UnknownHostException");
//			e.printStackTrace();
			msgContent = "Sorry, we don't find server: "+serverAddr+", port: "+port+". Please check server setting from menu.";
		} catch (IOException e) {
//			JOptionPane.showMessageDialog(JFrame.getFrames()[0], "IOException");
//			e.printStackTrace();
	    	msgContent = "Error connecting to Server. Please check server setting from menu as well as the network setting.";
		}
		return new Message( msgType, null, msgContent, null );
	}


	/**
	 * @return
	 */
	public Message checkLogin() {
	
		if(!userName.isEmpty() && !password.isEmpty()){
			send(new Message("login", userName, password, "SERVER"));
			//r/		type		sender		content		recipient
			tempMessage = receive("login");
						//r/	type of msg
			if (tempMessage == null ){
				JOptionPane.showMessageDialog(JFrame.getFrames()[0], "Server is not responding. Please retry.");
				return new Message("error", null, "Server is not responding. Please retry.", null);
			}
			else if ( tempMessage.sender.equals("SERVER") && tempMessage.content.equals("TRUE"))
				return new Message("success", null, null, null);
		}
		return new Message("error", null, "Sorry, we don't recognize your sign-in details. Please check sign in details then try again.", null);
	}


	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		tempMessage = connectToServer();
		if( tempMessage.type.equals("error") ) {
			
			returnToLoginUI(tempMessage.content);
		
		} else {
			
			//miliseconds delay just for showing the nice loading animation- fun haahaa ha
			delay(800); 
			
			tempMessage = checkLogin( );
			if( tempMessage.type.equals("success") ) {
				EventQueue.invokeLater(new Runnable() {
			            public void run() {
							new ChatFrame( new SocketData(socket, Out, In ) , userName);
			            }
			        });
				loginUI.exitLoginUI();
			}
			else {			
				returnToLoginUI(tempMessage.content);
			}	
		}
	}


	/**
	 * @param milis
	 */
	private void delay(int milis) {try {
	    Thread.sleep(milis);
	} catch(InterruptedException ex) {
	    Thread.currentThread().interrupt();
	}
		
	}


	/**
	 * @param errorMsg
	 */
	private void returnToLoginUI(String errorMsg) {
			
		closeStreams();
		closeSocket();
		loginUI.showLoginAndRemoveLoading(errorMsg);
	}


	/**
	 * @throws IOException
	 */
	public void closeSocket() {
		try {
			if(socket != null)
				socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * @throws IOException
	 */
	public void closeStreams() {
		try {
			if(In != null)
				In.close();
			if(Out != null)
				Out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
