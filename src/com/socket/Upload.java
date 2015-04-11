package com.socket;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JOptionPane;

import com.ui.ChatFrame;

public class Upload implements Runnable{

    public String addr;
    public int port;
    public Socket socket;
    public FileInputStream In;
    public OutputStream Out;
    public File file;
    public ChatFrame ui;
    
    public Upload(String addr, int port, File filepath, ChatFrame frame){
        super();
        try {
            file = filepath; ui = frame;
            socket = new Socket(InetAddress.getByName(addr), port);
            Out = socket.getOutputStream();
            In = new FileInputStream(filepath);
        } 
        catch (Exception ex) {
            System.out.println("Exception [Upload : Upload(...)]");
        }
    }
    
    @Override
    public void run() {
        try {       
            byte[] buffer = new byte[1024];
            int count;
            
            while((count = In.read(buffer)) >= 0){
                Out.write(buffer, 0, count);
            }
            Out.flush();
            
            if(In != null){ In.close(); }
            if(Out != null){ Out.close(); }
            if(socket != null){ socket.close(); }

            ui.largeTextArea.append("[Applcation > Me] : File Sent\n");
            JOptionPane.showMessageDialog(ui, "File Sent");
            ui.sendFileButton.setEnabled(true);
            ui.fileSendInputTextField.setVisible(true);
        }
        catch (Exception ex) {
            System.out.println("Exception [Upload : run()]");
            ex.printStackTrace();
            ui.largeTextArea.append("[Applcation > Me] : Problem in sending File\n");
            JOptionPane.showMessageDialog(ui, "Problem in sending File, please try again.");
        }
    }

}