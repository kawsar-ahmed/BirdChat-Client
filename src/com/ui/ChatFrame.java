package com.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.socket.Message;
import com.socket.SocketClient;
import com.socket.SocketData;
import com.ui.login.LoginUI;

public class ChatFrame extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public SocketClient client;
    public int port;
    public String serverAddr, username, password;
    public Thread clientThread;
	@SuppressWarnings("rawtypes")
	public DefaultListModel model;
    public File file;
	private JPanel panel;
	private int uiWidth;
	private int uiHeight;
	private JLabel allPeopleContactLabel;
	private Rectangle sendMessageButtonBounds ;
	private Rectangle messageBoxBounds;
	private Rectangle sendFileButtonBounds;
	private Rectangle sendFileLabelBounds;
	private Rectangle fileChooserBounds;
	private Rectangle fileNameBoxBounds;
	private JLabel userLablel;
	private JPanel chatPanel;
	private JLabel profileLabel;
	public ImageIcon defaultIcon130;
	public ImageIcon defaultIcon32;
	private Color seperatorColor;
    
    @SuppressWarnings("unchecked")
	public ChatFrame(SocketData socketData,  String user)  {
    	try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } 
        catch(Exception ex){
            System.out.println("Look & Feel exception");
        }
    	username = user;
    	init();
        buildComponents();
//		addComponents();
//        addWindowListener();
//        model.addElement(allPeopleContactLabel);
        contactList.setSelectedIndex(0);

        
        client = new SocketClient(this, socketData);
        clientThread = new Thread(client);
        clientThread.start();
        
        
        this.addWindowListener(new WindowListener() {

            @Override public void windowOpened(WindowEvent e) {}
            @Override public void windowClosing(WindowEvent e) {
            
            	client.requestLogOut();   
            }
            @Override public void windowClosed(WindowEvent e) {}					//r/		type		sender		content				recipient
            @Override public void windowIconified(WindowEvent e) {}
            @Override public void windowDeiconified(WindowEvent e) {}
            @Override public void windowActivated(WindowEvent e) {}
            @Override public void windowDeactivated(WindowEvent e) {}
        });
        setVisible(true);
        
    }
    
    private void init() {
    	setTitle("Birdchat : "+username);
		
		// the path must be relative to your *class* files
		String imagePath = "login/images/Birdchat-icon.png";
		InputStream imgStream = getClass().getResourceAsStream(imagePath );
		BufferedImage myImg = null;
		try {
			myImg = ImageIO.read(imgStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setIconImage(myImg);
		
		uiWidth = 850;
		uiHeight = 550;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds( 0, 0, uiWidth+5, uiHeight+28);
		setLocationRelativeTo(null);
		setLayout(null);
		setResizable(false);
		
		seperatorColor = blue;
		profileAreaBounds = new Rectangle(260, 10, uiWidth - 270, 110);
		contactListBounds = new Rectangle(10, 145, 240, uiHeight - 155);
		textPaneBounds = new Rectangle(300, 160, uiWidth -300 -50, uiHeight - 160 -120);
		messageBoxBounds = new Rectangle(300, uiHeight - 110, 420, 50);
		sendMessageButtonBounds = new Rectangle(uiWidth - 120, uiHeight - 110, 70, 30);
		fileNameBoxBounds = new Rectangle(300, uiHeight - 50, 350, 30);
		sendFileLabelBounds = new Rectangle(320, uiHeight - 50, 120, 30);
		fileChooserBounds = new Rectangle(uiWidth - 200, uiHeight - 50, 50, 30);
		sendFileButtonBounds = new Rectangle(uiWidth - 140, uiHeight - 50, 90, 30);
	}

	public boolean isWin32(){
        return System.getProperty("os.name").startsWith("Windows");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void buildComponents() {
        
    	panel = new JPanel();
        panel.setBackground(lightBlue);
        panel.setBounds(0, 0, uiWidth, uiHeight);
        panel.setLayout(null);
        
        addSeperators();
        
        

        String filePath = "login/images/logo.png";
		Icon imageIcon = new ImageIcon(getClass().getResource(filePath));
		birdchatLogoLabel = new JLabel(imageIcon);
		birdchatLogoLabel.setBounds(0,0,260,56);
		panel.add(birdchatLogoLabel);


		
		largeTextArea = new JTextArea();
		largeTextArea.setColumns(20);
		largeTextArea.setRows(5);
		largeTextArea.setEditable(false);
        largeTextArea.setBackground(white);
        largeTextArea.setBorder(BorderFactory.createLineBorder(lightBlue));
        largeTextArea.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        
        chatPanel = new JPanel();
        
        largeTextScrollPane = new JScrollPane();
        largeTextScrollPane.setBounds(textPaneBounds);
        largeTextScrollPane.setBorder(null);
        largeTextScrollPane.setViewportView(largeTextArea);
        panel.add(largeTextScrollPane);
        
        contactList = new JList();
        contactList.setModel((model = new DefaultListModel()));
        contactList.setBackground(white);
        contactList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				JLabel target = (JLabel)contactList.getSelectedValue();
				profileLabel.setText(target.getText());
				messageInputTextField.requestFocus();
			}
		});
        contactListScrollPane = new JScrollPane();
        contactListScrollPane.setBounds(contactListBounds);
        contactListScrollPane.setBorder(null);
        contactListScrollPane.setViewportView(contactList);

        contactList.setCellRenderer(new ContactCellRenderer());
        contactList.setSelectionForeground(white);
        contactList.setSelectionBackground(deepBlue);
//       contactList.setBorder(BorderFactory.createLineBorder(lightBlue));
        /*for(int i = 0; i<30; i++){
        	model.addElement(new JLabel("All of theindaf the afjdfh dfaj jkasf alg-"+i, imageIcon, SwingConstants.LEFT));
        }*/
        allPeopleContactLabel = new JLabel("All", imageIcon, SwingConstants.LEFT);
        
        filePath = "login/images/person-48.png";
        imageIcon = new ImageIcon(getClass().getResource(filePath));
        userLablel = new JLabel(username, imageIcon, SwingConstants.LEFT);
        userLablel.setFont(new Font("Helvetica Rounded Bold", Font.BOLD, 15));
        userLablel.setBounds(10, 76, 200, 48);
        panel.add(userLablel);

        filePath = "login/images/logout.png";
        imageIcon = new ImageIcon(getClass().getResource(filePath));
        JButton logOutButton = new JButton(imageIcon);
        logOutButton.setBounds(210, 80, 40,40);
        logOutButton.setBackground(null);
        logOutButton.setBorder(null);
        logOutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logOutButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				client.requestLogOut();
				
			}
		});
        panel.add(logOutButton);
      
        filePath = "login/images/default-contact-icon-130.png";
        defaultIcon130 = new ImageIcon(getClass().getResource(filePath));
        profileLabel = new JLabel("All", defaultIcon130, SwingConstants.LEFT);
        profileLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        profileLabel.setBounds(270, 10, uiWidth, 130);
        panel.add(profileLabel);
        

        filePath = "login/images/person-32.png";
        defaultIcon32 = new ImageIcon(getClass().getResource(filePath));
        
        filePath = "login/images/all-people.png";
        imageIcon = new ImageIcon(getClass().getResource(filePath));
        model.addElement(new JLabel("All", imageIcon, SwingConstants.LEFT));
        
       

