package com.socket;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.imageio.ImageIO;

import com.ui.ChatFrame;

public class Download implements Runnable{
    
    public ServerSocket server;
    public Socket socket;
    public int port;
    public String saveTo ;
    public InputStream In;
    public FileOutputStream Out;
    public ChatFrame ui;
    private boolean fileTransfer = true;
	private BufferedImage bufferedImage;
    

	public Download(String saveTo, ChatFrame ui){
        try {
            server = new ServerSocket(0);
            port = server.getLocalPort();
            this.saveTo = saveTo;
            this.ui = ui;
        } 
        catch (IOException ex) {
            System.out.println("Exception [Download : Download(...)]");
        }
    }
	public Download(){
        try {
            server = new ServerSocket(0);
            port = server.getLocalPort();
        } 
        catch (IOException ex) {
            System.out.println("Exception [Download : Download(...)]");
        }
        fileTransfer = false;
    }

    public BufferedImage getImage() {
    	return bufferedImage;
    }
    
	@Override
    public void run() {
        try {
            socket = server.accept();
            
            In = socket.getInputStream();
            if(fileTransfer){
            	System.out.println("Download : "+socket.getRemoteSocketAddress());
            	Out = new FileOutputStream((String) saveTo);
            	
            	byte[] buffer = new byte[1024];
            	int count;
            	
            	while((count = In.read(buffer)) >= 0){
            		Out.write(buffer, 0, count);
            	}
            	
            	Out.flush();
            	
            	ui.notificationArea.append("[Application > Me] : Download complete\n");
            	
            	if(Out != null){ Out.close(); }
            }
            else {
            	synchronized (this) {
            		bufferedImage=ImageIO.read(ImageIO.createImageInputStream(In));
            		notify();
				}
            }
            if(In != null){ In.close(); }
            if(socket != null){ socket.close(); }
        } 
        catch (Exception ex) {
            System.out.println("Exception [Download : run(...)]");
        }
    }
}