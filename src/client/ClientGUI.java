package client;

import java.beans.PropertyChangeSupport;

import javax.swing.JFrame;

public class ClientGUI extends JFrame {
	private PropertyChangeSupport pcs;
	
	public ClientGUI(PropertyChangeSupport pcs) {
		this.pcs = pcs;
	}

}
