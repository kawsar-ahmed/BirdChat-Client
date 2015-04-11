package com.socket;

import com.ui.ChatFrame;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

public class Download implements Runnable{
    
    public ServerSocket server;
    public Socket socket;
    public int port;
    public String saveTo = "";
    public InputStream In;
    public FileOutputStream Out;
    public ChatFrame ui;
    
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

    @Override
    public void run() {
        try {
            socket = server.accept();
            System.out.println("Download : "+socket.getRemoteSocketAddress());
            
            In = socket.getInputStream();
            Out = new FileOutputStream(saveTo);
            
            byte[] buffer = new byte[1024];
            int count;
            
            while((count = In.read(buffer)) >= 0){
                Out.write(buffer, 0, count);
            }
            
            Out.flush();
            
            if(Out != null){ Out.close(); }
            if(In != null){ In.close(); }
            if(socket != null){ socket.close(); }

            ui.largeTextArea.append("[Application > Me] : File Received Succesfully.\n");
            JOptionPane.showMessageDialog(ui, "File Received");
        } 
        catch (Exception ex) {
            System.out.println("Exception [Download : run(...)]");
            ui.largeTextArea.append("[Application > Me] : Problem in receiving File.\n");
            JOptionPane.showMessageDialog(ui, "Problem in receiving File");
        }
    }
}