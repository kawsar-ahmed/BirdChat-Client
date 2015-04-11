package com.ui;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

//Display an icon and a string for each object in the list.

public class ContactCellRenderer extends JLabel implements ListCellRenderer<Object> {
//  final static ImageIcon longIcon = new ImageIcon("long.gif");
//  final static ImageIcon shortIcon = new ImageIcon("short.gif");

  // This is the only method defined by ListCellRenderer.
  // We just reconfigure the JLabel each time we're called.

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public Component getListCellRendererComponent(
    JList<?> list,           // the list
    Object value,            // value to display
    int index,               // cell index
    boolean isSelected,      // is the cell selected
    boolean cellHasFocus)    // does the cell have focus
  {
	  if (value instanceof JLabel){
		  setText(((JLabel) value).getText());
		  setIcon(((JLabel) value).getIcon());
	  }
		  
//      String s = value.toString();
//      setText(s);
//      setIcon((s.length() > 10) ? longIcon : shortIcon);
      if (isSelected) {
          setBackground(list.getSelectionBackground());
          setForeground(list.getSelectionForeground());
      } else {
          setBackground(list.getBackground());
          setForeground(list.getForeground());
      }
      setEnabled(list.isEnabled());
      setFont(list.getFont());
      setBounds(0, 0, 20, 10);
      setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
      setOpaque(true);
      return this;
  }
}