//        addDummies();
//        messageLabel = new JLabel("Message : ");

        sendMessageButton = new JButton("Send");
//        sendMessageButton.setOpaque(false);
//        sendMessageButton.setForeground(deepBlue);
//        sendMessageButton.setBackground(white);
//        sendMessageButton.setBorder(BorderFactory.createLineBorder(deepBlue));
        sendMessageButton.setBounds(sendMessageButtonBounds );
        sendMessageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendMessageButtonActionPerformed(evt);
            }
        });
        getRootPane().setDefaultButton(sendMessageButton);
        
        messageInputTextField = new JTextField();
        messageInputTextField.setBounds(messageBoxBounds);
        messageInputTextField.setForeground(deepBlue);
        messageInputTextField.setBackground(white);
//        messageInputTextField.setMargin(new Insets(0, 10, 0, 10));
        messageInputTextField.requestFocus();
        messageInputTextField.setBorder(BorderFactory.createLineBorder(lightBlue));
        
        sendFileButton = new JButton("Send File");
        sendFileButton.setBounds(sendFileButtonBounds);
//        sendFileButton.setForeground(deepBlue);
//        sendFileButton.setBackground(white);
//        sendFileButton.setBorder(BorderFactory.createLineBorder(deepBlue));
        sendFileButton.setEnabled(false);
        sendFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendFileButtonActionPerformed(evt);
            }
        });

        sendFileLabel = new JLabel("File:");
        sendFileLabel.setBounds(sendFileLabelBounds);
        sendFileLabel.setOpaque(false);
        sendFileLabel.setForeground(deepBlue);
        sendFileLabel.setBackground(null);
        sendFileLabel.setBorder(null);


        sendFileChooserButton = new JButton("...");
        sendFileChooserButton.setBounds(fileChooserBounds);
