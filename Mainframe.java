package frame;

import javax.swing.*;


import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Mainframe extends JFrame{
    public Mainframe()
    {
    	this.setTitle("Tank Game");// Set title
		this.setSize(900, 700);// Set width and height
		this.setResizable(true);
		Toolkit tool = Toolkit.getDefaultToolkit(); 
		Dimension d = tool.getScreenSize(); // get the size of screen
		// Show the main window in the middle of the screen
		setLocation((d.width - getWidth()) / 2, (d.height - getHeight()) / 2);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addListener();// add event listener
		setPanel(new LoginPanel(this));// add login panel
		this.setVisible(true);
    }

    private void addListener() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {// when closing
				int closeCode = JOptionPane.showConfirmDialog(Mainframe.this, "Exit Game? ", "Warning",
						JOptionPane.YES_NO_OPTION);// pop an option dialog
				if (closeCode == JOptionPane.YES_OPTION) {// if yes
					System.exit(0);// exit
				}
			}
		});
	}
    
    public void setPanel(JPanel panel) {
		Container c = getContentPane();// retrieve container object
		c.removeAll();// remove all components in the container
		c.add(panel);// add specific panel into the container
		c.validate();
	}
}
