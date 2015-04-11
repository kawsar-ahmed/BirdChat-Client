/**
 * 
 */
package com.data;

import java.awt.GridBagConstraints;
import java.util.HashMap;

import javax.swing.JPanel;

/**
 * @author Kawsar
 *
 */
public class ChatData {
	private HashMap<String, JPanel> contactPanels;
	private HashMap<String, GridBagConstraints> gridBagConstraints;
	private HashMap<String, String> lastSender;
	
	/**
	 * 
	 */
	public ChatData() {
		super();
		contactPanels = new HashMap<String, JPanel>();
		gridBagConstraints = new HashMap<String, GridBagConstraints>();
		lastSender = new HashMap<String, String>();
	}

	/**
	 * @return the iRepliedTo
	 */
	public String getLastSender(String user) {
		return lastSender.get(user);
	}
	
	/**
	 * @param iRepliedTo the iRepliedTo to set
	 */
	public void setLastSender(String user, String lastSender) {
		this.lastSender.put(user, lastSender);
	}
	/**
	 * @return the contactPanels
	 */
	public JPanel getContactPanel(String user) {
		return contactPanels.get(user);
	}
	/**
	 * @param contactPanels the contactPanels to set
	 */
	public void setContactPanel(String user, JPanel contactPanel) {
		contactPanels.put(user, contactPanel);
	}
	/**
	 * @return the gridBagConstraints
	 */
	public GridBagConstraints getGridBagConstraint(String user) {
		return gridBagConstraints.get(user);
	}
	/**
	 * @param gridBagConstraints the gridBagConstraints to set
	 */
	public void setGridBagConstraint(String user,
			GridBagConstraints gridBagConstraint) {
		gridBagConstraint.gridx = 0;
		gridBagConstraint.gridy = 0;
		gridBagConstraints.put(user, gridBagConstraint);
	}
}
