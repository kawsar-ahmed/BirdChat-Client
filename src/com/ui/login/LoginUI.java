package com.ui.login;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class LoginUI extends JFrame{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LoginPanel loginPanel;
	
	private static final Color deepBlue = new Color(12, 125, 175);
	@SuppressWarnings("unused")
	private static final Color lightBlue = new Color(204, 239, 252);
	private static final Color white = new Color(255, 255, 255) ;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					new LoginUI();
				
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public LoginUI() throws IOException{
		
		init();
		addMenus();
		
		LoadingIconPanel loadingIconPanel = new LoadingIconPanel(getWidth(), getHeight());
		loadingIconPanel.setOpaque(false);

		loginPanel = new LoginPanel(loadingIconPanel);
		loginPanel.setOpaque(false);
		
		add(loadingIconPanel);
		add(loginPanel);
		
		JButton signInButton= loginPanel.getSignInButton();
		getRootPane().setDefaultButton(signInButton);

        setVisible(true);
	}
	
	public void init() throws IOException{
		setTitle("Sign in to Birdchat");
		
		// the path must be relative to your *class* files
		String imagePath = "images/Birdchat-icon.png";
		InputStream imgStream = getClass().getResourceAsStream(imagePath );
		BufferedImage myImg = ImageIO.read(imgStream);
		setIconImage(myImg);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 718, 500);
		setLocationRelativeTo(null);
		setResizable(false);
	}
	
	public void addMenus() {
		
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBorder(null);
		menuBar.setBackground(deepBlue );
	    setJMenuBar(menuBar);
		
	    JMenu fileMenu = new JMenu("File");
	    fileMenu.setForeground(white);
	    fileMenu.setBorder(null);
	    fileMenu.setMnemonic(KeyEvent.VK_F);
	    menuBar.add(fileMenu);
	    
	    JMenuItem serverSetting = new JMenuItem("Server Setting");
	    serverSetting.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				serverSettingMenuActionPerformed();
			}
		});
	    serverSetting.setMnemonic(KeyEvent.VK_S);
	    serverSetting.setBorder(null);
	    serverSetting.setBackground(deepBlue);
	    serverSetting.setForeground(white);
	    fileMenu.add(serverSetting);
	
	    JMenuItem exitMenu = new JMenuItem("Exit");
	    exitMenu.setMnemonic(KeyEvent.VK_X);
	    exitMenu.setBorder(null);
	    exitMenu.setBackground(deepBlue);
	    exitMenu.setForeground(white);
	    exitMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	    fileMenu.add(exitMenu);
	
	    JMenu helpMenu = new JMenu("Help");
	    helpMenu.setBorder(null);
	    helpMenu.setForeground(white);
	    helpMenu.setMnemonic(KeyEvent.VK_H);
	    menuBar.add(helpMenu);
	    
	    JMenuItem about = new JMenuItem("About");
	    about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				aboutMenuItemSelected();
			}
		});
	    about.setMnemonic(KeyEvent.VK_A);
	    about.setBorder(null);
	    about.setBackground(deepBlue);
	    about.setForeground(white);
	    helpMenu.add(about);
		
	}

	/**
	 * 
	 */
	public void setAppLookAndFeel(String type) {
		try {
			if (type.equals("SYSTEM"))
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			else if (type.equals("DEFAULT"))
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(getParent(), "ClassNotFoundException");
			e.printStackTrace();
		} catch (InstantiationException e) {
			JOptionPane.showMessageDialog(getParent(), "InstantiationException");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			JOptionPane.showMessageDialog(getParent(), "IllegalAccessException");
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			JOptionPane.showMessageDialog(getParent(), "UnsupportedLookAndFeelException");
			e.printStackTrace();
		}
	}
	
	
	
	protected void serverSettingMenuActionPerformed() {
		
		JPanel serverSettingPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        JTextField serverTextField = new JTextField(loginPanel.getServerAddress(), 10);
        JTextField serverPortTextField = new JTextField(Integer.toString(loginPanel.getServerPort()));
        
        // server name
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(1,1,1,5);
        serverSettingPanel.add(new JLabel("Server Address:", SwingConstants.LEFT), c);
        c.gridx++;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.LINE_END;
        serverSettingPanel.add(serverTextField, c);
        
        // server port
        c.gridx--;
        c.gridy++;
        c.fill = GridBagConstraints.NONE;
        serverSettingPanel.add(new JLabel("Server Port:", SwingConstants.RIGHT), c);
        c.gridx++;
        c.fill = GridBagConstraints.BOTH;
        serverSettingPanel.add(serverPortTextField, c);
        
        // add focus selection listener
        FocusAdapter focusListener = new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (e.getComponent() instanceof JTextField) {
                    JTextField entry = (JTextField) e.getComponent();
                    entry.selectAll();
                }
            }
        };
        
        serverTextField.addFocusListener(focusListener);
        serverPortTextField.addFocusListener(focusListener);
        
        int returnVal = JOptionPane.showConfirmDialog(this, serverSettingPanel, 
                "Specify server settings", JOptionPane.OK_CANCEL_OPTION);
        if (returnVal != JOptionPane.OK_OPTION) {
            return;
        }
        String serverAddress = serverTextField.getText().trim();
        loginPanel.setServerAddress(serverAddress);
        try {
            int serverPort = Integer.parseInt(serverPortTextField.getText().trim());
            loginPanel.setServerPort(serverPort);
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Invalid Port");
            return;
        }
        
		
	}
	
	
	protected void aboutMenuItemSelected() {
		JOptionPane.showMessageDialog(getParent()
				,	"Birdchat is a chat and file transfer application,\n" +
					" developed as the yearly project by Kawsar Ahmed " +
					"and Md. Abu Raihan\n under the supervision of " +
					"Md. Sujan Ali,\n assistant professor, " +
					"Jatiya Kabi Kazi Nazrul Islam University.", "About",JOptionPane.INFORMATION_MESSAGE);
	
	}


}