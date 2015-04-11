package com.socket;

import java.awt.EventQueue;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import com.ui.ChatFrame;
import com.ui.login.LoginUI;

public class SocketClient implements Runnable{
    
    public int port;
    public String serverAddr;
    public Socket socket;
    public ChatFrame ui;
    public ObjectInputStream In;
    public ObjectOutputStream Out;
	private boolean keepRunning;
    
    public SocketClient(ChatFrame frame, SocketData socketData) {
    	ui = frame; this.serverAddr = ui.serverAddr; this.port = ui.port;
        socket = socketData.socket;
        Out = socketData.objectOutputStream;
        try {
			Out.flush();
		} catch (IOException e1) {
			try {
				Out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        In = socketData.objectInputStream;
    }

    
    @SuppressWarnings("unchecked")
	@Override
    public void run() {
        keepRunning = true;
        while(keepRunning){
            try {
                Message msg = (Message) In.readObject();
                System.out.println("Incoming : "+msg.toString());
                ui.requestFocus();
                Toolkit.getDefaultToolkit().beep();
                
                if(msg.type.equals("message")){
                    if(msg.recipient.equals(ui.username)){
                        ui.largeTextArea.append("["+msg.sender +" > Me] : " + msg.content + "\n");
                    }
                    else{
                        ui.largeTextArea.append("["+ msg.sender +" > "+ msg.recipient +"] : " + msg.content + "\n");
                    }
                    if (msg.content.equals("Sending file to 'All' is forbidden") && msg.sender.equals("SERVER"))
                    	JOptionPane.showMessageDialog(ui, "Sending file to 'All' is forbidden");
                }
                else if(msg.type.equals("login")){
                    if(msg.content.equals("TRUE")){
                        ui.largeTextArea.setEnabled(false); 
                        ui.sendMessageButton.setEnabled(true);
                        ui.largeTextArea.append("[SERVER > Me] : Login Successful\n");
                    }
                    else{
                        ui.largeTextArea.append("[SERVER > Me] : Login Failed\n");
                    }
                }
                else if(msg.type.equals("test")){
                    ui.largeTextArea.setEnabled(true);
                    ui.sendFileChooserButton.setEnabled(true);
                }
                else if(msg.type.equals("newuser")){
                    if(!msg.content.equals(ui.username)){
                        boolean exists = false;
                        for(int i = 0; i < ui.model.getSize(); i++){
                        	JLabel user = (JLabel) ui.model.getElementAt(i); 
                            if(user.getText().equals(msg.content)){
                                exists = true; break;
                            }
                        }
                        if(!exists){
                        	ui.model.addElement(new JLabel(msg.content, ui.defaultIcon32, SwingConstants.LEFT)); 
                        }
                    }
                }
                else if(msg.type.equals("signup")){
                    if(msg.content.equals("TRUE")){
                        ui.largeTextArea.setEnabled(false); 
                        ui.sendMessageButton.setEnabled(true); 
                        ui.largeTextArea.append("[SERVER > Me] : Singup Successful\n");
                    }
                    else{
                        ui.largeTextArea.append("[SERVER > Me] : Signup Failed\n");
                    }
                }
                else if(msg.type.equals("signout")){
                    if(msg.content.equals(ui.username)){
                        ui.largeTextArea.append("["+ msg.sender +" > Me] : Bye\n");
                        ui.sendMessageButton.setEnabled(false); 
                        
                        for(int i = 1; i < ui.model.size(); i++){
                            ui.model.removeElementAt(i);
                        }

                        logOut();
                    }
                    else{
                    	ui.contactList.setSelectedIndex(0);
                    	for(int i = 1; i < ui.model.size(); i++){
                    		JLabel target = (JLabel) ui.model.getElementAt(i);
                    		if( target.getText().equals(msg.content)){
                    			ui.model.removeElementAt(i);
                    			break;
                    		}
                    	}
                        ui.largeTextArea.append("["+ msg.sender +" > All] : "+ msg.content +" has signed out\n");
                    }
                }
                else if(msg.type.equals("upload_req")){
                    											//r/	file name
                    if(JOptionPane.showConfirmDialog(ui, ("Accept '"+msg.content+"' from "+msg.sender+" ?")) == 0){
                        
                        JFileChooser jf = new JFileChooser();
                        jf.setSelectedFile(new File(msg.content));
                        int returnVal = jf.showSaveDialog(ui);
                       
                        String saveTo = jf.getSelectedFile().getPath();
                        if(saveTo != null && returnVal == JFileChooser.APPROVE_OPTION){
                            Download dwn = new Download(saveTo, ui);
                            Thread t = new Thread(dwn);
                            t.start();
                            //send(new Message("upload_res", (""+InetAddress.getLocalHost().getHostAddress()), (""+dwn.port), msg.sender));
                            send(new Message("upload_res", ui.username, (""+dwn.port), msg.sender));
                        }				//r/		type		sender		content		recipient
                        else{
                            send(new Message("upload_res", ui.username, "NO", msg.sender));
                        }			//r/		type		sender		content		recipient
                    }
                    else{
                        send(new Message("upload_res", ui.username, "NO", msg.sender));
                    }			//r/		type		sender		content		recipient
                }
                else if(msg.type.equals("upload_res")){
                    if(!msg.content.equals("NO")){
                        int port  = Integer.parseInt(msg.content);
                        String addr = msg.sender;
                        
                        ui.sendFileButton.setEnabled(false);
                        Upload upl = new Upload(addr, port, ui.file, ui);
                        Thread t = new Thread(upl);
                        t.start();
                    }
                    else{
                        ui.largeTextArea.append("[SERVER > Me] : "+msg.sender+" rejected file request\n");
                    }
                }
                else{
                    ui.largeTextArea.append("[SERVER > Me] : Unknown message type\n");
                }
                ui.largeTextScrollPane.getVerticalScrollBar().setValue(ui.largeTextScrollPane.getVerticalScrollBar().getMaximum()+1000);
                ui.messageInputTextField.requestFocus();
            }
            catch(Exception ex) {
                ui.largeTextArea.append("[Application > Me] : Connection Lost to the Server\n");
                ui.sendMessageButton.setEnabled(false);
                ui.sendFileChooserButton.setEnabled(false);
                ui.sendFileButton.setEnabled(false);
                
                for(int i = 1; i < ui.model.size(); i++){
                    ui.model.removeElementAt(i);
                }
                
                keepRunning = false;
                
                System.out.println("Exception SocketClient run()");
                ex.printStackTrace();
            }
        }
    }


	/**
	 * 
	 */
	private void logOut() {
		keepRunning = false;
		try {
			In.close();
			Out.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					new LoginUI();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		ui.dispose();
	}
    
    /**
	 * @param keepRunning the keepRunning to set
	 */
	public void setKeepRunning(boolean keepRunning) {
		this.keepRunning = keepRunning;
	}

	public void send(Message msg){
        try {
            Out.writeObject(msg);
            Out.flush();
            System.out.println("Outgoing : "+msg.toString());
        } 
        catch (IOException ex) {
        	if(msg.type.equals("signout"))
        		logOut();
        	else
        		System.out.println("Exception SocketClient send()");
        }
    }
    
    public void closeThread(Thread t){
        t = null;
    }
    public void requestLogOut(){
    	send(new Message("signout", ui.username, ".bye", "SERVER")); 
    	
    }
}
