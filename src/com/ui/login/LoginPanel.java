package com.ui.login;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


/**
 * @author Kawsar
 *
 */
public class LoginPanel extends JPanel implements MouseListener,FocusListener {
	//
	
	private static final long serialVersionUID = 1L;

	private final Font inputBoxFont = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
//	private final Font inputBoxFont = new Font("Consolas", Font.PLAIN, 16);
	private final Color deepBlue = new Color(12, 125, 175);
	private final Color blue = new Color(0, 175, 240);
	private final Color lightBlue = new Color(204, 239, 252);
	private final Color white = new Color(255, 255, 255) ;

	private final String defaultUserName = "Birdchat name";
	private final String defaultPassword = "````````";

	private JTextField userNameTextField ;
	private JPasswordField passwordTextField;
	private JButton signInButton;
	
	private JLabel signingInLabel;
	private JLabel errorLabel;
	
	public LoadingIconPanel loadingIconPanel;

	private String serverAddress = "192.168.200.45";
	private int serverPort = 13000;
	
	private JLabel backgroundImageLabel;
	private Icon backgroundImage;

	
	/**
	 * @param panelSize 
	 * 
	 */
	public LoginPanel(LoadingIconPanel loadingIconPanel){
		setLayout(null);
		
		String filePath = "images/Birdchat-login-screen.png";
		backgroundImage = new ImageIcon(getClass().getResource(filePath));
		setBounds(0, 20, backgroundImage.getIconWidth(), backgroundImage.getIconHeight());
		
		this.loadingIconPanel = loadingIconPanel;
		try {
			serverAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		addComponents();
	}
	
	
	/**
	 * Gets the server address as string e.g.: "192.168.10.2"
	 * @return the server address
	 */
	public String getServerAddress() {
		return serverAddress;
	}

	/**
	 * Sets the server Address
	 * @param server the server to set
	 */
	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	/**
	 * @return the serverPort
	 */
	public int getServerPort() {
		return serverPort;
	}

	/**
	 * @param serverPort the serverPort to set
	 */
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public void addComponents() {
		
        createUserNameTextField();
		add(userNameTextField);
		
		createPasswordTextField();
		add(passwordTextField);
		
		createSignInButton();
		add(signInButton);
		
		createSigningInLabel();
		createErrorLabel();
		
		createBackgroundImageLabel();
		add(backgroundImageLabel);
	}

	/**
	 * 
	 */
	private void createErrorLabel() {
		InputStream errorIconfilePath = getClass().getResourceAsStream("images/info-transparent.png");
		BufferedImage bi = null;
		try { bi = ImageIO.read(errorIconfilePath); } catch (IOException e1) { e1.printStackTrace(); }
		
		errorLabel = new JLabel(new ImageIcon( bi.getSubimage(0, 0, 16, 16)));
		errorLabel.setText("");
		errorLabel.setBounds( 80, 110, 600, 50);
		errorLabel.setBackground(null);
		errorLabel.setForeground(white);
		errorLabel.setBorder(null);
		errorLabel.setOpaque(false);
	}

	/**
	 * 
	 */
	private void createSigningInLabel() {
		signingInLabel = new JLabel("Signing in...");
		signingInLabel.setBounds(getWidth()/2-30, getHeight()*2/3, 100,20);
		signingInLabel.setBackground(null);
		signingInLabel.setForeground(white);
		signingInLabel.setBorder(null);
		signingInLabel.setOpaque(false);
	}

	/**
	 * 
	 */
	private void createUserNameTextField() {
		//r/ 204,154 514,188 310,34
		userNameTextField = new JTextField(defaultUserName);
		userNameTextField.setBounds(210, 170, 310, 35);
		userNameTextField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		userNameTextField.setFont(inputBoxFont);
		userNameTextField.setForeground(blue);
		userNameTextField.setBackground(lightBlue);
		userNameTextField.setSelectionColor(deepBlue);
		userNameTextField.setSelectedTextColor(white);
		userNameTextField.addMouseListener( this );
		userNameTextField.addFocusListener(this);
		userNameTextField.setFocusable(false);
	}


	/**
	 * 
	 */
	private void createPasswordTextField() {
		//r/ 204,231 514,365 310,34
		passwordTextField = new JPasswordField(defaultPassword);
		passwordTextField.setBounds(210, 240, 310, 35);
		passwordTextField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		passwordTextField.setFont(inputBoxFont);
		passwordTextField.setForeground(blue);
		passwordTextField.setBackground(lightBlue);
		passwordTextField.setSelectionColor(deepBlue);
		passwordTextField.setSelectedTextColor(white);
		passwordTextField.addMouseListener( this );
		passwordTextField.addFocusListener(this);
		passwordTextField.setFocusable(false);
	}


	/**
	 * 
	 */
	private void createSignInButton() {
		signInButton = new JButton("Sign in");
		signInButton.setForeground(white);
		signInButton.setBounds(210, 310, 310, 35);
		signInButton.setBorder(null);
		signInButton.setBackground(deepBlue);//r/12, 125, 175
		signInButton.setFocusable(false);
		signInButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		signInButton.addMouseListener( this);
		signInButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				signInButtonActionPerformed();
			}
		});
	}

	/**
	 * 
	 */
	private void createBackgroundImageLabel() {
		backgroundImageLabel = new JLabel(backgroundImage);
		backgroundImageLabel.setBounds(0, 0, backgroundImage.getIconWidth(), backgroundImage.getIconHeight());
		backgroundImageLabel.addMouseListener(this);
		backgroundImageLabel.setOpaque(false);
	}

	protected void exitLoginUI() {
		Window[] frame = Window.getWindows();
		for (int i = 0; i < frame.length; i++) {
			if(frame[i] instanceof LoginUI ) {
				//frame[i].dispatchEvent(new WindowEvent(frame[i], WindowEvent.WINDOW_CLOSING));
				frame[i].dispose();
//				break;
			}
		}
		
	}

	
	
	public JButton getSignInButton() {
		return signInButton;
	}
	
	
	public void signInButtonActionPerformed() {
		if (userNameTextField.getText().equals(defaultUserName) ||
				String.valueOf( passwordTextField.getPassword() ).equals(defaultPassword) ) {
			errorLabel.setText( "Any of the sign in details must not be empty" );
			add(errorLabel);
			add(backgroundImageLabel);
			updateUI();
			return;
		}
		removeLoginFieldsAndShowLoading();
		new Thread(	
				new Login(
							serverAddress, serverPort, this,  
							userNameTextField.getText(), 
							String.valueOf( passwordTextField.getPassword() )
						) 
				)
		.start();
				
	}

	/**
	 * @param errorMsg 
	 * 
	 */
	public void showLoginAndRemoveLoading(String errorMsg) {
		loadingIconPanel.setRuning(false);
		remove(signingInLabel);

		errorLabel.setText(errorMsg);
		add(errorLabel);
		add(userNameTextField);
		add(passwordTextField);
		add(signInButton);
		add(backgroundImageLabel);
		updateUI();
	}

	/**
	 * 
	 */
	public void removeLoginFieldsAndShowLoading() {
		remove(userNameTextField);
		remove(passwordTextField);
		remove(signInButton);
		remove(errorLabel);

		add(signingInLabel);
		add(backgroundImageLabel);
		updateUI();
		SwingUtilities.getRootPane(this).setDefaultButton(signInButton);
		loadingIconPanel.setRuning(true);
	}

	
	/**
	 * Implemented functions of FocusListener 
	 */
	/**
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		
		if(e.getComponent() == signInButton) {
			mouseExitedFromSignInButton();
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
	}
	
	@Override
	public void mouseExited(MouseEvent e) {

		if(e.getComponent() == signInButton) {
			mouseExitedFromSignInButton();
		}
	}


	/**
	 * 
	 */
	private void mouseExitedFromSignInButton() {
		signInButton.setBackground(deepBlue);
		signInButton.setForeground(white);
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		if (e.getComponent() == signInButton){
			signInButton.setBackground(white);
			signInButton.setForeground(deepBlue);
		}
		if(e.getComponent() == userNameTextField || e.getComponent() == passwordTextField ){
			e.getComponent().setFocusable(true);
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
//r/ 	mouse click on signInButton will be handled by ActionListener->actionPerformed
		Component selectedComponent = e.getComponent();
		if(selectedComponent == userNameTextField || selectedComponent == passwordTextField || selectedComponent == backgroundImageLabel ){
			e.getComponent().requestFocus();
			
		}
		else if(selectedComponent == signInButton) {
			mouseExitedFromSignInButton();
			
		}
			
	}

	/**
	 * Implemented functions of FocusListener 
	 */
	/**
	 */
	@Override
	public void focusGained(FocusEvent e) {
		if(e.getComponent() == userNameTextField ){
			userNameTextField.setBackground(white);
			if(userNameTextField.getText().equals(defaultUserName) )
				userNameTextField.setText("");
			userNameTextField.selectAll();
		}
		if(e.getComponent() == passwordTextField ){
			e.getComponent().setBackground(white);
			if(String.valueOf(passwordTextField.getPassword()).equals(defaultPassword) )
				passwordTextField.setText("");
			passwordTextField.selectAll();
		}

		if(signInButton.isFocusable() == false)
			signInButton.setFocusable(true);
		if(userNameTextField.isFocusable() == false)
			userNameTextField.setFocusable(true);
		if(passwordTextField.isFocusable() == false)
			passwordTextField.setFocusable(true);
	}

	@Override
	public void focusLost(FocusEvent e) {
		if(e.getComponent() == userNameTextField ){
			userNameTextField.setBackground(lightBlue);
			if(userNameTextField.getText().equals("" ) )
				userNameTextField.setText(defaultUserName);
		}
		if(e.getComponent() == passwordTextField ){
			e.getComponent().setBackground(lightBlue);
			if(String.valueOf(passwordTextField.getPassword()).equals(""))
				passwordTextField.setText(defaultPassword);
		}
	}

}


