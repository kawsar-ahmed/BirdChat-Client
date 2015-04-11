package com.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

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
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.socket.Download;
import com.socket.ImageProcessor;
import com.socket.Message;
import com.socket.SocketClient;
import com.socket.SocketData;

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
	private JPanel mainPanel;
	private int uiWidth;
	private int uiHeight;
	private JLabel allPeopleContactLabel;
	private Rectangle sendMessageButtonBounds ;
	public Dimension messageBoxSize;
	public Dimension nameBoxSize;
	public Dimension timeBoxSize;
	private Rectangle sendFileButtonBounds;
	private Rectangle sendFileLabelBounds;
	private Rectangle fileChooserBounds;
	private Rectangle fileNameBoxBounds;
	private JLabel userLablel;
	private JScrollPane notificationScrollPane;
	private JPanel chatPanelOfAll;
	private Rectangle notificationAreaBounds;
	private JPanel profilePanel;
	private JLabel profilePicLabel;
	private JLabel nameLabel;
	private JTextArea aboutTextArea;
	public ImageIcon defaultContactIcon32;
	public ImageIcon defaultContactIcon48;
	public BufferedImage defaultProfilePicture;
	public ImageIcon allPeopleIcon;
	public BufferedImage allPeopleProfilePicture;
	public Rectangle messageBoxBounds;
    
	/**
	 * @param socketData
	 * @param user
	 */
	public ChatFrame(SocketData socketData,  String user)  {
    	try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } 
        catch(Exception ex){
            System.out.println("Look & Feel exception");
        }
    	username = user;
    	init();
    	client = new SocketClient(this, socketData);
        
    	buildComponents();

        contactList.setSelectedIndex(0);
        
        clientThread = new Thread(client);
        clientThread.start();
		client.loadProfileData(username, "all");
		signOutAtWindowClose();
        setVisible(true);
    }

	/**
	 * 
	 */
	private void signOutAtWindowClose() {
		addWindowListener(new WindowListener() {

            @Override public void windowOpened(WindowEvent e) {}
            @Override public void windowClosing(WindowEvent e) { try{ client.send(new Message("message", username, ".bye", "SERVER")); client.setKeepRunning(false);  }catch(Exception ex){} }
            @Override public void windowClosed(WindowEvent e) {}					//r/		type		sender		content				recipient
            @Override public void windowIconified(WindowEvent e) {}
            @Override public void windowDeiconified(WindowEvent e) {}
            @Override public void windowActivated(WindowEvent e) {}
            @Override public void windowDeactivated(WindowEvent e) {}
        });
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
			e.printStackTrace();
		}
		setIconImage(myImg);
		
		uiWidth = 850;
		uiHeight = 550;
		normalContactBackground = white;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds( 0, 0, uiWidth+5, uiHeight+28);
		setLocationRelativeTo(null);
		setLayout(null);
		setResizable(false);
		setComponentBounds();
	}

	/**
	 * 
	 */
	private void setComponentBounds() {
		profileAreaBounds = new Rectangle(270, 10, 320, 130);
		notificationAreaBounds = new Rectangle(uiWidth - 250, 0, 250, 151);
		contactListBounds = new Rectangle(10, 145, 240, uiHeight - 155);
		chatPaneBounds = new Rectangle(270, 160, uiWidth -270 -10, uiHeight - 160 -100);
		messageBoxBounds = new Rectangle(340, uiHeight - 90, 380, 40);
		sendMessageButtonBounds = new Rectangle(uiWidth - 130, uiHeight - 90, 70, 40);
		fileNameBoxBounds = new Rectangle(270, uiHeight - 40, 420, 30);
		sendFileLabelBounds = new Rectangle(320, uiHeight - 50, 120, 30);
		fileChooserBounds = new Rectangle(uiWidth - 160, uiHeight - 40, 50, 30);
		sendFileButtonBounds = new Rectangle(uiWidth - 100, uiHeight - 40, 90, 30);
		messageBoxSize = new Dimension(320, 20);
		nameBoxSize = new Dimension(150, 20);
		timeBoxSize = new Dimension(90, 20);
	}

	

	public boolean isWin32(){
        return System.getProperty("os.name").startsWith("Windows");
    }

    private void buildComponents() {
        
    	mainPanel = new JPanel();
        mainPanel.setBackground(white);
        mainPanel.setBounds(0, 0, uiWidth, uiHeight);
        mainPanel.setLayout(null);
        
        loadDefaultIcons();
        
        buildSeperators();
		prepareNotificationArea();
        initOfAll();
        buildChatScrollPane();
        buildContactList();
        buildSendMessageButton();
        buildMessageBox();
        buildFileSendComponents();
        
        addBirdchatLogo();
//        addDummies();
        addProfilePanel();
        addMyLabel();

        mainPanel.add(notificationScrollPane);
        mainPanel.add(chatScrollPane);
        mainPanel.add(sendMessageButton);
        mainPanel.add(sendFileButton);
        mainPanel.add(sendFileChooserButton);
//        panel.add(messageLabel);
        
        mainPanel.add(contactListScrollPane);
        mainPanel.add(messageInputTextField);
        mainPanel.add(fileSendInputTextField);
//        panel.add(sendFileLabel);
        add(mainPanel);
        
    }

	/**
	 * 
	 */
	private void loadDefaultIcons() {
		String filePath = "login/images/default-contact-icon-32.png";
		InputStream fileInputStream = getClass().getResourceAsStream(filePath);
		BufferedImage image = null;
		try {
			image = ImageIO.read(fileInputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		defaultContactIcon32 = new ImageIcon(image);
		
		filePath = "login/images/default-contact-icon-48.png";
		fileInputStream = getClass().getResourceAsStream(filePath);
		try {
			image = ImageIO.read(fileInputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		defaultContactIcon48 = new ImageIcon(image);
		
		filePath = "login/images/default-contact-icon-130.png";
		fileInputStream = getClass().getResourceAsStream(filePath);
		try {
			defaultProfilePicture = ImageIO.read(fileInputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	private void buildFileSendComponents() {
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
	}

	/**
	 * 
	 */
	private void buildMessageBox() {
		messageInputTextField = new JTextField();
        messageInputTextField.setBounds(messageBoxBounds);
        messageInputTextField.setForeground(deepBlue);
        messageInputTextField.setBackground(white);
//        messageInputTextField.setMargin(new Insets(0, 10, 0, 10));
        messageInputTextField.requestFocus();
        messageInputTextField.setBorder(BorderFactory.createLineBorder(lightBlue));
	}

	/**
	 * 
	 */
	private void buildSendMessageButton() {
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
	}

	/**
	 * 
	 */
	private void buildContactList() {
		contactList = new JList();
        contactList.setModel((model = new DefaultListModel()));
        contactList.addListSelectionListener(new ListSelectionListener() {
			

			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				JLabel targetUserLabel = (JLabel)contactList.getSelectedValue();
				targetUserLabel.setBackground(normalContactBackground);
				String user = targetUserLabel.getText();
				BufferedImage image = client.profileData.getProfilePicture(user);
				profilePicLabel.setIcon(new ImageIcon(image));
				
				nameLabel.setText(client.profileData.getName(user));
				aboutTextArea.setText(client.profileData.getAbout(user));
				
				chatScrollPane.setViewportView(client.chatData.getContactPanel(user));
			}
		});
        contactList.setBackground(white);
        contactList.setCellRenderer(new ContactCellRenderer());
        contactList.setSelectionForeground(white);
        contactList.setSelectionBackground(deepBlue);
        contactListScrollPane = new JScrollPane();
        contactListScrollPane.setBounds(contactListBounds);
        contactListScrollPane.setBorder(null);
        contactListScrollPane.setViewportView(contactList);
        model.addElement(allPeopleContactLabel);
	}

	/**
	 * 
	 */
	private void buildChatScrollPane() {
		chatPanelOfAll = new JPanel();
		chatScrollPane = new JScrollPane();
		chatScrollPane.setBounds(chatPaneBounds);
        chatPanelOfAll.setBackground(blue);
        chatScrollPane.setViewportView(chatPanelOfAll);
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void addDummies() {
		

//      contactList.setBorder(BorderFactory.createLineBorder(lightBlue));
       /*for(int i = 0; i<30; i++){
       	model.addElement(new JLabel("All of theindaf the afjdfh dfaj jkasf alg-"+i, imageIcon, SwingConstants.LEFT));
       }*/

      
	
        

        String filePath;
        Icon imageIcon;
        filePath = "login/images/all-people.png";
        imageIcon = new ImageIcon(getClass().getResource(filePath));
        model.addElement(new JLabel("All", imageIcon, SwingConstants.LEFT));


        filePath = "login/images/raihan-32.png";
        imageIcon = new ImageIcon(getClass().getResource(filePath));
        model.addElement(new JLabel("Abu Raihan", imageIcon, SwingConstants.LEFT));
        
        filePath = "login/images/murad-32.png";
        imageIcon = new ImageIcon(getClass().getResource(filePath));
        model.addElement(new JLabel("S.M. Hasan Tanvir", imageIcon, SwingConstants.LEFT));
        
        filePath = "login/images/maklina-32.png";
        imageIcon = new ImageIcon(getClass().getResource(filePath));
        model.addElement(new JLabel("Maklina Khatun", imageIcon, SwingConstants.LEFT));
        
        filePath = "login/images/quasha-32.png";
        imageIcon = new ImageIcon(getClass().getResource(filePath));
        model.addElement(new JLabel("Sadeka Ferdousi", imageIcon, SwingConstants.LEFT));
        
        filePath = "login/images/person-32.png";
        imageIcon = new ImageIcon(getClass().getResource(filePath));
        model.addElement(new JLabel("Shariful Islam", imageIcon, SwingConstants.LEFT));
        
        filePath = "login/images/person-32.png";
        imageIcon = new ImageIcon(getClass().getResource(filePath));
        model.addElement(new JLabel("Abur Rahim", imageIcon, SwingConstants.LEFT));
        
        filePath = "login/images/person-32.png";
        imageIcon = new ImageIcon(getClass().getResource(filePath));
        model.addElement(new JLabel("Lal Mia", imageIcon, SwingConstants.LEFT));
        
        filePath = "login/images/person-32.png";
        imageIcon = new ImageIcon(getClass().getResource(filePath));
        model.addElement(new JLabel("Tuhin Ahmed", imageIcon, SwingConstants.LEFT));

        filePath = "login/images/person-32.png";
        imageIcon = new ImageIcon(getClass().getResource(filePath));
        model.addElement(new JLabel("Saidul", imageIcon, SwingConstants.LEFT));

        filePath = "login/images/person-32.png";
        imageIcon = new ImageIcon(getClass().getResource(filePath));
        model.addElement(new JLabel("Alamin", imageIcon, SwingConstants.LEFT));

        filePath = "login/images/person-32.png";
        imageIcon = new ImageIcon(getClass().getResource(filePath));
        model.addElement(new JLabel("Maruf Raihan", imageIcon, SwingConstants.LEFT));
	}

	/**
	 * 
	 */
	private void addProfilePanel() {
        String filePath = "login/images/all-people-130.png";
        ImageIcon imageIcon = new ImageIcon(getClass().getResource(filePath));
        profilePicLabel = new JLabel(imageIcon, SwingConstants.LEFT);
        profilePicLabel.setBackground(null);
        profilePicLabel.setBounds(0, 0, 130, 130);
        
        nameLabel = new JLabel("All");
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        nameLabel.setBounds(140, 0, 190, 30);
        
        aboutTextArea = new JTextArea();
        aboutTextArea.setBounds(140, 35, 190, 125);
        aboutTextArea.setLineWrap(true);
        aboutTextArea.setWrapStyleWord(true);
        
        
        profilePanel = new JPanel();
        profilePanel.setLayout(null);
        profilePanel.setBackground(null);
        profilePanel.setBounds(profileAreaBounds);
        profilePanel.add(profilePicLabel);
        profilePanel.add(nameLabel);
        profilePanel.add(aboutTextArea);
        
        mainPanel.add(profilePanel);
	}

	/**
	 * 
	 */
	private void addMyLabel() {
        userLablel = new JLabel(username,defaultContactIcon48, SwingConstants.LEFT);
        userLablel.setFont(new Font("Helvetica Rounded Bold", Font.BOLD, 15));
        userLablel.setBackground(Color.LIGHT_GRAY);
        userLablel.setForeground(deepBlue);
        userLablel.setBounds(10, 76, 240, 48);
        userLablel.addMouseListener(new MouseListener() {
			
			@Override public void mouseReleased(MouseEvent e) {}
			@Override public void mousePressed(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {
				userLablel.setBackground(Color.LIGHT_GRAY);
			}
			@Override public void mouseEntered(MouseEvent e) {
				userLablel.setBackground(lightBlue);
			}
			@Override public void mouseClicked(MouseEvent e) {}
		});
        mainPanel.add(userLablel);
        client.profileData.setContactLabel(username, userLablel);
        client.profileData.setProfilePicture(username, defaultProfilePicture);
	}

	/**
	 * 
	 */
	private void addBirdchatLogo() {
		String filePath = "login/images/logo.png";
		Icon imageIcon = new ImageIcon(getClass().getResource(filePath));
		birdchatLogoLabel = new JLabel(imageIcon);
		birdchatLogoLabel.setBounds(0,0,260,56);
		mainPanel.add(birdchatLogoLabel);
	}

	/**
	 * @param imageIcon
	 */
	private void initOfAll() {
		String filePath = "login/images/all-people-32.png";
		InputStream fileInputStream = getClass().getResourceAsStream(filePath);
		BufferedImage image = null;
		try {
			image = ImageIO.read(fileInputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		allPeopleIcon = new ImageIcon(image);
		
		filePath = "login/images/all-people-130.png";
		fileInputStream = getClass().getResourceAsStream(filePath);
		try {
			allPeopleProfilePicture = ImageIO.read(fileInputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		allPeopleContactLabel = new JLabel("ALL", allPeopleIcon, SwingConstants.LEFT);
		chatPanelOfAll = new JPanel(new GridBagLayout());
        
        client.chatData.setGridBagConstraint("ALL", new GridBagConstraints());
        client.chatData.setContactPanel("ALL", chatPanelOfAll);
        client.chatData.setLastSender("ALL", "none");
        client.profileData.setAbout("ALL", "The all contact mode is selected." +
        		"You can send message to all contacts as brodcasting. Enjoy!!!");
        client.profileData.setProfilePicture("ALL", allPeopleProfilePicture);
        client.profileData.setContactLabel("ALL", allPeopleContactLabel);
        client.profileData.setName("ALL", "All Contacts");

	}

	/**
	 * 
	 */
	private void prepareNotificationArea() {
		notificationArea = new JTextArea();
		notificationArea.setColumns(20);
		notificationArea.setRows(5);
		notificationArea.setEditable(false);
        notificationArea.setBackground(white);
        notificationArea.setBorder(BorderFactory.createLineBorder(lightBlue));
        notificationArea.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        notificationScrollPane = new JScrollPane();
        notificationScrollPane.setBounds(notificationAreaBounds);
        notificationScrollPane.setBorder(null);
        notificationScrollPane.setViewportView(notificationArea);
	}

	/**
	 * 
	 */
	private void buildSeperators() {
		JSeparator verSep = new JSeparator(SwingConstants.VERTICAL);
        verSep.setForeground(lightBlue);
        verSep.setBounds(260, 0, 1, uiHeight);
        mainPanel.add(verSep);
        
        JSeparator horSep = new JSeparator(SwingConstants.HORIZONTAL);
        horSep.setForeground(lightBlue);
        horSep.setBounds(260, 150, uiWidth, 1);
        mainPanel.add(horSep);
        
        JSeparator userBorderUp = new JSeparator(SwingConstants.HORIZONTAL);
        userBorderUp.setForeground(lightBlue);
        userBorderUp.setBounds(10, 66, 240, 1);
        mainPanel.add(userBorderUp);
        
        JSeparator userBorderDown = new JSeparator(SwingConstants.HORIZONTAL);
        userBorderDown.setForeground(lightBlue);
        userBorderDown.setBounds(10, 134, 240, 1);
        mainPanel.add(userBorderDown);
        
        JSeparator contactListBorderUp = new JSeparator(SwingConstants.HORIZONTAL);
        contactListBorderUp.setForeground(lightBlue);
        contactListBorderUp.setBounds(9, 144, 242, 1);
        mainPanel.add(contactListBorderUp);

        JSeparator contactListBorderDown = new JSeparator(SwingConstants.HORIZONTAL);
        contactListBorderDown.setForeground(lightBlue);
        contactListBorderDown.setBounds(9, uiHeight-9, 242, 1);
        mainPanel.add(contactListBorderDown);

        JSeparator contactListBorderLeft = new JSeparator(SwingConstants.VERTICAL);
        contactListBorderLeft.setForeground(lightBlue);
        contactListBorderLeft.setBounds(9, 144, 1, uiHeight-153);
        mainPanel.add(contactListBorderLeft);
        
        JSeparator contactListBorderRight = new JSeparator(SwingConstants.VERTICAL);
        contactListBorderRight.setForeground(lightBlue);
        contactListBorderRight.setBounds(251, 144, 1, uiHeight-153);
        mainPanel.add(contactListBorderRight);
	}

       private void sendMessageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendMessageButtonActionPerformed
        String msg = messageInputTextField.getText();
        JLabel targetUser = (JLabel)contactList.getSelectedValue();
        String target = targetUser.getText();
        
        if(!msg.isEmpty() && !target.isEmpty()){
            messageInputTextField.setText("");
            client.send(new Message("message", username, msg, target));
        }					//r/		type		sender		content		recipient
        
		JPanel chatPanel = client.chatData.getContactPanel(target);
		GridBagConstraints gridContraints = client.chatData.getGridBagConstraint(target);
		
		gridContraints.gridx = 0;
		if( client.chatData.getLastSender(target) != username) {
			client.chatData.setLastSender(target, username);
			JTextArea nameTextArea = new JTextArea(client.profileData.getName(username));
			nameTextArea.setSize(nameBoxSize);
			nameTextArea.setBorder(null);
			nameTextArea.setBackground(white);
			chatPanel.add(nameTextArea,gridContraints);
		}

		JTextArea messageTxtArea = new JTextArea();
		messageTxtArea.setLineWrap(true);
		messageTxtArea.setWrapStyleWord(true);
		messageTxtArea.setSize(messageBoxSize);
		messageTxtArea.setText(msg);
		messageTxtArea.setSize(messageTxtArea.getPreferredSize());
		messageTxtArea.setBorder(null);
		messageTxtArea.setBackground(white);
		gridContraints.gridx++;
		chatPanel.add(messageTxtArea,gridContraints);

		JTextArea timeTxtArea = new JTextArea(client.getTime());
		timeTxtArea.setSize(timeBoxSize);
		timeTxtArea.setBorder(null);
		timeTxtArea.setBackground(white);
		gridContraints.gridx++;
		chatPanel.add(timeTxtArea,gridContraints);
		
		gridContraints.gridy++;
		
    }

   
    private void sendFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendFileButtonActionPerformed
            long size = file.length();
            if(size < 120 * 1024 * 1024){
            	JLabel targetUser = (JLabel)contactList.getSelectedValue();
                client.send(new Message("upload_req", username, file.getName(),targetUser.getText() ));
            }						//r/		type		sender		content		recipient
            else{
                notificationArea.append("[Application > Me] : File is size too large\n");
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
    private JScrollPane chatScrollPane;
    private JScrollPane contactListScrollPane;
    public JTextArea notificationArea;
    public JTextField messageInputTextField;
    public JTextField fileSendInputTextField;
    
    public final Color deepBlue = new Color(12, 125, 175);
    public final Color blue = new Color(0, 175, 240);
    public final Color lightBlue = new Color(204, 239, 252);
    public final Color white = new Color(255, 255, 255) ;
	private Color normalContactBackground;
	private Rectangle chatPaneBounds ;
	private Rectangle contactListBounds ;
	private Rectangle profileAreaBounds ;
    // End of variables declaration//GEN-END:variables
	public Rectangle messageTextBounds;
}