//        sendFileChooserButton.setForeground(deepBlue);
//        sendFileChooserButton.setBackground(white);
//        sendFileChooserButton.setBorder(BorderFactory.createLineBorder(deepBlue));
        sendFileChooserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendFileChooserButtonActionPerformed(evt);
            }
        });
        fileSendInputTextField = new JTextField();
        fileSendInputTextField.setBounds(fileNameBoxBounds);
        fileSendInputTextField.setForeground(deepBlue);
        fileSendInputTextField.setBackground(white);
        fileSendInputTextField.setBorder(BorderFactory.createLineBorder(lightBlue));

        panel.add(sendMessageButton);
        panel.add(sendFileButton);
        panel.add(sendFileChooserButton);
//        panel.add(messageLabel);
        
        panel.add(contactListScrollPane);
        panel.add(messageInputTextField);
        panel.add(fileSendInputTextField);
//        panel.add(sendFileLabel);
        add(panel);
        
    }

	/**
	 * 
	 */
	private void addSeperators() {
		JSeparator verSep = new JSeparator(SwingConstants.VERTICAL);
        verSep.setForeground(seperatorColor);
        verSep.setBounds(260, 0, 1, uiHeight);
        panel.add(verSep);
        
        JSeparator horSep = new JSeparator(SwingConstants.HORIZONTAL);
        horSep.setForeground(seperatorColor);
        horSep.setBounds(260, 150, uiWidth, 1);
        panel.add(horSep);
        
        JSeparator userBorderUp = new JSeparator(SwingConstants.HORIZONTAL);
        userBorderUp.setForeground(seperatorColor);
        userBorderUp.setBounds(10, 66, 240, 1);
        panel.add(userBorderUp);
        
        JSeparator userBorderDown = new JSeparator(SwingConstants.HORIZONTAL);
        userBorderDown.setForeground(seperatorColor);
        userBorderDown.setBounds(10, 134, 240, 1);
        panel.add(userBorderDown);
        
        /*JSeparator contactListBorderUp = new JSeparator(SwingConstants.HORIZONTAL);
        contactListBorderUp.setForeground(seperatorColor);
        contactListBorderUp.setBounds(9, 144, 242, 1);
        panel.add(contactListBorderUp);

        JSeparator contactListBorderDown = new JSeparator(SwingConstants.HORIZONTAL);
        contactListBorderDown.setForeground(seperatorColor);
        contactListBorderDown.setBounds(9, uiHeight-9, 242, 1);
        panel.add(contactListBorderDown);

        JSeparator contactListBorderLeft = new JSeparator(SwingConstants.VERTICAL);
        contactListBorderLeft.setForeground(seperatorColor);
        contactListBorderLeft.setBounds(9, 144, 1, uiHeight-153);
        panel.add(contactListBorderLeft);
        
        JSeparator contactListBorderRight = new JSeparator(SwingConstants.VERTICAL);
        contactListBorderRight.setForeground(seperatorColor);
        contactListBorderRight.setBounds(251, 144, 1, uiHeight-153);
        panel.add(contactListBorderRight);*/
	}

	/**
	 * 
	 */
	private void addDummies() {
		 Icon imageIcon = null;
		chatPanel.add(new JLabel("All", imageIcon, SwingConstants.LEFT));
	        chatPanel.add(new JLabel("All", imageIcon, SwingConstants.LEFT));
	        chatPanel.add(new JLabel("All", imageIcon, SwingConstants.LEFT));
	        chatPanel.add(new JLabel("All", imageIcon, SwingConstants.LEFT));
	        chatPanel.add(new JLabel("All", imageIcon, SwingConstants.LEFT));
		String filePath;
		Icon imageIcon1;
		filePath = "login/images/raihan-32.png";
        imageIcon1 = new ImageIcon(getClass().getResource(filePath));
        model.addElement(new JLabel("Abu Raihan", imageIcon1, SwingConstants.LEFT));
        
        filePath = "login/images/murad-32.png";
        imageIcon1 = new ImageIcon(getClass().getResource(filePath));
        model.addElement(new JLabel("S.M. Hasan Tanvir", imageIcon1, SwingConstants.LEFT));
        
        filePath = "login/images/maklina-32.png";
        imageIcon1 = new ImageIcon(getClass().getResource(filePath));
        model.addElement(new JLabel("Maklina Khatun", imageIcon1, SwingConstants.LEFT));
        
        filePath = "login/images/quasha-32.png";
        imageIcon1 = new ImageIcon(getClass().getResource(filePath));
        model.addElement(new JLabel("Sadeka Ferdousi", imageIcon1, SwingConstants.LEFT));
        
        filePath = "login/images/person-32.png";
        imageIcon1 = new ImageIcon(getClass().getResource(filePath));
        model.addElement(new JLabel("Shariful Islam", imageIcon1, SwingConstants.LEFT));
        
        filePath = "login/images/person-32.png";
        imageIcon1 = new ImageIcon(getClass().getResource(filePath));
        model.addElement(new JLabel("Abur Rahim", imageIcon1, SwingConstants.LEFT));
        
        filePath = "login/images/person-32.png";
        imageIcon1 = new ImageIcon(getClass().getResource(filePath));
        model.addElement(new JLabel("Lal Mia", imageIcon1, SwingConstants.LEFT));
        
        filePath = "login/images/person-32.png";
        imageIcon1 = new ImageIcon(getClass().getResource(filePath));
        model.addElement(new JLabel("Tuhin Ahmed", imageIcon1, SwingConstants.LEFT));

        filePath = "login/images/person-32.png";
        imageIcon1 = new ImageIcon(getClass().getResource(filePath));
        model.addElement(new JLabel("Saidul", imageIcon1, SwingConstants.LEFT));

        filePath = "login/images/person-32.png";
        imageIcon1 = new ImageIcon(getClass().getResource(filePath));
        model.addElement(new JLabel("Alamin", imageIcon1, SwingConstants.LEFT));

        filePath = "login/images/person-32.png";
        imageIcon1 = new ImageIcon(getClass().getResource(filePath));
        model.addElement(new JLabel("Maruf Raihan", imageIcon1, SwingConstants.LEFT));
	}

       private void sendMessageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendMessageButtonActionPerformed
        String msg = messageInputTextField.getText();
        JLabel targetUser = (JLabel)contactList.getSelectedValue();
        String target = targetUser.getText();
        
        if(!msg.isEmpty() && !target.isEmpty()){
            messageInputTextField.setText("");
            client.send(new Message("message", username, msg, target));
        }					//r/		type		sender		content		recipient
        
        messageInputTextField.requestFocus();
    }

   
    private void sendFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendFileButtonActionPerformed
            long size = file.length();
            if(size < 120 * 1024 * 1024){
            	JLabel targetUser = (JLabel)contactList.getSelectedValue();
                client.send(new Message("upload_req", username, file.getName(),targetUser.getText() ));
            }						//r/		type		sender		content		recipient
            else{
                largeTextArea.append("[Application > Me] : File is size too large\n");
            }
    }


    private void sendFileChooserButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_historyFileChooserButtonActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.showDialog(this, "Select File");
        file = fileChooser.getSelectedFile();
        
        if(file != null){
            if(!file.getName().isEmpty()){
                sendFileButton.setEnabled(true); String str;
                
                if(fileSendInputTextField.getText().length() > 30){
                    String t = file.getPath();
                    str = t.substring(0, 20) + " [...] " + t.substring(t.length() - 20, t.length());
                }
                else{
                    str = file.getPath();
                }
                fileSendInputTextField.setText(str);
            }
        }
    }//GEN-LAST:event_historyFileChooserButtonActionPerformed


    public static void main(String args[]) {
        
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ChatFrame(null, null).setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    
    public JButton sendMessageButton;
    public JButton sendFileButton;
    public JButton sendFileChooserButton;
    private JLabel messageLabel;
    private JLabel sendFileLabel;
    private JLabel birdchatLogoLabel;
    public JList contactList;
    public JScrollPane largeTextScrollPane;
    private JScrollPane contactListScrollPane;
    public JTextArea largeTextArea;
    public JTextField messageInputTextField;
    public JTextField fileSendInputTextField;

    private final Color gray = new Color(240, 240, 240);
    private final Color deepBlue = new Color(12, 125, 175);
	private final Color blue = new Color(0, 175, 240);
	private final Color lightBlue = new Color(204, 239, 252);
	private final Color white = new Color(255, 255, 255) ;
	private Rectangle textPaneBounds ;
	private Rectangle contactListBounds ;
	private Rectangle profileAreaBounds ;
    // End of variables declaration//GEN-END:variables
}
