package com.socket;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import com.data.ChatData;
import com.data.ProfileData;
import com.ui.ChatFrame;

public class SocketClient implements Runnable{
    
    public int port;
    public String serverAddr;
    public Socket socket;
    public ChatFrame ui;
    public ObjectInputStream In;
    public ObjectOutputStream Out;
	private boolean keepRunning;
	public ProfileData profileData;
	public ChatData chatData;
	private Color contactHighlightColor;
    
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
        profileData = new ProfileData();
        chatData = new ChatData();
        contactHighlightColor = ui.lightBlue;
        
    }

    
    @SuppressWarnings("unchecked")
	@Override
    public void run() {
        keepRunning = true;
        while(keepRunning){
            try {
                Message msg = (Message) In.readObject();
                System.out.println("Incoming : "+msg.toString());
                
                if(msg.type.equals("message")){
                	if(msg.sender.equals(ui.username) == false) {
                		react();
                		handleNewMessage(msg);
                	}
                }
                else if(msg.type.equals("newuser")){
                	react();
                    if(!msg.content.equals(ui.username)){
                        boolean exists = false;
                        for(int i = 0; i < ui.model.getSize(); i++){
                        	JLabel user = (JLabel) ui.model.getElementAt(i); 
                            if(user.getText().equals(msg.content)){
                                exists = true; break;
                            }
                        }
                        if(!exists){
                        	manageNewUser(msg);
                        }
                    }
                }
                else if(msg.type.equals("signout")){
                	// TODO notify and remove all profiledata, chatdata
                    if(msg.content.equals(ui.username)){
                        ui.notificationArea.append("["+ msg.sender +" > Me] : Bye\n");
                        ui.sendMessageButton.setEnabled(false); 
                        
                        for(int i = 1; i < ui.model.size(); i++){
                            ui.model.removeElementAt(i);
                        }

                        keepRunning = false;
                    }
                    else{
                    	react();
                        ui.model.removeElement(msg.content);
                        ui.notificationArea.append("["+ msg.sender +" > All] : "+ msg.content +" has signed out\n");
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
                        	// TODO  sending file message add to the panel
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
                    	// TODO 
                        ui.notificationArea.append("[SERVER > Me] : "+msg.sender+" rejected file request\n");
                    }
                }
                else if(msg.type.equals("profile_data_res")){
                	if(msg.dataType.equals("name")){
                		profileData.setName(msg.recipient, msg.content);
                		if(msg.recipient.equals(ui.username))
                			profileData.getContactLabel(ui.username).setText(msg.content);
                	} else if (msg.dataType.equals("about")) {
                		profileData.setAbout(msg.recipient, msg.content);
                	}
                }
                else{
                    ui.notificationArea.append("[SERVER > Me] : Unknown message type\n");
                }
            }
            catch(Exception ex) {
                ui.notificationArea.append("[Application > Me] : Connection Failure\n");
                ui.sendMessageButton.setEnabled(false);
                ui.sendFileChooserButton.setEnabled(false);
                
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
	 * @param msg
	 */
	private void manageNewUser(Message msg) {
		ui.model.addElement(new JLabel(msg.content,ui.defaultContactIcon32, SwingConstants.LEFT)); 
		loadProfileData(msg.content, "all");
		JPanel chatPanel = new JPanel();
		chatPanel.setLayout(new GridBagLayout());
		profileData.setProfilePicture(msg.content, ui.defaultProfilePicture);
		chatData.setContactPanel(msg.content, chatPanel );
		chatData.setGridBagConstraint(msg.content, new GridBagConstraints());
		chatData.setLastSender(msg.content, "none");
		ui.notificationArea.append("\n"+msg.content+" is online.");
	}


	/**
	 * @param msg
	 */
	private void handleNewMessage(Message msg) {
		// TODO highlight user who sent a message
		ui.notificationArea.append("\n# new message from "+ msg.sender);
		if(ui.contactList.getSelectedValue() != profileData.getContactLabel(msg.sender))
			profileData.getContactLabel(msg.sender).setBackground(contactHighlightColor);
		// TODO
		JPanel chatPanel = chatData.getContactPanel(msg.sender);
		GridBagConstraints gridContraints = chatData.getGridBagConstraint(msg.sender);
		
		gridContraints.gridx = 0;
		if( chatData.getLastSender(msg.sender) == ui.username) {
			chatData.setLastSender(msg.sender, msg.sender);
			JTextArea nameTextArea = new JTextArea(profileData.getName(msg.sender));
			nameTextArea.setSize(ui.nameBoxSize);
			nameTextArea.setBorder(null);
			nameTextArea.setBackground(ui.white);
			chatPanel.add(nameTextArea,gridContraints);
		}

		JTextArea messageTxtArea = new JTextArea();
		messageTxtArea.setLineWrap(true);
		messageTxtArea.setWrapStyleWord(true);
		messageTxtArea.setSize(ui.messageBoxSize);
		messageTxtArea.setText(msg.content);
		messageTxtArea.setSize(messageTxtArea.getPreferredSize());
		messageTxtArea.setBorder(null);
		messageTxtArea.setBackground(ui.white);
		gridContraints.gridx++;
		chatPanel.add(messageTxtArea,gridContraints);

		JTextArea timeTxtArea = new JTextArea(getTime());
		timeTxtArea.setSize(ui.timeBoxSize);
		timeTxtArea.setBorder(null);
		timeTxtArea.setBackground(ui.white);
		gridContraints.gridx++;
		chatPanel.add(timeTxtArea,gridContraints);
		
		gridContraints.gridy++;
		
		if(msg.recipient.equals(ui.username)){
		    ui.notificationArea.append("["+msg.sender +" > Me] : " + msg.content + "\n");
		}
		else{
		    ui.notificationArea.append("["+ msg.sender +" > "+ msg.recipient +"] : " + msg.content + "\n");
		}
		// TODO add row to chatpanel
	}


	/**
	 * 
	 */
	private void react() {
		Toolkit.getDefaultToolkit().beep();
		ui.toFront();
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
            System.out.println("Exception SocketClient send()");
        }
    }
	/**
	 * 
	 */
	public void loadProfileData(final String user, final String dataType) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Download dwn = new Download();
					send(new Message("profile_data_req", ui.username, user, dataType, ""+dwn.port));
					if(dataType.equals("all") == false){
						return;
					}
					new Thread(dwn).start();
					synchronized (dwn) {
						dwn.wait();
					}
					profileData.setProfilePicture(ui.username, dwn.getImage());
					BufferedImage labelImage;
					if(user == ui.username)
						labelImage = ImageProcessor.scaleBalanced(dwn.getImage(), BufferedImage.TYPE_INT_RGB, 48, 0);
					else
						labelImage = ImageProcessor.scaleBalanced(dwn.getImage(), BufferedImage.TYPE_INT_RGB, 32, 0);
					profileData.getContactLabel(user).setIcon(new ImageIcon(labelImage));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
	
		}).start();
	}
	
	/**
	 * @return current time as h:mm AM/PM
	 */
	@SuppressWarnings("static-access")
	public String getTime(){
		SimpleDateFormat time = new SimpleDateFormat("h:mm a");
		return time.getInstance().format(Calendar.getInstance().getTime());
	}
	
    public void closeThread(Thread t){
        t = null;
    }
}
