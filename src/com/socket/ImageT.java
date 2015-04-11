package com.socket;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ImageT {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		Download dwn = new Download();
		JFileChooser fc = new JFileChooser("D:\\CSE_COURSES_JKKNIU\\Semesters\\Yearly project\\birdchat icons\\latest shot");
		int r = fc.showOpenDialog(null);
		if(r != JFileChooser.APPROVE_OPTION)
			return;
		File file = fc.getSelectedFile();
		BufferedImage bimg = ImageIO.read(file.getAbsoluteFile());
		bimg = ImageProcessor.scaleBalanced(bimg, BufferedImage.TYPE_INT_RGB, 48, 0);
		Upload ul = new Upload(dwn.server.getInetAddress().getHostAddress(), dwn.port, bimg);
		new Thread(dwn).start();
		new Thread(ul).start();
		synchronized (dwn) {
			dwn.wait();
		}
		JLabel lblimg = new JLabel();
        lblimg.setIcon(new ImageIcon(dwn.getImage()));
        JPanel jp = new JPanel();
        jp.add(lblimg);
        JScrollPane jsp = new JScrollPane();
        jsp.setViewportView(jp);
        JOptionPane.showMessageDialog(null, jsp);
	}

}
