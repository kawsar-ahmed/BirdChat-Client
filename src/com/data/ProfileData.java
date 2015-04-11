/**
 * 
 */
package com.data;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.JLabel;

/**
 * @author Kawsar
 *
 */
public class ProfileData {
	private HashMap<String, JLabel> contactLabels;
	private HashMap<String, BufferedImage> profilePictures ;
	private HashMap<String, String> names;
	private HashMap<String, String> abouts;
	
	
	/**
	 * @return the abouts
	 */
	public String getAbout(String user) {
		return abouts.get(user);
	}
	/**
	 * @param abouts the abouts to set
	 */
	public void setAbout(String user, String about) {
		abouts.put(user, about);
	}
	public ProfileData() {
		super();
		contactLabels = new HashMap<String, JLabel>();
		profilePictures = new HashMap<String, BufferedImage>();
		names = new HashMap<String, String>();
		abouts = new HashMap<String, String>();
	}
	/**
	 * @return the contactLabels
	 */
	public JLabel getContactLabel(String user) {
		return contactLabels.get(user);
	}
	/**
	 * @param contactLabels the contactLabels to set
	 */
	public void setContactLabel(String user, JLabel contactLabel) {
		contactLabels.put(user, contactLabel);
	}
	/**
	 * @return the profilePictures
	 */
	public BufferedImage getProfilePicture(String user) {
		return profilePictures.get(user);
	}
	/**
	 * @param profilePictures the profilePictures to set
	 */
	public void setProfilePicture(String user, BufferedImage pic) {
		profilePictures.put(user, pic);
	}
	/**
	 * @return the names
	 */
	public String getName(String user) {
		return names.get(user);
	}
	/**
	 * @param names the names to set
	 */
	public void setName(String user, String name) {
		names.put(user, name);
	}
}
